package uk.ac.aber.dcs.cs31620.coachplus.data.repository

import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseExerciseDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Exercise

class ExerciseRepository(private val exerciseDao: FirebaseExerciseDao) {

    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    suspend fun getExercisesByTeam(teamId: String): List<Exercise> {
        return exerciseDao.getExercisesByTeam(teamId)
    }

    suspend fun getExercisesByPositionGroup(teamId: String, positionGroup: String): List<Exercise> {
        return exerciseDao.getExercisesByPositionGroup(teamId, positionGroup)
    }

    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    suspend fun deleteExercise(exerciseId: String) {
        exerciseDao.deleteExerciseById(exerciseId)
    }
}
