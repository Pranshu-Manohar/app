package com.streaktracker.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Query("SELECT * FROM days ORDER BY epochDay ASC")
    fun observeAll(): Flow<List<DayEntity>>

    @Query("SELECT * FROM days WHERE epochDay = :epochDay LIMIT 1")
    suspend fun getByEpochDay(epochDay: Long): DayEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(day: DayEntity)

    @Query("DELETE FROM days")
    suspend fun clearAll()
}
