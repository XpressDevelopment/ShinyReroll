package net.xpressdev.shinyreroll.guis

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.storage.pc.PCPosition
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.pc
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
import kotlin.math.floor

object PcSelectGui {

    fun openGui(player: ServerPlayerEntity, boxNum: Int) {
        val gui = SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false)

        gui.title = Text.literal("Select Pokemon - Box ${boxNum + 1}")

        for (i in 0 until gui.size) {
            if (i in 0..5 || i in 9..14 || i in 18..23 || i in 27..32 || i in 36..41) {
                gui.setSlot(
                    i,
                    GuiElementBuilder.from(GRAY_STAINED_GLASS_PANE.defaultStack)
                        .setName(MMUtils.parseText("Empty"))
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

        val box = player.pc().boxes[boxNum]
        for (i in 0 until 30) {
            val pokemon: Pokemon? = box[i]
            val row = floor(i.toDouble() / 6.0)
            val index = i % 6
            if (pokemon != null) {
                var item = if (PokemonSelectionManager.isPcPokemonSelected(player, PCPosition(boxNum, i)))
                    GuiElementBuilder.from(PokemonUtils.getSelectedPokemonItem(pokemon))
                else
                    GuiElementBuilder.from(PokemonUtils.getPokemonItem(pokemon))

                item = if (pokemon.shiny) item else GuiElementBuilder.from(CobblemonItems.CHERISH_BALL.defaultStack)
                    .setName(MMUtils.parseText("<red>Not a Shiny Pokemon"))

                gui.setSlot(
                    (row * 9).toInt() + index,
                    item
                        .setCallback { _, _, _ ->
                            if (!pokemon.shiny)
                                return@setCallback

                            if (PokemonSelectionManager.isPcPokemonSelected(player, PCPosition(boxNum, i)))
                                PokemonSelectionManager.deselectPcPokemon(player, PCPosition(boxNum, i))
                            else
                                PokemonSelectionManager.selectPcPokemon(player, PCPosition(boxNum, i))
                            openGui(player, boxNum)
                        }.build()
                )
            }
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
                    if (boxNum > 0)
                        openGui(player, boxNum - 1)
                    else
                        openGui(player, player.pc().boxes.size - 1)
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
                    if (boxNum < player.pc().boxes.size - 1)
                        openGui(player, boxNum + 1)
                    else
                        openGui(player, 0)
                }
                .build()
        )

        gui.setSlot(
            16,
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

        gui.open()
    }

}