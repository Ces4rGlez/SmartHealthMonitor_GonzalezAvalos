package mx.utng.cfga.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun AlertaScreen(
    fc: Int,                                       // FC actual del Dashboard
    onDismiss: () -> Unit,                         // Cancelar / cerrar
    onConfirmar: (nota: String) -> Unit            // RETO: Pasa la nota como parámetro
) {
    var enviando by remember { mutableStateOf(false) }
    var notaOpcional by remember { mutableStateOf("") } // Estado del reto adicional
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        // En Preview no se renderizan Dialogs; mostramos el contenido dentro de un Surface
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar alerta de emergencia",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Text(
                    text = "FC actual: $fc bpm",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = "Se notificará a tus contactos de emergencia.\nEsta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = notaOpcional,
                    onValueChange = { notaOpcional = it },
                    label = { Text("Nota opcional (ej. Me siento mareado)") },
                    placeholder = { Text("Escribe cómo te sientes...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !enviando
                )

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss, modifier = Modifier.defaultMinSize(minHeight = 48.dp)) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            enviando = true
                            onConfirmar(notaOpcional)
                        },
                        enabled = !enviando,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    ) {
                        if (enviando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onError,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "CONFIRMAR ALERTA",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Enviar alerta de emergencia",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "FC actual: $fc bpm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Se notificará a tus contactos de emergencia.\nEsta acción no se puede deshacer.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // 👇 RETO ADICIONAL: Campo para texto opcional
                    OutlinedTextField(
                        value = notaOpcional,
                        onValueChange = { notaOpcional = it },
                        label = { Text("Nota opcional (ej. Me siento mareado)") },
                        placeholder = { Text("Escribe cómo te sientes...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !enviando
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        enviando = true
                        onConfirmar(notaOpcional) // Envía la nota capturada
                    },
                    enabled = !enviando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp) // WCAG AA 48dp táctil
                ) {
                    if (enviando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "CONFIRMAR ALERTA",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp) // WCAG AA 48dp táctil
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "Alerta - Light")
@Preview(showBackground = true, name = "Alerta - Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlertaScreenPreview() {
    SmartHealthMonitorTheme {
        AlertaScreen(fc = 145, onDismiss = { }, onConfirmar = { _ -> })
    }
}