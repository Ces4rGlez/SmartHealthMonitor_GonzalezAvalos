package mx.utng.cfga.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import mx.utng.cfga.smarthealthmonitor.wear.presentation.LecturaFC

@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    // Definimos el color del texto: 
    // - Rojo (error) si es anormal.
    // - OnSurface (blanco/gris claro) si es normal, para que contraste con el fondo del botón.
    val textColor = if (lectura.valorBpm in 60..100) 
        MaterialTheme.colorScheme.onSurface 
    else 
        MaterialTheme.colorScheme.error

    Button(
        label = {
            Text(
                text = "${lectura.valorBpm} BPM", 
                color = textColor,
                style = MaterialTheme.typography.titleSmall
            )
        },
        secondaryLabel = {
            Text(
                text = lectura.hora,
                style = MaterialTheme.typography.labelSmall
            )
        },
        onClick = { },
        // Usamos filledTonalButtonColors para un diseño más moderno y menos "pesado" que el primary puro
        colors = ButtonDefaults.filledTonalButtonColors(),
        modifier = Modifier.fillMaxWidth()
    )
}
