package uk.ac.aber.dcs.cs31620.coachplus.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = "player",
    val teamId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)