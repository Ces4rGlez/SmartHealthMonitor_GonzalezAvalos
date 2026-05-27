package mx.utng.cfga.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.utng.cfga.smarthealthmonitor.navigation.SmartHealthNavGraph
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            SmartHealthMonitorTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Aquí inicia la navegación principal
                    SmartHealthNavGraph()
                }
            }
        }
    }
}

@Composable
fun SmartHealthScreen(
    isLandscape: Boolean,
    nombre: String = "Juan García",
    frecuenciaCardiaca: Int = 78,
    pasos: Int = 4250
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (isLandscape) {
                LandscapeLayout(
                    nombre,
                    frecuenciaCardiaca,
                    pasos
                )
            } else {

                PortraitLayout(
                    nombre,
                    frecuenciaCardiaca,
                    pasos
                )
            }
        }
    }
}

@Composable
fun PortraitLayout(
    nombre: String,
    frecuenciaCardiaca: Int,
    pasos: Int
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nombre,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$frecuenciaCardiaca bpm",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.DirectionsWalk,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "%,d pasos".format(pasos),
                    fontSize = 20.sp
                )
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {

            Icon(
                Icons.Default.Warning,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("ENVIAR ALERTA")
        }
    }
}

@Composable
fun LandscapeLayout(
    nombre: String,
    frecuenciaCardiaca: Int,
    pasos: Int
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "$frecuenciaCardiaca bpm",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "%,d pasos".format(pasos),
                        fontSize = 18.sp
                    )
                }
            }
        }

        Button(
            onClick = { },

            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {

            Icon(
                Icons.Default.Warning,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text("ENVIAR ALERTA")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPortrait() {

    SmartHealthMonitorTheme {

        SmartHealthScreen(
            isLandscape = false
        )
    }
}