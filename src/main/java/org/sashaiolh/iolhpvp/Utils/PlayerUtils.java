package org.sashaiolh.iolhpvp.Utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class PlayerUtils {

    public static void dropAllItems(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.getCommandSenderWorld();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (!itemStack.isEmpty()) {
                double x = player.getX();
                double y = player.getY() - 0.5; // Небольшая высота
                double z = player.getZ();
                ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack.copy());
                level.addFreshEntity(itemEntity);
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }
    }

    public static void killPlayer(ServerPlayer player) {
        player.hurt(DamageSource.GENERIC, Float.MAX_VALUE); // Максимальный урон
    }

    public static boolean isPlayerInSurvival(ServerPlayer player) {
        return player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
    }
}
