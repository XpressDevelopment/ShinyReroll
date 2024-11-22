package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items.*
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.shinyreroll.managers.PokemonSelectionManager
import net.xpressdev.shinyreroll.utils.MMUtils
import net.xpressdev.shinyreroll.utils.PokemonUtils
import net.xpressdev.shinyreroll.utils.ServerUtils

object PartySelectGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X4, player, false)
        gui.setTitle(Text.literal("Select Pokemon"))

        for (i in 0 until gui.size) {
            gui.setSlot(
                i,
                GuiElementBuilder.from(BLACK_STAINED_GLASS_PANE.defaultStack)
                    .hideTooltip()
                    .build()
            )
        }

        gui.setSlot(
            10,
            GuiElementBuilder(PLAYER_HEAD).setSkullOwner(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=",
                null,
                null
            ).setName(MMUtils.parseText("<green>Confirm"))
                .setCallback { _, _, _ ->
                    PartyPcSelectGui.openGui(player)
                }
                .build()
        )

        val partyGuiSlots = listOf(14, 15, 16, 23, 24, 25)
        partyGuiSlots.forEach { slot ->
            gui.setSlot(
                slot,
                GuiElementBuilder.from(LIGHT_GRAY_STAINED_GLASS_PANE.defaultStack)
                    .hideDefaultTooltip()
                    .setName(Text.literal("Empty Slot"))
                    .build()
            )
        }

        val party = player.party()
        val partySlots = listOf(0, 1, 2, 3, 4, 5)
        partySlots.forEach {
            val pokemon = party.get(it)
            if (pokemon != null) {
                val pokemonElement =
                    if (pokemon.shiny)
                        GuiElementBuilder.from(
                            if (PokemonSelectionManager.isPartyPokemonSelected(player, it))
                                PokemonUtils.getSelectedPokemonItem(pokemon)
                            else
                                PokemonUtils.getPokemonItem(pokemon)
                        )
                    else
                        GuiElementBuilder.from(CobblemonItems.CHERISH_BALL.defaultStack)
                            .setName(MMUtils.parseText("<red>Not a Shiny Pokemon"))
                            .hideDefaultTooltip()

                gui.setSlot(
                    partyGuiSlots[it],
                    pokemonElement
                        .setCallback { _, _, _ ->
                            if (!pokemon.shiny)
                                return@setCallback

                            if (PokemonSelectionManager.isPartyPokemonSelected(player, it))
                                PokemonSelectionManager.deselectPartyPokemon(player, it)
                            else
                                PokemonSelectionManager.selectPartyPokemon(player, it)
                            openGui(player)
                        }
                        .build()
                )
            }
        }

        gui.open()
    }

}