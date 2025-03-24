package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey val id: String = "",
    val teamId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val heightCm: Int = 0,
    val weightKg: Float = 0f,
    val position: String = "", // "attacker", "midfielder", "defender"
    val joinedAt: Long = System.currentTimeMillis()
)