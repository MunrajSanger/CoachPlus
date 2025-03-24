package uk.ac.aber.dcs.cs31620.coachplus.model

data class Player(
    val user: User,
    val teamId: String,
    val heightCm: Int,
    val weightKg: Float,
    val position: String // "attacker", "midfielder", "defender"
) {
    init {
        require(user.role == "player") { "User must have role 'player'" }
        require(position in listOf("attacker", "midfielder", "defender")) { "Invalid position" }
    }

    fun getFullName(): String = "${user.firstName} ${user.lastName}"
    fun getHeightInMeters(): Float = heightCm / 100f
}