package mx.utng.cfga.smarthealthmonitor.tv.presentation

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.tv.material3.*
import androidx.compose.ui.text.font.FontWeight

@OptIn(UnstableApi::class)
@ExperimentalTvMaterial3Api
@Composable
fun TvPlaybackScreen(navController: NavController) {
    val ctx = LocalContext.current
    var isBuffering by remember { mutableStateOf(true) }

    val exoPlayer = remember {
        ExoPlayer.Builder(ctx).build().apply {
            // URL Directa MP4 más compatible
            val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("TvPlayback", "Error: ${error.message}")
                    isBuffering = false
                }
                override fun onPlaybackStateChanged(state: Int) {
                    isBuffering = state == Player.STATE_BUFFERING
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isBuffering) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // Indicador de carga personalizado
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Cargando video...", color = Color.White)
                }
            }
        }

        // Overlay con botón Volver
        Surface(
            onClick = {
                exoPlayer.stop()
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.TopStart).padding(32.dp),
            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp)),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color.Black.copy(0.6f),
                focusedContainerColor = Color.White,
                contentColor = Color.White,
                focusedContentColor = Color.Black
            )
        ) {
            Text("← VOLVER", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold)
        }
    }
}
