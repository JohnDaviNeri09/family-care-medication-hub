package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareTask
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SlateTextTertiary
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WarmAmber

@Composable
fun TaskCard(
    task: CareTask,
    onToggleCompleted: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val catIcon = when (task.category.uppercase()) {
        "MEDICATION" -> Icons.Default.Medication
        "MEAL" -> Icons.Default.Restaurant
        "APPOINTMENT" -> Icons.Default.Event
        "VITALS" -> Icons.Default.Favorite
        else -> Icons.AutoMirrored.Filled.DirectionsWalk
    }

    val cardBg = if (task.isCompleted) SlateSurfaceVariant else SlateSurface
    val borderStroke = if (task.isCompleted) {
        androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight)
    } else {
        androidx.compose.foundation.BorderStroke(1.5.dp, SlateOutline)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleCompleted(!task.isCompleted) }
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(20.dp),
        color = cardBg,
        border = borderStroke,
        shadowElevation = if (task.isCompleted) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rounded Icon Box (teal if completed, slate-100 if pending)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (task.isCompleted) TealPrimary else SlateBorderLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = catIcon,
                    contentDescription = task.category,
                    tint = if (task.isCompleted) Color.White else SlateTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Task Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        fontSize = 14.sp
                    )
                    if (task.priority == "High" && !task.isCompleted) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AlertCoral.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "HIGH",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertCoral
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                if (task.isCompleted) {
                    Text(
                        text = "Taken at ${task.scheduledTime} • ${task.assignedCaregiver}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextSecondary,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Pending • ${task.scheduledTime} • ${task.assignedCaregiver}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = WarmAmber,
                        fontSize = 12.sp
                    )
                }

                if (task.dosageOrDetails.isNotBlank()) {
                    Text(
                        text = task.dosageOrDetails,
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextTertiary,
                        fontSize = 11.sp
                    )
                }
            }

            // Check State Icon
            IconButton(
                onClick = { onToggleCompleted(!task.isCompleted) },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("task_checkbox_${task.id}")
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = TealPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Pending",
                        tint = SlateOutline,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Delete Action Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(32.dp)
                    .testTag("delete_task_${task.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete task",
                    tint = SlateTextTertiary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

