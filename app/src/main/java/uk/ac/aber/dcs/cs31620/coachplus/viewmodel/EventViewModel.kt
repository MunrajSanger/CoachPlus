package uk.ac.aber.dcs.cs31620.coachplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseEventDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Event
import uk.ac.aber.dcs.cs31620.coachplus.data.repository.EventRepository

/**
 * EventViewModel manages the creation and retrieval of team events.
 */
class EventViewModel(private val authViewModel: AuthViewModel? = null) : ViewModel() {

    // CHANGED: Using the Firebase-based Event Repository
    private val eventRepository = EventRepository(FirebaseEventDao())

    // Typically set after sign in or if the user has a team
    var teamId: String = ""

    // Example of storing local "availability" states (not persisted in DB)
    private val eventAvailability = mutableMapOf<String, MutableList<String>>() // eventId -> [playerIds]

    fun createEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }
    }

    suspend fun getEvents(): List<Event>? {
        return eventRepository.getEventsByTeam(teamId)
    }

    fun markPlayerAvailability(eventId: String, playerId: String, isAvailable: Boolean) {
        val players = eventAvailability.getOrPut(eventId) { mutableListOf() }
        if (isAvailable) players.add(playerId) else players.remove(playerId)
    }

    fun isEventConfirmed(eventId: String): Boolean {
        // We consider an event confirmed if at least one player is available
        return (eventAvailability[eventId]?.isNotEmpty() == true)
    }
}
