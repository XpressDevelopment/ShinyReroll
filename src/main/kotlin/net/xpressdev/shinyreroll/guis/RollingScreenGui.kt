package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.pokemon.Pokemon
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.utils.MMUtils
import net.xpressdev.shinyreroll.utils.PokemonUtils

object RollingScreenGui {

    fun openGui(player: ServerPlayerEntity, reward: Pokemon? = null) {
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
        val shownItemList =
            if (RerollManager.shownItemMap.contains(player.uuid)) RerollManager.shownItemMap[player.uuid]
            else RerollManager.shownItemList

        for (i in shownItemList!!.indices) {
            gui.setSlot(
                guiIndexes[i],
                GuiElementBuilder.from(shownItemList[i])
                    .hideTooltip()
                    .build()
            )
        }

        reward?.let {
            gui.setSlot(
                13,
                GuiElementBuilder.from(PokemonUtils.getPokemonItem(it))
                    .build()
            )
        }

        gui.open()
    }

}