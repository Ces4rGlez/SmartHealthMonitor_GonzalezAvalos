package mx.utng.cfga.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.models.MockData
import mx.utng.cfga.smarthealthmonitor.data.mqtt.TvMessage
import mx.utng.cfga.smarthealthmonitor.data.remote.LecturaFcDto
import mx.utng.cfga.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.cfga.smarthealthmonitor.tv.mqtt.MqttTvSubscriber

data class TvState(
    val lecturas: List<LecturaFC> = MockData.historialFC,
    val estadisticas: List<LecturaFC> = emptyList(),
    val fcActual: Int? = null,
    val fcEstado: String? = null,
    val ultimaHora: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class TvViewModel(private val context: Context) : ViewModel() {
    private val neonRepo = TvNeonRepository()
    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()

    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    init {
        mqttSubscriber.connect()
        cargarDatos()
        
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                if (tvMsg != null) {
                    val nuevaLectura = LecturaFC(
                        id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                        fecha = tvMsg.fecha,
                        hora = tvMsg.hora,
                        bpm = tvMsg.bpm,
                        estado = tvMsg.estado
                    )
                    
                    _state.update { currentState ->
                        val listaActualizada = currentState.lecturas.toMutableList()
                        listaActualizada.add(0, nuevaLectura)
                        if (listaActualizada.size > 15) listaActualizada.removeAt(listaActualizada.size - 1)
                        
                        currentState.copy(
                            lecturas = listaActualizada,
                            fcActual = tvMsg.bpm,
                            fcEstado = tvMsg.estado,
                            ultimaHora = tvMsg.hora,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lecturasDto = neonRepo.obtenerHistorialCompleto(50)
                val statsDto = neonRepo.obtenerEstadisticas()
                
                _state.update { it.copy(
                    lecturas = lecturasDto.map { dto -> dto.toLecturaFC() },
                    estadisticas = statsDto.map { dto -> dto.toLecturaFC() },
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun refresh() = cargarDatos()

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}

private fun LecturaFcDto.toLecturaFC(): LecturaFC {
    return LecturaFC(
        id = this.id,
        fecha = this.fecha,
        hora = this.hora,
        bpm = this.bpm,
        estado = this.estado
    )
}

class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
