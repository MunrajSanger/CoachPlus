package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs31620.coachplus.R


@Composable
fun SplashScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    // Start animation
    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000L) // Keep splash screen for 2 seconds
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000)) + expandIn(expandFrom = Alignment.Center),
                exit = fadeOut(animationSpec = tween(1000))
            ) {
                Text(
                    text = "CoachPlus",
                    style = TextStyle(
                        fontSize = 65.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.playfair_display)),
                        color = Color(0xFF5F6AFF),
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Subtitle
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1500)) + slideInVertically(),
                exit = fadeOut(animationSpec = tween(1000))
            ) {
                Text(
                    text = "Team-Management Application",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.lato_regular)),
                        color = Color(0xFF7E7B77),
                        letterSpacing = 0.sp
                    )
                )
            }
        }
    }
}
