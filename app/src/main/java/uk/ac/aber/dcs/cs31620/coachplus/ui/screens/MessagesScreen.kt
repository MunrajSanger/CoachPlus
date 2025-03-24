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
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.AuthViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.MessageViewModel
import uk.ac.aber.dcs.cs31620.coachplus.viewmodel.UiMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessagesScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: MessageViewModel
) {
    val user = authViewModel.currentUser ?: return

    // Our flow of UiMessage
    val messages by viewModel.messages.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load messages on first entry
    LaunchedEffect(Unit) {
        viewModel.loadMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { msg: UiMessage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val dateString = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())
                            .format(Date(msg.timestamp))

                        // Show user name + content
                        Text(
                            text = "${msg.senderName}: ${msg.content} - $dateString",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Any user can delete for now
                        Button(
                            onClick = { viewModel.deleteMessage(msg) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Delete", color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }
        }

        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            label = { Text("New Message") },
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
                if (newMessage.isBlank()) {
                    errorMessage = "Message cannot be empty"
                } else {
                    viewModel.sendMessage(newMessage, user.id)
                    newMessage = ""
                    errorMessage = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Send", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
