package com.ddaodan.buddycardsexpansion.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CCBKubeJSPlugin extends KubeJSPlugin {
    public static final EventGroup GROUP = EventGroup.of("CCBEvents");
    public static final EventHandler REGISTER_SETS = GROUP.startup("registerCardSets", () -> RegisterCardSetsEventJS.class);

    @Override
    public void registerEvents() {
        GROUP.register();
    }
}
