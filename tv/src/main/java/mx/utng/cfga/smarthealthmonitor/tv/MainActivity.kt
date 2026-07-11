package mx.utng.cfga.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.tv.material3.ExperimentalTvMaterial3Api
import mx.utng.cfga.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.cfga.smarthealthmonitor.tv.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthMonitorTheme {
                TvCatalogScreen(onCardClick = { /* Acción al hacer click */ })
            }
        }
    }
}
