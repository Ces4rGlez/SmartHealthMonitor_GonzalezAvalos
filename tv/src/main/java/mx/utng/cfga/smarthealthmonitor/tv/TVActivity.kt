package mx.utng.cfga.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import mx.utng.cfga.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.cfga.smarthealthmonitor.tv.presentation.TvDetailScreen
import mx.utng.cfga.smarthealthmonitor.tv.presentation.TvPlaybackScreen
import mx.utng.cfga.smarthealthmonitor.tv.ui.theme.SmartHealthMonitorTheme // Ajusta según tu paquete de temas

class TVActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthMonitorTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "catalog") {

                    // Ruta 1: Catálogo principal
                    composable("catalog") {
                        TvCatalogScreen(onCardClick = { lecturaId ->
                            navController.navigate("detail/$lecturaId")
                        })
                    }

                    // Ruta 2: Pantalla de Detalle con Argumento
                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(navArgument("lecturaId") { type = NavType.IntType })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("lecturaId") ?: return@composable
                        TvDetailScreen(lecturaId = id, navController = navController)
                    }

                    // Ruta 3: Reproductor de Video
                    composable("playback") {
                        TvPlaybackScreen(navController = navController)
                    }
                }
            }
        }
    }
}