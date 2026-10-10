package com.ddaodan.buddycardsexpansion.client;

import com.ddaodan.buddycardsexpansion.core.CardDefinition;
import com.ddaodan.buddycardsexpansion.core.CardRegistry;
import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import com.ddaodan.buddycardsexpansion.util.CCBConstants;
import com.ddaodan.buddycardsexpansion.util.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = CCBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicModelManager {
    private static final Logger LOGGER = LogManager.getLogger(DynamicModelManager.class);
    private static final String PACK_ID = "ccb_generated";

    public static void generateModels() {
        try {
            // 生成在运行目录下的 resourcepacks/ccb_generated
            Path gameDir = PlatformHelper.get().getConfigDir().getParent();
            Path packDir = gameDir.resolve("resourcepacks").resolve(PACK_ID);
            Path modelsDir = packDir.resolve("assets").resolve(CCBConstants.MOD_ID).resolve("models").resolve("item");
            Files.createDirectories(modelsDir);

            // 1. 生成 pack.mcmeta
            Path mcmeta = packDir.resolve("pack.mcmeta");
            String mcmetaContent = "{\n  \"pack\": {\n    \"description\": \"CCB Auto-Generated Models\",\n    \"pack_format\": 15\n  }\n}";
            Files.writeString(mcmeta, mcmetaContent, StandardCharsets.UTF_8);

            // 2. 遍历所有注册的卡组，生成每个物品的默认模型映射
            for (CardSetDefinition set : CardRegistry.getRegisteredSets()) {
                String setId = set.getId();

                // 卡包模型
                writeModel(modelsDir.resolve("pack_" + setId + ".json"), "ccb:item/pack_template");

                // 卡册模型
                writeModel(modelsDir.resolve("binder_" + setId + ".json"), "ccb:item/binder_template");

                // 奖章模型
                writeModel(modelsDir.resolve("medal_" + setId + ".json"), "ccb:item/medal_template");

                // 所有卡牌模型
                for (CardDefinition card : set.getCards()) {
                    String cardModelName = "card_" + setId + "_" + card.getCardNumber() + ".json";
                    writeModel(modelsDir.resolve(cardModelName), "ccb:item/card_template");
                }
            }
            LOGGER.info("Successfully generated default item models for all dynamic Buddycards in {}", packDir);
        } catch (Exception e) {
            LOGGER.error("Failed to generate dynamic models for CCB", e);
        }
    }

    private static void writeModel(Path file, String parent) throws IOException {
        String json = "{\n  \"parent\": \"" + parent + "\"\n}";
        Files.writeString(file, json, StandardCharsets.UTF_8);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            // 先确保模型文件已生成完毕
            generateModels();

            Path gameDir = PlatformHelper.get().getConfigDir().getParent();
            Path packDir = gameDir.resolve("resourcepacks").resolve(PACK_ID);

            if (Files.exists(packDir)) {
                Pack pack = Pack.readMetaAndCreate(
                        PACK_ID,
                        Component.literal("Buddycards Expansion Auto Models"),
                        true, // required = true，强制开启！
                        path -> new PathPackResources(PACK_ID, packDir, true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP, // 放在顶层
                        PackSource.BUILT_IN
                );

                if (pack != null) {
                    event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
                    LOGGER.info("Registered Buddycards Expansion auto-generated client model pack!");
                }
            }
        }
    }
}
