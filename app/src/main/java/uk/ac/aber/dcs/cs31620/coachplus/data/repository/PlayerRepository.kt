package uk.ac.aber.dcs.cs31620.coachplus.data.repository

import uk.ac.aber.dcs.cs31620.coachplus.data.dao.FirebasePlayerDao
import uk.ac.aber.dcs.cs31620.coachplus.data.entities.Player

class PlayerRepository(private val playerDao: FirebasePlayerDao) {

    suspend fun insertPlayer(player: Player) {
        playerDao.insert(player)
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.update(player)
    }

    suspend fun getPlayersByTeam(teamId: String): List<Player> {
        return playerDao.getPlayersByTeam(teamId)
    }

    suspend fun getPlayerById(playerId: String): Player? {
        return playerDao.getPlayerById(playerId)
    }

    suspend fun deletePlayer(playerId: String) {
        playerDao.deletePlayerById(playerId)
    }

    suspend fun getPlayersByPosition(teamId: String, position: String): List<Player> {
        return playerDao.getPlayersByPosition(teamId, position)
    }
}
