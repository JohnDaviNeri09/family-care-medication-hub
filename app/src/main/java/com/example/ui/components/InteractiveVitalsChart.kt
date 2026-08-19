package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChartMetricMode
import com.example.data.model.HealthLog
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.InfoIndigo
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateOutline
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.theme.WarmAmber

@Composable
fun InteractiveVitalsChart(
    logs: List<HealthLog>,
    metricMode: ChartMetricMode,
    modifier: Modifier = Modifier
) {
    // Sort logs oldest to newest for chronological left-to-right display
    val sortedLogs = remember(logs) {
        logs.sortedBy { it.timestamp }.takeLast(7)
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val selectedLog = selectedIndex?.let { if (it in sortedLogs.indices) sortedLogs[it] else null }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_vitals_chart"),
        shape = RoundedCornerShape(24.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Chart Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (metricMode) {
                        ChartMetricMode.BLOOD_PRESSURE -> "7-Day BP Trends (mmHg)"
                        ChartMetricMode.HEART_RATE -> "7-Day Pulse Trends (bpm)"
                        ChartMetricMode.SEVERITY_INDEX -> "7-Day Symptom Severity (1-10)"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (metricMode) {
                        ChartMetricMode.BLOOD_PRESSURE -> {
                            LegendItem(color = AlertCoral, label = "Systolic")
                            LegendItem(color = TealSecondary, label = "Diastolic")
                        }
                        ChartMetricMode.HEART_RATE -> {
                            LegendItem(color = WarmAmber, label = "Pulse bpm")
                            LegendItem(color = SuccessMint.copy(alpha = 0.6f), label = "Normal (60-100)")
                        }
                        ChartMetricMode.SEVERITY_INDEX -> {
                            LegendItem(color = InfoIndigo, label = "Severity Level")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (sortedLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(SlateBackground, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No vital records available to plot yet.", color = SlateTextSecondary)
                }
            } else {
                // Main Chart Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .pointerInput(sortedLogs) {
                                detectTapGestures { offset ->
                                    val count = sortedLogs.size
                                    if (count > 0) {
                                        val horizontalPadding = 50f
                                        val usableWidth = size.width - (horizontalPadding * 2)
                                        val step = if (count > 1) usableWidth / (count - 1) else 0f

                                        var closestIdx = 0
                                        var minDistance = Float.MAX_VALUE
                                        for (i in 0 until count) {
                                            val x = horizontalPadding + (i * step)
                                            val distance = kotlin.math.abs(offset.x - x)
                                            if (distance < minDistance) {
                                                minDistance = distance
                                                closestIdx = i
                                            }
                                        }
                                        selectedIndex = if (selectedIndex == closestIdx) null else closestIdx
                                    }
                                }
                            }
                    ) {
                        val count = sortedLogs.size
                        val leftPad = 60f
                        val rightPad = 40f
                        val topPad = 30f
                        val botPad = 45f

                        val chartWidth = size.width - leftPad - rightPad
                        val chartHeight = size.height - topPad - botPad

                        // Determine Y scale min/max
                        val (minY, maxY) = when (metricMode) {
                            ChartMetricMode.BLOOD_PRESSURE -> {
                                val minSystolic = sortedLogs.minOfOrNull { it.diastolicBp } ?: 60
                                val maxSystolic = sortedLogs.maxOfOrNull { it.systolicBp } ?: 150
                                Pair(minOf(60f, (minSystolic - 10).toFloat()), maxOf(160f, (maxSystolic + 15).toFloat()))
                            }
                            ChartMetricMode.HEART_RATE -> {
                                val minHr = sortedLogs.minOfOrNull { it.heartRate } ?: 55
                                val maxHr = sortedLogs.maxOfOrNull { it.heartRate } ?: 100
                                Pair(minOf(50f, (minHr - 10).toFloat()), maxOf(115f, (maxHr + 15).toFloat()))
                            }
                            ChartMetricMode.SEVERITY_INDEX -> Pair(0f, 10f)
                        }

                        fun yPos(value: Float): Float {
                            val ratio = (value - minY) / (maxY - minY).coerceAtLeast(1f)
                            return topPad + (chartHeight * (1f - ratio))
                        }

                        fun xPos(index: Int): Float {
                            return if (count > 1) {
                                leftPad + (index * (chartWidth / (count - 1)))
                            } else {
                                leftPad + (chartWidth / 2)
                            }
                        }

                        // Draw Grid lines & labels
                        val gridSteps = 4
                        val stepVal = (maxY - minY) / gridSteps
                        for (i in 0..gridSteps) {
                            val v = minY + (i * stepVal)
                            val y = yPos(v)
                            drawLine(
                                color = SlateOutline.copy(alpha = 0.4f),
                                start = Offset(leftPad, y),
                                end = Offset(size.width - rightPad, y),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                            )
                        }

                        // Normal Zone Background Shading
                        if (metricMode == ChartMetricMode.BLOOD_PRESSURE) {
                            val yTop120 = yPos(120f)
                            val yBot80 = yPos(80f)
                            drawRect(
                                color = SuccessMint.copy(alpha = 0.08f),
                                topLeft = Offset(leftPad, yTop120),
                                size = Size(chartWidth, (yBot80 - yTop120).coerceAtLeast(4f))
                            )
                        } else if (metricMode == ChartMetricMode.HEART_RATE) {
                            val yTop100 = yPos(100f)
                            val yBot60 = yPos(60f)
                            drawRect(
                                color = SuccessMint.copy(alpha = 0.08f),
                                topLeft = Offset(leftPad, yTop100),
                                size = Size(chartWidth, (yBot60 - yTop100).coerceAtLeast(4f))
                            )
                        }

                        // Draw Path for Data
                        when (metricMode) {
                            ChartMetricMode.BLOOD_PRESSURE -> {
                                // Systolic Line
                                val sysPath = Path()
                                val diaPath = Path()

                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val ySys = yPos(log.systolicBp.toFloat())
                                    val yDia = yPos(log.diastolicBp.toFloat())

                                    if (i == 0) {
                                        sysPath.moveTo(x, ySys)
                                        diaPath.moveTo(x, yDia)
                                    } else {
                                        val prevX = xPos(i - 1)
                                        val prevYSys = yPos(sortedLogs[i - 1].systolicBp.toFloat())
                                        val prevYDia = yPos(sortedLogs[i - 1].diastolicBp.toFloat())
                                        val cX = (prevX + x) / 2f
                                        sysPath.cubicTo(cX, prevYSys, cX, ySys, x, ySys)
                                        diaPath.cubicTo(cX, prevYDia, cX, yDia, x, yDia)
                                    }
                                }

                                drawPath(
                                    path = sysPath,
                                    color = AlertCoral,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawPath(
                                    path = diaPath,
                                    color = TealSecondary,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )

                                // Data Points
                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val ySys = yPos(log.systolicBp.toFloat())
                                    val yDia = yPos(log.diastolicBp.toFloat())
                                    val isSelected = selectedIndex == i

                                    drawCircle(
                                        color = Color.White,
                                        radius = if (isSelected) 8.dp.toPx() else 5.dp.toPx(),
                                        center = Offset(x, ySys)
                                    )
                                    drawCircle(
                                        color = AlertCoral,
                                        radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                                        center = Offset(x, ySys)
                                    )

                                    drawCircle(
                                        color = Color.White,
                                        radius = if (isSelected) 8.dp.toPx() else 5.dp.toPx(),
                                        center = Offset(x, yDia)
                                    )
                                    drawCircle(
                                        color = TealSecondary,
                                        radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                                        center = Offset(x, yDia)
                                    )

                                    if (isSelected) {
                                        drawLine(
                                            color = TealPrimary.copy(alpha = 0.5f),
                                            start = Offset(x, topPad),
                                            end = Offset(x, topPad + chartHeight),
                                            strokeWidth = 1.5.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                        )
                                    }
                                }
                            }
                            ChartMetricMode.HEART_RATE -> {
                                val hrPath = Path()
                                val fillPath = Path()

                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val yHr = yPos(log.heartRate.toFloat())

                                    if (i == 0) {
                                        hrPath.moveTo(x, yHr)
                                        fillPath.moveTo(x, topPad + chartHeight)
                                        fillPath.lineTo(x, yHr)
                                    } else {
                                        val prevX = xPos(i - 1)
                                        val prevY = yPos(sortedLogs[i - 1].heartRate.toFloat())
                                        val cX = (prevX + x) / 2f
                                        hrPath.cubicTo(cX, prevY, cX, yHr, x, yHr)
                                        fillPath.cubicTo(cX, prevY, cX, yHr, x, yHr)
                                    }
                                    if (i == count - 1) {
                                        fillPath.lineTo(x, topPad + chartHeight)
                                        fillPath.close()
                                    }
                                }

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(WarmAmber.copy(alpha = 0.25f), WarmAmber.copy(alpha = 0.02f)),
                                        startY = topPad,
                                        endY = topPad + chartHeight
                                    )
                                )

                                drawPath(
                                    path = hrPath,
                                    color = WarmAmber,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )

                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val y = yPos(log.heartRate.toFloat())
                                    val isSelected = selectedIndex == i

                                    drawCircle(
                                        color = Color.White,
                                        radius = if (isSelected) 8.dp.toPx() else 5.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                    drawCircle(
                                        color = WarmAmber,
                                        radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                                        center = Offset(x, y)
                                    )

                                    if (isSelected) {
                                        drawLine(
                                            color = WarmAmber.copy(alpha = 0.6f),
                                            start = Offset(x, topPad),
                                            end = Offset(x, topPad + chartHeight),
                                            strokeWidth = 1.5.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                        )
                                    }
                                }
                            }
                            ChartMetricMode.SEVERITY_INDEX -> {
                                val sevPath = Path()
                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val ySev = yPos(log.severity.toFloat())
                                    if (i == 0) sevPath.moveTo(x, ySev)
                                    else {
                                        val prevX = xPos(i - 1)
                                        val prevY = yPos(sortedLogs[i - 1].severity.toFloat())
                                        val cX = (prevX + x) / 2f
                                        sevPath.cubicTo(cX, prevY, cX, ySev, x, ySev)
                                    }
                                }

                                drawPath(
                                    path = sevPath,
                                    color = InfoIndigo,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )

                                sortedLogs.forEachIndexed { i, log ->
                                    val x = xPos(i)
                                    val y = yPos(log.severity.toFloat())
                                    val isSelected = selectedIndex == i

                                    drawCircle(
                                        color = Color.White,
                                        radius = if (isSelected) 8.dp.toPx() else 5.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                    drawCircle(
                                        color = if (log.severity > 5) AlertCoral else InfoIndigo,
                                        radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                }
                            }
                        }
                    }
                }

                // X-Axis Labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    sortedLogs.forEachIndexed { index, log ->
                        Text(
                            text = log.dateDisplay,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selectedIndex == index) TealPrimary else SlateTextSecondary,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Interactive Tooltip Callout
            AnimatedVisibility(visible = selectedLog != null) {
                selectedLog?.let { log ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SlateBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${log.dateDisplay} at ${log.timeDisplay}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (log.bpCategory) {
                                        "Normal" -> SuccessMint.copy(alpha = 0.15f)
                                        "Elevated" -> WarmAmber.copy(alpha = 0.15f)
                                        else -> AlertCoral.copy(alpha = 0.15f)
                                    }
                                ) {
                                    Text(
                                        text = log.bpCategory,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (log.bpCategory) {
                                            "Normal" -> SuccessMint
                                            "Elevated" -> WarmAmber
                                            else -> AlertCoral
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "BP: ${log.systolicBp}/${log.diastolicBp} mmHg",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = "Pulse: ${log.heartRate} bpm",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = "Severity: ${log.severity}/10 ${log.mood}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SlateTextSecondary
                                )
                            }

                            if (log.symptoms.isNotBlank() && log.symptoms != "None") {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Symptoms: ${log.symptoms}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AlertCoral
                                )
                            }

                            if (log.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Notes: ${log.notes}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SlateTextSecondary,
            fontSize = 11.sp
        )
    }
}
