package mx.utng.cfga.smarthealthmonitor.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import mx.utng.cfga.smarthealthmonitor.data.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
 
class MqttTvSubscriber(
    private val context : Context,
    private val tvFlow  : MutableStateFlow<TvMessage?>
) {
    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_TV_SUB"
 
    fun connect() {
        try {
            client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_TV, MemoryPersistence())
     
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        try {
                            val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                            tvFlow.value = tvMsg
                            Log.d(TAG, "📺 Recibido: ${tvMsg.bpm} bpm")
                        } catch (e: Exception) {
                            Log.e(TAG, "Error decode: ${e.message}")
                        }
                    }
                }
                override fun connectionLost(cause: Throwable?) {
                    Log.w(TAG, "Conexión perdida, reintentando...")
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                if (MqttConfig.USERNAME.isNotEmpty()) {
                    userName = MqttConfig.USERNAME
                    password = MqttConfig.PASSWORD.toCharArray()
                }
            }
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                    Log.d(TAG, "✅ TV suscrita a ${MqttConfig.TOPIC_TV}")
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e(TAG, "❌ Error suscripción: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }
    fun disconnect() { 
        try {
            client?.disconnect() 
        } catch (e: Exception) {}
    }
}
