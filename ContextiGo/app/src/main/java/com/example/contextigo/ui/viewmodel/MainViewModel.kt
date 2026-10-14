package com.example.contextigo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contextigo.data.local.AppDatabase
import com.example.contextigo.data.local.VisitEntity
import com.example.contextigo.domain.engine.RecommendationEngine
import com.example.contextigo.domain.model.Recommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val visitDao = db.visitDao()
    private val engine = RecommendationEngine(visitDao)

    val visits = visitDao.getAllVisits()

    private val _currentRecommendation = MutableStateFlow<Recommendation?>(null)
    val currentRecommendation: StateFlow<Recommendation?> = _currentRecommendation.asStateFlow()

    fun simulateArrival(locationName: String, category: String) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val visit = VisitEntity(
                locationName = locationName,
                category = category,
                timestamp = System.currentTimeMillis(),
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            )
            visitDao.insertVisit(visit)

            // Evaluar reglas de recomendación según el contexto
            val result = engine.evaluateContext(
                currentLocation = locationName,
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            )
            _currentRecommendation.value = result
        }
    }
}