package com.ddaodan.buddycardsexpansion.kubejs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KubeJSEventHandler {
    private static final Logger LOGGER = LogManager.getLogger(KubeJSEventHandler.class);

    public static void trigger() {
        try {
            RegisterCardSetsEventJS event = new RegisterCardSetsEventJS();
            if (CCBKubeJSPlugin.REGISTER_SETS.hasListeners()) {
                CCBKubeJSPlugin.REGISTER_SETS.post(event);
                event.postProcess();
                LOGGER.info("KubeJS card set registration event completed successfully.");
            }
        } catch (Throwable t) {
            LOGGER.warn("Exception during KubeJS event dispatching", t);
        }
    }
}
