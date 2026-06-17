package mx.utng.cfga.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WearDashboardViewModel : ViewModel() {

    // Flujo de Frecuencia Cardíaca reactivo usando el repositorio local
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) 72 else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 72)

    // Flujo de pasos reactivo
    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)


    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository.obtenerHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}