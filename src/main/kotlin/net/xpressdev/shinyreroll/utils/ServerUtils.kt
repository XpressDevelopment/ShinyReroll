package net.xpressdev.shinyreroll.utils

import net.xpressdev.shinyreroll.ShinyReroll
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

object ServerUtils {
    fun broadcast(message: String) {
        val server = ShinyReroll.serverInstance

        for (player in server.playerManager.playerList) {
            if (player is ServerPlayerEntity) {
                player.sendMessage(Text.literal(message), false)
            }
        }
    }

    fun getPlayerByUUID(uuid: UUID): ServerPlayerEntity? {
        val server = ShinyReroll.serverInstance
        val playerManager = server.playerManager

        return playerManager.getPlayer(uuid)
    }

    fun getPlayerByName(name: String): ServerPlayerEntity? {
        val server = ShinyReroll.serverInstance
        val playerManager = server.playerManager

        return playerManager.getPlayer(name)
    }

    fun getAllPlayerNames(): Array<out String>? {
        val server = ShinyReroll.serverInstance
        val playerManager = server.playerManager

        return playerManager.playerNames
    }

    fun getAllPlayers(): MutableList<ServerPlayerEntity> {
        val server = ShinyReroll.serverInstance
        val playerManager = server.playerManager

        return playerManager.playerList
    }
}