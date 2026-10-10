package com.ddaodan.buddycardsexpansion.item;

import com.ddaodan.buddycardsexpansion.core.CardDefinition;
import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CustomCardItem extends Item {
    private final CardSetDefinition setDefinition;
    private final CardDefinition cardDefinition;

    public CustomCardItem(CardSetDefinition setDefinition, CardDefinition cardDefinition, Properties properties) {
        super(properties.rarity(cardDefinition.getRarity().getVanillaRarity()));
        this.setDefinition = setDefinition;
        this.cardDefinition = cardDefinition;
    }

    public CardSetDefinition getSetDefinition() {
        return setDefinition;
    }

    public CardDefinition getCardDefinition() {
        return cardDefinition;
    }

    public int getSetNumber() {
        return setDefinition.getSetNumber();
    }

    public int getCardNumber() {
        return cardDefinition.getCardNumber();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.getBoolean("foil")) || super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        String rawName = cardDefinition.getName().replace('&', '§');
        return Component.literal(rawName);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        boolean isFoil = (tag != null && tag.getBoolean("foil"));

        // 1. 卡牌描述（风味文本）：支持换行与 & / § 颜色代码
        if (cardDefinition.getDescription() != null && !cardDefinition.getDescription().isEmpty()) {
            String formattedDesc = cardDefinition.getDescription().replace('&', '§');
            String[] lines = formattedDesc.split("\\r?\\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    // 如果该行未显式包含颜色代码，则默认保持原版白色斜体
                    if (!line.contains("§")) {
                        tooltip.add(Component.literal(line).withStyle(ChatFormatting.ITALIC));
                    } else {
                        tooltip.add(Component.literal(line));
                    }
                }
            }
        }

        // 2. 卡组行：原版格式 "<卡组名> - <卡牌单号>"，灰色文本
        MutableComponent setInfo = Component.literal(setDefinition.getDisplayName() + " - " + cardDefinition.getCardNumber());
        if (isFoil) {
            setInfo.append(Component.literal("✦").withStyle(ChatFormatting.GOLD));
        }
        setInfo.withStyle(ChatFormatting.GRAY);
        tooltip.add(setInfo);

        // 3. 评级行：原版仅在拥有评级(graded)时才显示
        if (tag != null && tag.contains("grade")) {
            int grade = tag.getInt("grade");
            Component gradeText = switch (grade) {
                case 1 -> Component.literal("Grade: F").withStyle(ChatFormatting.GRAY);
                case 2 -> Component.literal("Grade: C").withStyle(ChatFormatting.GREEN);
                case 3 -> Component.literal("Grade: B").withStyle(ChatFormatting.BLUE);
                case 4 -> Component.literal("Grade: A").withStyle(ChatFormatting.DARK_PURPLE);
                case 5 -> Component.literal("Grade: S").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
                default -> Component.literal("Grade: " + grade).withStyle(ChatFormatting.AQUA);
            };
            tooltip.add(gradeText);
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
