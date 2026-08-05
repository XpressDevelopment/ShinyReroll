package net.xpressdev.shinyreroll.guis

import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.Messages

object ConfirmationGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Messages.text(Message.GUI_CONFIRMATION_TITLE, "chance" to GeneralUtils.formatShinyChance(player))

        for (i in 0 until gui.size) {
            gui.setSlot(i, GuiItems.blackFiller().build())
        }

        gui.setSlot(
            13,
            GuiItems.infoHead()
                .setName(Messages.text(Message.GUI_CONFIRMATION_PROMPT))
                .addLoreLine(
                    Messages.text(Message.GUI_INFO_CURRENT_CHANCE, "chance" to GeneralUtils.formatShinyChance(player)),
                )
                .addLoreLine(
                    Messages.text(Message.GUI_INFO_MAX_CHANCE, "max" to GeneralUtils.formatMaxShinyChance()),
                )
                .build()
        )

        gui.setSlot(
            30,
            GuiItems.confirmHead()
                .setName(Messages.text(Message.GUI_CONFIRM))
                .setCallback { _, _, _ ->
                    RerollManager.init(player)
                    RollingScreenGui.openGui(player)
                }
                .build()
        )

        gui.setSlot(
            32,
            GuiItems.cancelHead()
                .setName(Messages.text(Message.GUI_CANCEL))
                .setCallback { _, _, _ ->
                    SelectedPokemonGui.openGui(player, 0)
                }
                .build()
        )

        gui.open()
    }

}