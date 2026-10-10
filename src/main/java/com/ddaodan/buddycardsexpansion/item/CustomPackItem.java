package com.ddaodan.buddycardsexpansion.item;

import com.ddaodan.buddycardsexpansion.core.CardDefinition;
import com.ddaodan.buddycardsexpansion.core.CardRarity;
import com.ddaodan.buddycardsexpansion.core.CardSetDefinition;
import com.ddaodan.buddycardsexpansion.core.PackSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomPackItem extends Item {
    private final CardSetDefinition setDefinition;
    private final Random random = new Random();

    public CustomPackItem(CardSetDefinition setDefinition, Properties properties) {
        super(properties);
        this.setDefinition = setDefinition;
    }

    public CardSetDefinition getSetDefinition() {
        return setDefinition;
    }

    @Override
    public Component getName(ItemStack stack) {
        // 对标原版 Buddycards 命名: "<卡组名> 巴迪卡牌包" / "<Set Name> Buddycard Pack"
        return Component.translatable("item.ccb.pack_title", setDefinition.getDisplayName());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack packStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            PackSettings settings = setDefinition.getPackSettings();
            int count = settings.getCardsPerPack();
            List<CardDefinition> cards = setDefinition.getCards();

            if (cards.isEmpty()) {
                player.sendSystemMessage(Component.literal("This card pack is empty!").withStyle(ChatFormatting.RED));
                return InteractionResultHolder.pass(packStack);
            }

            for (int i = 0; i < count; i++) {
                boolean guarantee = settings.isGuaranteedUncommon() && (i == count - 1);
                CardDefinition picked = pickCard(cards, guarantee);
                ItemStack cardStack = createCardStack(picked, settings.getFoilChance());

                if (!player.getInventory().add(cardStack)) {
                    player.drop(cardStack, false);
                }
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 1.2F);

            if (!player.getAbilities().instabuild) {
                packStack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(packStack, level.isClientSide);
    }

    private CardDefinition pickCard(List<CardDefinition> allCards, boolean guaranteeUncommon) {
        List<CardDefinition> pool = new ArrayList<>();
        int totalWeight = 0;

        for (CardDefinition card : allCards) {
            CardRarity r = card.getRarity();
            if (guaranteeUncommon && r == CardRarity.COMMON) {
                continue;
            }
            int weight = r.getDefaultWeight();
            pool.add(card);
            totalWeight += weight;
        }

        if (pool.isEmpty()) {
            pool = allCards;
            totalWeight = pool.size();
        }

        int roll = random.nextInt(Math.max(1, totalWeight));
        int current = 0;
        for (CardDefinition c : pool) {
            current += c.getRarity().getDefaultWeight();
            if (roll < current) {
                return c;
            }
        }
        return pool.get(0);
    }

    private ItemStack createCardStack(CardDefinition card, double foilChance) {
        String itemId = setDefinition.getNamespace() + ":card_" + setDefinition.getId() + "_" + card.getCardNumber();
        Item item = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(itemId));
        if (item == null) {
            item = this;
        }
        ItemStack stack = new ItemStack(item);
        if (random.nextDouble() < foilChance) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean("foil", true);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        // 对标原版 Buddycards：简洁自然的卡包介绍
        tooltip.add(Component.translatable("tooltip.ccb.pack_desc",
                setDefinition.getPackSettings().getCardsPerPack())
                .withStyle(ChatFormatting.GRAY));

        if (setDefinition.getPackSettings().isGuaranteedUncommon()) {
            tooltip.add(Component.translatable("tooltip.ccb.pack_guarantee")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
