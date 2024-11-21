package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.pokemon.Pokemon
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items.*
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.shinyreroll.managers.PokemonSelectionManager
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.MMUtils
import net.xpressdev.shinyreroll.utils.PokemonUtils
import kotlin.math.floor

object SelectedPokemonGui {

    fun openGui(player: ServerPlayerEntity, sectionNum: Int) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Text.literal("Selected Pokemon - ${sectionNum + 1}")

        for (i in 0 until gui.size) {
            if (i in 0..5 || i in 9..14 || i in 18..23 || i in 27..32 || i in 36..41) {
                gui.setSlot(
                    i,
                    GuiElementBuilder.from(GRAY_STAINED_GLASS_PANE.defaultStack)
                        .setName(Text.literal("Empty"))
                        .build()
                )
            } else {
                gui.setSlot(
                    i,
                    GuiElementBuilder.from(BLACK_STAINED_GLASS_PANE.defaultStack)
                        .hideTooltip()
                        .build()
                )
            }
        }

        val selectedPokemon = PokemonSelectionManager.getSelectedPokemon(player)
        for (i in 0 until 30) {
            if (i + sectionNum * 30 >= selectedPokemon.size)
                break
            val pokemon: Pokemon = selectedPokemon[i + sectionNum * 30]
            val row = floor(i.toDouble() / 6.0)
            val index = i % 6
            gui.setSlot(
                (row * 9).toInt() + index,
                GuiElementBuilder.from(PokemonUtils.getPokemonItem(pokemon)).setCallback { _, _, _ ->
                    if (PokemonSelectionManager.isPokemonSelected(player, pokemon))
                        PokemonSelectionManager.removeSelectedPokemon(player, pokemon)
                    openGui(player, sectionNum)
                }
                    .build()
            )
        }

        gui.setSlot(
            42,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=",
                null,
                null
            )
                .setName(MMUtils.parseText("Previous"))
                .setCallback { _, _, _ ->
                    if (sectionNum > 0)
                        openGui(player, sectionNum - 1)
                    else
                        openGui(player, floor(selectedPokemon.size * 1.0 / 30).toInt())
                }
                .build()
        )
        gui.setSlot(
            44,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19",
                null,
                null
            )
                .setName(MMUtils.parseText("Next"))
                .setCallback { _, _, _ ->
                    if (sectionNum < floor(selectedPokemon.size * 1.0 / 30).toInt())
                        openGui(player, sectionNum + 1)
                    else
                        openGui(player, 0)
                }
                .build()
        )

        if (!GeneralUtils.isOverLimit(player))
            gui.setSlot(
                24,
                GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=",
                    null,
                    null
                ).setName(MMUtils.parseText("<green>Confirm"))
                    .setCallback { _, _, _ ->
                        if (PokemonSelectionManager.getSelectedPokemon(player).size > 0)
                            ConfirmationGui.openGui(player)
                        else
                            player.sendMessage(MMUtils.parseText("<red>You must select at least one Pokemon"))
                    }
                    .build()
            )
        else
            gui.setSlot(
                24,
                GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmYwNGIyOTJhZDk0OGMwZTlmMGQzM2M2NGYxMzFmMzJlM2M1MTg4NDU2Zjc3ODc0NWEwMGYyYjRiNWFkMDM1OSJ9fX0=",
                    null,
                    null
                ).setName(MMUtils.parseText("<red>Too many Pokemon selected"))
                    .addLoreLine(MMUtils.parseText("<gray>You can only select up to ${ShinyReroll.maxPokemonSelected} Pokemon"))
                    .build()
            )

        gui.setSlot(
            26,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc1NDgzNjJhMjRjMGZhODQ1M2U0ZDkzZTY4YzU5NjlkZGJkZTU3YmY2NjY2YzAzMTljMWVkMWU4NGQ4OTA2NSJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<dark_red>Cancel"))
                .setCallback { _, _, _ ->
                    PartyPcSelectGui.openGui(player)
                }
                .build()
        )

        gui.setSlot(
            7,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<gold>Info"))
                .addLoreLine(MMUtils.parseText("<gray>Current Shiny Chance: <gold>${GeneralUtils.formatShinyChance(player)}%"))
                .addLoreLine(MMUtils.parseText("<gray>Max Shiny Chance: <gold>${GeneralUtils.formatMaxShinyChance()}%"))
                .build()
        )

        gui.open()
    }

}