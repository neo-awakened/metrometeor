/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.TileProvider
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberTileOverlayState
import com.google.maps.android.heatmaps.HeatmapTileProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    eventId: Int,
    navController: androidx.navigation.NavController,
    viewModel: EventsViewModel = viewModel()
) {
    val event = viewModel.events.collectAsState().value.find { it.id == eventId }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (event != null) {
        val cameraState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(event.area.first(), 16f)
        }

        Box(modifier = Modifier.fillMaxSize()) {
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

                    // Cerchi concentrici per simulare la heatmap
                    if (event.heatmapPoints.isNotEmpty()) {
                        event.heatmapPoints.forEach { heatPoint ->
                            // Cerchio rosso più interno
                            Circle(
                                center = heatPoint.latLng,
                                radius = (heatPoint.intensity * 80.0).coerceAtLeast(25.0), // raggio in metri
                                fillColor = Color(0x55FF0000), // rosso trasparente
                                strokeColor = Color.Transparent,
                                zIndex = 2f
                            )

                            // Cerchio giallo più esterno
                            Circle(
                                center = heatPoint.latLng,
                                radius = (heatPoint.intensity * 150.0).coerceAtLeast(40.0), // più largo
                                fillColor = Color(0x33FFFF00), // giallo trasparente
                                strokeColor = Color.Transparent,
                                zIndex = 1f
                            )
                        }
                    }

                    event.pois.forEach { poi ->
                        Marker(
                            state = MarkerState(position = poi.latLng),
                            icon = context.vectorToBitmap(poi.color.iconImage, 56, 56),
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(id = R.drawable.ic_toilette),
                    contentDescription = "Queue for Toilet"
                )
            }

            // Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Queue") },
                    text = { Text("Do you want to save that you are in the queue for the toilette?") },
                    confirmButton = {
                        Button(onClick = {
                            showDialog = false
                            // TODO: Save queue status here
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

fun Context.vectorToBitmap(@DrawableRes id: Int, width: Int, height: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this, id) ?: return BitmapDescriptorFactory.defaultMarker()
    vectorDrawable.setBounds(0, 0, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

