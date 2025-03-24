package uk.ac.aber.dcs.cs31620.coachplus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.coachplus.ui.navigation.NavGraph
import uk.ac.aber.dcs.cs31620.coachplus.ui.theme.CoachPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoachPlusTheme {
                CoachPlusApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachPlusApp() {
    val navController = rememberNavController()

    // We use a Scaffold with a TopAppBar that provides navigation
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CoachPlus")
                },
                actions = {
                    // Profile
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    // Team Info
                    IconButton(onClick = { navController.navigate("team_info") }) {
                        Icon(Icons.Default.Group, contentDescription = "Team Info")
                    }
                    // Events
                    IconButton(onClick = { navController.navigate("events") }) {
                        Icon(Icons.Default.Event, contentDescription = "Events")
                    }
                    // Exercises
                    IconButton(onClick = { navController.navigate("exercises") }) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = "Exercises")
                    }
                    // Messages
                    IconButton(onClick = { navController.navigate("messages") }) {
                        Icon(Icons.Default.Message, contentDescription = "Messages")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}
