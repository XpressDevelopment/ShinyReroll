package net.xpressdev.shinyreroll.guis

import eu.pb4.sgui.api.elements.GuiElementBuilder
import net.minecraft.item.Items
import net.minecraft.text.Text

object GuiItems {

    private const val CONFIRM_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0="
    private const val CANCEL_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc1NDgzNjJhMjRjMGZhODQ1M2U0ZDkzZTY4YzU5NjlkZGJkZTU3YmY2NjY2YzAzMTljMWVkMWU4NGQ4OTA2NSJ9fX0="
    private const val INFO_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0="
    private const val PREVIOUS_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0="
    private const val NEXT_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19"
    private const val WARNING_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmYwNGIyOTJhZDk0OGMwZTlmMGQzM2M2NGYxMzFmMzJlM2M1MTg4NDU2Zjc3ODc0NWEwMGYyYjRiNWFkMDM1OSJ9fX0="
    private const val STAR_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VhODYzYWY4ZTIwOGNjNzBkZjhlZmM4MzU5YzZhY2MwZGVkMzBkY2M1ODhiN2IwNGRjZGU1NWRjNjEzMmY1YyJ9fX0="

    fun confirmHead(): GuiElementBuilder = playerHead(CONFIRM_TEXTURE)
    fun cancelHead(): GuiElementBuilder = playerHead(CANCEL_TEXTURE)
    fun infoHead(): GuiElementBuilder = playerHead(INFO_TEXTURE)
    fun previousHead(): GuiElementBuilder = playerHead(PREVIOUS_TEXTURE)
    fun nextHead(): GuiElementBuilder = playerHead(NEXT_TEXTURE)
    fun warningHead(): GuiElementBuilder = playerHead(WARNING_TEXTURE)
    fun starHead(): GuiElementBuilder = playerHead(STAR_TEXTURE)

    private fun playerHead(texture: String): GuiElementBuilder =
        GuiElementBuilder(Items.PLAYER_HEAD).setSkullOwner(texture, null, null)

    fun blackFiller(): GuiElementBuilder =
        GuiElementBuilder.from(Items.BLACK_STAINED_GLASS_PANE.defaultStack).hideTooltip()

    fun grayFiller(name: Text): GuiElementBuilder =
        GuiElementBuilder.from(Items.GRAY_STAINED_GLASS_PANE.defaultStack).setName(name)
}
