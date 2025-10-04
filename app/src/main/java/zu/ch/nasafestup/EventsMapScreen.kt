/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    eventId: Int,
    navController: androidx.navigation.NavController,
    viewModel: EventsViewModel = viewModel()
) {
    val event = viewModel.events.collectAsState().value.find { it.id == eventId }

    val context = LocalContext.current

    if (event != null) {
        val cameraState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(event.area.first(), 16f)
        }

        Column {
            TopAppBar(
                title = { Text(event.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )


            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraState
            ) {
                Polygon(
                    points = event.area,
                    fillColor = Color(0x5500FF00),
                    strokeColor = Color(0xFF00FF00),
                    strokeWidth = 5f
                )

                // Simulazione di zone con heatmap random
                val colors = listOf(
                    Color(0x55FF0000), // rosso
                    Color(0x55FF7F00), // arancione
                    Color(0x55FFFF00), // giallo
                    Color(0x5500FF00)  // verde
                )

                val centerLat = event.area.map { it.latitude }.average()
                val centerLng = event.area.map { it.longitude }.average()

                val count = Random.nextInt(8, 13)
                repeat(count) {
                    val offsetLat = Random.nextDouble(-0.002, 0.002)
                    val offsetLng = Random.nextDouble(-0.002, 0.002)
                    val size = Random.nextDouble(0.0002, 0.0007)
                    val polygonPoints = listOf(
                        LatLng(centerLat + offsetLat + size, centerLng + offsetLng + size),
                        LatLng(centerLat + offsetLat + size, centerLng + offsetLng - size),
                        LatLng(centerLat + offsetLat - size, centerLng + offsetLng - size),
                        LatLng(centerLat + offsetLat - size, centerLng + offsetLng + size)
                    )

                    Polygon(
                        points = polygonPoints,
                        fillColor = colors.random(),
                        strokeColor = Color.Transparent,
                        strokeWidth = 0f,
                        // aggiungo un buco (hole) casuale all'interno di ciascun poligono
                        holes = listOf(
                            listOf(
                                LatLng(centerLat + offsetLat, centerLng + offsetLng),
                                LatLng(centerLat + offsetLat, centerLng + offsetLng - size / 2),
                                LatLng(centerLat + offsetLat - size / 2, centerLng + offsetLng - size / 2),
                                LatLng(centerLat + offsetLat - size / 2, centerLng + offsetLng)
                            )
                        )
                    )
                }
                // MARKERS: mostra i tuoi POI
                event.pois.forEach { poi ->
                    val bitmap = BitmapFactory.decodeResource(context.resources, poi.color.iconImage)
                    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 48, 48, false) // px fissi
                    Marker(
                        state = MarkerState(position = poi.latLng),
                        icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                    )
                }

            }
        }
    }
}
