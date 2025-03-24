package uk.ac.aber.dcs.cs31620.coachplus.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local database user entity.
 * For roles: "coach" or "player"
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = "player", // Default to "player" until role is selected
    val teamId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val password: String = "" // In production, you would hash this
)