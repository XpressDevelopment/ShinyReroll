package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.util.party
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items.*
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.managers.SelectionManager
import net.xpressdev.shinyreroll.utils.Messages
import net.xpressdev.shinyreroll.utils.PokemonUtils

object PartySelectGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X4, player, false)
        gui.setTitle(Messages.text(Message.GUI_PARTY_SELECT_TITLE))

        for (i in 0 until gui.size) {
            gui.setSlot(i, GuiItems.blackFiller().build())
        }

        gui.setSlot(
            10,
            GuiItems.confirmHead()
                .setName(Messages.text(Message.GUI_CONFIRM))
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
                    .setName(Messages.text(Message.GUI_PARTY_SELECT_EMPTY_SLOT))
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
                            if (SelectionManager.isSelected(player, pokemon))
                                PokemonUtils.getSelectedPokemonItem(pokemon)
                            else
                                PokemonUtils.getPokemonItem(pokemon)
                        )
                    else
                        GuiElementBuilder.from(CobblemonItems.CHERISH_BALL.defaultStack)
                            .setName(Messages.text(Message.GUI_NOT_SHINY))
                            .hideDefaultTooltip()

                gui.setSlot(
                    partyGuiSlots[it],
                    pokemonElement
                        .setCallback { _, _, _ ->
                            if (!pokemon.shiny)
                                return@setCallback

                            SelectionManager.toggle(player, pokemon)
                            openGui(player)
                        }
                        .build()
                )
            }
        }

        gui.open()
    }

}