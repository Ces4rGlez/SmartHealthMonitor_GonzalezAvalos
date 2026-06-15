package mx.utng.cfga.smarthealthmonitor.wear.presentation

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    private val messageClient by lazy { Wearable.getMessageClient(context) }
    private val nodeClient by lazy { Wearable.getNodeClient(context) }

    suspend fun enviarFC(bpm: Int) {
        try {
            // Buscar los nodos conectados (en este caso, tu teléfono móvil)
            val nodos = nodeClient.connectedNodes.await()
            val datos = bpm.toString().toByteArray(Charsets.UTF_8)

            if (nodos.isEmpty()) {
                Log.w("WearDataSender", "⚠️ No se encontraron nodos conectados. Verifica el emparejamiento con el teléfono.")
            }

            for (nodo in nodos) {
                Log.d("WearDataSender", "🔗 Enviando a nodo: ${nodo.displayName}")
                // Envía el mensaje a la ruta "/smarthealthmonitor/fc" para que coincida con el receptor
                messageClient.sendMessage(nodo.id, "/smarthealthmonitor/fc", datos).await()
                Log.d("WearDataSender", "👉 FC enviada con éxito al teléfono: $bpm BPM")
            }
        } catch (e: Exception) {
            Log.e("WearDataSender", "❌ Error al enviar la frecuencia cardíaca", e)
        }
    }
}