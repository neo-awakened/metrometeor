/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup.data

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
