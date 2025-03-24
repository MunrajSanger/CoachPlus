package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Team
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.TeamViewModel
import java.util.UUID

@Composable
fun CoachSetupScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: TeamViewModel
) {
    val user = authViewModel.currentUser ?: return
    var teamName by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("") }
    var teamType by remember { mutableStateOf("") }
    var trainingDays by remember { mutableStateOf(listOf<String>()) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Set Up Your Team",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = teamName,
            onValueChange = { teamName = it },
            label = { Text("Team Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        TextField(
            value = sport,
            onValueChange = { sport = it },
            label = { Text("Sport") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        TextField(
            value = teamType,
            onValueChange = { teamType = it },
            label = { Text("Team Type (e.g., mens, womens)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        Text(
            text = "Select Training Days:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        LazyColumn {
            items(days) { day ->
                var isChecked by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            trainingDays = if (it) trainingDays + day else trainingDays - day
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(day, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
        TextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time (e.g., 14:00)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        TextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time (e.g., 16:00)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Button(
            onClick = {
                if (teamName.isBlank() || sport.isBlank() || teamType.isBlank()
                    || trainingDays.isEmpty() || startTime.isBlank() || endTime.isBlank()
                ) {
                    errorMessage = "All fields are required"
                } else if (!startTime.matches("\\d{2}:\\d{2}".toRegex())
                    || !endTime.matches("\\d{2}:\\d{2}".toRegex())
                ) {
                    errorMessage = "Time format must be HH:MM"
                } else {
                    val team = Team(
                        id = UUID.randomUUID().toString(),
                        name = teamName,
                        sport = sport,
                        type = teamType,
                        invitationCode = UUID.randomUUID().toString().substring(0, 6)
                    )
                    viewModel.createTeam(team)
                    // Create recurring training events
                    trainingDays.forEach { day ->
                        viewModel.createTrainingEvent(team.id, day, startTime, endTime)
                    }
                    // Update the local user to be a coach with this team
                    viewModel.updateCoachTeam(userId = user.id, teamId = team.id)
                    navController.navigate("profile") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Complete Setup", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
