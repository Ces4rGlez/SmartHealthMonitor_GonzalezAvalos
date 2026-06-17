# SmartHealth Monitor
![Android CI](https://img.shields.io/badge/Android-API26+-green)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-blue)

Aplicación Android de monitoreo de salud personal en tiempo real orientada a dispositivos móviles y wearables (Wear OS). Proyecto desarrollado en arquitectura limpia y componentes declarativos nativos.

Desarrollada como proyecto integrador — UTNG 9° Cuatrimestre 2026.

## 🛠️ Stack Tecnológico
| Tecnología | Uso / Aplicación |
|---|---|
| **Kotlin + Jetpack Compose** | Construcción de UI declarativa nativa con Material Design 3. |
| **Wearable Data Layer API** | Canal de comunicación bidireccional mediante Bluetooth Low Energy (BLE). |
| **Health Services API** | Suscripción y lectura al sensor de frecuencia cardíaca continuo en background (Wear OS). |
| **Room Database** | Persistencia local y reactiva de los datos históricos recolectados del reloj. |
| **Jetpack Navigation** | Control de flujos de navegación estructurado por medio de un `NavHost`. |
| **Conventional Commits** | Estandarización estricta del historial de control de versiones. |

## 📱 Arquitectura de Pantallas
| Pantalla | Descripción de Funcionalidades |
|---|---|
| **LoginScreen** | Control de acceso seguro con validaciones de campos de texto en tiempo real. |
| **DashboardScreen** | Panel de control primario que recolecta flujos de datos (`StateFlow`) del Wear OS. |
| **HistorialScreen** | Listado inteligente de lecturas persistidas desde Room usando recolectores reactivos. |
| **AlertaScreen** | Cuadro de diálogo de confirmación Material Design 3 con envío de notas añadidas ante emergencias. |

## 📸 Evidencia de Interfaces (Unidad I)
| Autenticación | Panel Principal |
|--- |--- |
| ![Login](screenshots/login.png) | ![Dashboard](screenshots/dashboard.png) |

| Historial de Lecturas Room | Confirmación Crítica |
|--- |--- |
| ![Historial](screenshots/historial.png) | ![Alerta](screenshots/alerta.png) |


## Unidad II — Aplicaciones para Wearables (Compose for Wear OS)

| Pantalla | Descripción |
|---|---|
| **WearDashboardScreen** | Despliegue de frecuencia cardíaca e indicador de pasos en tiempo real implementando `ScalingLazyColumn` y ocultación fluida de `TimeText`. |
| **WearHistorialScreen** | Listado de mediciones pasadas integrado con Room persistente, utilizando el control físico de la corona del reloj (`.rotaryScrollable`) y anclaje por magnetismo (*Snap*). |
| **WearAlertaScreen** | Interfaz optimizada con botones de confirmación y cancelación preparados para diálogos rápidos en situaciones de emergencia. |
| **SmartHealth WatchFace** | Pantalla de inicio nativa del reloj que dibuja de forma optimizada la hora, segundos y el último registro cardíaco, incluyendo atenuación energética para el modo Always-On Display. |

![WatchFace](screenshots/watchface.png)
![WearDashboard](screenshots/wear_dashboard.png)


## ✒️ Autor
* **César Fernando González Ávalos** - Estudiante de Ingeniería en Desarrollo y Gestión de Software
* *Universidad Tecnológica del Norte de Guanajuato (UTNG)*