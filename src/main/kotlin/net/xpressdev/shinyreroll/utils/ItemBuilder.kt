package net.xpressdev.shinyreroll.utils

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.Unit

class ItemBuilder(private val stack: ItemStack) {

    fun addLore(lore: List<Text>): ItemBuilder {
        stack.set(DataComponentTypes.LORE, LoreComponent(lore))
        return this
    }

    fun setCount(count: Int): ItemBuilder {
        stack.count = count
        return this
    }

    fun hideAdditional(): ItemBuilder {
        stack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
        return this
    }

    fun setCustomName(customName: Text): ItemBuilder {
        val pokemonName = Texts.join(customName.getWithStyle(Style.EMPTY.withItalic(false)), Text.of(""))
        stack.set(DataComponentTypes.ITEM_NAME, pokemonName)
        return this
    }

    fun build(): ItemStack {
        return stack
    }
}