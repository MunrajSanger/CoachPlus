package uk.ac.aber.dcs.cs31620.coachplus.data.repository

import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebaseTeamDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Team

class TeamRepository(private val teamDao: FirebaseTeamDao) {

    suspend fun insertTeam(team: Team) {
        teamDao.insert(team)
    }

    suspend fun updateTeam(team: Team) {
        teamDao.update(team)
    }

    suspend fun getTeamById(teamId: String): Team? {
        return teamDao.getTeamById(teamId)
    }

    suspend fun getTeamByInvitationCode(invitationCode: String): Team? {
        return teamDao.getTeamByInvitationCode(invitationCode)
    }

    suspend fun getAllTeams(): List<Team> {
        return teamDao.getAllTeams()
    }

    suspend fun deleteTeam(teamId: String) {
        teamDao.deleteTeamById(teamId)
    }
}
