package mx.utng.cfga.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModel
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 64.dp, vertical = 48.dp)) {
            // Header con indicador de conexión
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SmartHealth Monitor",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Monitoreo en tiempo real vía MQTT",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF60A5FA)
                    )
                }

                // Indicador de "En Vivo"
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFF0000).copy(alpha = 0.2f), CircleShape)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "EN VIVO", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            TvLazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(state.lecturas) { lectura ->
                    ReadingCard(lectura = lectura, onClick = { onCardClick(lectura.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ReadingCard(lectura: LecturaFC, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(16.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color(0xFF1E293B),
            focusedContainerColor = Color(0xFF3B82F6),
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // BPM Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "❤",
                    fontSize = 32.sp,
                    color = if (lectura.bpm > 90) Color(0xFFEF4444) else Color(0xFF60A5FA)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${lectura.bpm}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black
                    )
                    Text(text = "BPM", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                }
            }

            // Time Section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = lectura.hora, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = lectura.fecha, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.4f))
            }

            // Status Chip
            Box(
                modifier = Modifier
                    .background(
                        color = if (lectura.estado == "Normal") Color(0xFF059669) else Color(0xFFDC2626),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = lectura.estado.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
