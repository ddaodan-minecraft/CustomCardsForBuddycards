package com.ddaodan.buddycardsexpansion;

import com.ddaodan.buddycardsexpansion.core.CardRegistry;
import com.ddaodan.buddycardsexpansion.data.CardSetLoader;
import com.ddaodan.buddycardsexpansion.kubejs.KubeJSIntegration;
import com.ddaodan.buddycardsexpansion.util.CCBConstants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CCBConstants.MOD_ID)
public class CustomCardsBuddycards {
    private static final Logger LOGGER = LogManager.getLogger(CustomCardsBuddycards.class);

    public CustomCardsBuddycards() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Initializing {} [ModID: {}]", CCBConstants.MOD_NAME, CCBConstants.MOD_ID);

        // 1. 从 config/ccb/card_sets 目录加载 JSON 卡组定义
        CardSetLoader.loadConfigSets();

        // 2. 如果存在 KubeJS，派发 KubeJS 启动期卡组注册事件
        KubeJSIntegration.fireEventIfAvailable();

        // 3. 将所有卡牌物品批量注册到 Forge 注册表与创造物品栏
        CardRegistry.initializeRegistration(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("{} common setup complete.", CCBConstants.MOD_NAME);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("{} client setup complete.", CCBConstants.MOD_NAME);
    }
}
