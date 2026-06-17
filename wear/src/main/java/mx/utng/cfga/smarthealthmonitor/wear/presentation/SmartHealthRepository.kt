package mx.utng.cfga.smarthealthmonitor.wear.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _historialFlow = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historialFlow: StateFlow<List<LecturaFC>> = _historialFlow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Añadir a historial local (mantener las últimas 20)
        val nuevaLectura = LecturaFC(valorBpm = bpm)
        val listaActual = _historialFlow.value.toMutableList()
        listaActual.add(0, nuevaLectura)
        if (listaActual.size > 20) listaActual.removeAt(listaActual.size - 1)
        _historialFlow.value = listaActual
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial() = historialFlow
}