package com.ddaodan.buddycardsexpansion.kubejs;

import com.ddaodan.buddycardsexpansion.core.*;

import java.util.function.Consumer;

public class CardSetBuilderJS {
    private final CardSetDefinition definition;

    public CardSetBuilderJS(String id) {
        this.definition = new CardSetDefinition(id);
    }

    public CardSetBuilderJS displayName(String name) {
        definition.setDisplayName(name);
        return this;
    }

    public CardSetBuilderJS namespace(String namespace) {
        definition.setNamespace(namespace);
        return this;
    }

    public CardSetBuilderJS cardCount(int count) {
        definition.setCardCount(count);
        return this;
    }

    public CardSetBuilderJS requiredMod(String modId) {
        definition.setRequiredModId(modId);
        return this;
    }

    public CardSetBuilderJS card(int number, String name, String rarity) {
        return card(number, name, rarity, null, null);
    }

    public CardSetBuilderJS card(int number, String name, String rarity, String description) {
        return card(number, name, rarity, description, null);
    }

    public CardSetBuilderJS card(int number, String name, String rarity, String[] descriptionLines) {
        String combined = (descriptionLines != null) ? String.join("\n", descriptionLines) : null;
        return card(number, name, rarity, combined, null);
    }

    public CardSetBuilderJS card(int number, String name, String rarity, String description, String texture) {
        definition.addCard(new CardDefinition(number, name, CardRarity.fromString(rarity), description, texture));
        return this;
    }

    public CardSetBuilderJS pack(Consumer<PackSettingsBuilderJS> consumer) {
        PackSettingsBuilderJS builder = new PackSettingsBuilderJS(definition.getPackSettings());
        consumer.accept(builder);
        return this;
    }

    public CardSetBuilderJS binder(Consumer<BinderSettingsBuilderJS> consumer) {
        BinderSettingsBuilderJS builder = new BinderSettingsBuilderJS(definition.getBinderSettings());
        consumer.accept(builder);
        return this;
    }

    public CardSetBuilderJS medal(Consumer<MedalSettingsBuilderJS> consumer) {
        MedalSettingsBuilderJS builder = new MedalSettingsBuilderJS(definition.getMedalSettings());
        consumer.accept(builder);
        return this;
    }

    public CardSetDefinition getDefinition() {
        return definition;
    }

    public static class PackSettingsBuilderJS {
        private final PackSettings settings;

        public PackSettingsBuilderJS(PackSettings settings) {
            this.settings = settings;
        }

        public PackSettingsBuilderJS cardsPerPack(int count) {
            settings.setCardsPerPack(count);
            return this;
        }

        public PackSettingsBuilderJS guaranteedUncommon(boolean guaranteed) {
            settings.setGuaranteedUncommon(guaranteed);
            return this;
        }

        public PackSettingsBuilderJS foilChance(double chance) {
            settings.setFoilChance(chance);
            return this;
        }
    }

    public static class BinderSettingsBuilderJS {
        private final BinderSettings settings;

        public BinderSettingsBuilderJS(BinderSettings settings) {
            this.settings = settings;
        }

        public BinderSettingsBuilderJS color(int hexColor) {
            settings.setColor(hexColor);
            return this;
        }

        public BinderSettingsBuilderJS color(String hexString) {
            if (hexString.startsWith("#")) hexString = hexString.substring(1);
            try {
                settings.setColor(Integer.parseInt(hexString, 16));
            } catch (NumberFormatException ignored) {}
            return this;
        }

        public BinderSettingsBuilderJS pages(int pages) {
            settings.setPages(pages);
            return this;
        }
    }

    public static class MedalSettingsBuilderJS {
        private final MedalSettings settings;

        public MedalSettingsBuilderJS(MedalSettings settings) {
            this.settings = settings;
        }

        public MedalSettingsBuilderJS effect(String effectId, int level) {
            settings.setEffectId(effectId);
            settings.setEffectLevel(level);
            return this;
        }

        public MedalSettingsBuilderJS duration(int durationTicks) {
            settings.setDurationTicks(durationTicks);
            return this;
        }
    }
}
