package com.ddaodan.buddycardsexpansion.core;

import com.ddaodan.buddycardsexpansion.util.CCBConstants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardSetDefinition {
    private final String id;
    private String displayName;
    private String namespace = CCBConstants.MOD_ID;
    private int setNumber = -1; // 自动分配或手动设置
    private int cardCount = 27;
    private final List<CardDefinition> cards = new ArrayList<>();
    private PackSettings packSettings = new PackSettings();
    private BinderSettings binderSettings = new BinderSettings();
    private MedalSettings medalSettings = new MedalSettings();
    @Nullable
    private String requiredModId;

    public CardSetDefinition(String id) {
        this.id = id;
        this.displayName = id;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = Math.max(1, cardCount);
    }

    public List<CardDefinition> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(CardDefinition card) {
        this.cards.add(card);
    }

    public void clearCards() {
        this.cards.clear();
    }

    public PackSettings getPackSettings() {
        return packSettings;
    }

    public void setPackSettings(PackSettings packSettings) {
        this.packSettings = packSettings;
    }

    public BinderSettings getBinderSettings() {
        return binderSettings;
    }

    public void setBinderSettings(BinderSettings binderSettings) {
        this.binderSettings = binderSettings;
    }

    public MedalSettings getMedalSettings() {
        return medalSettings;
    }

    public void setMedalSettings(MedalSettings medalSettings) {
        this.medalSettings = medalSettings;
    }

    @Nullable
    public String getRequiredModId() {
        return requiredModId;
    }

    public void setRequiredModId(@Nullable String requiredModId) {
        this.requiredModId = requiredModId;
    }
}
