package com.ddaodan.buddycardsexpansion.item;

import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import com.ddaodan.buddycardsexpansion.core.MedalSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class CustomMedalItem extends Item {
    private final CardSetDefinition setDefinition;

    public CustomMedalItem(CardSetDefinition setDefinition, Properties properties) {
        super(properties.stacksTo(1));
        this.setDefinition = setDefinition;
    }

    public CardSetDefinition getSetDefinition() {
        return setDefinition;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.ccb.medal_title", setDefinition.getDisplayName())
                .withStyle(ChatFormatting.GOLD);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        MedalSettings settings = setDefinition.getMedalSettings();
        String effectId = settings.getEffectId();

        if (effectId != null && !effectId.isEmpty() && !level.isClientSide) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));
            if (effect != null) {
                player.addEffect(new MobEffectInstance(effect, settings.getDurationTicks(), settings.getEffectLevel() - 1, false, true, true));
                player.sendSystemMessage(Component.translatable("message.ccb.medal_activated", setDefinition.getDisplayName())
                        .withStyle(ChatFormatting.GOLD));
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.ccb.medal_desc", setDefinition.getDisplayName())
                .withStyle(ChatFormatting.GRAY));

        MedalSettings settings = setDefinition.getMedalSettings();
        if (settings.getEffectId() != null && !settings.getEffectId().isEmpty()) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(settings.getEffectId()));
            String effectName = (effect != null) ? effect.getDescriptionId() : settings.getEffectId();
            tooltip.add(Component.translatable("tooltip.ccb.medal_effect",
                    Component.translatable(effectName), settings.getEffectLevel())
                    .withStyle(ChatFormatting.DARK_GREEN));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
