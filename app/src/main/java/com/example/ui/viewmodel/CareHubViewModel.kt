package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.gemini.GeminiClient
import com.example.data.model.CareTask
import com.example.data.model.Caregiver
import com.example.data.model.ChartMetricMode
import com.example.data.model.DoctorBrief
import com.example.data.model.HealthLog
import com.example.data.repository.CareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TaskStatusFilter(val label: String) {
    ALL("All Tasks"),
    PENDING("Pending"),
    COMPLETED("Completed")
}

class CareHubViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("care_hub_prefs", Context.MODE_PRIVATE)
    private val database = AppDatabase.getDatabase(application)
    private val repository = CareRepository(database.careTaskDao(), database.healthLogDao())

    val caregivers: List<Caregiver> = repository.caregiversList

    // Patient Profile
    private val _patientName = MutableStateFlow(prefs.getString("patient_name", "Robert Vance") ?: "Robert Vance")
    val patientName: StateFlow<String> = _patientName.asStateFlow()

    private val _patientAge = MutableStateFlow(prefs.getInt("patient_age", 74))
    val patientAge: StateFlow<Int> = _patientAge.asStateFlow()

    val rawTasks: StateFlow<List<CareTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthLogs: StateFlow<List<HealthLog>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filters
    private val _statusFilter = MutableStateFlow(TaskStatusFilter.ALL)
    val statusFilter: StateFlow<TaskStatusFilter> = _statusFilter.asStateFlow()

    private val _caregiverFilter = MutableStateFlow("All")
    val caregiverFilter: StateFlow<String> = _caregiverFilter.asStateFlow()

    private val _categoryFilter = MutableStateFlow("All")
    val categoryFilter: StateFlow<String> = _categoryFilter.asStateFlow()

    private val _chartMetricMode = MutableStateFlow(ChartMetricMode.BLOOD_PRESSURE)
    val chartMetricMode: StateFlow<ChartMetricMode> = _chartMetricMode.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Doctor Brief State
    private val _doctorBrief = MutableStateFlow<DoctorBrief?>(null)
    val doctorBrief: StateFlow<DoctorBrief?> = _doctorBrief.asStateFlow()

    private val _isBriefLoading = MutableStateFlow(false)
    val isBriefLoading: StateFlow<Boolean> = _isBriefLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Filtered Tasks
    val filteredTasks: StateFlow<List<CareTask>> = combine(
        rawTasks,
        _statusFilter,
        _caregiverFilter,
        _categoryFilter
    ) { tasks, status, caregiver, category ->
        tasks.filter { task ->
            val matchesStatus = when (status) {
                TaskStatusFilter.ALL -> true
                TaskStatusFilter.PENDING -> !task.isCompleted
                TaskStatusFilter.COMPLETED -> task.isCompleted
            }
            val matchesCaregiver = caregiver == "All" || task.assignedCaregiver.contains(caregiver, ignoreCase = true)
            val matchesCategory = category == "All" || task.category.equals(category, ignoreCase = true)
            matchesStatus && matchesCaregiver && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    fun setStatusFilter(filter: TaskStatusFilter) {
        _statusFilter.value = filter
    }

    fun setCaregiverFilter(caregiver: String) {
        _caregiverFilter.value = caregiver
    }

    fun setCategoryFilter(category: String) {
        _categoryFilter.value = category
    }

    fun setChartMetricMode(mode: ChartMetricMode) {
        _chartMetricMode.value = mode
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun toggleTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(taskId, isCompleted)
            showToast(if (isCompleted) "Task marked completed ✓" else "Task marked pending")
        }
    }

    fun addTask(task: CareTask) {
        viewModelScope.launch {
            repository.insertTask(task)
            showToast("Added care task: ${task.title}")
        }
    }

    fun deleteTask(task: CareTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
            showToast("Removed task: ${task.title}")
        }
    }

    fun addHealthLog(log: HealthLog) {
        viewModelScope.launch {
            repository.insertLog(log)
            showToast("Logged vitals: ${log.systolicBp}/${log.diastolicBp} mmHg")
        }
    }

    fun deleteHealthLog(log: HealthLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
            showToast("Removed vital log from ${log.dateDisplay}")
        }
    }

    fun updatePatientProfile(name: String, age: Int) {
        val trimmedName = name.trim().ifBlank { "Robert Vance" }
        val validAge = if (age in 1..120) age else 74
        _patientName.value = trimmedName
        _patientAge.value = validAge
        prefs.edit()
            .putString("patient_name", trimmedName)
            .putInt("patient_age", validAge)
            .apply()
        showToast("Patient profile updated: $trimmedName ($validAge y/o)")
    }

    fun generateDoctorBrief() {
        viewModelScope.launch {
            _isBriefLoading.value = true
            val tasks = rawTasks.value
            val logs = healthLogs.value
            val result = GeminiClient.generateDoctorBrief(
                tasks = tasks,
                logs = logs,
                patientName = _patientName.value,
                patientAge = _patientAge.value
            )
            _isBriefLoading.value = false
            result.onSuccess { brief ->
                _doctorBrief.value = brief
                showToast("Clinical Doctor Brief generated successfully!")
            }.onFailure { error ->
                showToast("Notice: ${error.message}")
            }
        }
    }
}
