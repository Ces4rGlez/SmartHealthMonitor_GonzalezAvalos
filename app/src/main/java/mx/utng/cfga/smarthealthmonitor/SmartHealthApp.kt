package mx.utng.cfga.smarthealthmonitor

import android.app.Application
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar el repositorio con el contexto de la aplicación
        SmartHealthRepository.init(this)
    }
}
