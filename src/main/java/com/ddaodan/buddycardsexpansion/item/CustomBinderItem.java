package com.ddaodan.buddycardsexpansion.item;

import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 0.8F, 1.0F);

            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) ->
                            ChestMenu.sixRows(containerId, playerInventory, new BinderInventory(stack)),
                    stack.getHoverName()
            ));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        // 卡册描述
        tooltip.add(Component.translatable("tooltip.ccb.binder_desc", setDefinition.getDisplayName())
                .withStyle(ChatFormatting.GRAY));

        // 显示已收纳卡牌进度
        int stored = BinderInventory.getStoredCount(stack);
        tooltip.add(Component.literal("§7已收纳卡牌: §e" + stored + " §7/ " + BinderInventory.BINDER_SLOTS));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
