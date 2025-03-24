package uk.ac.aber.dcs.cs31620.coachplus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.aber.dcs.cs31620.coachplus.ui.screens.*
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.*

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel = AuthViewModel()
    val teamViewModel = TeamViewModel(authViewModel)
    val eventViewModel = EventViewModel(authViewModel)
    val exerciseViewModel = ExerciseViewModel(authViewModel)
    val messageViewModel = MessageViewModel(authViewModel)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("signup") {
            SignUpScreen(navController, authViewModel)
        }
        composable("role_selection") {
            RoleSelectionScreen(navController)
        }
        composable("coach_setup") {
            CoachSetupScreen(navController, authViewModel, teamViewModel)
        }
        composable("player_setup") {
            PlayerSetupScreen(navController, authViewModel, teamViewModel)
        }
        composable("profile") {
            ProfileScreen(navController, authViewModel)
        }
        composable("team_info") {
            TeamInfoScreen(navController, authViewModel, teamViewModel)
        }
        composable("events") {
            EventsScreen(navController, authViewModel, eventViewModel)
        }
        composable("exercises") {
            ExercisesScreen(navController, authViewModel, exerciseViewModel)
        }
        composable("messages") {
            MessagesScreen(navController, authViewModel, messageViewModel)
        }
        composable("event_details/{eventId}") {
            // TODO: Implement EventDetailsScreen
        }
        composable("view_players") {
            // TODO: Implement ViewPlayersScreen
        }
    }
}