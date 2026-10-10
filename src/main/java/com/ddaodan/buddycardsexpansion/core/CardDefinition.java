package com.ddaodan.buddycardsexpansion.core;

import javax.annotation.Nullable;

public class CardDefinition {
    private final int cardNumber;
    private final String name;
    private final CardRarity rarity;
    @Nullable
    private final String description;
    @Nullable
    private final String texture; // 可选自定义贴图路径，不填则使用默认/自动推断

    public CardDefinition(int cardNumber, String name, CardRarity rarity, @Nullable String description, @Nullable String texture) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.rarity = rarity != null ? rarity : CardRarity.COMMON;
        this.description = description;
        this.texture = texture;
    }

    public CardDefinition(int cardNumber, String name, CardRarity rarity) {
        this(cardNumber, name, rarity, null, null);
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public String getName() {
        return name;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getTexture() {
        return texture;
    }
}
