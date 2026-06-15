package mx.utng.cfga.smarthealthmonitor.wear.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.cfga.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: WearDashboardViewModel

    // Manejador de la respuesta de permisos por parte del usuario
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val bodySensorsGranted = permissions[Manifest.permission.BODY_SENSORS] ?: false
        if (bodySensorsGranted) {
            iniciarServicioSalud()
        } else {
            Log.w("MainActivity", "⚠️ El usuario denegó los permisos de sensores corporales.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewModel manualmente ya que no tenemos la extensión viewmodel-compose
        viewModel = ViewModelProvider(this).get(WearDashboardViewModel::class.java)

        // ── VERIFICACIÓN DE PERMISOS PARA WEARABLES ──────────────────────────
        val permissionsToRequest = mutableListOf(Manifest.permission.BODY_SENSORS)

        // Android 13+ requiere de manera obligatoria permisos en segundo plano para servicios pasivos
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.BODY_SENSORS_BACKGROUND)
        }

        val missingPermissions = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            iniciarServicioSalud()
        } else {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        }

        // ── INYECCIÓN DEL GRAFO DE NAVEGACIÓN COMPOSE FOR WEAR OS ────────────
        setContent {
            SmartHealthWearTheme {
                // Conectamos el NavGraph que controla el Dashboard circular y la pantalla de Alerta
                SmartHealthWearNavGraph(viewModel = viewModel)
            }
        }
    }

    // ── REGISTRO DEL SERVICIO RECEPTOR EN SEGUNDO PLANO ──────────────────────
    private fun iniciarServicioSalud() {
        lifecycleScope.launch {
            try {
                HealthDataService.registrar(applicationContext)
                Log.d("MainActivity", "✅ Registro de HealthDataService exitoso")
            } catch (e: Exception) {
                Log.e("MainActivity", "❌ Error al registrar HealthDataService", e)
            }
        }
    }
}