package uk.ac.aber.dcs.cs31620.coachplus.ui.screens

import androidx.compose.foundation.clickable
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
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Event
import uk.ac.aber.dcs.cs31620.coachplus.model.EventType
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.EventViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel
) {
    val user = authViewModel.currentUser ?: return
    var events by remember { mutableStateOf<List<Event>?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var newDay by remember { mutableStateOf("") }
    var newStartTime by remember { mutableStateOf("") }
    var newEndTime by remember { mutableStateOf("") }
    var newLocation by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf(EventType.TRAINING) }
    var isRecurring by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Set the team ID in the EventViewModel
    eventViewModel.teamId = user.teamId ?: ""

    LaunchedEffect(refreshTrigger) {
        events = eventViewModel.getEvents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Events",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (user.role == "coach") {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Create New Event", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        events?.let { eventList ->
            LazyColumn {
                items(eventList) { event ->
                    // Example check to see if "any players are available"
                    val color = if (eventViewModel.isEventConfirmed(event.id))
                        Color.Green else Color.Red

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable { navController.navigate("event_details/${event.id}") },
                        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "${event.day} - ${event.startTime} to ${event.endTime}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Type: ${event.type.capitalize()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "Location: ${event.location}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        } ?: Text("Loading events...", style = MaterialTheme.typography.bodyLarge)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Create Event", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    TextField(
                        value = newDay,
                        onValueChange = { newDay = it },
                        label = { Text("Day (e.g., Monday)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = newStartTime,
                        onValueChange = { newStartTime = it },
                        label = { Text("Start Time (e.g., 14:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = newEndTime,
                        onValueChange = { newEndTime = it },
                        label = { Text("End Time (e.g., 16:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = newLocation,
                        onValueChange = { newLocation = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = newType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Event Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            EventType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        newType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isRecurring,
                            onCheckedChange = { isRecurring = it },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text("Repeat Weekly")
                    }
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
                        if (newDay.isBlank() || newStartTime.isBlank() || newEndTime.isBlank() || newLocation.isBlank()) {
                            errorMessage = "All fields are required"
                        } else if (!newStartTime.matches("\\d{2}:\\d{2}".toRegex())
                            || !newEndTime.matches("\\d{2}:\\d{2}".toRegex())
                        ) {
                            errorMessage = "Time format must be HH:MM"
                        } else {
                            val event = Event(
                                id = UUID.randomUUID().toString(),
                                teamId = eventViewModel.teamId,
                                day = newDay.capitalize(),
                                startTime = newStartTime,
                                endTime = newEndTime,
                                location = newLocation,
                                type = newType.name.lowercase(),
                                isRecurring = isRecurring
                            )
                            eventViewModel.createEvent(event)
                            showDialog = false
                            errorMessage = null
                            refreshTrigger++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Create", color = MaterialTheme.colorScheme.onPrimary)
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
