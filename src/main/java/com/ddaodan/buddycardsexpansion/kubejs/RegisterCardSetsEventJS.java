package com.ddaodan.buddycardsexpansion.kubejs;

import com.ddaodan.buddycardsexpansion.core.CardRegistry;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.ArrayList;
import java.util.List;

public class RegisterCardSetsEventJS extends EventJS {
    private final List<CardSetBuilderJS> builders = new ArrayList<>();

    public CardSetBuilderJS createSet(String id) {
        CardSetBuilderJS builder = new CardSetBuilderJS(id);
        builders.add(builder);
        return builder;
    }

    public void postProcess() {
        for (CardSetBuilderJS builder : builders) {
            CardRegistry.registerSet(builder.getDefinition());
        }
    }
}
