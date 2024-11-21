package net.xpressdev.shinyreroll.managers

import me.lucko.fabric.api.permissions.v0.Permissions
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.ShinyReroll

object PermissionManager {

    fun canReload(source: ServerCommandSource): Boolean {
        return source.hasPermissionLevel(2) || (ShinyReroll.luckPerms != null && Permissions.check(
            source,
            "shinyreroll.reload",
            2
        ))
    }

    fun canUseCommand(source: ServerCommandSource): Boolean {
        return source.hasPermissionLevel(2) || (ShinyReroll.luckPerms != null && Permissions.check(
            source,
            "shinyreroll.use",
            2
        ))
    }

    fun canUseOtherCommand(source: ServerCommandSource): Boolean {
        return source.hasPermissionLevel(2) || (ShinyReroll.luckPerms != null && Permissions.check(
            source,
            "shinyreroll.use.other",
            2
        ))
    }

    fun getRemainingUses(player: ServerPlayerEntity): Int {
        if (ShinyReroll.luckPerms == null) return 0

        val user = getLuckPermsUser(player)
        val nodes = user.nodes
        nodes.forEach {
            if (it.key.contains("shinyreroll.uses."))
                return it.key.split(".")[2].toInt()
        }
        return 0
    }

    fun lowerRemainingUses(player: ServerPlayerEntity) {
        if (ShinyReroll.luckPerms == null) return

        val user = getLuckPermsUser(player)
        val nodes = user.nodes
        nodes.forEach {
            if (it.key.contains("shinyreroll.uses.")) {
                val uses = it.key.split(".")[2].toInt()
                user.data().remove(it)
                user.data().add(Node.builder("shinyreroll.uses.${uses - 1}").build())
            }
        }
    }

    private fun getLuckPermsUser(player: ServerPlayerEntity): User {
        return ShinyReroll.luckPerms!!.getPlayerAdapter(ServerPlayerEntity::class.java).getUser(player)
    }

}