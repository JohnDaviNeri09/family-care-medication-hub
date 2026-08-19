package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CareTask
import com.example.data.model.HealthLog
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FamilyCare Hub", appName)
  }

  @Test
  fun `verify health log bp category calculations`() {
    val normalLog = HealthLog(
        dateDisplay = "Today",
        timeDisplay = "08:00 AM",
        systolicBp = 118,
        diastolicBp = 76,
        heartRate = 70,
        symptoms = "None",
        severity = 1,
        mood = "😊 Good"
    )
    assertEquals("Normal", normalLog.bpCategory)

    val elevatedLog = HealthLog(
        dateDisplay = "Today",
        timeDisplay = "08:00 AM",
        systolicBp = 124,
        diastolicBp = 78,
        heartRate = 72,
        symptoms = "None",
        severity = 2,
        mood = "😊 Good"
    )
    assertEquals("Elevated", elevatedLog.bpCategory)

    val stage1Log = HealthLog(
        dateDisplay = "Today",
        timeDisplay = "08:00 AM",
        systolicBp = 136,
        diastolicBp = 86,
        heartRate = 75,
        symptoms = "Mild headache",
        severity = 3,
        mood = "😐 Fair"
    )
    assertEquals("Stage 1 HTN", stage1Log.bpCategory)
  }

  @Test
  fun `verify patient initials computation`() {
    assertEquals("RV", com.example.ui.components.getInitials("Robert Vance"))
    assertEquals("EB", com.example.ui.components.getInitials("Eleanor Brooks"))
    assertEquals("AL", com.example.ui.components.getInitials("Alice"))
    assertEquals("MS", com.example.ui.components.getInitials("Mary Jane Smith"))
    assertEquals("P", com.example.ui.components.getInitials(""))
  }
}

