package net.xpressdev.shinyreroll.managers

import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.ServerCommandSource

object PermissionManager {

    fun canReload(source: ServerCommandSource): Boolean =
        Permissions.check(source, "shinyreroll.reload", 2)

    fun canUseCommand(source: ServerCommandSource): Boolean =
        Permissions.check(source, "shinyreroll.self", 2)

    fun canUseOtherCommand(source: ServerCommandSource): Boolean =
        Permissions.check(source, "shinyreroll.other", 2)

}
