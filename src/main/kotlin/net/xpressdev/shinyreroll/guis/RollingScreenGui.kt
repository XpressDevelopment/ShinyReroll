package net.xpressdev.shinyreroll.guis

import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.utils.MMUtils

object RollingScreenGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false)
        gui.setTitle(MMUtils.parseText("Shiny Reroll"))

        for (i in 0 until gui.size) {
            gui.setSlot(
                i,
                GuiElementBuilder.from(Items.BLACK_STAINED_GLASS_PANE.defaultStack)
                    .hideTooltip()
                    .build()
            )
        }

        gui.setSlot(
            4,
            GuiElementBuilder.from(Items.LIME_STAINED_GLASS_PANE.defaultStack)
                .hideTooltip()
                .build()
        )

        gui.setSlot(
            22,
            GuiElementBuilder.from(Items.LIME_STAINED_GLASS_PANE.defaultStack)
                .hideTooltip()
                .build()
        )

        val guiIndexes = (10 .. 16).toList()
        val shownItemList = RerollManager.shownItemMap[player.uuid]

        for (i in shownItemList!!.indices) {
            gui.setSlot(
                guiIndexes[i],
                GuiElementBuilder.from(shownItemList[i])
                    .hideTooltip()
                    .build()
            )
        }

        gui.open()
    }

}