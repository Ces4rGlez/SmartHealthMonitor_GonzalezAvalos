package mx.utng.cfga.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.MaterialTheme

import androidx.compose.ui.text.style.TextAlign

@Composable
fun WearFCCard(
    fc: Int,
    modifier: Modifier = Modifier
) {
    val colorFC = if (fc in 60..100) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Card(
        onClick = { },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "❤️", 
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$fc",
                style = MaterialTheme.typography.displayMedium,
                color = colorFC,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "BPM",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}