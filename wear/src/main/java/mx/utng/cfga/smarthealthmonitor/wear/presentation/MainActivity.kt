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

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: WearDashboardViewModel

    // Manejador de la respuesta de permisos por parte del usuario
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val bodySensorsGranted = permissions[Manifest.permission.BODY_SENSORS] ?: false
        if (bodySensorsGranted) {
            Log.d("MainActivity", "✅ Permiso BODY_SENSORS concedido.")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                verificarPermisoBackground()
            } else {
                iniciarServicioSalud()
            }
        } else {
            Log.e("MainActivity", "❌ El usuario denegó los permisos de sensores corporales.")
            Toast.makeText(this, "Debes habilitar los sensores en Ajustes", Toast.LENGTH_LONG).show()
            
            // Si el permiso fue denegado, abrimos los ajustes de la app para que lo active manualmente
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }

    private val backgroundPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MainActivity", "✅ Permiso BACKGROUND concedido.")
            iniciarServicioSalud()
        } else {
            Log.e("MainActivity", "❌ Permiso de sensores en segundo plano denegado.")
            Toast.makeText(this, "Habilita 'Permitir siempre' en Ajustes", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(WearDashboardViewModel::class.java)

        // Verificación inicial
        checkPermissionsAndStart()

        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph(viewModel = viewModel)
            }
        }
    }

    private fun checkPermissionsAndStart() {
        val sensorStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
        
        when {
            sensorStatus == PackageManager.PERMISSION_GRANTED -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    verificarPermisoBackground()
                } else {
                    iniciarServicioSalud()
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.BODY_SENSORS) -> {
                // El usuario ya lo denegó antes, pero podemos pedirlo de nuevo con una explicación
                permissionLauncher.launch(arrayOf(Manifest.permission.BODY_SENSORS))
            }
            else -> {
                // Si llegamos aquí y no es la primera vez, el cuadro está bloqueado.
                // Abrimos ajustes directamente para no dejar al usuario bloqueado.
                permissionLauncher.launch(arrayOf(Manifest.permission.BODY_SENSORS))
            }
        }
    }

    private fun verificarPermisoBackground() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS_BACKGROUND)
            != PackageManager.PERMISSION_GRANTED) {
            
            Log.d("MainActivity", "Solicitando permiso BACKGROUND...")
            backgroundPermissionLauncher.launch(Manifest.permission.BODY_SENSORS_BACKGROUND)
        } else {
            iniciarServicioSalud()
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