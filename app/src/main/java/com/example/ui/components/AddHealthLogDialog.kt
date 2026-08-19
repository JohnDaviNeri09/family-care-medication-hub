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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.HealthLog
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.AlertCoralLight
import com.example.ui.theme.InfoIndigo
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.theme.WarmAmber
import com.example.ui.theme.WarmAmberLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthLogSheet(
    onDismiss: () -> Unit,
    onAddLog: (HealthLog) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var systolic by remember { mutableIntStateOf(128) }
    var diastolic by remember { mutableIntStateOf(82) }
    var heartRate by remember { mutableIntStateOf(72) }
    var severity by remember { mutableFloatStateOf(2f) }
    var selectedMood by remember { mutableStateOf("😊 Good") }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var customSymptom by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val commonSymptoms = listOf(
        "None",
        "Morning Dizziness",
        "Ankle Swelling",
        "Headache",
        "Fatigue",
        "Shortness of Breath",
        "Heart Flutter",
        "Knee Pain"
    )

    val moods = listOf(
        "😊 Good",
        "😐 Fair",
        "😴 Fatigued",
        "😟 Anxious",
        "💪 Energetic",
        "🤕 In Pain"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface,
        modifier = Modifier.testTag("add_health_log_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log Daily Vitals & Symptoms",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Blood Pressure Steppers (Systolic & Diastolic)
            Text(
                text = "Blood Pressure (mmHg)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Systolic
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = SlateBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("SYSTOLIC (Top)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AlertCoral)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { if (systolic > 70) systolic -= 2 },
                                modifier = Modifier.size(32.dp).testTag("systolic_decrement")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SlateTextPrimary)
                            }
                            Text(
                                text = "$systolic",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SlateTextPrimary
                            )
                            IconButton(
                                onClick = { if (systolic < 220) systolic += 2 },
                                modifier = Modifier.size(32.dp).testTag("systolic_increment")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = SlateTextPrimary)
                            }
                        }
                    }
                }

                // Diastolic
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = SlateBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TealSecondary.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("DIASTOLIC (Bottom)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TealSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { if (diastolic > 40) diastolic -= 2 },
                                modifier = Modifier.size(32.dp).testTag("diastolic_decrement")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SlateTextPrimary)
                            }
                            Text(
                                text = "$diastolic",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SlateTextPrimary
                            )
                            IconButton(
                                onClick = { if (diastolic < 140) diastolic += 2 },
                                modifier = Modifier.size(32.dp).testTag("diastolic_increment")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = SlateTextPrimary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Heart Rate / Pulse Stepper
            Text(
                text = "Pulse Rate (bpm)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SlateBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmAmber.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = WarmAmber, modifier = Modifier.size(20.dp))
                        Text("Resting Heart Rate", fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IconButton(
                            onClick = { if (heartRate > 40) heartRate -= 1 },
                            modifier = Modifier.size(32.dp).testTag("pulse_decrement")
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SlateTextPrimary)
                        }
                        Text(
                            text = "$heartRate bpm",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        IconButton(
                            onClick = { if (heartRate < 160) heartRate += 1 },
                            modifier = Modifier.size(32.dp).testTag("pulse_increment")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = SlateTextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Severity Rating Slider (1-10)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Symptom Severity Rating (1 - 10)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "${severity.toInt()}/10",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = when {
                        severity <= 3 -> SuccessMint
                        severity <= 6 -> WarmAmber
                        else -> AlertCoral
                    }
                )
            }
            Slider(
                value = severity,
                onValueChange = { severity = it },
                valueRange = 1f..10f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = TealPrimary,
                    activeTrackColor = when {
                        severity <= 3 -> SuccessMint
                        severity <= 6 -> WarmAmber
                        else -> AlertCoral
                    }
                ),
                modifier = Modifier.testTag("severity_slider")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Mood Selector
            Text(
                text = "Overall Mood & Energy",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                moods.forEach { mood ->
                    val isSelected = selectedMood == mood
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedMood = mood },
                        label = { Text(mood, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TealPrimary.copy(alpha = 0.15f),
                            selectedLabelColor = TealPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Symptoms Chips
            Text(
                text = "Observed Symptoms",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                commonSymptoms.chunked(3).forEach { rowSymptoms ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowSymptoms.forEach { symptom ->
                            val isSelected = selectedSymptoms.contains(symptom)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (symptom == "None") {
                                        selectedSymptoms = setOf("None")
                                    } else {
                                        val newSet = selectedSymptoms.toMutableSet()
                                        newSet.remove("None")
                                        if (isSelected) newSet.remove(symptom) else newSet.add(symptom)
                                        selectedSymptoms = newSet
                                    }
                                },
                                label = { Text(symptom, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (symptom == "None") SuccessMint.copy(alpha = 0.2f) else AlertCoral.copy(alpha = 0.2f),
                                    selectedLabelColor = if (symptom == "None") SuccessMint else AlertCoral
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Clinical Notes / Context") },
                placeholder = { Text("e.g. Taken 10 mins post-breakfast. Well rested.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("health_notes_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = SlateOutline
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            Button(
                onClick = {
                    val dateFmt = SimpleDateFormat("MMM dd", Locale.US)
                    val timeFmt = SimpleDateFormat("hh:mm a", Locale.US)
                    val now = Date()

                    val symptomStr = if (selectedSymptoms.isEmpty()) "None" else selectedSymptoms.joinToString(", ")

                    onAddLog(
                        HealthLog(
                            timestamp = System.currentTimeMillis(),
                            dateDisplay = "Today",
                            timeDisplay = timeFmt.format(now),
                            systolicBp = systolic,
                            diastolicBp = diastolic,
                            heartRate = heartRate,
                            symptoms = symptomStr,
                            severity = severity.toInt(),
                            mood = selectedMood,
                            notes = notes.trim()
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_health_log_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Vitals & Symptom Log", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
