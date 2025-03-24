package uk.ac.aber.dcs.cs31620.coachplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseMessageDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseUserDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Message
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.MessageRepository
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.UserRepository

/**
 * A UI-friendly version of [Message] that also stores the senderName
 */
data class UiMessage(
    val id: String,
    val content: String,
    val timestamp: Long,
    val userId: String,
    val senderName: String
)

/**
 * MessageViewModel manages chat-like messages in Firebase Realtime Database.
 */
class MessageViewModel(private val authViewModel: AuthViewModel? = null) : ViewModel() {

    // CHANGED: Using Firebase-based Repositories
    private val messageRepository = MessageRepository(FirebaseMessageDao())
    private val userRepository = UserRepository(FirebaseUserDao())

    // We store UI messages in a Flow to observe changes
    val messages = MutableStateFlow<List<UiMessage>>(emptyList())

    fun loadMessages() {
        viewModelScope.launch {
            val rawMessages = messageRepository.getAllMessages()

            val uiList = rawMessages.map { msg ->
                // fetch user name from user table if needed
                val user = userRepository.getUserById(msg.userId)
                val name = if (user != null) "${user.firstName} ${user.lastName}" else "Unknown"
                UiMessage(
                    id = msg.id,
                    content = msg.content,
                    timestamp = msg.timestamp,
                    userId = msg.userId,
                    senderName = name
                )
            }.sortedBy { it.timestamp }

            messages.value = uiList
        }
    }

    /**
     * Inserts a new message from the given userId with current timestamp.
     */
    fun sendMessage(content: String, userId: String) {
        viewModelScope.launch {
            val newMessage = Message(
                id = UUID.randomUUID().toString(),
                content = content,
                timestamp = System.currentTimeMillis(),
                userId = userId
            )
            messageRepository.insertMessage(newMessage)
            loadMessages()
        }
    }

    /**
     * Deletes a specific message from the DB.
     */
    fun deleteMessage(uiMessage: UiMessage) {
        viewModelScope.launch {
            val raw = Message(
                id = uiMessage.id,
                content = uiMessage.content,
                timestamp = uiMessage.timestamp,
                userId = uiMessage.userId
            )
            messageRepository.deleteMessage(raw)
            loadMessages()
        }
    }
}
