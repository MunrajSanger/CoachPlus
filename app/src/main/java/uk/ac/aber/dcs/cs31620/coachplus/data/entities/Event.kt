package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String = "",
    val teamId: String = "",
    val day: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val location: String = "",
    val type: String = "training", // "training", "match", "event", "tournament"
    val isRecurring: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)