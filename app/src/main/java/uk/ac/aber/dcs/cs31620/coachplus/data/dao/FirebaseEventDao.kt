package uk.ac.aber.dcs.cs31620.coachplus.data.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Event

class FirebaseEventDao {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("events")

    suspend fun insert(event: Event) {
        dbRef.child(event.id).setValue(event).await()
    }

    suspend fun update(event: Event) {
        dbRef.child(event.id).setValue(event).await()
    }

    suspend fun getEventsByTeam(teamId: String): List<Event> {
        return dbRef
            .orderByChild("teamId")
            .equalTo(teamId)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Event::class.java) }
    }

    suspend fun getEventById(eventId: String): Event? {
        return dbRef
            .child(eventId)
            .get()
            .await()
            .getValue(Event::class.java)
    }

    suspend fun deleteEventById(eventId: String) {
        dbRef.child(eventId).removeValue().await()
    }

    suspend fun getEventsByType(teamId: String, type: String): List<Event> {

        val allTeamEvents = getEventsByTeam(teamId)
        return allTeamEvents.filter { it.type == type }
    }
}
