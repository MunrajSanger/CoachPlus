package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.User

class FirebaseUserDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    suspend fun insert(user: User) {
        dbRef.child(user.id).setValue(user).await()
    }

    suspend fun update(user: User) {
        dbRef.child(user.id).setValue(user).await()
    }

    suspend fun getUserById(userId: String): User? {
        return dbRef
            .child(userId)
            .get()
            .await()
            .getValue(User::class.java)
    }

    suspend fun getUserByEmail(email: String): User? {
        // Realtime DB can "orderByChild('email').equalTo(email)"
        return dbRef
            .orderByChild("email")
            .equalTo(email)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(User::class.java) }
            .firstOrNull()
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        val user = getUserByEmail(email)
        return if (user != null && user.password == password) user else null
    }

    suspend fun deleteUserById(userId: String) {
        dbRef.child(userId).removeValue().await()
    }
}
