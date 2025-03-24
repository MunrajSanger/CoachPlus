package uk.ac.aber.dcs.cs31620.coachplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import uk.ac.aber.dcs.cs31620.coachplus.R

private val CoachPlusColorScheme = lightColorScheme(
    primary = Color(0xFF5F6AFF), // Purple
    secondary = Color(0xFFBB86FC), // Lighter Purple
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun CoachPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoachPlusColorScheme,
        typography = Typography,
        content = content
    )
}

val LatoFont = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_bold, weight = androidx.compose.ui.text.font.FontWeight.Bold)
)

val MontserratFont = FontFamily(
    Font(R.font.montserrat_semibold, weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
)