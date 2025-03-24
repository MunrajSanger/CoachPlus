package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Player

class FirebasePlayerDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("players")

    suspend fun insert(player: Player) {
        dbRef.child(player.id).setValue(player).await()
    }

    suspend fun update(player: Player) {
        dbRef.child(player.id).setValue(player).await()
    }

    suspend fun getPlayersByTeam(teamId: String): List<Player> {
        // cannot do "ORDER BY weightKg DESC" server-side, so sort in memory
        return dbRef
            .orderByChild("teamId")
            .equalTo(teamId)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Player::class.java) }
            .sortedByDescending { it.weightKg }
    }

    suspend fun getPlayerById(playerId: String): Player? {
        return dbRef
            .child(playerId)
            .get()
            .await()
            .getValue(Player::class.java)
    }

    suspend fun deletePlayerById(playerId: String) {
        dbRef.child(playerId).removeValue().await()
    }

    suspend fun getPlayersByPosition(teamId: String, position: String): List<Player> {
        return getPlayersByTeam(teamId).filter { it.position == position }
    }
}
