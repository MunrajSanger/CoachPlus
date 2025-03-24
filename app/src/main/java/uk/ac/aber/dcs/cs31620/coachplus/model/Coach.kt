package uk.ac.aber.dcs.cs31620.coachplus.model

data class Coach(
    val user: User,
    val teamId: String
) {
    init {
        require(user.role == "coach") { "User must have role 'coach'" }
    }

    fun getFullName(): String = "${user.firstName} ${user.lastName}"
}
