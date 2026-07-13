package mx.utng.cfga.smarthealthmonitor

import android.app.Application
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.cfga.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApplication : Application() {
    lateinit var mqttService: MqttAppService
 
    override fun onCreate() {
        super.onCreate()
        // Inicializar MQTT con el StateFlow del Repository
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository.fcFlow
        )
        mqttService.connect()
    }
}
