package mx.utng.cfga.smarthealthmonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC

@Composable
fun FilaHistorial(lectura: LecturaFC, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "${lectura.fecha} - ${lectura.hora}", style = MaterialTheme.typography.bodyMedium)
                Text(text = lectura.estado, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "${lectura.bpm} bpm",
                style = MaterialTheme.typography.titleMedium,
                color = if (lectura.bpm > 90) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}