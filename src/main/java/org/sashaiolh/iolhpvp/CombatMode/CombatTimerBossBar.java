package org.sashaiolh.iolhpvp.CombatMode;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.sashaiolh.iolhpvp.IolhPvP;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatTimerBossBar {
    private static final Map<UUID, ServerBossEvent> bossBars = new HashMap<>();
    private static final long COMBAT_DELAY = 10000; // 10 секунд

    public static void startCombatTimer(UUID playerUUID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);

        if (player != null) {
            // Проверяем, если босс-бар уже создан
            ServerBossEvent bossBar = bossBars.get(playerUUID);

            if (bossBar == null) { // Если нет, создаем новый
                bossBar = new ServerBossEvent(
                        Component.literal(IolhPvP.configManager.getConfig("timerBarLabel")),
                        ServerBossEvent.BossBarColor.RED,
                        ServerBossEvent.BossBarOverlay.PROGRESS
                );
                bossBars.put(playerUUID, bossBar);
            }

            if (!bossBar.getPlayers().contains(player)) { // Если игрок еще не добавлен
                bossBar.addPlayer(player);  // Добавляем игрока к босс-бару
            }
        }
    }

    public static void stopCombatTimer(UUID playerUUID) {
        ServerBossEvent bossBar = bossBars.get(playerUUID);
        if (bossBar != null) {
            bossBar.removeAllPlayers();  // Удаляем всех игроков из босс-бара
            bossBars.remove(playerUUID);  // Удаляем босс-бар из карты
        }
    }

    public static void updateCombatTimer(UUID playerUUID, long remainingTime) {
        ServerBossEvent bossBar = bossBars.get(playerUUID);
        if (bossBar != null) {
            float progress = (float) remainingTime / COMBAT_DELAY;  // Прогресс таймера
            bossBar.setProgress(progress);  // Обновляем прогресс босс-бара
        }
    }
}
