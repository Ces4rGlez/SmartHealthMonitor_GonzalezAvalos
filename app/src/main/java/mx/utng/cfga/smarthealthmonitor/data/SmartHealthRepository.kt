package mx.utng.cfga.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.cfga.smarthealthmonitor.data.db.SmartHealthDB

object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Guarda automáticamente cada registro en Room DB
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}
