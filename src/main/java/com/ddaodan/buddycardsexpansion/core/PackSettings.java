package com.ddaodan.buddycardsexpansion.core;

public class PackSettings {
    private int cardsPerPack = 5;
    private boolean guaranteedUncommon = true;
    private double foilChance = 0.05; // 默认 5% 闪卡概率

    public PackSettings() {}

    public PackSettings(int cardsPerPack, boolean guaranteedUncommon, double foilChance) {
        this.cardsPerPack = Math.max(1, cardsPerPack);
        this.guaranteedUncommon = guaranteedUncommon;
        this.foilChance = Math.max(0.0, Math.min(1.0, foilChance));
    }

    public int getCardsPerPack() {
        return cardsPerPack;
    }

    public void setCardsPerPack(int cardsPerPack) {
        this.cardsPerPack = Math.max(1, cardsPerPack);
    }

    public boolean isGuaranteedUncommon() {
        return guaranteedUncommon;
    }

    public void setGuaranteedUncommon(boolean guaranteedUncommon) {
        this.guaranteedUncommon = guaranteedUncommon;
    }

    public double getFoilChance() {
        return foilChance;
    }

    public void setFoilChance(double foilChance) {
        this.foilChance = Math.max(0.0, Math.min(1.0, foilChance));
    }
}
