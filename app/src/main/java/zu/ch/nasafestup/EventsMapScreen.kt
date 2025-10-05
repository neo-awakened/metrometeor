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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    eventId: Int,
    navController: androidx.navigation.NavController,
    viewModel: EventsViewModel = viewModel()
) {
    val event = viewModel.events.collectAsState().value.find { it.id == eventId }
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) } // Toilet dialog
    var showFriendDialog by remember { mutableStateOf(false) } // Friend dialog
    var friendLocation by remember { mutableStateOf<LatLng?>(null) }

    val myLocation = remember { LatLng(47.3667, 8.5423) } // red marker middle of bridge
    val friendPosition = LatLng(47.3676, 8.5413) // friend marker on land

    // Esempio di punti dettagliati lungo la strada tra te e l'amico
    val routePointsExample = listOf(
        myLocation,
        LatLng(47.36672, 8.54225),
        LatLng(47.36675, 8.5421),
        LatLng(47.36678, 8.54195),
        LatLng(47.36682, 8.5418),
        LatLng(47.36685, 8.5417),
        LatLng(47.36688, 8.5416),
        LatLng(47.36692, 8.5415),
        LatLng(47.36696, 8.54145),
        LatLng(47.3670, 8.5414),
        LatLng(47.3671, 8.54135),
        LatLng(47.3672, 8.54133),
        LatLng(47.3673, 8.54132),
        LatLng(47.3674, 8.54132),
        friendPosition
    )


    if (event != null) {
        val cameraState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(myLocation, 16f)
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
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraState
                ) {
                    // Event polygon
                    Polygon(
                        points = event.area,
                        fillColor = Color(0x5500FF00),
                        strokeColor = Color(0xFF00FF00),
                        strokeWidth = 5f
                    )

                    // Heatmap
                    event.heatmapPoints.forEach { heatPoint ->
                        Circle(
                            center = heatPoint.latLng,
                            radius = (heatPoint.intensity * 80.0).coerceAtLeast(25.0),
                            fillColor = Color(0x55FF0000),
                            strokeColor = Color.Transparent,
                            zIndex = 2f
                        )
                        Circle(
                            center = heatPoint.latLng,
                            radius = (heatPoint.intensity * 150.0).coerceAtLeast(40.0),
                            fillColor = Color(0x33FFFF00),
                            strokeColor = Color.Transparent,
                            zIndex = 1f
                        )
                    }

                    // Toilets POIs
                    event.pois.forEach { poi ->
                        Marker(
                            state = MarkerState(position = poi.latLng),
                            icon = context.vectorToBitmap(poi.color.iconImage, 56, 56)
                        )
                    }

                    // 🧍 You
                    Marker(
                        state = MarkerState(position = myLocation),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        title = "You are here"
                    )

                    // 👯 Friend
                    friendLocation?.let { friend ->
                        Marker(
                            state = MarkerState(position = friend),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                            title = "Your Friend"
                        )

                        // Draw route along points
                        Polyline(
                            points = routePointsExample,
                            color = Color(0xFF1976D2),
                            width = 8f
                        )
                    }
                }
            }

            // Floating Buttons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_toilette),
                        contentDescription = "Queue for Toilet"
                    )
                }

                FloatingActionButton(onClick = { showFriendDialog = true }) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_friend_poi),
                        contentDescription = "Find your friend"
                    )
                }
            }

            // Toilet dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Queue") },
                    text = { Text("Do you want to save that you are in the queue for the toilette?") },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) { Text("Yes") }
                    },
                    dismissButton = { Button(onClick = { showDialog = false }) { Text("No") } }
                )
            }

            // Friend dialog
            if (showFriendDialog) {
                AlertDialog(
                    onDismissRequest = { showFriendDialog = false },
                    title = { Text("Find your friend") },
                    text = { Text("Do you want to mark your friend’s position on the map?") },
                    confirmButton = {
                        Button(onClick = {
                            showFriendDialog = false
                            friendLocation = friendPosition
                        }) { Text("Yes") }
                    },
                    dismissButton = { Button(onClick = { showFriendDialog = false }) { Text("No") } }
                )
            }
        }
    }
}

fun Context.vectorToBitmap(@DrawableRes id: Int, width: Int, height: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this, id)
        ?: return BitmapDescriptorFactory.defaultMarker()
    vectorDrawable.setBounds(0, 0, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
