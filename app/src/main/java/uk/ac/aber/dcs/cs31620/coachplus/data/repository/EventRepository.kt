package uk.ac.aber.dcs.cs31620.coachplus.data.repository

import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseEventDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Event

class EventRepository(private val eventDao: FirebaseEventDao) {

    suspend fun insertEvent(event: Event) {
        eventDao.insert(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    suspend fun getEventsByTeam(teamId: String): List<Event> {
        return eventDao.getEventsByTeam(teamId)
    }

    suspend fun getEventById(eventId: String): Event? {
        return eventDao.getEventById(eventId)
    }

    suspend fun deleteEvent(eventId: String) {
        eventDao.deleteEventById(eventId)
    }

    suspend fun getEventsByType(teamId: String, type: String): List<Event> {
        return eventDao.getEventsByType(teamId, type)
    }
}
