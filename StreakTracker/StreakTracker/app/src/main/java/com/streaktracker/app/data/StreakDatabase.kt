package com.streaktracker.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DayEntity::class], version = 1, exportSchema = false)
abstract class StreakDatabase : RoomDatabase() {
    abstract fun dayDao(): DayDao

    companion object {
        @Volatile private var INSTANCE: StreakDatabase? = null

        fun getInstance(context: Context): StreakDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StreakDatabase::class.java,
                    "streak_database"
                ).build().also { INSTANCE = it }
            }
    }
}
