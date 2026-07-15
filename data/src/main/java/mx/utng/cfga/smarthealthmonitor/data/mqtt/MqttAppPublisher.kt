package mx.utng.cfga.smarthealthmonitor.data.mqtt

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttAppPublisher(private val context: Context) {
    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_PUBLISHER"

    fun connect() {
        try {
            client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_APP, MemoryPersistence())
            
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                if (MqttConfig.USERNAME.isNotEmpty()) {
                    userName = MqttConfig.USERNAME
                    password = MqttConfig.PASSWORD.toCharArray()
                }
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    Log.d(TAG, "✅ Publisher conectado")
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e(TAG, "❌ Error al conectar: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    fun publicarLectura(lectura: TvMessage) {
        try {
            if (client?.isConnected == true) {
                val payload = Json.encodeToString(lectura)
                val message = MqttMessage(payload.toByteArray()).apply { qos = MqttConfig.QOS }
                client?.publish(MqttConfig.TOPIC_TV, message)
                Log.d(TAG, "📤 Publicado: $payload")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al publicar: ${e.message}")
        }
    }

    fun disconnect() {
        client?.disconnect()
    }
}
