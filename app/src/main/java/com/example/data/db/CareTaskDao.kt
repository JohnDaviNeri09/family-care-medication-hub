package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CareTask
import kotlinx.coroutines.flow.Flow

@Dao
interface CareTaskDao {
    @Query("SELECT * FROM care_tasks ORDER BY isCompleted ASC, id ASC")
    fun getAllTasks(): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks WHERE dayLabel = :day ORDER BY isCompleted ASC, id ASC")
    fun getTasksByDay(day: String): Flow<List<CareTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CareTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<CareTask>)

    @Update
    suspend fun updateTask(task: CareTask)

    @Delete
    suspend fun deleteTask(task: CareTask)

    @Query("DELETE FROM care_tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    @Query("UPDATE care_tasks SET isCompleted = :completed, completedAt = :time WHERE id = :taskId")
    suspend fun setTaskCompleted(taskId: Long, completed: Boolean, time: Long?)

    @Query("SELECT COUNT(*) FROM care_tasks")
    suspend fun getTaskCount(): Int

    @Query("SELECT * FROM care_tasks WHERE category = 'MEDICATION'")
    suspend fun getAllMedicationTasks(): List<CareTask>
}
