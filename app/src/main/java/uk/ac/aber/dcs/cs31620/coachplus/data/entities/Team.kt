package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val sport: String = "",
    val type: String = "", // e.g., "mens" or "womens"
    val invitationCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)