package net.xpressdev.shinyreroll.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.xpresslib.text.MiniMessages

object ColorUtil {

    fun toText(component: Component): Text =
        Text.Serialization.fromJson(
            GsonComponentSerializer.gson().serialize(component),
            ShinyReroll.serverInstance.registryManager,
        ) ?: Text.literal(PlainTextComponentSerializer.plainText().serialize(component))

    fun toText(input: String): Text = toText(MiniMessages.parse(input))
}
