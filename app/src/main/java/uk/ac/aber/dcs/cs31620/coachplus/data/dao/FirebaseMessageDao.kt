package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Message

class FirebaseMessageDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("messages")

    suspend fun insert(message: Message) {
        dbRef.child(message.id).setValue(message).await()
    }

    suspend fun getAllMessages(): List<Message> {
        return dbRef
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Message::class.java) }
            .sortedBy { it.timestamp }
    }

    suspend fun deleteMessage(messageId: String) {
        dbRef.child(messageId).removeValue().await()
    }
}
