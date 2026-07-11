package mx.utng.cfga.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.models.MockData

data class TvState(
    val lecturas: List<LecturaFC> = MockData.historialFC
)

class TvViewModel(private val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()
}

class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
