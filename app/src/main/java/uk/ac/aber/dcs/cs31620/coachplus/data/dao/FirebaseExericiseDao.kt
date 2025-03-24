package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Exercise

class FirebaseExerciseDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("exercises")

    suspend fun insert(exercise: Exercise) {
        dbRef.child(exercise.id).setValue(exercise).await()
    }

    suspend fun update(exercise: Exercise) {
        dbRef.child(exercise.id).setValue(exercise).await()
    }

    suspend fun getExercisesByTeam(teamId: String): List<Exercise> {
        return dbRef
            .orderByChild("teamId")
            .equalTo(teamId)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Exercise::class.java) }
    }

    suspend fun getExercisesByPositionGroup(teamId: String, positionGroup: String): List<Exercise> {
        // Realtime Database only supports limited querying:
        return getExercisesByTeam(teamId).filter { it.positionGroup == positionGroup }
    }

    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return dbRef
            .child(exerciseId)
            .get()
            .await()
            .getValue(Exercise::class.java)
    }

    suspend fun deleteExerciseById(exerciseId: String) {
        dbRef.child(exerciseId).removeValue().await()
    }
}
