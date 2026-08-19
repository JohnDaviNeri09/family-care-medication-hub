package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.HealthLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthLogDao {
    @Query("SELECT * FROM health_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<HealthLog>>

    @Query("SELECT * FROM health_logs ORDER BY timestamp ASC")
    fun getAllLogsAscending(): Flow<List<HealthLog>>

    @Query("SELECT * FROM health_logs ORDER BY timestamp DESC LIMIT 7")
    fun getRecent7Logs(): Flow<List<HealthLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HealthLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<HealthLog>)

    @Delete
    suspend fun deleteLog(log: HealthLog)

    @Query("DELETE FROM health_logs WHERE id = :logId")
    suspend fun deleteLogById(logId: Long)

    @Query("SELECT COUNT(*) FROM health_logs")
    suspend fun getLogCount(): Int
}
