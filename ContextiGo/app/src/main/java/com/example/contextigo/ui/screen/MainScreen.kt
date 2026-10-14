package com.example.contextigo.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contextigo.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val visits by viewModel.visits.collectAsState(initial = emptyList())
    val recommendation by viewModel.currentRecommendation.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("ContextiGo - MVP") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Simulador de Entorno / Ubicación", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { viewModel.simulateArrival("UNI Pabellón Central", "UNIVERSIDAD") }) {
                    Text("Llegar a UNI")
                }
                Button(onClick = { viewModel.simulateArrival("Restaurante Plaza", "RESTAURANTE") }) {
                    Text("Ir a Comer")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Recomendación Contextual
            recommendation?.let { rec ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = rec.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = rec.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /* Acción adicional */ }) {
                            Text(rec.actionText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Histórico de Visitas (Room DB)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(visits) { visit ->
                    ListItem(
                        headlineContent = { Text(visit.locationName) },
                        supportingContent = { Text("Categoría: ${visit.category} | Hora: ${visit.hourOfDay}:00") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}