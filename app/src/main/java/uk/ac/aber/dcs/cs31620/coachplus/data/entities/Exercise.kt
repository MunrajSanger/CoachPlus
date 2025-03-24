package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: String = "",
    val teamId: String = "",
    val positionGroup: String? = null, // null if assigned to whole team
    val description: String = "",
    val isCompleted: Boolean = false,
    val assignedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)