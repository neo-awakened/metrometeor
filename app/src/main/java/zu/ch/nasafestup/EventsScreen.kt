import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import zu.ch.nasafestup.EventsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun EventsScreen(
    viewModel: EventsViewModel = viewModel(),
    onEventClick: (Int) -> Unit
) {
    val eventsState = viewModel.events.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val sortedEvents = eventsState.value.sortedBy { it.date }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sortedEvents) { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEventClick(event.id) },
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = event.imageUrl),
                        contentDescription = event.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📅 ${event.date.format(formatter)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
