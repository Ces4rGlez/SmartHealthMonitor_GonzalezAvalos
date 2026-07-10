package mx.utng.cfga.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModel
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text(
            text = "Catálogo de Lecturas",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TvLazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.lecturas) { lectura ->
                Surface(
                    onClick = { onCardClick(lectura.id) },
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${lectura.bpm} BPM")
                        Text(text = lectura.hora)
                        Text(text = lectura.estado)
                    }
                }
            }
        }
    }
}
