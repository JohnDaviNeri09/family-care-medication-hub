package com.example.data.repository

import com.example.data.db.CareTaskDao
import com.example.data.db.HealthLogDao
import com.example.data.model.CareTask
import com.example.data.model.Caregiver
import com.example.data.model.HealthLog
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CareRepository(
    private val careTaskDao: CareTaskDao,
    private val healthLogDao: HealthLogDao
) {
    val allTasks: Flow<List<CareTask>> = careTaskDao.getAllTasks()
    val allLogs: Flow<List<HealthLog>> = healthLogDao.getAllLogs()
    val logsAscending: Flow<List<HealthLog>> = healthLogDao.getAllLogsAscending()

    val caregiversList = listOf(
        Caregiver("1", "Sarah Vance", "Daughter (Primary)", "(555) 234-5678", "Morning Meds & Meal Prep", "SV", isPrimary = true),
        Caregiver("2", "David Vance", "Son", "(555) 345-6789", "Afternoon Transport & Exercise", "DV"),
        Caregiver("3", "Elena Rostova", "Visiting RN", "(555) 876-5432", "Weekly Clinical Vitals & Lab Review", "ER"),
        Caregiver("4", "Mark Vance", "Self (Dad)", "(555) 123-4567", "Evening Logs & Hydration", "MV")
    )

    suspend fun checkAndSeedInitialData() {
        if (careTaskDao.getTaskCount() == 0) {
            val initialTasks = listOf(
                CareTask(
                    title = "Lisinopril 10mg",
                    category = "MEDICATION",
                    scheduledTime = "08:00 AM",
                    assignedCaregiver = "Sarah Vance",
                    dosageOrDetails = "1 tablet with full glass of water",
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 7200000,
                    priority = "High"
                ),
                CareTask(
                    title = "Morning Blood Pressure & Pulse",
                    category = "VITALS",
                    scheduledTime = "08:30 AM",
                    assignedCaregiver = "Sarah Vance",
                    dosageOrDetails = "Log seated reading in Hub",
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 5400000,
                    priority = "High"
                ),
                CareTask(
                    title = "Low-Sodium Oatmeal & Berries",
                    category = "MEAL",
                    scheduledTime = "09:00 AM",
                    assignedCaregiver = "Sarah Vance",
                    dosageOrDetails = "Prepared without added salt, fresh blueberries",
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 3600000,
                    priority = "Normal"
                ),
                CareTask(
                    title = "Metformin 500mg",
                    category = "MEDICATION",
                    scheduledTime = "12:30 PM",
                    assignedCaregiver = "David Vance",
                    dosageOrDetails = "Take halfway through lunch meal",
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 1800000,
                    priority = "High"
                ),
                CareTask(
                    title = "Gentle Physical Therapy Walk",
                    category = "ACTIVITY",
                    scheduledTime = "03:30 PM",
                    assignedCaregiver = "David Vance",
                    dosageOrDetails = "15-minute garden walk with support cane",
                    isCompleted = false,
                    priority = "Normal"
                ),
                CareTask(
                    title = "Atorvastatin 20mg",
                    category = "MEDICATION",
                    scheduledTime = "07:30 PM",
                    assignedCaregiver = "Elena Rostova",
                    dosageOrDetails = "1 tablet with evening water",
                    isCompleted = false,
                    priority = "High"
                ),
                CareTask(
                    title = "Evening Blood Pressure Check",
                    category = "VITALS",
                    scheduledTime = "08:30 PM",
                    assignedCaregiver = "Mark Vance",
                    dosageOrDetails = "Record pre-bed seated reading",
                    isCompleted = false,
                    priority = "Normal"
                ),
                CareTask(
                    title = "Bedtime Hydration & Leg Elevation",
                    category = "ACTIVITY",
                    scheduledTime = "09:30 PM",
                    assignedCaregiver = "Sarah Vance",
                    dosageOrDetails = "Elevate legs 20 mins to mitigate ankle swelling",
                    isCompleted = false,
                    priority = "Low"
                )
            )
            careTaskDao.insertTasks(initialTasks)
        }

        if (healthLogDao.getLogCount() == 0) {
            val now = System.currentTimeMillis()
            val dayMs = 86400000L

            val initialLogs = listOf(
                HealthLog(
                    timestamp = now - (0 * dayMs),
                    dateDisplay = "Today",
                    timeDisplay = "08:30 AM",
                    systolicBp = 126,
                    diastolicBp = 82,
                    heartRate = 72,
                    symptoms = "None, feeling well-rested",
                    severity = 2,
                    mood = "😊 Good",
                    notes = "Taken after 10 mins quiet sitting. Lisinopril taken at 8am."
                ),
                HealthLog(
                    timestamp = now - (1 * dayMs),
                    dateDisplay = "Aug 18",
                    timeDisplay = "08:15 AM",
                    systolicBp = 134,
                    diastolicBp = 86,
                    heartRate = 78,
                    symptoms = "Mild dizziness upon standing",
                    severity = 4,
                    mood = "😐 Fair",
                    notes = "Dizziness cleared after 16oz hydration and sitting."
                ),
                HealthLog(
                    timestamp = now - (2 * dayMs),
                    dateDisplay = "Aug 17",
                    timeDisplay = "08:30 AM",
                    systolicBp = 128,
                    diastolicBp = 80,
                    heartRate = 70,
                    symptoms = "No noticeable symptoms",
                    severity = 1,
                    mood = "😊 Good",
                    notes = "Full compliance with low sodium meal plan."
                ),
                HealthLog(
                    timestamp = now - (3 * dayMs),
                    dateDisplay = "Aug 16",
                    timeDisplay = "09:00 AM",
                    systolicBp = 142,
                    diastolicBp = 90,
                    heartRate = 84,
                    symptoms = "Ankle swelling (1+ edema), slight tension headache",
                    severity = 6,
                    mood = "😴 Fatigued",
                    notes = "Poor sleep (4 hrs). Ate canned soup previous night."
                ),
                HealthLog(
                    timestamp = now - (4 * dayMs),
                    dateDisplay = "Aug 15",
                    timeDisplay = "08:20 AM",
                    systolicBp = 130,
                    diastolicBp = 84,
                    heartRate = 74,
                    symptoms = "Slight knee stiffness",
                    severity = 3,
                    mood = "😐 Fair",
                    notes = "Stiffness improved after warm shower."
                ),
                HealthLog(
                    timestamp = now - (5 * dayMs),
                    dateDisplay = "Aug 14",
                    timeDisplay = "08:45 AM",
                    systolicBp = 122,
                    diastolicBp = 78,
                    heartRate = 68,
                    symptoms = "Energetic, clear mind",
                    severity = 1,
                    mood = "💪 Energetic",
                    notes = "Completed full 20-min garden walk with David."
                ),
                HealthLog(
                    timestamp = now - (6 * dayMs),
                    dateDisplay = "Aug 13",
                    timeDisplay = "08:15 AM",
                    systolicBp = 136,
                    diastolicBp = 88,
                    heartRate = 80,
                    symptoms = "Transient flutter sensation (2 mins)",
                    severity = 4,
                    mood = "😟 Anxious",
                    notes = "Relaxation breathing technique stabilized pulse."
                )
            )
            healthLogDao.insertLogs(initialLogs)
        }
    }

    suspend fun insertTask(task: CareTask): Long = careTaskDao.insertTask(task)
    suspend fun updateTask(task: CareTask) = careTaskDao.updateTask(task)
    suspend fun deleteTask(task: CareTask) = careTaskDao.deleteTask(task)
    suspend fun toggleTaskCompleted(taskId: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        careTaskDao.setTaskCompleted(taskId, isCompleted, completedAt)
    }

    suspend fun insertLog(log: HealthLog): Long = healthLogDao.insertLog(log)
    suspend fun deleteLog(log: HealthLog) = healthLogDao.deleteLog(log)
}
