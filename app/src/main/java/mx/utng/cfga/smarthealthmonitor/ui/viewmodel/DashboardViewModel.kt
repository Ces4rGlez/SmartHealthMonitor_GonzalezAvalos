// ui/viewmodel/DashboardViewModel.kt
package mx.utng.cfga.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.cfga.smarthealthmonitor.data.models.MockData

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(
            scope          = viewModelScope,
            started        = SharingStarted.WhileSubscribed(5_000),
            initialValue   = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(
            scope          = viewModelScope,
            started        = SharingStarted.WhileSubscribed(5_000),
            initialValue   = MockData.pasosActual
        )

    val historial = MockData.historialFC
}