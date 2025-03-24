package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.coachplus.ui.theme.LatoFont
import uk.ac.aber.dcs.cs31620.coachplus.ui.theme.MontserratFont

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Successfully Registered Message
        Text(
            text = "Successfully Registered!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = LatoFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E232C),
                letterSpacing = 0.72.sp
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Choose Role
        Text(
            text = "Please select whether you are a player or a coach",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = LatoFont,
                fontSize = 14.sp,
                color = Color(0xFF222326),
                letterSpacing = 0.72.sp
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        // Coach Button
        Button(
            onClick = { navController.navigate("coach_setup") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F6AFF))
        ) {
            Text("Coach", fontFamily = LatoFont, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Player Button
        Button(
            onClick = { navController.navigate("player_setup") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F6AFF))
        ) {
            Text("Player", fontFamily = LatoFont, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(40.dp))


        Text(
            text = "Welcome to CoachPlus - Team Management Application",
            fontSize = 14.sp,
            fontFamily = LatoFont,
            color = Color(0xFF222326),
            modifier = Modifier.padding(bottom = 24.dp)
        )


        Spacer(modifier = Modifier.height(40.dp))
    }
}
