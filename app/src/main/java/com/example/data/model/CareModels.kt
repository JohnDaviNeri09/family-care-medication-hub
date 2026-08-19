package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskCategory(val displayName: String, val iconName: String) {
    MEDICATION("Medication", "pill"),
    MEAL("Meal & Nutrition", "utensils"),
    APPOINTMENT("Appointment", "calendar"),
    VITALS("Vitals Check", "heartbeat"),
    ACTIVITY("Exercise & Care", "activity")
}

@Entity(tableName = "care_tasks")
data class CareTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // String representation of TaskCategory
    val scheduledTime: String, // e.g. "08:00 AM"
    val assignedCaregiver: String, // e.g. "Sarah (Daughter)"
    val dosageOrDetails: String, // e.g. "10mg with breakfast"
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val dayLabel: String = "Today", // "Today", "Yesterday", "Upcoming"
    val priority: String = "Normal" // "High", "Normal", "Low"
)

@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateDisplay: String, // e.g. "Aug 19", "Aug 18"
    val timeDisplay: String, // e.g. "08:30 AM"
    val systolicBp: Int, // e.g. 128
    val diastolicBp: Int, // e.g. 82
    val heartRate: Int, // e.g. 72
    val symptoms: String, // e.g. "Mild dizziness, slight ankle swelling"
    val severity: Int, // 1 to 10
    val mood: String, // "😊 Good", "😐 Fair", "😴 Fatigued", "😟 Anxious", "🤕 In Pain"
    val notes: String = ""
) {
    val bpCategory: String
        get() = when {
            systolicBp < 120 && diastolicBp < 80 -> "Normal"
            systolicBp in 120..129 && diastolicBp < 80 -> "Elevated"
            systolicBp in 130..139 || diastolicBp in 80..89 -> "Stage 1 HTN"
            systolicBp >= 140 || diastolicBp >= 90 -> "Stage 2 HTN"
            else -> "Standard"
        }
}

data class Caregiver(
    val id: String,
    val name: String,
    val relation: String,
    val phone: String,
    val roleDescription: String,
    val initials: String,
    val isPrimary: Boolean = false
)

data class DoctorBrief(
    val id: String = System.currentTimeMillis().toString(),
    val generatedAt: String,
    val executiveSummary: String,
    val complianceRateText: String,
    val vitalTrendsSummary: String,
    val criticalQuestions: List<String>,
    val fullRawText: String,
    val patientName: String = "Robert Vance",
    val patientAge: Int = 74,
    val compliancePercentage: Int = 85
)

enum class ChartMetricMode {
    BLOOD_PRESSURE,
    HEART_RATE,
    SEVERITY_INDEX
}
