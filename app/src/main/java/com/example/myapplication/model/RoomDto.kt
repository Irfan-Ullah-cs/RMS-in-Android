import com.example.myapplication.model.WindowDto
data class RoomDto(
    val id: Long = 0L, // Default value
    val name: String,
    val currentTemperature: Double?,
    val targetTemperature: Double?,
    val windows: List<WindowDto> = emptyList()
)