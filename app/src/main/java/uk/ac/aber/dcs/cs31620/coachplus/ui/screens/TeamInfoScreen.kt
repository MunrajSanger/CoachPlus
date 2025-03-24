package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.TeamViewModel

@Composable
fun TeamInfoScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    teamViewModel: TeamViewModel
) {
    val user = authViewModel.currentUser ?: return
    val team = teamViewModel.currentTeam ?: return

    var teamName by remember { mutableStateOf(team.name) }
    var teamType by remember { mutableStateOf(team.type) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var coachEmail by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(team.id) {
        coachEmail = teamViewModel.getCoachEmail(team.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Team Info",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (user.role == "coach") {
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
                value = teamType,
                onValueChange = { teamType = it },
                label = { Text("Team Type") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            Text(
                text = "Invitation Code: ${team.invitationCode}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(
                onClick = {
                    if (teamName.isBlank() || teamType.isBlank()) {
                        errorMessage = "Team name and type are required"
                    } else {
                        teamViewModel.updateTeam(team.copy(name = teamName, type = teamType))
                        errorMessage = "Team info updated successfully"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Edits", color = MaterialTheme.colorScheme.onPrimary)
            }
        } else {
            Text(
                text = "Team Name: $teamName",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Team Type: $teamType",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            coachEmail?.let {
                Text(
                    text = "Coach Email: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } ?: Text("Loading coach email...", style = MaterialTheme.typography.bodyLarge)
        }


        Button(
            onClick = { navController.navigate("view_players") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("View All Players", color = MaterialTheme.colorScheme.onPrimary)
        }

        errorMessage?.let {
            Text(
                text = it,
                color = if (it.contains("successfully")) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
