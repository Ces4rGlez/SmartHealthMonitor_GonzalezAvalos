// ui/viewmodel/DashboardViewModel.kt
package mx.utng.cfga.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC as LecturaUi
import mx.utng.cfga.smarthealthmonitor.data.models.MockData

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.fcActual)

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.pasosActual)

    // Conexión reactiva a Room -> mapeo a modelos de UI
    val historial: StateFlow<List<LecturaUi>> = SmartHealthRepository.obtenerHistorial()
        .map { listaDb ->
            listaDb.map { db ->
                // Formatear fecha a "dd MMM" (p. ej. "25 May") a partir del timestamp
                val fecha = try {
                    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(db.timestamp))
                } catch (e: Exception) {
                    ""
                }

                val estado = when {
                    db.esNormal -> "Normal"
                    db.valorBpm > 100 -> "Alto"
                    else -> "Bajo"
                }

                LecturaUi(
                    id = db.id,
                    fecha = fecha,
                    hora = db.hora,
                    bpm = db.valorBpm,
                    estado = estado
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}