package com.ddaodan.buddycardsexpansion.core;

import com.ddaodan.buddycardsexpansion.item.CustomBinderItem;
import com.ddaodan.buddycardsexpansion.item.CustomCardItem;
import com.ddaodan.buddycardsexpansion.item.CustomMedalItem;
import com.ddaodan.buddycardsexpansion.item.CustomPackItem;
import com.ddaodan.buddycardsexpansion.util.CCBConstants;
import com.ddaodan.buddycardsexpansion.util.PlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CardRegistry {
    private static final Logger LOGGER = LogManager.getLogger(CardRegistry.class);

    private static final Map<String, CardSetDefinition> SETS = new LinkedHashMap<>();
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCBConstants.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCBConstants.MOD_ID);

    private static final List<RegistryObject<Item>> ALL_EXPANSION_ITEMS = new ArrayList<>();
    private static int nextSetNumber = CCBConstants.STARTING_SET_NUMBER;

    public static void registerSet(CardSetDefinition set) {
        if (set == null || set.getId() == null) return;

        // 如果该卡组声明了前置 mod 且未加载，则跳过注册
        if (set.getRequiredModId() != null && !PlatformHelper.get().isModLoaded(set.getRequiredModId())) {
            LOGGER.info("Skipping card set [{}] because required mod [{}] is not loaded.", set.getId(), set.getRequiredModId());
            return;
        }

        if (SETS.containsKey(set.getId())) {
            LOGGER.warn("Duplicate card set id [{}]. Overwriting existing definition.", set.getId());
        }

        if (set.getSetNumber() <= 0) {
            set.setSetNumber(nextSetNumber++);
        } else {
            nextSetNumber = Math.max(nextSetNumber, set.getSetNumber() + 1);
        }

        SETS.put(set.getId(), set);
        LOGGER.info("Registered card set definition: [{}] (Set #{}) with {} cards", set.getId(), set.getSetNumber(), set.getCards().size());
    }

    public static Collection<CardSetDefinition> getRegisteredSets() {
        return Collections.unmodifiableCollection(SETS.values());
    }

    public static void initializeRegistration(IEventBus modEventBus) {
        Item.Properties defaultProps = new Item.Properties();

        // 遍历所有收集到的卡组，注册物品
        for (CardSetDefinition set : SETS.values()) {
            String setId = set.getId();

            // 1. 注册卡包 (Pack)
            RegistryObject<Item> packObj = ITEMS.register("pack_" + setId,
                    () -> new CustomPackItem(set, new Item.Properties()));
            ALL_EXPANSION_ITEMS.add(packObj);

            // 2. 注册卡册 (Binder)
            RegistryObject<Item> binderObj = ITEMS.register("binder_" + setId,
                    () -> new CustomBinderItem(set, new Item.Properties()));
            ALL_EXPANSION_ITEMS.add(binderObj);

            // 3. 注册奖章 (Medal)
            RegistryObject<Item> medalObj = ITEMS.register("medal_" + setId,
                    () -> new CustomMedalItem(set, new Item.Properties()));
            ALL_EXPANSION_ITEMS.add(medalObj);

            // 4. 注册所有单张卡牌
            for (CardDefinition card : set.getCards()) {
                String cardItemId = "card_" + setId + "_" + card.getCardNumber();
                RegistryObject<Item> cardObj = ITEMS.register(cardItemId,
                        () -> new CustomCardItem(set, card, new Item.Properties()));
                ALL_EXPANSION_ITEMS.add(cardObj);
            }
        }

        // 注册专用的创造模式物品栏 Tab
        CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.ccb"))
                .icon(() -> {
                    if (!ALL_EXPANSION_ITEMS.isEmpty()) {
                        return new ItemStack(ALL_EXPANSION_ITEMS.get(0).get());
                    }
                    return ItemStack.EMPTY;
                })
                .displayItems((parameters, output) -> {
                    for (RegistryObject<Item> itemObj : ALL_EXPANSION_ITEMS) {
                        output.accept(itemObj.get());
                    }
                })
                .build());

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        LOGGER.info("Buddycards Expansion registered {} items into Forge registry.", ALL_EXPANSION_ITEMS.size());
    }
}
