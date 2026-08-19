package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareTask
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.AlertCoralLight
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SlateTextTertiary
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintLight
import com.example.ui.theme.TealBgLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.theme.WarmAmber
import com.example.ui.theme.WarmAmberLight

@Composable
fun ComplianceSummaryCard(
    tasks: List<CareTask>,
    modifier: Modifier = Modifier
) {
    val totalCount = tasks.size
    val completedCount = tasks.count { it.isCompleted }
    val pendingCount = totalCount - completedCount
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 1f
    val percentage = (progressFraction * 100).toInt()

    val pendingMeds = tasks.filter { !it.isCompleted && it.category.contains("MEDICATION", ignoreCase = true) }
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 600),
        label = "progress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("compliance_summary_card"),
        shape = RoundedCornerShape(24.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Daily Progress + percentage and X of Y tasks done
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Daily Progress",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$percentage%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SlateTextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$completedCount OF $totalCount TASKS DONE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .size(width = 140.dp, height = 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(SlateBorderLight)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = animatedProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(TealPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task Stat Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Completed
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = SuccessMintLight.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessMint.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessMint, modifier = Modifier.size(15.dp))
                        Column {
                            Text(text = "$completedCount Done", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SuccessMint)
                            Text(text = "Verified", fontSize = 9.sp, color = SlateTextSecondary)
                        }
                    }
                }

                // Pending
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = WarmAmberLight.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmAmber.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.PendingActions, contentDescription = null, tint = WarmAmber, modifier = Modifier.size(15.dp))
                        Column {
                            Text(text = "$pendingCount Pending", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WarmAmber)
                            Text(text = "Remaining", fontSize = 9.sp, color = SlateTextSecondary)
                        }
                    }
                }

                // Meds Alert
                Surface(
                    modifier = Modifier.weight(1.1f),
                    shape = RoundedCornerShape(14.dp),
                    color = if (pendingMeds.isNotEmpty()) AlertCoralLight.copy(alpha = 0.6f) else TealBgLight,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (pendingMeds.isNotEmpty()) AlertCoral.copy(alpha = 0.2f) else TealPrimary.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (pendingMeds.isNotEmpty()) Icons.Default.WarningAmber else Icons.Default.Medication,
                            contentDescription = null,
                            tint = if (pendingMeds.isNotEmpty()) AlertCoral else TealPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Column {
                            Text(
                                text = if (pendingMeds.isNotEmpty()) "${pendingMeds.size} Meds Due" else "Meds Up to Date",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (pendingMeds.isNotEmpty()) AlertCoral else TealPrimary
                            )
                            Text(
                                text = if (pendingMeds.isNotEmpty()) "Action required" else "All taken",
                                fontSize = 9.sp,
                                color = SlateTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

