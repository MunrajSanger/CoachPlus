package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Team

class FirebaseTeamDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("teams")

    suspend fun insert(team: Team) {
        dbRef.child(team.id).setValue(team).await()
    }

    suspend fun update(team: Team) {
        dbRef.child(team.id).setValue(team).await()
    }

    suspend fun getTeamById(teamId: String): Team? {
        return dbRef.child(teamId).get().await().getValue(Team::class.java)
    }

    suspend fun getTeamByInvitationCode(invitationCode: String): Team? {
        val allTeams = getAllTeams()
        return allTeams.firstOrNull { it.invitationCode == invitationCode }
    }

    suspend fun getAllTeams(): List<Team> {
        return dbRef
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Team::class.java) }
    }

    suspend fun deleteTeamById(teamId: String) {
        dbRef.child(teamId).removeValue().await()
    }
}
