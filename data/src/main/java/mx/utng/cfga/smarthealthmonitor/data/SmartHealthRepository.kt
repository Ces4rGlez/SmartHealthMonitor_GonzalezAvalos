package mx.utng.cfga.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.cfga.smarthealthmonitor.data.db.SmartHealthDB
import mx.utng.cfga.smarthealthmonitor.data.mqtt.MqttAppPublisher
import mx.utng.cfga.smarthealthmonitor.data.mqtt.TvMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmartHealthRepository {
    val fcFlow = MutableStateFlow(0)
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private var dao: LecturaFCDao? = null
    private var mqttPublisher: MqttAppPublisher? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
        mqttPublisher = MqttAppPublisher(context)
        mqttPublisher?.connect()
    }

    suspend fun actualizarFC(bpm: Int) {
        fcFlow.value = bpm
        
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val fechaActual = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
        val estado = if (bpm > 90) "Alto" else "Normal"
        
        // 1. Guardar en Room (Smartphone)
        dao?.insertar(
            LecturaFC(
                bpm = bpm,
                estado = estado,
                hora = horaActual,
                dispositivo = "app"
            )
        )
        
        // 2. Publicar por MQTT para la TV
        mqttPublisher?.publicarLectura(
            TvMessage(
                bpm = bpm,
                estado = estado,
                hora = horaActual,
                fecha = fechaActual
            )
        )
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}
