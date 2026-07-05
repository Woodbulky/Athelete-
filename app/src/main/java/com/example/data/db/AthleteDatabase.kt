package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AthleteDao
import com.example.data.model.*

@Database(
    entities = [
        DailyLog::class,
        CricketLog::class,
        FitnessLog::class,
        MatchLog::class,
        VideoLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AthleteDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao

    companion object {
        @Volatile
        private var INSTANCE: AthleteDatabase? = null

        fun getDatabase(context: Context): AthleteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AthleteDatabase::class.java,
                    "athlete_os_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
