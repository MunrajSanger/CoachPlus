package uk.ac.aber.dcs.cs31620.coachplus.data.repository

import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseMessageDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Message

class MessageRepository(private val messageDao: FirebaseMessageDao) {

    suspend fun getAllMessages(): List<Message> {
        return messageDao.getAllMessages()
    }

    suspend fun insertMessage(message: Message) {
        messageDao.insert(message)
    }

    suspend fun deleteMessage(message: Message) {
        messageDao.deleteMessage(message.id)
    }
}
