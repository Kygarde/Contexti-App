package com.example.contextigo.domain.engine

import com.example.contextigo.data.local.VisitDao
import com.example.contextigo.domain.model.Recommendation

class RecommendationEngine(private val visitDao: VisitDao) {

    suspend fun evaluateContext(
        currentLocation: String,
        dayOfWeek: Int,
        hourOfDay: Int
    ): Recommendation? {
        val restaurantVisits = visitDao.getCategoryFrequency("RESTAURANTE")
        val uniVisits = visitDao.getCategoryFrequency("UNIVERSIDAD")

        // Regla 1: Entorno universitario durante horario académico
        if (currentLocation.contains("UNI", ignoreCase = true) || currentLocation.contains("Universidad", ignoreCase = true)) {
            return if (hourOfDay in 8..12) {
                Recommendation(
                    title = "Aviso Universitario",
                    description = "Atención: Tienes clase registrada. Recuerda verificar si la clase es presencial o virtual.",
                    category = "UNIVERSIDAD",
                    actionText = "Ver Horario"
                )
            } else {
                Recommendation(
                    title = "Librería Cercana",
                    description = "Hay una feria de libros y fotocopiadora abierta cerca del pabellón principal.",
                    category = "ACADEMICO",
                    actionText = "Ver Mapa"
                )
            }
        }

        // Regla 2: Patrón frecuente de restaurantes a la hora de almuerzo
        if (hourOfDay in 12..15 && restaurantVisits >= 3) {
            return Recommendation(
                title = "Sugerencia de Almuerzo",
                description = "Basado en tus visitas frecuentes, hay un restaurante ejecutivo cercano con menú por S/ 20.",
                category = "RESTAURANTE",
                actionText = "Ver Menú"
            )
        }

        // Recomendación general por defecto
        return Recommendation(
            title = "Lugar Frecuente Detectado",
            description = "Te encuentras en $currentLocation. Has visitado lugares de categoría similar $uniVisits veces esta semana.",
            category = "GENERAL",
            actionText = "Explorar"
        )
    }
}