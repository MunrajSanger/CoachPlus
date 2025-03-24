package uk.ac.aber.dcs.cs31620.coachplus.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseExerciseDao
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebasePlayerDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Exercise
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.ExerciseRepository
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.PlayerRepository

/**
 * ExerciseViewModel manages the logic for exercises assigned to players or teams.
 */
class ExerciseViewModel(private val authViewModel: AuthViewModel? = null) : ViewModel() {

    // CHANGED: Using Firebase-based Repositories
    private val exerciseRepository = ExerciseRepository(FirebaseExerciseDao())
    private val playerRepository = PlayerRepository(FirebasePlayerDao())

    // We store the current teamId so we can fetch relevant exercises
    var teamId: String = ""
        set(value) {
            field = value
            fetchExercises()
        }

    private val _exercises = mutableStateOf<List<Exercise>?>(null)
    val exercises: State<List<Exercise>?> = _exercises

    init {
        // If we want to auto-fetch on init, we can do that here
        fetchExercises()
    }

    private fun fetchExercises() {
        viewModelScope.launch {
            _exercises.value = exerciseRepository.getExercisesByTeam(teamId)
        }
    }

    fun createExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.insertExercise(exercise)
            fetchExercises()
        }
    }

    suspend fun getExercises(): List<Exercise>? {
        return exerciseRepository.getExercisesByTeam(teamId)
    }

    fun markExerciseComplete(exerciseId: String) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseById(exerciseId)
            exercise?.let {
                val updated = it.copy(
                    isCompleted = true,
                    completedAt = System.currentTimeMillis()
                )
                exerciseRepository.updateExercise(updated)
                fetchExercises()
            }
        }
    }

    /**
     * Example function to get heaviest player by position - might not be used.
     */
    suspend fun getPlayerWeight(teamId: String, positionGroup: String): Float {
        return playerRepository
            .getPlayersByPosition(teamId, positionGroup)
            .maxOfOrNull { it.weightKg } ?: 0f
    }

    /**
     * For a given userId (player), returns their position for filtering exercises.
     */
    suspend fun getPlayerPosition(playerId: String): String? {
        return playerRepository.getPlayerById(playerId)?.position
    }
}
