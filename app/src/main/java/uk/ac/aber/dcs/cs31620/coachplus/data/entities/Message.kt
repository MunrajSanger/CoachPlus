package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val userId: String = ""
)