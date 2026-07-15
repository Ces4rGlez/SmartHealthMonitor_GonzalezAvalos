package mx.utng.cfga.smarthealthmonitor

import android.app.Application
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.cfga.smarthealthmonitor.data.mqtt.MqttAppService
import mx.utng.cfga.smarthealthmonitor.data.sync.NeonSyncWorker

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        // Inicializar el repositorio con el contexto de la aplicación
        SmartHealthRepository.init(this)

        // Programar sync periódico con Neon
        NeonSyncWorker.schedule(this)
        
        // Inicializar MQTT con el StateFlow del Repository
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository.fcFlow
        )
        mqttService.connect()
    }
}
