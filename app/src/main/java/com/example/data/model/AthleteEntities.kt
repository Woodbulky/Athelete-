package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_logs")
data class DailyLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val wakeTime: String = "",
    val sleepDuration: Float = 0f, // in hours
    val sleepQuality: Int = 0, // 1 to 5
    val meals: String = "", // Description of meals
    val waterIntake: Float = 0f, // in liters
    val supplements: String = "",
    val mood: Int = 3, // 1 to 5
    val stress: Int = 3, // 1 to 5
    val energy: Int = 3, // 1 to 5
    val recovery: Int = 3 // 1 to 5
)

@Entity(tableName = "cricket_logs")
data class CricketLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val battingDuration: Int = 0, // in minutes
    val battingFocus: String = "",
    val bowlingWorkload: Int = 0, // in overs
    val bowlingFocus: String = "",
    val fieldingDuration: Int = 0, // in minutes
    val fieldingFocus: String = "",
    val coachFeedback: String = "",
    val strengths: String = "",
    val weaknesses: String = ""
)

@Entity(tableName = "fitness_logs")
data class FitnessLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val gymSession: String = "", // e.g. "Leg Day", "Upper Body Power"
    val gymDuration: Int = 0, // in minutes
    val runningDistance: Float = 0f, // in km
    val runningDuration: Int = 0, // in minutes
    val mobilityDuration: Int = 0, // in minutes
    val recoveryActivity: String = "", // e.g. "Ice bath", "Foam rolling"
    val injuries: String = "" // Pain points or injury notes
)

@Entity(tableName = "match_logs")
data class MatchLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val matchName: String = "",
    val format: String = "T20", // T20, ODI, Test, Nets Match
    val role: String = "All-rounder", // Batsman, Bowler, All-rounder, Wicketkeeper
    val runsScored: Int = 0,
    val ballsFaced: Int = 0,
    val wicketsTaken: Int = 0,
    val oversBowled: Float = 0f,
    val runsConceded: Int = 0,
    val fieldingPerformance: String = "", // catches, run-outs, etc.
    val matchResult: String = "" // Win, Loss, Draw
)

@Entity(tableName = "video_logs")
data class VideoLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val videoUri: String = "", // Reference to a video (local filename or label)
    val analysisNotes: String = "" // Technical analysis notes (e.g., "head falling away")
)
