package mx.utng.cfga.smarthealthmonitor.tv.presentation

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.tv.material3.*

@OptIn(UnstableApi::class)
@ExperimentalTvMaterial3Api
@Composable
fun TvPlaybackScreen(navController: NavController) {
    val ctx = LocalContext.current

    // Instanciar y preparar el reproductor ExoPlayer de Media3
    val exoPlayer = remember {
        ExoPlayer.Builder(ctx).build().apply {
            val mediaItem = MediaItem.fromUri(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // CRÍTICO: Liberar recursos al salir de la pantalla para cortar audio/video de fondo
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // El puente AndroidView aloja e infla el PlayerView tradicional de Android
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true // Muestra la barra de reproducción interactiva
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Botón Superpuesto para regresar de forma manual con D-pad
        Surface(
            onClick = {
                exoPlayer.stop()
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color(0x88000000),
                focusedContainerColor = Color(0xCCFFFFFF)
            )
        ) {
            Text(
                text = "← Volver",
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}