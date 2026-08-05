package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.item.Items
import net.minecraft.item.Items.BLACK_STAINED_GLASS_PANE
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.utils.Messages

object PartyPcSelectGui {

    fun openGui(player: ServerPlayerEntity) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false)
        gui.setTitle(Messages.text(Message.GUI_PARTY_PC_SELECT_TITLE))

        for (i in 0 until gui.size) {
            gui.setSlot(
                i,
                GuiElementBuilder.from(BLACK_STAINED_GLASS_PANE.defaultStack)
                    .hideTooltip()
                    .build()
            )
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
            GuiElementBuilder(Items.PLAYER_HEAD)
                .setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VhODYzYWY4ZTIwOGNjNzBkZjhlZmM4MzU5YzZhY2MwZGVkMzBkY2M1ODhiN2IwNGRjZGU1NWRjNjEzMmY1YyJ9fX0=")
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