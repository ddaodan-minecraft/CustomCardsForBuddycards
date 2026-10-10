package com.ddaodan.buddycardsexpansion.core;

import javax.annotation.Nullable;

public class MedalSettings {
    @Nullable
    private String effectId = "minecraft:luck";
    private int effectLevel = 1;
    private int durationTicks = 6000;

    public MedalSettings() {}

    public MedalSettings(@Nullable String effectId, int effectLevel, int durationTicks) {
        this.effectId = effectId;
        this.effectLevel = Math.max(1, effectLevel);
        this.durationTicks = Math.max(20, durationTicks);
    }

    @Nullable
    public String getEffectId() {
        return effectId;
    }

    public void setEffectId(@Nullable String effectId) {
        this.effectId = effectId;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public void setEffectLevel(int effectLevel) {
        this.effectLevel = Math.max(1, effectLevel);
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public void setDurationTicks(int durationTicks) {
        this.durationTicks = Math.max(20, durationTicks);
    }
}
