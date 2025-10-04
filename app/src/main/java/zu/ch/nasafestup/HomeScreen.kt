/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen() {

    val zurich = LatLng(47.3769, 8.5417)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(zurich, 10f)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        // Definisci i punti dell’area
        val polygonPoints = listOf(
            LatLng(47.4, 8.5),
            LatLng(47.5, 8.6),
            LatLng(47.45, 8.7),
            LatLng(47.35, 8.65)
        )

        Polygon(
            points = polygonPoints,
            fillColor = Color(0x5500FF00),
            strokeColor = Color(0xFF00FF00),
            strokeWidth = 5f
        )
    }
}

