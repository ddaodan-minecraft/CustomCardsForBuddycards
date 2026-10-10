package com.ddaodan.buddycardsexpansion.item;

import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CustomBinderItem extends Item {
    private final CardSetDefinition setDefinition;

    public CustomBinderItem(CardSetDefinition setDefinition, Properties properties) {
        super(properties.stacksTo(1));
        this.setDefinition = setDefinition;
    }

    public CardSetDefinition getSetDefinition() {
        return setDefinition;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.ccb.binder_title", setDefinition.getDisplayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.ccb.binder_desc", setDefinition.getDisplayName())
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
