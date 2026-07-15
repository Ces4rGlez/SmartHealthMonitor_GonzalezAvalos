package mx.utng.cfga.smarthealthmonitor.data.mqtt

object MqttConfig {
    const val BROKER_URL  = "ssl://fe5ba5145eca4575bf0177b8aa005a9a.s1.eu.hivemq.cloud:8883"
    const val USERNAME    = "CesarGlez"
    const val PASSWORD    = "desdeCero17"
 
    // Topics del proyecto
    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV    = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"
 
    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1
 
    // Client IDs únicos por dispositivo
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP  = "smarthealthmonitor-app"
    const val CLIENT_APP_SERVICE = "smarthealthmonitor-app-service"
    const val CLIENT_TV   = "smarthealthmonitor-tv"
}
