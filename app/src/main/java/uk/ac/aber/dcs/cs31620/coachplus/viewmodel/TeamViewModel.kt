package uk.ac.aber.dcs.cs31620.coachplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseEventDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebasePlayerDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseTeamDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseUserDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Event
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Player
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Team
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.PlayerRepository
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.TeamRepository
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.UserRepository

/**
 * TeamViewModel manages team creation, updating, and membership (player joining).
 */
class TeamViewModel(private val authViewModel: AuthViewModel) : ViewModel() {

    // CHANGED: using Firebase-based DAOs & Repositories
    private val teamRepository = TeamRepository(FirebaseTeamDao())
    private val playerRepository = PlayerRepository(FirebasePlayerDao())
    private val userRepository = UserRepository(FirebaseUserDao())
    private val eventDao = FirebaseEventDao()

    var currentTeam: Team? = null
        private set

    fun createTeam(team: Team) {
        viewModelScope.launch {
            teamRepository.insertTeam(team)
            currentTeam = team
        }
    }

    fun updateTeam(team: Team) {
        viewModelScope.launch {
            teamRepository.updateTeam(team)
            currentTeam = team
        }
    }

    /**
     * Updates a user's role to "coach" and sets their teamId.
     */
    fun updateCoachTeam(userId: String, teamId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId) ?: return@launch
            val updatedUser = user.copy(role = "coach", teamId = teamId)
            userRepository.updateUser(updatedUser)
            authViewModel.currentUser = updatedUser
        }
    }

    /**
     * Allows a user to join a team as "player," given an invite code.
     */
    fun joinTeam(
        userId: String,
        height: Int,
        weight: Float,
        position: String,
        inviteCode: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val team = teamRepository.getTeamByInvitationCode(inviteCode)
            if (team != null) {
                val user = userRepository.getUserById(userId)
                if (user == null) {
                    onComplete(false)
                    return@launch
                }

                // Insert into 'players' table
                val newPlayer = Player(
                    id = userId,
                    teamId = team.id,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    heightCm = height,
                    weightKg = weight,
                    position = position
                )
                playerRepository.insertPlayer(newPlayer)

                // Update user to role=player with that team
                val updatedUser = user.copy(role = "player", teamId = team.id)
                userRepository.updateUser(updatedUser)
                authViewModel.currentUser = updatedUser

                currentTeam = team
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    /**
     * Creates a recurring "training" event for the specified day/time.
     */
    fun createTrainingEvent(teamId: String, day: String, startTime: String, endTime: String) {
        val event = Event(
            id = UUID.randomUUID().toString(),
            teamId = teamId,
            day = day,
            startTime = startTime,
            endTime = endTime,
            location = "Training Ground",
            type = "training",
            isRecurring = true
        )
        viewModelScope.launch {
            eventDao.insert(event)
        }
    }

    /**
     * Example function that tries to return the coach's email for a team.
     * (Your code doesn't yet do a real query; you'd need a full approach.)
     */
    suspend fun getCoachEmail(teamId: String): String? {
        // Real logic might do: "fetch all users where teamId=..., role=coach"
        // For now, returning null or a placeholder
        return null
    }
}
