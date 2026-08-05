package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.pokemon.Pokemon
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.managers.SelectionManager
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.Messages
import net.xpressdev.shinyreroll.utils.PokemonUtils
import kotlin.math.floor

object SelectedPokemonGui {

    fun openGui(player: ServerPlayerEntity, sectionNum: Int) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Messages.text(Message.GUI_SELECTED_TITLE, "page" to "${sectionNum + 1}")

        for (i in 0 until gui.size) {
            if (i in 0..5 || i in 9..14 || i in 18..23 || i in 27..32 || i in 36..41) {
                gui.setSlot(i, GuiItems.grayFiller(Messages.text(Message.GUI_EMPTY)).build())
            } else {
                gui.setSlot(i, GuiItems.blackFiller().build())
            }
        }

        val selectedPokemon = SelectionManager.resolve(player)
        val lastPage = (selectedPokemon.size - 1).coerceAtLeast(0) / 30
        for (i in 0 until 30) {
            if (i + sectionNum * 30 >= selectedPokemon.size)
                break
            val pokemon: Pokemon = selectedPokemon[i + sectionNum * 30]
            val row = floor(i.toDouble() / 6.0)
            val index = i % 6
            gui.setSlot(
                (row * 9).toInt() + index,
                GuiElementBuilder.from(PokemonUtils.getPokemonItem(pokemon)).setCallback { _, _, _ ->
                    SelectionManager.toggle(player, pokemon)
                    openGui(player, sectionNum)
                }
                    .build()
            )
        }

        gui.setSlot(
            42,
            GuiItems.previousHead()
                .setName(Messages.text(Message.GUI_PREVIOUS))
                .setCallback { _, _, _ ->
                    if (sectionNum > 0)
                        openGui(player, sectionNum - 1)
                    else
                        openGui(player, lastPage)
                }
                .build()
        )
        gui.setSlot(
            44,
            GuiItems.nextHead()
                .setName(Messages.text(Message.GUI_NEXT))
                .setCallback { _, _, _ ->
                    if (sectionNum < lastPage)
                        openGui(player, sectionNum + 1)
                    else
                        openGui(player, 0)
                }
                .build()
        )

        if (!GeneralUtils.isOverLimit(player))
            gui.setSlot(
                24,
                GuiItems.confirmHead()
                    .setName(Messages.text(Message.GUI_CONFIRM))
                    .setCallback { _, _, _ ->
                        if (SelectionManager.resolve(player).isNotEmpty())
                            ConfirmationGui.openGui(player)
                        else
                            player.sendMessage(Messages.text(Message.GUI_SELECTED_MUST_SELECT_ONE))
                    }
                    .build()
            )
        else
            gui.setSlot(
                24,
                GuiItems.warningHead()
                    .setName(Messages.text(Message.GUI_SELECTED_TOO_MANY_NAME))
                    .addLoreLine(
                        Messages.text(
                            Message.GUI_SELECTED_TOO_MANY_LORE,
                            "max" to "${ConfigStore.config.reroll.maxPokemonSelected}",
                        ),
                    )
                    .build()
            )

        gui.setSlot(
            26,
            GuiItems.cancelHead()
                .setName(Messages.text(Message.GUI_CANCEL))
                .setCallback { _, _, _ ->
                    PartyPcSelectGui.openGui(player)
                }
                .build()
        )

        gui.setSlot(
            7,
            GuiItems.infoHead()
                .setName(Messages.text(Message.GUI_INFO_NAME))
                .addLoreLine(
                    Messages.text(Message.GUI_INFO_CURRENT_CHANCE, "chance" to GeneralUtils.formatShinyChance(player)),
                )
                .addLoreLine(
                    Messages.text(Message.GUI_INFO_MAX_CHANCE, "max" to GeneralUtils.formatMaxShinyChance()),
                )
                .build()
        )

        gui.open()
    }

}