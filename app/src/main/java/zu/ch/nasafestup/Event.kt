import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng
import zu.ch.nasafestup.R
import java.time.LocalDate

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val area: List<LatLng>,
    val holes: List<List<LatLng>> = emptyList(),
    @DrawableRes val imageUrl: Int,
    val date: LocalDate,
    val heatmapPoints: List<HeatPoint> = emptyList(),
    val pois: List<POI> = emptyList()
)

data class HeatPoint(
    val latLng: LatLng,
    val intensity: Double // 0.0 low - 1.0 high
)

data class POI(
    val latLng: LatLng,
    val color: POIColor
) {
    val icon: Int get() = color.iconImage
}

enum class POIColor(@DrawableRes val iconImage: Int) {
    RED(R.drawable.ic_toilette_red),
    GREEN(R.drawable.ic_toilette_green),
}

val fakeEvents = listOf(
    Event(
        id = 1,
        title = "Street Parade",
        description = "The world's largest techno parade, attracting up to one million visitors annually along the shore of Lake Zurich.",
        area = listOf(
            LatLng(47.366292, 8.548093),
            LatLng(47.366570, 8.547778),
            LatLng(47.369814, 8.545597),
            LatLng(47.369533, 8.538093),
            LatLng(47.367008, 8.534965),
            LatLng(47.365180, 8.535193),
            LatLng(47.365444, 8.539257)
        ),
        imageUrl = R.drawable.img_streetparade,
        date = LocalDate.of(2024, 8, 10),
        heatmapPoints = listOf(
            HeatPoint(LatLng(47.367347, 8.545308), 0.5),
            HeatPoint(LatLng(47.367111, 8.540306), 0.8),
            HeatPoint(LatLng(47.366682, 8.545884), 0.4),
            HeatPoint(LatLng(47.365901, 8.539517), 0.3),

        ),
        pois = listOf(
            POI(LatLng(47.3665, 8.5450), POIColor.RED),     // Near Bellevue
            POI(LatLng(47.3669, 8.5435), POIColor.GREEN),   // Along parade route
            POI(LatLng(47.3672, 8.5418), POIColor.RED),     // Close to Bürkliplatz
            POI(LatLng(47.3660, 8.5406), POIColor.GREEN),   // Near lake edge
            POI(LatLng(47.3668, 8.5378), POIColor.RED),     // Toward Enge
            POI(LatLng(47.3661, 8.5389), POIColor.GREEN),   // Central inside area
            POI(LatLng(47.3670, 8.5442), POIColor.RED),     // Main route
            POI(LatLng(47.3663, 8.5463), POIColor.GREEN),   // Close to start of area
            POI(LatLng(47.3667, 8.5428), POIColor.RED),     // Between Bellevue/Bürkliplatz
            POI(LatLng(47.3659, 8.5398), POIColor.GREEN)    // Near water edge
        )
    ),
    Event(
        id = 2,
        title = "Züri Fäscht",
        description = "One of Europe's largest public festivals, held every three years, featuring fireworks, rides, and numerous markets.",
        area = listOf(
            LatLng(47.3662, 8.5445),
            LatLng(47.3663, 8.5414),
            LatLng(47.3630, 8.5360),
            LatLng(47.3715, 8.5418)
        ),
        imageUrl = R.drawable.img_zurifascht,
        date = LocalDate.of(2025, 7, 5),
        heatmapPoints = listOf(
            // RED POIs
            HeatPoint(LatLng(47.3665, 8.5430), 1.0),
            HeatPoint(LatLng(47.3665, 8.5430), 0.7),
            HeatPoint(LatLng(47.3700, 8.5410), 0.9),
            HeatPoint(LatLng(47.3700, 8.5410), 0.6),

            // GREEN POIs
            HeatPoint(LatLng(47.3670, 8.5405), 0.9),
            HeatPoint(LatLng(47.3670, 8.5405), 0.6),
            HeatPoint(LatLng(47.3640, 8.5375), 0.75),
            HeatPoint(LatLng(47.3640, 8.5375), 0.5),

            // extra scattered points
            HeatPoint(LatLng(47.3662, 8.5445), 0.6),
            HeatPoint(LatLng(47.3663, 8.5414), 0.85),
            HeatPoint(LatLng(47.3635, 8.5390), 0.5)
        ),
        pois = listOf(
            POI(LatLng(47.3665, 8.5430), POIColor.RED),
            POI(LatLng(47.3670, 8.5405), POIColor.GREEN),
            POI(LatLng(47.3640, 8.5375), POIColor.GREEN),
            POI(LatLng(47.3700, 8.5410), POIColor.RED),
            POI(LatLng(47.3680, 8.5390), POIColor.GREEN)
        )
    ),
    Event(
        id = 3,
        title = "Sechseläuten (Böögg)",
        description = "Traditional spring festival; the climax is the burning of the 'Böögg' snowman figure at Sechseläutenplatz.",
        area = listOf(
            LatLng(47.3752, 8.5372),
            LatLng(47.3663, 8.5414),
            LatLng(47.3670, 8.5452)
        ),
        imageUrl = R.drawable.img_sechs,
        date = LocalDate.of(2024, 4, 15),
        heatmapPoints = listOf(
            HeatPoint(LatLng(47.3752, 8.5372), 0.5),
            HeatPoint(LatLng(47.3663, 8.5414), 0.7),
            HeatPoint(LatLng(47.3670, 8.5452), 0.95),
            HeatPoint(LatLng(47.3668, 8.5418), 0.9),
            HeatPoint(LatLng(47.3672, 8.5440), 0.8)
        ),
        pois = listOf(
            POI(LatLng(47.3740, 8.5380), POIColor.RED),
            POI(LatLng(47.3668, 8.5418), POIColor.GREEN),
            POI(LatLng(47.3672, 8.5440), POIColor.RED),
            POI(LatLng(47.3680, 8.5435), POIColor.GREEN)
        )
    ),
    Event(
        id = 4,
        title = "Caliente! Latin Festival",
        description = "Europe's largest Latin festival. Features Latin American music, food, and market stalls in the Helvetiaplatz area.",
        area = listOf(
            LatLng(47.3731, 8.5323),
            LatLng(47.3735, 8.5310),
            LatLng(47.3725, 8.5335)
        ),
        imageUrl = R.drawable.img_caliente,
        date = LocalDate.of(2026, 7, 3),
        heatmapPoints = listOf(
            HeatPoint(LatLng(47.3731, 8.5323), 0.8),
            HeatPoint(LatLng(47.3735, 8.5310), 0.6),
            HeatPoint(LatLng(47.3725, 8.5335), 0.7),
            HeatPoint(LatLng(47.3730, 8.5325), 0.9),
            HeatPoint(LatLng(47.3732, 8.5315), 0.7)
        ),
        pois = listOf(
            POI(LatLng(47.3730, 8.5325), POIColor.RED),
            POI(LatLng(47.3732, 8.5315), POIColor.GREEN),
            POI(LatLng(47.3727, 8.5330), POIColor.GREEN),
            POI(LatLng(47.3731, 8.5328), POIColor.RED)
        )
    ),
    Event(
        id = 5,
        title = "Knabenschiessen",
        description = "Zurich's largest public fair, featuring a historic shooting competition for adolescents and a large carnival at Albisgütli.",
        area = listOf(
            LatLng(47.3510, 8.5126),
            LatLng(47.3505, 8.5135),
            LatLng(47.3520, 8.5140)
        ),
        imageUrl = R.drawable.img_festival5,
        date = LocalDate.of(2026, 9, 12),
        heatmapPoints = listOf(
            HeatPoint(LatLng(47.3510, 8.5126), 0.9),
            HeatPoint(LatLng(47.3505, 8.5135), 0.7),
            HeatPoint(LatLng(47.3520, 8.5140), 0.8),
            HeatPoint(LatLng(47.3512, 8.5128), 1.0),
            HeatPoint(LatLng(47.3507, 8.5132), 0.6)
        ),
        pois = listOf(
            POI(LatLng(47.3512, 8.5128), POIColor.RED),
            POI(LatLng(47.3507, 8.5132), POIColor.GREEN),
            POI(LatLng(47.3518, 8.5135), POIColor.RED),
            POI(LatLng(47.3515, 8.5130), POIColor.GREEN)
        )
    )
)
