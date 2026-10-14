package com.example.contextigo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Insert
    suspend fun insertVisit(visit: VisitEntity)

    @Query("SELECT * FROM visit_history ORDER BY timestamp DESC")
    fun getAllVisits(): Flow<List<VisitEntity>>

    @Query("SELECT COUNT(*) FROM visit_history WHERE category = :category")
    suspend fun getCategoryFrequency(category: String): Int
}