package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoctorBrief
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.AlertCoralLight
import com.example.ui.theme.InfoIndigo
import com.example.ui.theme.InfoIndigoLight
import com.example.ui.theme.SlateBackground
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
import com.example.ui.theme.TealPrimaryLight
import com.example.ui.theme.WarmAmber
import com.example.ui.theme.WarmAmberLight

@Composable
fun DoctorBriefCard(
    brief: DoctorBrief?,
    isLoading: Boolean,
    onGenerate: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("doctor_brief_card"),
        shape = RoundedCornerShape(24.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Clinical Badge + Title + Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(TealPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = "Clinical Brief",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Clinical Doctor Brief",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = WarmAmberLight
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = WarmAmber, modifier = Modifier.size(10.dp))
                                    Text("Gemini AI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarmAmber)
                                }
                            }
                        }
                        Text(
                            text = "Physician Summary Engine • 3 Structured Sections",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button to Generate
            Button(
                onClick = onGenerate,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_doctor_brief_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compiling 7-Day Care Payload & Generating...", fontWeight = FontWeight.SemiBold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (brief != null) "Regenerate Doctor Brief" else "Generate Doctor Brief", fontWeight = FontWeight.Bold)
                }
            }

            if (brief != null && !isLoading) {
                Spacer(modifier = Modifier.height(16.dp))

                // Patient Banner & Generated Timestamp
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SlateBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateOutline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Patient: ${brief.patientName} (${brief.patientAge} y/o)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Generated: ${brief.generatedAt}",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        // Copy & Share buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(brief.fullRawText))
                                    onShowToast("Doctor brief copied to clipboard!")
                                },
                                modifier = Modifier.size(36.dp).testTag("copy_brief_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TealPrimary, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, brief.fullRawText)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Doctor Brief")
                                    context.startActivity(shareIntent)
                                },
                                modifier = Modifier.size(36.dp).testTag("share_brief_button")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = TealPrimary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SECTION 1: Executive Summary & Compliance Rate
                SectionCard(
                    sectionNumber = 1,
                    title = "Executive Summary & Compliance Rate",
                    icon = Icons.Default.Summarize,
                    accentColor = TealPrimary,
                    bgColor = TealPrimaryLight.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = brief.executiveSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextPrimary,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SECTION 2: Key Vital Trends & Symptoms
                SectionCard(
                    sectionNumber = 2,
                    title = "Key Vital Trends & Symptoms",
                    icon = Icons.AutoMirrored.Filled.ShowChart,
                    accentColor = WarmAmber,
                    bgColor = WarmAmberLight.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = brief.vitalTrendsSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextPrimary,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SECTION 3: Critical Questions for the Next Doctor Visit
                SectionCard(
                    sectionNumber = 3,
                    title = "Critical Questions for Doctor Visit",
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    accentColor = InfoIndigo,
                    bgColor = InfoIndigoLight.copy(alpha = 0.5f)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        brief.criticalQuestions.forEachIndexed { idx, q ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = InfoIndigo,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${idx + 1}",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = q,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SlateTextPrimary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            } else if (brief == null && !isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SlateBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tap above to compile today's care tasks, missed/pending medications, and the last 7 days of vital logs into an actionable, physician-ready report.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    sectionNumber: Int,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    bgColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = bgColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                        Text(
                            text = "SECTION $sectionNumber",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
