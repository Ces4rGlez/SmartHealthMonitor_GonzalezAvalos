package mx.utng.cfga.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Chip
import androidx.wear.compose.material3.ChipDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC

@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    // Si la lectura está fuera del rango normal (60-100), se pinta de rojo (error)
    val color = if (lectura.valorBpm in 60..100) MaterialTheme.colors.primary else MaterialTheme.colors.error

    Chip(
        label = {
            Text(text = "${lectura.valorBpm} bpm", color = color)
        },
        secondaryLabel = {
            Text(text = lectura.hora)
        },
        onClick = { },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = Modifier.fillMaxWidth()
    )
}