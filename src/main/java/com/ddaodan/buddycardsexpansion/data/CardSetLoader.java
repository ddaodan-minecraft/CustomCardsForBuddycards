package com.ddaodan.buddycardsexpansion.data;

import com.ddaodan.buddycardsexpansion.core.*;
import com.ddaodan.buddycardsexpansion.util.CCBConstants;
import com.ddaodan.buddycardsexpansion.util.PlatformHelper;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CardSetLoader {
    private static final Logger LOGGER = LogManager.getLogger(CardSetLoader.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfigSets() {
        Path configDir = PlatformHelper.get().getConfigDir().resolve(CCBConstants.MOD_ID).resolve("card_sets");

        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                createExampleConfigFile(configDir.resolve("example_set.json"));
            }

            try (Stream<Path> stream = Files.list(configDir)) {
                stream.filter(p -> p.toString().endsWith(".json")).forEach(CardSetLoader::loadFile);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load custom card set configurations", e);
        }
    }

    private static void loadFile(Path jsonPath) {
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (!root.has("id")) {
                LOGGER.error("Skipping config file {} because it lacks a valid 'id' field.", jsonPath.getFileName());
                return;
            }

            String id = root.get("id").getAsString();
            CardSetDefinition set = new CardSetDefinition(id);

            if (root.has("display_name")) {
                set.setDisplayName(root.get("display_name").getAsString());
            }
            if (root.has("namespace")) {
                set.setNamespace(root.get("namespace").getAsString());
            }
            if (root.has("card_count")) {
                set.setCardCount(root.get("card_count").getAsInt());
            }
            if (root.has("required_mod")) {
                set.setRequiredModId(root.get("required_mod").getAsString());
            }

            // 解析卡包设置
            if (root.has("pack")) {
                JsonObject packObj = root.getAsJsonObject("pack");
                PackSettings pack = new PackSettings();
                if (packObj.has("cards_per_pack")) pack.setCardsPerPack(packObj.get("cards_per_pack").getAsInt());
                if (packObj.has("guaranteed_uncommon")) pack.setGuaranteedUncommon(packObj.get("guaranteed_uncommon").getAsBoolean());
                if (packObj.has("foil_chance")) pack.setFoilChance(packObj.get("foil_chance").getAsDouble());
                set.setPackSettings(pack);
            }

            // 解析卡册设置
            if (root.has("binder")) {
                JsonObject binderObj = root.getAsJsonObject("binder");
                BinderSettings binder = new BinderSettings();
                if (binderObj.has("color")) {
                    String colorStr = binderObj.get("color").getAsString();
                    if (colorStr.startsWith("#")) {
                        colorStr = colorStr.substring(1);
                    }
                    try {
                        binder.setColor(Integer.parseInt(colorStr, 16));
                    } catch (NumberFormatException ignored) {}
                }
                if (binderObj.has("pages")) binder.setPages(binderObj.get("pages").getAsInt());
                set.setBinderSettings(binder);
            }

            // 解析奖章设置
            if (root.has("medal")) {
                JsonObject medalObj = root.getAsJsonObject("medal");
                MedalSettings medal = new MedalSettings();
                if (medalObj.has("effect")) medal.setEffectId(medalObj.get("effect").getAsString());
                if (medalObj.has("effect_level")) medal.setEffectLevel(medalObj.get("effect_level").getAsInt());
                if (medalObj.has("duration")) medal.setDurationTicks(medalObj.get("duration").getAsInt());
                set.setMedalSettings(medal);
            }

            // 解析具体卡牌
            if (root.has("cards") && root.get("cards").isJsonArray()) {
                JsonArray cardsArray = root.getAsJsonArray("cards");
                for (JsonElement elem : cardsArray) {
                    if (!elem.isJsonObject()) continue;
                    JsonObject cardObj = elem.getAsJsonObject();
                    int number = cardObj.has("number") ? cardObj.get("number").getAsInt() : (set.getCards().size() + 1);
                    String name = cardObj.has("name") ? cardObj.get("name").getAsString() : "Card #" + number;
                    String rarityStr = cardObj.has("rarity") ? cardObj.get("rarity").getAsString() : "common";
                    String desc = null;
                    if (cardObj.has("description")) {
                        JsonElement descElem = cardObj.get("description");
                        if (descElem.isJsonArray()) {
                            StringBuilder sb = new StringBuilder();
                            JsonArray arr = descElem.getAsJsonArray();
                            for (int idx = 0; idx < arr.size(); idx++) {
                                if (idx > 0) sb.append("\n");
                                sb.append(arr.get(idx).getAsString());
                            }
                            desc = sb.toString();
                        } else if (descElem.isJsonPrimitive()) {
                            desc = descElem.getAsString();
                        }
                    }
                    String texture = cardObj.has("texture") ? cardObj.get("texture").getAsString() : null;

                    set.addCard(new CardDefinition(number, name, CardRarity.fromString(rarityStr), desc, texture));
                }
            } else {
                // 如果没有显式指定每张卡，自动按卡牌总数填充默认卡牌
                for (int i = 1; i <= set.getCardCount(); i++) {
                    CardRarity r = (i <= 12) ? CardRarity.COMMON : (i <= 21 ? CardRarity.UNCOMMON : (i <= 25 ? CardRarity.RARE : CardRarity.EPIC));
                    set.addCard(new CardDefinition(i, set.getDisplayName() + " #" + i, r));
                }
            }

            CardRegistry.registerSet(set);
        } catch (Exception e) {
            LOGGER.error("Failed to parse card set file: {}", jsonPath, e);
        }
    }

    private static void createExampleConfigFile(Path examplePath) {
        JsonObject example = new JsonObject();
        example.addProperty("_comment", "Buddycards Expansion custom card set definition");
        example.addProperty("id", "example_set");
        example.addProperty("display_name", "示例卡组");
        example.addProperty("card_count", 9);

        JsonObject pack = new JsonObject();
        pack.addProperty("cards_per_pack", 3);
        pack.addProperty("guaranteed_uncommon", true);
        pack.addProperty("foil_chance", 0.05);
        example.add("pack", pack);

        JsonObject binder = new JsonObject();
        binder.addProperty("color", "#FF6600");
        binder.addProperty("pages", 1);
        example.add("binder", binder);

        JsonObject medal = new JsonObject();
        medal.addProperty("effect", "minecraft:speed");
        medal.addProperty("effect_level", 1);
        medal.addProperty("duration", 6000);
        example.add("medal", medal);

        JsonArray cards = new JsonArray();
        cards.add(createCardJson(1, "萌新史莱姆", "common", "普通的绿色小史莱姆"));
        cards.add(createCardJson(2, "矿坑蝙蝠", "common", "在黑暗深处吱吱叫"));
        cards.add(createCardJson(3, "铁甲僵尸", "common", "捡到铁盔甲的幸运儿"));
        cards.add(createCardJson(4, "沙漠尸壳", "uncommon", "不畏惧烈日的变种"));
        cards.add(createCardJson(5, "洞穴蜘蛛", "uncommon", "致命的剧毒伏击者"));
        cards.add(createCardJson(6, "末影人", "uncommon", "不要与它对视"));
        cards.add(createCardJson(7, "烈焰人", "rare", "下界要塞的烈焰卫士"));
        cards.add(createCardJson(8, "恶魂", "rare", "浮空哭泣的幽灵"));
        cards.add(createCardJson(9, "末影龙", "epic", "终末之地的统治者"));
        example.add("cards", cards);

        try (Writer writer = Files.newBufferedWriter(examplePath, StandardCharsets.UTF_8)) {
            GSON.toJson(example, writer);
            LOGGER.info("Generated default example card set at: {}", examplePath);
        } catch (IOException e) {
            LOGGER.warn("Could not generate example card set config", e);
        }
    }

    private static JsonObject createCardJson(int number, String name, String rarity, String description) {
        JsonObject c = new JsonObject();
        c.addProperty("number", number);
        c.addProperty("name", name);
        c.addProperty("rarity", rarity);
        c.addProperty("description", description);
        return c;
    }
}
