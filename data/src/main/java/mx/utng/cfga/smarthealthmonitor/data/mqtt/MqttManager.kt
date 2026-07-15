package mx.utng.cfga.smarthealthmonitor.data.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object MqttManager {
    private const val TAG = "MqttManager"
    private const val BROKER_URL = "tcp://broker.hivemq.com:1883"
    private const val TOPIC = "utng/smarthealth/lecturas"
    
    private var mqttClient: MqttClient? = null

    fun init(context: Context) {
        if (mqttClient != null) return
        
        try {
            val clientId = MqttClient.generateClientId()
            mqttClient = MqttClient(BROKER_URL, clientId, MemoryPersistence())
            
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                connectionTimeout = 10
                keepAliveInterval = 60
            }

            mqttClient?.connect(options)
            Log.d(TAG, "Conectado al broker MQTT: $BROKER_URL")
        } catch (e: Exception) {
            Log.e(TAG, "Error al conectar MQTT: ${e.message}")
        }
    }

    fun publicar(mensaje: String) {
        try {
            if (mqttClient?.isConnected == true) {
                val mqttMessage = MqttMessage(mensaje.toByteArray()).apply {
                    qos = 1
                }
                mqttClient?.publish(TOPIC, mqttMessage)
                Log.d(TAG, "Mensaje publicado en $TOPIC: $mensaje")
            } else {
                Log.w(TAG, "No se pudo publicar: Cliente MQTT no conectado")
                // Intentar reconectar
                mqttClient?.connect()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al publicar MQTT: ${e.message}")
        }
    }

    fun suscribir(onMensajeRecibido: (String) -> Unit) {
        try {
            if (mqttClient?.isConnected == true) {
                mqttClient?.subscribe(TOPIC) { topic, message ->
                    val payload = String(message.payload)
                    Log.d(TAG, "Mensaje recibido en $topic: $payload")
                    onMensajeRecibido(payload)
                }
                Log.d(TAG, "Suscrito al tópico: $TOPIC")
            } else {
                Log.w(TAG, "No se pudo suscribir: Cliente MQTT no conectado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al suscribir MQTT: ${e.message}")
        }
    }
}
