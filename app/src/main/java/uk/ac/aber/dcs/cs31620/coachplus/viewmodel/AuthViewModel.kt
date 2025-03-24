package uk.ac.aber.dcs.cs31620.coachplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebasePlayerDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseUserDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Player
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.User
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.PlayerRepository
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.UserRepository

/**
 * AuthViewModel is responsible for user sign-up, sign-in, and sign-out logic.
 * It stores the currently signed-in user in [currentUser].
 */
class AuthViewModel : ViewModel() {

    // CHANGED: Instantiating Firebase-based DAOs and Repositories
    private val userRepository = UserRepository(FirebaseUserDao())
    private val playerRepository = PlayerRepository(FirebasePlayerDao())

    var currentUser: User? = null

    /**
     * Sign up a new user. If a user with the same email exists, fail.
     */
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Check if user with same email already exists
            val existing = userRepository.getUserByEmail(email)
            if (existing != null) {
                onComplete(false)
            } else {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    role = "player",  // default role until user picks
                    password = password
                )
                userRepository.insertUser(newUser)
                currentUser = newUser
                onComplete(true)
            }
        }
    }

    /**
     * Attempt to sign in by checking email/password in Firebase Realtime Database.
     */
    fun signIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmailAndPassword(email, password)
            currentUser = user
            onComplete(user != null)
        }
    }

    /**
     * Sign out simply clears the current user in this local model.
     */
    fun signOut() {
        currentUser = null
    }

    /**
     * Returns the Player entity for this user, if role == "player".
     */
    suspend fun getPlayerInfo(userId: String): Player? {
        return playerRepository.getPlayerById(userId)
    }

    /**
     * Updates user's name/email, and if user is a player, updates height/weight in 'players'.
     */
    fun updateUserProfile(
        userId: String,
        firstName: String,
        lastName: String,
        email: String,
        height: Int,
        weight: Float
    ) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId) ?: return@launch
            val updatedUser = user.copy(
                firstName = firstName,
                lastName = lastName,
                email = email
            )
            userRepository.updateUser(updatedUser)
            currentUser = updatedUser

            // If user is a player, also update the 'players' table in Firebase
            if (updatedUser.role == "player") {
                val player = playerRepository.getPlayerById(userId)
                if (player != null) {
                    val updatedPlayer = player.copy(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        heightCm = height,
                        weightKg = weight
                    )
                    playerRepository.updatePlayer(updatedPlayer)
                }
            }
        }
    }
}
