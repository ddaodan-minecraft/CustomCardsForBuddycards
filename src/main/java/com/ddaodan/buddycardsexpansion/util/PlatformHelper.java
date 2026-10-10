package com.ddaodan.buddycardsexpansion.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public interface PlatformHelper {
    default boolean isModLoaded(String modId) {
        if (modId == null || modId.isEmpty()) return true;
        return ModList.get().isLoaded(modId);
    }

    default Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    static PlatformHelper get() {
        return DefaultPlatformHelper.INSTANCE;
    }

    class DefaultPlatformHelper implements PlatformHelper {
        private static final DefaultPlatformHelper INSTANCE = new DefaultPlatformHelper();
    }
}
