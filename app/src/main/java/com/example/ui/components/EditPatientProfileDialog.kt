package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SlateTextTertiary
import com.example.ui.theme.TealBgLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

fun getInitials(name: String): String {
    val clean = name.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
    return when {
        clean.isEmpty() -> "P"
        clean.size == 1 -> clean[0].take(2).uppercase()
        else -> "${clean[0].first().uppercase()}${clean.last().first().uppercase()}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientProfileSheet(
    currentName: String,
    currentAge: Int,
    onDismiss: () -> Unit,
    onSaveProfile: (name: String, age: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var nameInput by remember { mutableStateOf(currentName) }
    var ageInput by remember { mutableStateOf(currentAge.toString()) }
    var isNameError by remember { mutableStateOf(false) }

    val liveInitials = remember(nameInput) { getInitials(nameInput) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface,
        modifier = Modifier.testTag("edit_patient_sheet")
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(TealBgLight, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "EDIT PATIENT PROFILE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Change Patient Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_edit_patient_button")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Preview Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SlateBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(TealPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = liveInitials,
                            color = TealPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nameInput.trim().ifBlank { "Patient Name" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (nameInput.isNotBlank()) SlateTextPrimary else SlateTextTertiary
                        )
                        val ageParsed = ageInput.toIntOrNull()
                        Text(
                            text = if (ageParsed != null && ageParsed in 1..120) "$ageParsed years old • Primary Care Recipient" else "Age not specified",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Patient Name Input Field
            Text(
                text = "Patient Full Name",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = nameInput,
                onValueChange = {
                    nameInput = it
                    if (it.isNotBlank()) isNameError = false
                },
                placeholder = { Text("e.g. Robert Vance or Eleanor Brooks") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary)
                },
                isError = isNameError,
                supportingText = {
                    if (isNameError) {
                        Text("Please enter a valid patient name", color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = SlateOutline,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Patient Age Input Field
            Text(
                text = "Patient Age (Years)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = ageInput,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 3) {
                        ageInput = input
                    }
                },
                placeholder = { Text("e.g. 74") },
                leadingIcon = {
                    Icon(Icons.Default.Cake, contentDescription = null, tint = TealPrimary)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (nameInput.isNotBlank()) {
                            val parsedAge = ageInput.toIntOrNull() ?: 74
                            onSaveProfile(nameInput.trim(), parsedAge)
                            onDismiss()
                        } else {
                            isNameError = true
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_age_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = SlateOutline
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons (Cancel / Save)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("cancel_edit_patient_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateOutline)
                ) {
                    Text("Cancel", color = SlateTextSecondary, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            val parsedAge = ageInput.toIntOrNull() ?: 74
                            onSaveProfile(nameInput.trim(), parsedAge)
                            onDismiss()
                        } else {
                            isNameError = true
                        }
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp)
                        .testTag("save_patient_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
