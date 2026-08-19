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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SlateTextTertiary
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight
import com.example.ui.theme.WarmAmber
import com.example.ui.theme.WarmAmberLight

@Composable
fun HealthLogCard(
    log: HealthLog,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bpBadgeColor, bpBadgeBg) = when (log.bpCategory) {
        "Normal" -> Pair(SuccessMint, SuccessMintLight)
        "Elevated" -> Pair(WarmAmber, WarmAmberLight)
        else -> Pair(AlertCoral, AlertCoralLight)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("health_log_card_${log.id}"),
        shape = RoundedCornerShape(20.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Date/Time + BP Category Badge + Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${log.dateDisplay} • ${log.timeDisplay}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = bpBadgeBg
                    ) {
                        Text(
                            text = log.bpCategory,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = bpBadgeColor,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_log_${log.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete entry",
                        tint = SlateTextTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics Grid (BP, Heart Rate, Severity)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // BP Metric Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = AlertCoralLight.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(12.dp))
                            Text("BLOOD PRESSURE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AlertCoral)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${log.systolicBp}/${log.diastolicBp}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SlateTextPrimary
                        )
                        Text("mmHg", fontSize = 10.sp, color = SlateTextSecondary)
                    }
                }

                // Heart Rate Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = WarmAmberLight.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = WarmAmber, modifier = Modifier.size(12.dp))
                            Text("PULSE RATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = WarmAmber)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${log.heartRate}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SlateTextPrimary
                        )
                        Text("bpm", fontSize = 10.sp, color = SlateTextSecondary)
                    }
                }

                // Severity / Mood Box
                Surface(
                    modifier = Modifier.weight(1.1f),
                    shape = RoundedCornerShape(10.dp),
                    color = TealPrimary.copy(alpha = 0.08f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("SEVERITY & MOOD", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${log.severity}/10 ${log.mood}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        // Severity color bar indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(SlateOutline.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = (log.severity / 10f).coerceIn(0.1f, 1f))
                                    .height(4.dp)
                                    .background(
                                        when {
                                            log.severity <= 3 -> SuccessMint
                                            log.severity <= 6 -> WarmAmber
                                            else -> AlertCoral
                                        },
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            // Symptoms & Notes
            if (log.symptoms.isNotBlank() && log.symptoms != "None") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Symptoms:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = log.symptoms,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (log.severity > 4) AlertCoral else SlateTextSecondary
                    )
                }
            }

            if (log.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"${log.notes}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
