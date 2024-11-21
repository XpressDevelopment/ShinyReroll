package net.xpressdev.shinyreroll.utils

import net.xpressdev.shinyreroll.ShinyReroll.Companion.adventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.text.Text

object MMUtils {
    private val mm = MiniMessage.miniMessage()

    fun parseText(string: String): Text {
        if (adventure == null) { return Text.literal(string) }
        return adventure!!.toNative(
            mm.deserialize(string).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        )
    }

    fun parseComponent(component: Component): Text {
        return adventure!!.toNative(component)
    }
}