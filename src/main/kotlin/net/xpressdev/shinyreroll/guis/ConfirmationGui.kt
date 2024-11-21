package net.xpressdev.shinyreroll.guis

import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items.*
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.MMUtils

object ConfirmationGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Text.literal("Reroll Shiny - ${GeneralUtils.formatShinyChance(player)}%")

        for (i in 0 until gui.size) {
            gui.setSlot(
                i,
                GuiElementBuilder.from(BLACK_STAINED_GLASS_PANE.defaultStack)
                    .setName(Text.literal(""))
                    .build()
            )
        }

        gui.setSlot(
            13,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<white><bold>Are you sure you want to reroll your shinies?"))
                .addLoreLine(MMUtils.parseText("<gray>Current Shiny Chance: <gold>${GeneralUtils.formatShinyChance(player)}%"))
                .addLoreLine(MMUtils.parseText("<gray>Max Shiny Chance: <gold>${GeneralUtils.formatMaxShinyChance()}%"))
                .build()
        )

        gui.setSlot(
            30,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<green>Confirm"))
                .setCallback { _, _, _ ->
                    RerollManager.init(player)
                    RollingScreenGui.openGui(player)
                }
                .build()
        )

        gui.setSlot(
            32,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc1NDgzNjJhMjRjMGZhODQ1M2U0ZDkzZTY4YzU5NjlkZGJkZTU3YmY2NjY2YzAzMTljMWVkMWU4NGQ4OTA2NSJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<dark_red>Cancel"))
                .setCallback { _, _, _ ->
                    SelectedPokemonGui.openGui(player, 0)
                }
                .build()
        )

        gui.open()
    }

}