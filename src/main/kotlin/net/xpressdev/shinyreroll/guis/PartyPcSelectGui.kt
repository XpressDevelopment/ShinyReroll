package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.utils.Messages

object PartyPcSelectGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false)
        gui.setTitle(Messages.text(Message.GUI_PARTY_PC_SELECT_TITLE))

        for (i in 0 until gui.size) {
            gui.setSlot(i, GuiItems.blackFiller().build())
        }

        gui.setSlot(
            11,
            GuiElementBuilder.from(CobblemonItems.POKE_BALL.defaultStack)
                .hideDefaultTooltip()
                .setName(Messages.text(Message.GUI_PARTY_PC_SELECT_FROM_PARTY))
                .setCallback { _, _, _ ->
                    PartySelectGui.openGui(player)
                }
                .build()
        )

        gui.setSlot(
            13,
            GuiElementBuilder.from(CobblemonItems.PC.defaultStack)
                .setName(Messages.text(Message.GUI_PARTY_PC_SELECT_FROM_PC))
                .setCallback { _, _, _ ->
                    PcSelectGui.openGui(player, 0)
                }
                .build()
        )

        gui.setSlot(
            15,
            GuiItems.starHead()
                .setName(Messages.text(Message.GUI_PARTY_PC_SELECT_REROLL))
                .glow(true)
                .setCallback { _, _, _ ->
                    SelectedPokemonGui.openGui(player, 0)
                }
                .build()
        )

        gui.open()
    }

}