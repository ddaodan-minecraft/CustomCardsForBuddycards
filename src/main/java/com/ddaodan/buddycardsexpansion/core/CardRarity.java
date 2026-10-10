package com.ddaodan.buddycardsexpansion.core;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import java.util.Locale;

public enum CardRarity {
    COMMON("common", Rarity.COMMON, ChatFormatting.WHITE, 50),
    UNCOMMON("uncommon", Rarity.UNCOMMON, ChatFormatting.YELLOW, 30),
    RARE("rare", Rarity.RARE, ChatFormatting.AQUA, 15),
    EPIC("epic", Rarity.EPIC, ChatFormatting.LIGHT_PURPLE, 5);

    private final String id;
    private final Rarity vanillaRarity;
    private final ChatFormatting formatting;
    private final int defaultWeight;

    CardRarity(String id, Rarity vanillaRarity, ChatFormatting formatting, int defaultWeight) {
        this.id = id;
        this.vanillaRarity = vanillaRarity;
        this.formatting = formatting;
        this.defaultWeight = defaultWeight;
    }

    public String getId() {
        return id;
    }

    public Rarity getVanillaRarity() {
        return vanillaRarity;
    }

    public ChatFormatting getFormatting() {
        return formatting;
    }

    public int getDefaultWeight() {
        return defaultWeight;
    }

    public static CardRarity fromString(String name) {
        if (name == null || name.isEmpty()) {
            return COMMON;
        }
        String lower = name.trim().toLowerCase(Locale.ROOT);
        for (CardRarity rarity : values()) {
            if (rarity.id.equals(lower) || rarity.name().toLowerCase(Locale.ROOT).equals(lower)) {
                return rarity;
            }
        }
        return COMMON;
    }
}
