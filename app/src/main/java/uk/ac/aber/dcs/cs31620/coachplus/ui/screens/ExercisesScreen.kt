package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Exercise
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.ExerciseViewModel
import java.util.UUID

@Composable
fun ExercisesScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    exerciseViewModel: ExerciseViewModel
) {
    val user = authViewModel.currentUser ?: return

    var showDialog by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var positionGroup by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var playerPosition by remember { mutableStateOf<String?>(null) }

    // Set the team ID in the ViewModel
    exerciseViewModel.teamId = user.teamId ?: ""

    // Fetch player's position if needed
    LaunchedEffect(user.id) {
        playerPosition = exerciseViewModel.getPlayerPosition(user.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Exercises",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (user.role == "coach") {
            // Coach can assign a new exercise
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Assign Exercise", color = MaterialTheme.colorScheme.onPrimary)
            }

            exerciseViewModel.exercises.value?.let { exerciseList ->
                // Show exercises grouped by position
                LazyColumn {
                    exerciseList.groupBy { it.positionGroup ?: "Whole Team" }.forEach { (group, groupExercises) ->
                        item {
                            Text(
                                text = "Group: $group",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(groupExercises) { exercise ->
                            // Indicate if it is completed
                            val statusText = if (exercise.isCompleted) " (Completed)" else " (Not Completed)"

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Text(
                                    text = exercise.description + statusText,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            } ?: Text("Loading exercises...", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Role == "player"
            exerciseViewModel.exercises.value?.let { exerciseList ->
                val filteredExercises = exerciseList.filter { ex ->
                    ex.positionGroup == null || ex.positionGroup == playerPosition
                }
                LazyColumn {
                    items(filteredExercises) { exercise ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (exercise.isCompleted) {
                                    Color.Green.copy(alpha = 0.1f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exercise.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (!exercise.isCompleted) {
                                    Button(
                                        onClick = {
                                            // Mark complete
                                            exerciseViewModel.markExerciseComplete(exercise.id)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text("Mark Complete", color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                } else {
                                    Text("Completed", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            } ?: Text("Loading exercises...", style = MaterialTheme.typography.bodyLarge)
        }
    }

    // Dialog for assigning new exercise
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Assign Exercise", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Exercise Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = positionGroup,
                        onValueChange = { positionGroup = it },
                        label = { Text("Position Group (optional: attacker, midfielder, defender)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (description.isBlank()) {
                            errorMessage = "Description is required"
                        } else if (
                            positionGroup.isNotEmpty() &&
                            positionGroup.lowercase() !in listOf("attacker", "midfielder", "defender")
                        ) {
                            errorMessage = "Position group must be attacker, midfielder, or defender"
                        } else {
                            val exercise = Exercise(
                                id = UUID.randomUUID().toString(),
                                teamId = exerciseViewModel.teamId,
                                positionGroup = if (positionGroup.isEmpty()) null else positionGroup.lowercase(),
                                description = description
                            )
                            exerciseViewModel.createExercise(exercise)
                            showDialog = false
                            errorMessage = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Assign", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        )
    }
}
