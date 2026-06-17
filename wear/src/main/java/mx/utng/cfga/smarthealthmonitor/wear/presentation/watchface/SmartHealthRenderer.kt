package mx.utng.cfga.smarthealthmonitor.wear.presentation.watchface

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import mx.utng.cfga.smarthealthmonitor.wear.presentation.SmartHealthRepository
import java.time.ZonedDateTime

class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder, currentUserStyleRepository, watchState,
    CanvasType.HARDWARE, interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = true
) {

    private val paintHora = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    private val paintFC = Paint().apply {
        color = Color.RED
        textSize = 30f
        isAntiAlias = true
    }

    private val paintSub = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = true
    }

    override suspend fun createSharedAssets(): SharedAssets = object : SharedAssets {
        override fun onDestroy() {}
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        // Fondo negro puro — preserva pixeles apagados en pantallas AMOLED
        canvas.drawColor(Color.BLACK)

        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        // Ajustar pinceles dinámicamente según el modo de pantalla (RETO ADICIONAL - AOD)
        val esModoAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        if (esModoAmbient) {
            paintHora.isAntiAlias = false
            paintSub.isAntiAlias = false
            paintSub.color = Color.DKGRAY
        } else {
            paintHora.isAntiAlias = true
            paintSub.isAntiAlias = true
            paintSub.color = Color.GRAY
        }

        // 1. Dibujar Hora Digital Centrada
        val hora = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)
        val tw = paintHora.measureText(hora)
        canvas.drawText(hora, cx - tw / 2, cy - 10f, paintHora)

        // 2. Si no está en Always-On Display, renderizar elementos interactivos
        if (!esModoAmbient) {
            // Segundos abajo del reloj
            val seg = String.format("%02d", zonedDateTime.second)
            canvas.drawText(seg, cx - 11f, cy + 25f, paintSub)

            // Frecuencia Cardíaca reactiva del Repositorio
            val fc = SmartHealthRepository.fcFlow.value
            if (fc > 0) {
                val fcStr = "❤️ $fc bpm"
                val fcW = paintFC.measureText(fcStr)
                canvas.drawText(fcStr, cx - fcW / 2, cy + 70f, paintFC)
            }
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }
}