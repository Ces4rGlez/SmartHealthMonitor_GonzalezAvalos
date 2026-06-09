package mx.utng.cfga.smarthealthmonitor.wear.presentation

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        // Extraer los puntos de datos que correspondan al sensor de ritmo cardíaco
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)

        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<*>) {
                val bpm = (dataPoint.value as Double).toInt()
                // Lanzar corrutina para despachar el valor al teléfono mediante nuestro Sender
                scope.launch {
                    wearDataSender.enviarFC(bpm)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel() // Limpiar corrutinas para evitar fugas de memoria
    }

    companion object {
        suspend fun registrar(context: Context) {
            val hsClient = HealthServices.getClient(context)
            val passiveClient = hsClient.passiveMonitoringClient

            // Configurar para escuchar exclusivamente el sensor cardíaco de forma pasiva
            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .build()

            passiveClient.setPassiveListenerServiceAsync(
                HealthDataService::class.java,
                config
            ).await()
        }
    }
}