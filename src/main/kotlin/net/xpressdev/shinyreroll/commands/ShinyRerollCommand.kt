package net.xpressdev.shinyreroll.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager

import net.minecraft.server.command.ServerCommandSource
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.guis.PartyPcSelectGui
import net.xpressdev.shinyreroll.managers.PermissionManager
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.managers.SelectionManager
import net.xpressdev.shinyreroll.utils.Messages
import net.xpressdev.shinyreroll.utils.ServerUtils

class ShinyRerollCommand {

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("shinyreroll")
                    .then(CommandManager.literal("reload").requires { PermissionManager.canReload(it) }
                        .executes(reloadConfig())
                    )
                    .then(CommandManager.literal("open").requires { PermissionManager.canUseCommand(it) }
                        .executes(openReroll())
                        .then(CommandManager.argument("player", StringArgumentType.string()).requires { PermissionManager.canUseOtherCommand(it) }
                            .executes(openRerollOther())
                        )
                    )
            )
        }
    }

    private fun reloadConfig(): Command<ServerCommandSource> {
        return Command { ctx ->
            ConfigStore.load()
            Messages.feedback(ctx.source, Message.COMMAND_CONFIG_RELOADED)
            1
        }
    }

    private fun openReroll(): Command<ServerCommandSource> {
        return Command { ctx ->
            val player = ctx.source.player ?: return@Command 1

            if (RerollManager.isRolling(player)) {
                Messages.error(ctx.source, Message.COMMAND_ROLL_IN_PROGRESS)
                return@Command 1
            }

            SelectionManager.clear(player)
            PartyPcSelectGui.openGui(player)
            1
        }
    }

    private fun openRerollOther(): Command<ServerCommandSource> {
        return Command { ctx ->

            val playerName = StringArgumentType.getString(ctx, "player")
            val player = ServerUtils.getPlayerByName(playerName)

            if (player == null) {
                Messages.error(ctx.source, Message.COMMAND_PLAYER_NOT_FOUND)
                return@Command 1
            }

            if (RerollManager.isRolling(player)) {
                Messages.error(ctx.source, Message.COMMAND_ROLL_IN_PROGRESS)
                return@Command 1
            }

            SelectionManager.clear(player)
            PartyPcSelectGui.openGui(player)
            1
        }
    }
}