package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.TeamViewModel

@Composable
fun PlayerSetupScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: TeamViewModel
) {
    val user = authViewModel.currentUser ?: return
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Join a Team",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        TextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Position (attacker, midfielder, defender)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )
        TextField(
            value = inviteCode,
            onValueChange = { inviteCode = it },
            label = { Text("Invite Code") },
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
                if (height.isBlank() || weight.isBlank() || position.isBlank() || inviteCode.isBlank()) {
                    errorMessage = "All fields are required"
                } else if (position.lowercase() !in listOf("attacker", "midfielder", "defender")) {
                    errorMessage = "Position must be attacker, midfielder, or defender"
                } else {
                    viewModel.joinTeam(
                        userId = user.id,
                        height = height.toIntOrNull() ?: 0,
                        weight = weight.toFloatOrNull() ?: 0f,
                        position = position.lowercase(),
                        inviteCode = inviteCode
                    ) { success ->
                        if (success) {
                            navController.navigate("profile") {
                                popUpTo("role_selection") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Invalid invite code or team not found"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Join Team", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
