package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareTask
import com.example.data.model.Caregiver
import com.example.data.model.TaskCategory
import com.example.ui.theme.ActivityColor
import com.example.ui.theme.AppointmentColor
import com.example.ui.theme.MealColor
import com.example.ui.theme.MedicationColor
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.VitalsColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareTaskSheet(
    caregivers: List<Caregiver>,
    onDismiss: () -> Unit,
    onAddTask: (CareTask) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(TaskCategory.MEDICATION) }
    var scheduledTime by remember { mutableStateOf("08:00 AM") }
    var selectedCaregiver by remember { mutableStateOf(caregivers.firstOrNull()?.name ?: "Sarah Vance") }
    var dosageOrDetails by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Normal") }

    val presetTimes = listOf("08:00 AM", "12:30 PM", "03:30 PM", "07:30 PM", "09:00 PM")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface,
        modifier = Modifier.testTag("add_care_task_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Care Schedule Task",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task Name Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task / Medication Name") },
                placeholder = { Text("e.g. Lisinopril 10mg, Evening Walk") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_title_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = SlateOutline
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Chips
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TaskCategory.values().forEach { cat ->
                    val isSelected = category == cat
                    val chipColor = when (cat) {
                        TaskCategory.MEDICATION -> MedicationColor
                        TaskCategory.MEAL -> MealColor
                        TaskCategory.APPOINTMENT -> AppointmentColor
                        TaskCategory.VITALS -> VitalsColor
                        TaskCategory.ACTIVITY -> ActivityColor
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { category = cat },
                        label = { Text(cat.displayName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor.copy(alpha = 0.2f),
                            selectedLabelColor = chipColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Scheduled Time Selector
            Text(
                text = "Scheduled Time",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                presetTimes.forEach { time ->
                    val isSelected = scheduledTime == time
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) TealPrimary else SlateBackground,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TealPrimary else SlateOutline
                        ),
                        modifier = Modifier
                            .clickable { scheduledTime = time }
                            .testTag("time_chip_$time")
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else SlateTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Assigned Caregiver Selector
            Text(
                text = "Assigned Caregiver",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                caregivers.forEach { caregiver ->
                    val isSelected = selectedCaregiver == caregiver.name
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) TealPrimary.copy(alpha = 0.1f) else SlateBackground,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TealPrimary else SlateOutline.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCaregiver = caregiver.name }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(TealPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(caregiver.initials, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(caregiver.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = SlateTextPrimary)
                                Text(caregiver.relation, fontSize = 11.sp, color = SlateTextSecondary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dosage & Instructions Field
            OutlinedTextField(
                value = dosageOrDetails,
                onValueChange = { dosageOrDetails = it },
                label = { Text("Dosage / Instructions") },
                placeholder = { Text("e.g. 1 tablet with full meal and 8oz water") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_details_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = SlateOutline
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(
                            CareTask(
                                title = title.trim(),
                                category = category.name,
                                scheduledTime = scheduledTime,
                                assignedCaregiver = selectedCaregiver,
                                dosageOrDetails = dosageOrDetails.trim(),
                                isCompleted = false,
                                priority = priority
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_care_task_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Schedule Care Task", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
