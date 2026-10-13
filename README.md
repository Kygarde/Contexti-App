# Contexti 📍🧠

> **Motor de Recomendaciones Contextuales y Adaptativas Basadas en Ubicación e Historial de Comportamiento**

[![Android](https://img.shields.io/badge/Platform-Android%208.0%2B-brightgreen.svg?style=for-the-badge&logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%201.9%2B-purple.svg?style=for-the-badge&logo=kotlin)](https://kotlinlang.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-blue.svg?style=for-the-badge&logo=androidstudio)](https://developer.android.com/topic/architecture)
[![License](https://img.shields.io/badge/License-MIT-orange.svg?style=for-the-badge)](LICENSE)

---

## 📌 Tabla de Contenidos
- [Acerca del Proyecto](#-acerca-del-proyecto)
- [Propuesta de Valor](#-propuesta-de-valor)
- [Características Principales](#-características-principales)
- [Arquitectura de Software](#-arquitectura-de-software)
- [Estrategia de Captura e Inferencia](#-estrategia-de-captura-e-inferencia)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Guía de Instalación y Configuración](#-guía-de-instalación-y-configuración)
- [Escenarios de Prueba y Simulación](#-escenarios-de-prueba-y-simulación)
- [Equipo y Licencia](#-equipo-y-licencia)

---

## 🧭 Acerca del Proyecto

**ContextiGo** es un sistema inteligente de recomendación móvil desarrollado en Android (Kotlin). A diferencia de las aplicaciones de navegación convencionales (como Waze o Google Maps) enfocas en el trazado de rutas paso a paso, **ContextiGo** opera en segundo plano para aprender de las rutinas, la frecuencia de visitas y las preferencias implícitas del usuario.

Mediante el análisis de patrones espaciotemporales (ventana histórica de comportamiento) y el contexto inmediato (día, hora, ubicación actual y desviación de la rutina habitual), la aplicación genera **sugerencias contextuales relevantes, comerciales e institucionales** en tiempo real.

---

## 💡 Propuesta de Valor

El sistema resuelve la sobrecarga de información y las notificaciones irrelevantes (*spam*) ofreciendo sugerencias hiperpersonalizadas:

1. **Ubicabilidad + Comportamiento:** No basta con saber dónde está el usuario; la app entiende *por qué* suele estar ahí y cómo se desvía de su comportamiento estándar.
2. **Recomendaciones Adaptativas:** Si el usuario modifica su ruta habitual (ej. no ingresa a su pabellón universitario a la hora programada), la app detecta la anomalía y ofrece opciones útiles de acuerdo a sus intereses.
3. **Privacidad Local (Privacy-First):** La acumulación de datos históricos y el cálculo de matrices de afinidad se realizan prioritariamente en el dispositivo.

---

## ✨ Características Principales

* **📍 Rastreos Ambientales de Bajo Consumo:** Monitoreo inteligente de ubicación mediante geocercas dinámicas (*Geofencing API*) y reconocimiento de actividad (*Activity Recognition*) para proteger la vida útil de la batería.
* **📊 Perfilado de Afinidad del Usuario:** Generación progresiva de puntuaciones de afinidad (restaurantes, librerías, eventos, etc.) a partir del historial de permanencia acumulado durante semanas.
* **🧠 Motor de Inferencia Contextual:** Evaluación de la tripleta `(Día, Hora, Ubicación Predicha)` para decidir la pertinencia de enviar una sugerencia.
* **🌐 Enriquecimiento Comercial Extorno:** Integración con *Google Places API* y APIs locales para identificar negocios, menús u ofertas cercanas acordes con el perfil económico y de hábitos del usuario.
* **🔔 Notificaciones Enriquecidas:** Alertas interactivas no intrusivas que ofrecen acciones concretas en pantalla.

---

## 🏗️ Arquitectura de Software

La aplicación está construida siguiendo los principios de **Clean Architecture** y el patrón de diseño **MVVM (Model-View-ViewModel)** recomendado por Google, garantizando la escalabilidad, mantenibilidad y la separación clara de responsabilidades.

```
┌────────────────────────────────────────────────────────┐
│                        CAPA UI                         │
│       (Jetpack Compose / ViewModels / Notifications)   │
└───────────────────────────┬────────────────────────────┘
                            │ (State / Events)
┌───────────────────────────▼────────────────────────────┐
│                    CAPA DE DOMINIO                     │
│            (Use Cases / Engine Recomendador)           │
└───────────────────────────┬────────────────────────────┘
                            │ (Repositories Interfaces)
┌───────────────────────────▼────────────────────────────┐
│                    CAPA DE DATOS                       │
│ ┌──────────────────────────┬─────────────────────────┐ │
│ │  Local Source (Room DB)  │ Remote Source (Retrofit)│ │
│ └──────────────────────────┴─────────────────────────┘ │
└────────────────────────────────────────────────────────┘
                            ▲
┌───────────────────────────┴────────────────────────────┐
│              SERVICIOS EN SEGUNDO PLANO                │
│    (FusedLocationClient + Geofencing + WorkManager)    │
└────────────────────────────────────────────────────────┘
```

### Descripción de Capas
* **UI Layer:** Construida con **Jetpack Compose**. Escucha estados inmutables expuestos por los `ViewModels` mediante `StateFlow`.
* **Domain Layer:** Contiene la lógica central del negocio y las reglas de inferencia (`GetContextualRecommendationUseCase`, `EvaluateRoutineAnomaliesUseCase`).
* **Data Layer:** Gestiona la abstracción de fuentes de datos. Utiliza **Room** para la persistencia del historial local de ubicaciones y **Retrofit** para comunicarse con APIs externas.
* **Background Layer:** Maneja tareas asíncronas y eventos en segundo plano usando **WorkManager** y **Coroutines**.

---

## ⚡ Estrategia de Captura e Inferencia

Para evitar el consumo excesivo de energía por muestreo GPS continuo:

1. **Activity Recognition:** Pausa el muestreo GPS cuando detecta que el usuario está estático.
2. **Geofencing Dinámico:** Registra áreas circulares en puntos de alta frecuencia (ej. Universidad, Centros Comerciales) para activar lecturas solo al cruzar sus límites.
3. **Filtering Rules:** Si el usuario no posee un historial de interés en una categoría específica o ha rechazado recomendaciones similares, el motor descarta la sugerencia.

---

## 🛠️ Stack Tecnológico

| Módulo / Función | Tecnología Utilizada |
| :--- | :--- |
| **Lenguaje Core** | [Kotlin](https://kotlinlang.org/) |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) |
| **Inyección de Dependencias** | [Hilt (Dagger)](https://dagger.dev/hilt/) |
| **Asincronía & Reactividad** | Kotlin Coroutines & `StateFlow` / `SharedFlow` |
| **Ubicación & Contexto** | Google Play Services (`FusedLocationProviderClient`, `GeofencingClient`, `ActivityRecognitionClient`) |
| **Base de Datos Local** | [Room Persistence Library](https://developer.android.com/training/data-storage/room) |
| **Conexión de Red** | Retrofit 2 + OkHttp3 + Kotlinx Serialization |
| **Programación de Tareas** | Jetpack WorkManager |

---

## 📁 Estructura del Proyecto

```text
com.example.contextigo/
├── data/
│   ├── local/            # Room Database, DAOs, Entidades (UserHistory, Affinities)
│   ├── remote/           # API Services, DTOs (Google Places API Client)
│   └── repository/       # Implementaciones concretas de Repositorios
├── domain/
│   ├── model/            # Modelos puros de dominio (Recommendation, UserProfile, Context)
│   ├── repository/       # Interfaces de repositorios
│   └── usecase/          # Casos de uso e inferencia de recomendaciones
├── ui/
│   ├── components/       # Componentes reutilizables de UI (Compose)
│   ├── home/             # Pantalla principal y resumen contextual
│   ├── preferences/      # Ajustes de perfil de usuario e intereses
│   └── theme/            # Colores, Tipografías y Temas
├── services/             # Background Services, Receivers para Geofencing y Location
└── di/                   # Módulos de inyección de dependencias con Hilt
```

---

## ⚙️ Guía de Instalación y Configuración

### Prerrequisitos
* **Android Studio** Ladybug (2024.2.1) o superior.
* SDK de Android compatible con **API Level 26 (Android 8.0 Oreo)** o superior.
* Dispositivo móvil físico o Emulador con Google Play Services activos.
* Una clave API válida para **Google Places API (New)**.

### Pasos de Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/ContextiGo.git
   cd ContextiGo
   ```

2. **Configurar credenciales locales:**
   Crea o edita el archivo `local.properties` en la raíz del proyecto y añade tu API Key:
   ```properties
   PLACES_API_KEY=TU_API_KEY_DE_GOOGLE_PLACES
   ```

3. **Sincronizar el proyecto:**
   Abre el proyecto en Android Studio y ejecuta **Sync Project with Gradle Files**.

4. **Ejecutar la aplicación:**
   Conecta tu dispositivo Android (asegúrate de habilitar la depuración USB y los permisos de ubicación exacta y en segundo plano) y presione **Run (Shift + F10)**.

---

## 🧪 Escenarios de Prueba y Simulación

Dado que el motor requiere un historial previo de frecuencia para generar recomendaciones, la app incluye un **Panel de Simulación para Pruebas (Mock Data Panel)**:

* **Inyección de Historial Sintético:** Permite cargar datos de comportamiento simulados de 3 semanas (rutinas de UNI, visitas a restaurantes) para probar las reglas de inferencia de inmediato.
* **Simulación de Ubicación Ficticia:** Permite emitir eventos de ubicación desde la interfaz de desarrollador interna para probar el disparo de Geofences y la entrega de notificaciones sin salir al exterior.

---

## 👥 Equipo y Licencia

* **Desarrolladores:** [Nombre de los Integrantes del Equipo]
* **Curso:** Proyecto Final de Carrera / Ingeniería de Software
* **Institución:** [Nombre de la Universidad]

Este proyecto está bajo la Licencia **MIT**. Consulta el archivo `LICENSE` para obtener más información.