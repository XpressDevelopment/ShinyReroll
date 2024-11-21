package net.xpressdev.shinyreroll.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager

import net.minecraft.server.command.ServerCommandSource
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.shinyreroll.guis.PartyPcSelectGui
import net.xpressdev.shinyreroll.managers.PermissionManager
import net.xpressdev.shinyreroll.managers.PokemonSelectionManager

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
            1
        }
    }

    private fun openReroll(): Command<ServerCommandSource> {
        return Command { ctx ->
            if (ctx.source.player == null) return@Command 1

            PokemonSelectionManager.init(ctx.source.player!!)
            PartyPcSelectGui.openGui(ctx.source.player!!)
            1
        }
    }

    private fun openRerollOther(): Command<ServerCommandSource> {
        return Command { ctx ->
            1
        }
    }
}