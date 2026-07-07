package mx.utng.cfga.smarthealthmonitor.tv // ¡Tu paquete correcto!

import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
// OJO: Asegúrate de que estas importaciones apunten a tu módulo :app
import mx.utng.cfga.smarthealthmonitor.data.MockData
import mx.utng.cfga.smarthealthmonitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de la cabecera visual de Leanback
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color del sidebar izquierdo
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (Datos fijos o mockups rápidos) ──
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id = 0, valorBpm = 88, hora = "Ahora"))
        estadoAdapter.add(LecturaFC(id = 1, valorBpm = 115, hora = "Alerta Taquicardia")) // Saldrá roja por la lógica del presenter
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC desde MockData ──
        val histAdapter = ArrayObjectAdapter(FCCardPresenter())
        MockData.historialFC.forEach { histAdapter.add(it) }
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}