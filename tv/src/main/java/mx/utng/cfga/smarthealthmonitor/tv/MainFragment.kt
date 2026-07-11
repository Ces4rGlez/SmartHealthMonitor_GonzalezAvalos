package mx.utng.cfga.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.models.MockData

class MainFragment : BrowseSupportFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)

        cargarFilas()
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual ──
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id = 0, fecha = "Hoy", hora = "Ahora", bpm = 88, estado = "Normal"))
        estadoAdapter.add(LecturaFC(id = 1, fecha = "Hoy", hora = "Alerta", bpm = 115, estado = "Alto"))
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC desde MockData ──
        val histAdapter = ArrayObjectAdapter(FCCardPresenter())
        MockData.historialFC.forEach { histAdapter.add(it) }
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}
