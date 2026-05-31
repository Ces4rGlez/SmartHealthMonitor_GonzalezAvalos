package mx.utng.cfga.smarthealthmonitor.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {

    // Flujo para Frecuencia Cardíaca
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Flujo para Pasos Diarios
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
}