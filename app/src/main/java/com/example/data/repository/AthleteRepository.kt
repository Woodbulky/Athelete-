package com.example.data.repository

import com.example.data.dao.AthleteDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class AthleteRepository(private val athleteDao: AthleteDao) {

    // --- Daily Logs ---
    val allDailyLogs: Flow<List<DailyLog>> = athleteDao.getAllDailyLogs()
    
    suspend fun getDailyLogByDate(date: String): DailyLog? = athleteDao.getDailyLogByDate(date)
    
    suspend fun insertDailyLog(log: DailyLog) = athleteDao.insertDailyLog(log)
    
    suspend fun deleteDailyLog(log: DailyLog) = athleteDao.deleteDailyLog(log)


    // --- Cricket Logs ---
    val allCricketLogs: Flow<List<CricketLog>> = athleteDao.getAllCricketLogs()
    
    suspend fun getCricketLogByDate(date: String): CricketLog? = athleteDao.getCricketLogByDate(date)
    
    suspend fun insertCricketLog(log: CricketLog) = athleteDao.insertCricketLog(log)
    
    suspend fun deleteCricketLog(log: CricketLog) = athleteDao.deleteCricketLog(log)


    // --- Fitness Logs ---
    val allFitnessLogs: Flow<List<FitnessLog>> = athleteDao.getAllFitnessLogs()
    
    suspend fun getFitnessLogByDate(date: String): FitnessLog? = athleteDao.getFitnessLogByDate(date)
    
    suspend fun insertFitnessLog(log: FitnessLog) = athleteDao.insertFitnessLog(log)
    
    suspend fun deleteFitnessLog(log: FitnessLog) = athleteDao.deleteFitnessLog(log)


    // --- Match Logs ---
    val allMatchLogs: Flow<List<MatchLog>> = athleteDao.getAllMatchLogs()
    
    suspend fun insertMatchLog(log: MatchLog) = athleteDao.insertMatchLog(log)
    
    suspend fun deleteMatchLog(log: MatchLog) = athleteDao.deleteMatchLog(log)


    // --- Video Logs ---
    val allVideoLogs: Flow<List<VideoLog>> = athleteDao.getAllVideoLogs()
    
    suspend fun insertVideoLog(log: VideoLog) = athleteDao.insertVideoLog(log)
    
    suspend fun deleteVideoLog(log: VideoLog) = athleteDao.deleteVideoLog(log)
}
