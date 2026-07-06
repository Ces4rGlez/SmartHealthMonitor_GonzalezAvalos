package mx.utng.cfga.smarthealthmonitor.tv // ¡Tu paquete correcto!

import android.graphics.Color
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
// OJO: Asegúrate de que esta importación coincida con la ruta de tu entidad Room en :app
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFC

class FCCardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            // CRÍTICO: Requerido para la navegación con control D-pad
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(240, 180)
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card = viewHolder.view as ImageCardView
        val lectura = item as LecturaFC

        card.titleText = "${lectura.valorBpm} bpm"
        card.contentText = lectura.hora

        // Cambiar el color de fondo dinámicamente si los BPM están fuera de rango
        // Asumiendo que LecturaFC tiene un booleano esNormal o calculándolo manualmente si es necesario
        // Ejemplo: val esNormal = lectura.valorBpm in 60..100
        val esNormal = lectura.valorBpm in 60..100

        val bgColor = if (esNormal) {
            Color.parseColor("#1B4F8A") // sh_primary
        } else {
            Color.parseColor("#B3261E") // sh_error
        }
        card.setBackgroundColor(bgColor)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        (viewHolder.view as ImageCardView).mainImage = null
    }
}