package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.pc
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.managers.SelectionManager
import net.xpressdev.shinyreroll.utils.Messages
import net.xpressdev.shinyreroll.utils.PokemonUtils
import kotlin.math.floor

object PcSelectGui {

    fun openGui(player: ServerPlayerEntity, boxNum: Int) {
        val boxes = player.pc().boxes
        val safeBoxNum = boxNum.coerceIn(0, boxes.size - 1)

        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Messages.text(Message.GUI_PC_SELECT_TITLE, "box" to "${safeBoxNum + 1}")

        for (i in 0 until gui.size) {
            if (i in 0..5 || i in 9..14 || i in 18..23 || i in 27..32 || i in 36..41) {
                gui.setSlot(i, GuiItems.grayFiller(Messages.text(Message.GUI_EMPTY)).build())
            } else {
                gui.setSlot(i, GuiItems.blackFiller().build())
            }
        }

        val box = boxes[safeBoxNum]
        for (i in 0 until 30) {
            val pokemon: Pokemon? = box[i]
            val row = floor(i.toDouble() / 6.0)
            val index = i % 6
            if (pokemon != null) {
                var item = if (SelectionManager.isSelected(player, pokemon))
                    GuiElementBuilder.from(PokemonUtils.getSelectedPokemonItem(pokemon))
                else
                    GuiElementBuilder.from(PokemonUtils.getPokemonItem(pokemon))

                item = if (pokemon.shiny) item else GuiElementBuilder.from(CobblemonItems.CHERISH_BALL.defaultStack)
                    .setName(Messages.text(Message.GUI_NOT_SHINY))

                gui.setSlot(
                    (row * 9).toInt() + index,
                    item
                        .setCallback { _, _, _ ->
                            if (!pokemon.shiny)
                                return@setCallback

                            SelectionManager.toggle(player, pokemon)
                            openGui(player, safeBoxNum)
                        }.build()
                )
            }
        }

        gui.setSlot(
            42,
            GuiItems.previousHead()
                .setName(Messages.text(Message.GUI_PREVIOUS))
                .setCallback { _, _, _ ->
                    if (safeBoxNum > 0)
                        openGui(player, safeBoxNum - 1)
                    else
                        openGui(player, boxes.size - 1)
                }
                .build()
        )
        gui.setSlot(
            44,
            GuiItems.nextHead()
                .setName(Messages.text(Message.GUI_NEXT))
                .setCallback { _, _, _ ->
                    if (safeBoxNum < boxes.size - 1)
                        openGui(player, safeBoxNum + 1)
                    else
                        openGui(player, 0)
                }
                .build()
        )

        gui.setSlot(
            16,
            GuiItems.confirmHead()
                .setName(Messages.text(Message.GUI_CONFIRM))
                .setCallback { _, _, _ ->
                    PartyPcSelectGui.openGui(player)
                }
                .build()
        )

        gui.open()
    }

}