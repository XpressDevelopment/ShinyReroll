package net.xpressdev.shinyreroll.commands

import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.guis.PartyPcSelectGui
import net.xpressdev.shinyreroll.managers.PermissionManager
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.managers.SelectionManager
import net.xpressdev.shinyreroll.utils.Messages

object ShinyRerollCommand {

    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("shinyreroll")
                    .then(CommandManager.literal("reload").requires { PermissionManager.canReload(it) }
                        .executes(reloadConfig())
                    )
                    .then(CommandManager.literal("open").requires { PermissionManager.canUseCommand(it) }
                        .executes(openReroll())
                        .then(
                            CommandManager.argument("player", EntityArgumentType.player())
                                .requires { PermissionManager.canUseOtherCommand(it) }
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
            val player = ctx.source.player ?: return@Command 0

            if (RerollManager.isRolling(player)) {
                Messages.error(ctx.source, Message.COMMAND_ROLL_IN_PROGRESS)
                return@Command 0
            }

            SelectionManager.clear(player)
            PartyPcSelectGui.openGui(player)
            1
        }
    }

    private fun openRerollOther(): Command<ServerCommandSource> {
        return Command { ctx ->
            val player = EntityArgumentType.getPlayer(ctx, "player")

            if (RerollManager.isRolling(player)) {
                Messages.error(ctx.source, Message.COMMAND_ROLL_IN_PROGRESS)
                return@Command 0
            }

            SelectionManager.clear(player)
            PartyPcSelectGui.openGui(player)
            1
        }
    }
}
