package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Caregiver
import com.example.data.model.ChartMetricMode
import com.example.data.model.HealthLog
import com.example.ui.components.AddCareTaskSheet
import com.example.ui.components.AddHealthLogSheet
import com.example.ui.components.ComplianceSummaryCard
import com.example.ui.components.DoctorBriefCard
import com.example.ui.components.EditPatientProfileSheet
import com.example.ui.components.HealthLogCard
import com.example.ui.components.InteractiveVitalsChart
import com.example.ui.components.TaskCard
import com.example.ui.components.getInitials
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.AlertCoralLight
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateDarkCard
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SlateTextTertiary
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintLight
import com.example.ui.theme.TealBgLight
import com.example.ui.theme.TealNeon
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight
import com.example.ui.theme.WarmAmber
import com.example.ui.viewmodel.CareHubViewModel
import com.example.ui.viewmodel.TaskStatusFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CareHubViewModel,
    modifier: Modifier = Modifier
) {
    val patientName by viewModel.patientName.collectAsStateWithLifecycle()
    val patientAge by viewModel.patientAge.collectAsStateWithLifecycle()
    val tasks by viewModel.rawTasks.collectAsStateWithLifecycle()
    val filteredTasks by viewModel.filteredTasks.collectAsStateWithLifecycle()
    val healthLogs by viewModel.healthLogs.collectAsStateWithLifecycle()
    val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()
    val caregiverFilter by viewModel.caregiverFilter.collectAsStateWithLifecycle()
    val categoryFilter by viewModel.categoryFilter.collectAsStateWithLifecycle()
    val chartMetricMode by viewModel.chartMetricMode.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val doctorBrief by viewModel.doctorBrief.collectAsStateWithLifecycle()
    val isBriefLoading by viewModel.isBriefLoading.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showAddTaskSheet by remember { mutableStateOf(false) }
    var showAddLogSheet by remember { mutableStateOf(false) }
    var showEditPatientSheet by remember { mutableStateOf(false) }

    val initials = remember(patientName) { getInitials(patientName) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Clean Utility Minimal Header with Changeable Patient Name
            Surface(
                color = SlateBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .clickable { showEditPatientSheet = true }
                            .testTag("patient_name_header_clickable")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "FAMILY CARE HUB",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary,
                                letterSpacing = 1.2.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit patient name",
                                tint = TealPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = patientName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SlateTextPrimary,
                                letterSpacing = (-0.3).sp,
                                modifier = Modifier.testTag("patient_name_text")
                            )
                        }
                    }

                    // Avatar Initials Badge (Clickable to Edit Patient Profile)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TealPrimaryLight)
                            .clickable { showEditPatientSheet = true }
                            .testTag("patient_avatar_badge"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = TealPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                color = SlateSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                shadowElevation = 4.dp
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { viewModel.setSelectedTab(0) },
                        icon = {
                            Icon(
                                if (selectedTab == 0) Icons.Filled.Checklist else Icons.Outlined.Checklist,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text(
                                "HOME",
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            unselectedIconColor = SlateTextTertiary,
                            unselectedTextColor = SlateTextTertiary,
                            indicatorColor = TealBgLight
                        ),
                        modifier = Modifier.testTag("nav_tab_tasks")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { viewModel.setSelectedTab(1) },
                        icon = {
                            Icon(
                                if (selectedTab == 1) Icons.AutoMirrored.Filled.ShowChart else Icons.AutoMirrored.Outlined.ShowChart,
                                contentDescription = "Logs"
                            )
                        },
                        label = {
                            Text(
                                "LOGS",
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            unselectedIconColor = SlateTextTertiary,
                            unselectedTextColor = SlateTextTertiary,
                            indicatorColor = TealBgLight
                        ),
                        modifier = Modifier.testTag("nav_tab_vitals")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { viewModel.setSelectedTab(2) },
                        icon = {
                            Icon(
                                if (selectedTab == 2) Icons.Filled.LocalHospital else Icons.Outlined.LocalHospital,
                                contentDescription = "Brief"
                            )
                        },
                        label = {
                            Text(
                                "BRIEF",
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            unselectedIconColor = SlateTextTertiary,
                            unselectedTextColor = SlateTextTertiary,
                            indicatorColor = TealBgLight
                        ),
                        modifier = Modifier.testTag("nav_tab_brief")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { viewModel.setSelectedTab(3) },
                        icon = {
                            Icon(
                                if (selectedTab == 3) Icons.Filled.People else Icons.Outlined.People,
                                contentDescription = "Care"
                            )
                        },
                        label = {
                            Text(
                                "CARE",
                                fontSize = 10.sp,
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            unselectedIconColor = SlateTextTertiary,
                            unselectedTextColor = SlateTextTertiary,
                            indicatorColor = TealBgLight
                        ),
                        modifier = Modifier.testTag("nav_tab_team")
                    )
                }
            }
        },
        floatingActionButton = {
            when (selectedTab) {
                0 -> {
                    ExtendedFloatingActionButton(
                        onClick = { showAddTaskSheet = true },
                        containerColor = TealPrimary,
                        contentColor = Color.White,
                        icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
                        text = { Text("Add Task", fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.testTag("add_task_fab")
                    )
                }
                1 -> {
                    ExtendedFloatingActionButton(
                        onClick = { showAddLogSheet = true },
                        containerColor = TealPrimary,
                        contentColor = Color.White,
                        icon = { Icon(Icons.Default.Add, contentDescription = "Log Vitals") },
                        text = { Text("Log Vitals", fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.testTag("log_vitals_fab")
                    )
                }
                else -> {}
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SlateBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TasksTabContent(
                    tasks = tasks,
                    filteredTasks = filteredTasks,
                    healthLogs = healthLogs,
                    statusFilter = statusFilter,
                    caregiverFilter = caregiverFilter,
                    categoryFilter = categoryFilter,
                    caregivers = viewModel.caregivers,
                    onSetStatusFilter = { viewModel.setStatusFilter(it) },
                    onSetCaregiverFilter = { viewModel.setCaregiverFilter(it) },
                    onSetCategoryFilter = { viewModel.setCategoryFilter(it) },
                    onToggleTask = { taskId, completed -> viewModel.toggleTask(taskId, completed) },
                    onDeleteTask = { task -> viewModel.deleteTask(task) },
                    onGenerateBrief = {
                        viewModel.generateDoctorBrief()
                        viewModel.setSelectedTab(2)
                    },
                    onOpenVitals = { viewModel.setSelectedTab(1) }
                )
                1 -> VitalsTabContent(
                    logs = healthLogs,
                    metricMode = chartMetricMode,
                    onSetMetricMode = { viewModel.setChartMetricMode(it) },
                    onDeleteLog = { log -> viewModel.deleteHealthLog(log) }
                )
                2 -> DoctorBriefTabContent(
                    tasks = tasks,
                    logs = healthLogs,
                    doctorBrief = doctorBrief,
                    isLoading = isBriefLoading,
                    onGenerate = { viewModel.generateDoctorBrief() },
                    onShowToast = { viewModel.showToast(it) }
                )
                3 -> CareTeamTabContent(
                    patientName = patientName,
                    patientAge = patientAge,
                    caregivers = viewModel.caregivers,
                    onEditPatient = { showEditPatientSheet = true },
                    onShowToast = { viewModel.showToast(it) }
                )
            }
        }
    }

    if (showEditPatientSheet) {
        EditPatientProfileSheet(
            currentName = patientName,
            currentAge = patientAge,
            onDismiss = { showEditPatientSheet = false },
            onSaveProfile = { name, age ->
                viewModel.updatePatientProfile(name, age)
            }
        )
    }

    if (showAddTaskSheet) {
        AddCareTaskSheet(
            caregivers = viewModel.caregivers,
            onDismiss = { showAddTaskSheet = false },
            onAddTask = { task -> viewModel.addTask(task) }
        )
    }

    if (showAddLogSheet) {
        AddHealthLogSheet(
            onDismiss = { showAddLogSheet = false },
            onAddLog = { log -> viewModel.addHealthLog(log) }
        )
    }
}

// ---------------------- TAB 0: CAREGIVER DASHBOARD & TASKS ----------------------
@Composable
private fun TasksTabContent(
    tasks: List<com.example.data.model.CareTask>,
    filteredTasks: List<com.example.data.model.CareTask>,
    healthLogs: List<HealthLog>,
    statusFilter: TaskStatusFilter,
    caregiverFilter: String,
    categoryFilter: String,
    caregivers: List<Caregiver>,
    onSetStatusFilter: (TaskStatusFilter) -> Unit,
    onSetCaregiverFilter: (String) -> Unit,
    onSetCategoryFilter: (String) -> Unit,
    onToggleTask: (Long, Boolean) -> Unit,
    onDeleteTask: (com.example.data.model.CareTask) -> Unit,
    onGenerateBrief: () -> Unit,
    onOpenVitals: () -> Unit
) {
    val latestLog = healthLogs.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("tasks_tab_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Daily Compliance & Progress Bar Card
        item {
            ComplianceSummaryCard(tasks = tasks)
        }

        // Section: Medications & Tasks Card Container
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = SlateSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "MEDICATIONS & TASKS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextTertiary,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Filter Tabs (All, Pending, Completed)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskStatusFilter.values().forEach { filter ->
                            val isSelected = statusFilter == filter
                            val count = when (filter) {
                                TaskStatusFilter.ALL -> tasks.size
                                TaskStatusFilter.PENDING -> tasks.count { !it.isCompleted }
                                TaskStatusFilter.COMPLETED -> tasks.count { it.isCompleted }
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) TealPrimary else SlateBorderLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSetStatusFilter(filter) }
                                    .testTag("filter_status_${filter.name}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${filter.label} ($count)",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else SlateTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Caregiver Filter Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = caregiverFilter == "All",
                            onClick = { onSetCaregiverFilter("All") },
                            label = { Text("All", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimaryLight,
                                selectedLabelColor = TealPrimary
                            )
                        )
                        caregivers.forEach { caregiver ->
                            val isSelected = caregiverFilter == caregiver.name
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSetCaregiverFilter(if (isSelected) "All" else caregiver.name) },
                                label = { Text(caregiver.name, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TealPrimaryLight,
                                    selectedLabelColor = TealPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (filteredTasks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No tasks match current filter.", color = SlateTextSecondary, fontSize = 13.sp)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            filteredTasks.forEach { task ->
                                TaskCard(
                                    task = task,
                                    onToggleCompleted = { completed -> onToggleTask(task.id, completed) },
                                    onDelete = { onDeleteTask(task) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Hero Dark Vitals Trend Card (from Clean Utility Minimal design)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenVitals() }
                    .testTag("hero_vitals_card"),
                shape = RoundedCornerShape(24.dp),
                color = SlateDarkCard,
                shadowElevation = 6.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header row: Vitals Trend + STABLE pill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Vitals Trend",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = TealPrimary.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TealNeon.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = if (latestLog != null && latestLog.bpCategory == "Normal") "STABLE" else "TRACKING",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealNeon,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Large Reading
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (latestLog != null) {
                                Text(
                                    text = "${latestLog.systolicBp}",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White
                                )
                                Text(
                                    text = "/${latestLog.diastolicBp}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = SlateTextTertiary,
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            } else {
                                Text(
                                    text = "118/76",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "BLOOD PRESSURE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = SlateTextTertiary,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Sleek Neon Teal Canvas Sparkline
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            val path = Path()

                            val points = listOf(
                                Offset(0f, h * 0.75f),
                                Offset(w * 0.2f, h * 0.65f),
                                Offset(w * 0.4f, h * 0.8f),
                                Offset(w * 0.6f, h * 0.45f),
                                Offset(w * 0.8f, h * 0.25f),
                                Offset(w, h * 0.4f)
                            )

                            path.moveTo(points[0].x, points[0].y)
                            for (i in 1 until points.size) {
                                val prev = points[i - 1]
                                val cur = points[i]
                                val cX = (prev.x + cur.x) / 2f
                                path.cubicTo(cX, prev.y, cX, cur.y, cur.x, cur.y)
                            }

                            drawPath(
                                path = path,
                                color = TealNeon,
                                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Highlight point
                            val highlight = points[4]
                            drawCircle(
                                color = TealNeon,
                                radius = 4.dp.toPx(),
                                center = highlight
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = highlight
                            )
                        }
                    }
                }
            }
        }

        // Action Button: Generate Doctor Brief (Clean Utility Minimal Teal Button)
        item {
            Button(
                onClick = onGenerateBrief,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("home_generate_brief_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "GENERATE DOCTOR BRIEF",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

// ---------------------- TAB 1: HEALTH VITALS & INTERACTIVE CHARTS ----------------------
@Composable
private fun VitalsTabContent(
    logs: List<com.example.data.model.HealthLog>,
    metricMode: ChartMetricMode,
    onSetMetricMode: (ChartMetricMode) -> Unit,
    onDeleteLog: (com.example.data.model.HealthLog) -> Unit
) {
    val latestLog = logs.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("vitals_tab_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Vitals Overview Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Latest BP Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("BLOOD PRESSURE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AlertCoral, letterSpacing = 0.5.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (latestLog != null) "${latestLog.systolicBp}/${latestLog.diastolicBp}" else "--/--",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = if (latestLog != null) "${latestLog.bpCategory} • ${latestLog.dateDisplay}" else "No logs",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )
                    }
                }

                // Latest Pulse Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("RESTING PULSE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarmAmber, letterSpacing = 0.5.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (latestLog != null) "${latestLog.heartRate} bpm" else "-- bpm",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = if (latestLog != null) "Sinus rhythm • ${latestLog.dateDisplay}" else "No logs",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )
                    }
                }
            }
        }

        // Chart Metric Selector Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChartMetricMode.values().forEach { mode ->
                    val isSelected = metricMode == mode
                    val modeLabel = when (mode) {
                        ChartMetricMode.BLOOD_PRESSURE -> "Blood Pressure"
                        ChartMetricMode.HEART_RATE -> "Heart Rate"
                        ChartMetricMode.SEVERITY_INDEX -> "Symptom Index"
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) TealPrimary else SlateSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TealPrimary else SlateBorderLight
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSetMetricMode(mode) }
                            .testTag("chart_mode_${mode.name}")
                    ) {
                        Text(
                            text = modeLabel,
                            modifier = Modifier.padding(vertical = 10.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else SlateTextPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Interactive Canvas Chart
        item {
            InteractiveVitalsChart(
                logs = logs,
                metricMode = metricMode
            )
        }

        // Historical Log List Header
        item {
            Text(
                text = "DAILY VITAL & SYMPTOM HISTORY (${logs.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextTertiary,
                letterSpacing = 1.2.sp
            )
        }

        if (logs.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = SlateTextTertiary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No vitals logged yet",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap '+ Log Vitals' below to record daily BP and pulse readings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(logs, key = { it.id }) { log ->
                HealthLogCard(
                    log = log,
                    onDelete = { onDeleteLog(log) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

// ---------------------- TAB 2: GEMINI AI DOCTOR BRIEF ----------------------
@Composable
private fun DoctorBriefTabContent(
    tasks: List<com.example.data.model.CareTask>,
    logs: List<com.example.data.model.HealthLog>,
    doctorBrief: com.example.data.model.DoctorBrief?,
    isLoading: Boolean,
    onGenerate: () -> Unit,
    onShowToast: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("doctor_brief_tab_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Info Banner
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = TealBgLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(22.dp))
                    Column {
                        Text(
                            text = "Gemini Clinical Summary Engine",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Synthesizes medication compliance records, pending doses, and 7-day vital trends into a high-contrast 3-section report formatted specifically for doctor consultations.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Doctor Brief Main Card
        item {
            DoctorBriefCard(
                brief = doctorBrief,
                isLoading = isLoading,
                onGenerate = onGenerate,
                onShowToast = onShowToast
            )
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

// ---------------------- TAB 3: FAMILY CARE TEAM & PATIENT PROFILE ----------------------
@Composable
private fun CareTeamTabContent(
    patientName: String,
    patientAge: Int,
    caregivers: List<Caregiver>,
    onEditPatient: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    val initials = remember(patientName) { getInitials(patientName) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("care_team_tab_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Patient Profile Overview Card
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SlateSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_profile_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRIMARY PATIENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
                            letterSpacing = 1.2.sp
                        )

                        OutlinedButton(
                            onClick = onEditPatient,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("edit_patient_profile_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Name", color = TealPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(TealPrimaryLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = TealPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Column {
                            Text(
                                text = patientName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "$patientAge years old • Primary Care Recipient",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Emergency Call Hotline Banner
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AlertCoralLight,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertCoral.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AlertCoral),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("Emergency Clinic Line", fontWeight = FontWeight.Bold, color = AlertCoral, fontSize = 13.sp)
                            Text("Dr. Harrison Clinic: (555) 998-1122", fontSize = 11.sp, color = SlateTextPrimary)
                        }
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:5559981122"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertCoral, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Call", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Caregivers Roster Header
        item {
            Text(
                text = "ASSIGNED FAMILY CAREGIVERS (${caregivers.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextTertiary,
                letterSpacing = 1.2.sp
            )
        }

        items(caregivers, key = { it.id }) { caregiver ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SlateSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (caregiver.isPrimary) TealPrimary else SlateBorderLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = caregiver.initials,
                                color = if (caregiver.isPrimary) Color.White else SlateTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = caregiver.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                                if (caregiver.isPrimary) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SuccessMintLight
                                    ) {
                                        Text(
                                            text = "PRIMARY",
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessMint
                                        )
                                    }
                                }
                            }
                            Text(text = caregiver.relation, style = MaterialTheme.typography.bodySmall, color = SlateTextSecondary)
                            Text(text = caregiver.roleDescription, style = MaterialTheme.typography.bodySmall, color = TealPrimary, fontSize = 11.sp)
                        }
                    }

                    // Contact Actions
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${caregiver.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = TealPrimary, modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${caregiver.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "SMS", tint = TealPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}
