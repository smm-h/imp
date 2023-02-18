import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class OutputLine(private val text: String, val category: Category) {
    private val dateTime: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    override fun toString(): String = "${dateTime.format(formatter)}: $text"

    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_TIME
    }

    enum class Category(val color: Color) {
        MESSAGE(Color.DarkGray), INFO(Color.Blue), ERROR(Color.Red)
    }
}