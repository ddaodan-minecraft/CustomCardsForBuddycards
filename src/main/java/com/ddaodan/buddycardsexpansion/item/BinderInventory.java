package com.ddaodan.buddycardsexpansion.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class BinderInventory extends SimpleContainer {
    public static final int BINDER_SLOTS = 54;
    private final ItemStack binderStack;

    public BinderInventory(ItemStack binderStack) {
        super(BINDER_SLOTS);
        this.binderStack = binderStack;
        load();
    }

    public void load() {
        CompoundTag tag = binderStack.getTag();
        if (tag != null && tag.contains("Cards", 9)) {
            ListTag list = tag.getList("Cards", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                int slot = itemTag.getInt("Slot");
                if (slot >= 0 && slot < BINDER_SLOTS) {
                    setItem(slot, ItemStack.of(itemTag));
                }
            }
        }
    }

    public void save() {
        ListTag list = new ListTag();
        for (int i = 0; i < BINDER_SLOTS; i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                stack.save(itemTag);
                list.add(itemTag);
            }
        }
        binderStack.getOrCreateTag().put("Cards", list);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        save();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        // 禁止放入卡册自身（防止套娃）
        if (stack.getItem() instanceof CustomBinderItem || stack.getItem().getDescriptionId().contains("binder")) {
            return false;
        }
        // 允许放入自定义卡牌，或者包含 card 字样的物品
        if (stack.getItem() instanceof CustomCardItem) {
            return true;
        }
        String registryName = stack.getItem().getDescriptionId();
        return registryName.contains("card");
    }

    public static int getStoredCount(ItemStack binderStack) {
        CompoundTag tag = binderStack.getTag();
        if (tag != null && tag.contains("Cards", 9)) {
            return tag.getList("Cards", 10).size();
        }
        return 0;
    }
}
