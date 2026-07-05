package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {

    // --- Daily Logs ---
    @Query("SELECT * FROM daily_logs ORDER BY timestamp DESC")
    fun getAllDailyLogs(): Flow<List<DailyLog>>

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    suspend fun getDailyLogByDate(date: String): DailyLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyLog)

    @Delete
    suspend fun deleteDailyLog(log: DailyLog)


    // --- Cricket Logs ---
    @Query("SELECT * FROM cricket_logs ORDER BY timestamp DESC")
    fun getAllCricketLogs(): Flow<List<CricketLog>>

    @Query("SELECT * FROM cricket_logs WHERE date = :date LIMIT 1")
    suspend fun getCricketLogByDate(date: String): CricketLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCricketLog(log: CricketLog)

    @Delete
    suspend fun deleteCricketLog(log: CricketLog)


    // --- Fitness Logs ---
    @Query("SELECT * FROM fitness_logs ORDER BY timestamp DESC")
    fun getAllFitnessLogs(): Flow<List<FitnessLog>>

    @Query("SELECT * FROM fitness_logs WHERE date = :date LIMIT 1")
    suspend fun getFitnessLogByDate(date: String): FitnessLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitnessLog(log: FitnessLog)

    @Delete
    suspend fun deleteFitnessLog(log: FitnessLog)


    // --- Match Logs ---
    @Query("SELECT * FROM match_logs ORDER BY timestamp DESC")
    fun getAllMatchLogs(): Flow<List<MatchLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchLog(log: MatchLog)

    @Delete
    suspend fun deleteMatchLog(log: MatchLog)


    // --- Video Logs ---
    @Query("SELECT * FROM video_logs ORDER BY timestamp DESC")
    fun getAllVideoLogs(): Flow<List<VideoLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoLog(log: VideoLog)

    @Delete
    suspend fun deleteVideoLog(log: VideoLog)
}
