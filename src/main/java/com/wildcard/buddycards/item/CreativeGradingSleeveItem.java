package com.wildcard.buddycards.item;

import com.wildcard.buddycards.savedata.PerfectBuddycardCollectionSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public class CreativeGradingSleeveItem extends GradingSleeveItem{
    public CreativeGradingSleeveItem(Properties properties, float[] odds) {
        super(properties, odds);
    }

    @Override
    public boolean tryGrade(ItemStack card, ItemStack sleeves, Player player, Level level) {
        CompoundTag nbt = card.getOrCreateTag().copy();
        if (level instanceof ServerLevel serverLevel && card.getItem() instanceof BuddycardItem && !nbt.contains("grade")) {
            int grade;
            float rand = level.getRandom().nextFloat();
            for (grade = 1; grade < 5; grade++) {
                if (rand < ODDS[grade - 1])
                    break;
                rand -= ODDS[grade - 1];
            }
            nbt.putInt("grade", grade);
            ItemStack newCard = new ItemStack(card.getItem());
            newCard.setTag(nbt);
            card.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, newCard);
            if(grade == 5) {
                player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                PerfectBuddycardCollectionSaveData.get(serverLevel).addPlayerCardFound(player.getUUID(), ((BuddycardItem) card.getItem()).getSet(), ((BuddycardItem) card.getItem()).getCardNumber());
            }
            return true;
        }
        return false;
    }
}
