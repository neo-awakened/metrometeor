/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup.data

import Event
import HeatPoint
import POI
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

data class EventDto(
    val events: List<EventItemDto>,
    val total: Int,
    val offset: Int,
    val limit: Int
)

data class EventItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val date: LocalDateTime,
    val imageUrl: String? = null,
    val heatmapPoints: List<HeatPointDto> = emptyList(),
    val pois: List<PoiDto> = emptyList()
)

data class HeatPointDto(
    val lat: Double,
    val lng: Double,
    val intensity: Double
)

data class PoiDto(
    val lat: Double,
    val lng: Double,
    val color: PoiColorDto
)

enum class PoiColorDto {
    RED,
    GREEN
}


fun EventItemDto.toEvent(): Event {
    val areaLatLng = if (this.latitude != null && this.longitude != null) {
        listOf(LatLng(this.latitude, this.longitude))
    } else emptyList()

    return Event(
        id = this.id,
        title = this.title,
        description = this.description,
        area = areaLatLng,
        holes = emptyList(), // You can populate if DTO contains holes info
        imageUrl = 0, // Since Event expects drawable, you may need to map it manually
        date = this.date.toLocalDate(), // Convert LocalDateTime -> LocalDate
        heatmapPoints = this.heatmapPoints.map { it.toHeatPoint() },
        pois = this.pois.map { it.toPOI() }
    )
}

fun HeatPointDto.toHeatPoint(): HeatPoint {
    return HeatPoint(
        latLng = LatLng(this.lat, this.lng),
        intensity = this.intensity
    )
}

fun PoiDto.toPOI(): POI {
    return POI(
        latLng = LatLng(this.lat, this.lng),
        color = when (this.color) {
            PoiColorDto.RED -> POIColor.RED
            PoiColorDto.GREEN -> POIColor.GREEN
        }
    )
}

// Convert EventDto (list) to List<Event>
fun EventDto.toEvents(): List<Event> {
    return this.events.map { it.toEvent() }
}
