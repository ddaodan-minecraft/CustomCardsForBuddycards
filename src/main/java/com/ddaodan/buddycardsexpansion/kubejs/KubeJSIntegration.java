package com.ddaodan.buddycardsexpansion.kubejs;

import com.ddaodan.buddycardsexpansion.util.CCBConstants;
import com.ddaodan.buddycardsexpansion.util.PlatformHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class KubeJSIntegration {
    private static final Logger LOGGER = LogManager.getLogger(KubeJSIntegration.class);

    public static void fireEventIfAvailable() {
        if (!PlatformHelper.get().isModLoaded(CCBConstants.KUBEJS_MOD_ID)) {
            LOGGER.info("KubeJS is not loaded, skipping KubeJS script event dispatch.");
            return;
        }

        try {
            // 通过反射安全隔离，防止在未安装 KubeJS 时触发 JVM 类的提前链接和验证
            Class<?> handlerClass = Class.forName("com.ddaodan.buddycardsexpansion.kubejs.KubeJSEventHandler");
            Method method = handlerClass.getMethod("trigger");
            method.invoke(null);
        } catch (ClassNotFoundException e) {
            LOGGER.info("KubeJSEventHandler not found or KubeJS classes missing, safely skipped.");
        } catch (Throwable t) {
            LOGGER.warn("Failed to fire KubeJS card registration event", t);
        }
    }
}
