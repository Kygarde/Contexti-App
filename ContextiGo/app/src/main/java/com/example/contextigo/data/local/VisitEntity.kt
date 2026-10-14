package com.example.contextigo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_history")
data class VisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val locationName: String,
    val category: String, // Ejemplos: "UNIVERSIDAD", "RESTAURANTE", "GIMNASIO"
    val timestamp: Long,
    val dayOfWeek: Int,   // 1 = Domingo, 2 = Lunes, ..., 7 = Sábado
    val hourOfDay: Int    // Hora en formato 0 a 23
)