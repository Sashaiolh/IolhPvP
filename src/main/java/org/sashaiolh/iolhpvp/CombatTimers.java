package org.sashaiolh.iolhpvp;



import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.sashaiolh.iolhpvp.CombatMode.CombatTimerBossBar;
import org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler;
import org.sashaiolh.iolhpvp.Utils.MessageSender;

import java.util.*;

public class CombatTimers {
    private static final Map<UUID, Timer> combatTimers = new HashMap<>();
    private static final Map<UUID, Long> combatStartTimes = new HashMap<>(); // Время начала каждого таймера
    private static final long COMBAT_DELAY = 10000; // 10 секунд
    private static Timer updateTimer;

    static {
        startUpdateTimer();
    }

    public static void startUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.cancel();
        }

        updateTimer = new Timer();

        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (combatStartTimes) {
                    Set<Map.Entry<UUID, Long>> entries = new HashSet<>(combatStartTimes.entrySet()); // Копия элементов

                    for (Map.Entry<UUID, Long> entry : entries) {
                        UUID playerUUID = entry.getKey();
                        long startTime = entry.getValue();

                        long currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - startTime;
                        long remainingTime = COMBAT_DELAY - elapsedTime;

                        // Преобразуем оставшееся время в секунды, округляя в меньшую сторону
                        int remainingSeconds = (int) Math.max(remainingTime / 1000, 0); // Убедимся, что не будет отрицательных значений

                        if (remainingSeconds <= 0) {
                            stopCombatTimer(playerUUID, IolhPvP.configManager.getConfig("pvpModeEndMessage")); // Завершаем таймер
                        } else {
                            CombatTimerBossBar.updateCombatTimer(playerUUID, remainingSeconds * 1000); // Обновляем таймер в миллисекундах
                        }
                    }
                }
            }
        }, 1000, 1000); // Обновление каждую секунду
    }


    public static void stopCombatTimer(UUID playerUUID, String message) {
        synchronized (combatStartTimes) {
            if (combatTimers.containsKey(playerUUID)) {
                combatTimers.get(playerUUID).cancel();
                combatTimers.remove(playerUUID);

                if (combatStartTimes.containsKey(playerUUID)) {
                    combatStartTimes.remove(playerUUID); // Безопасное удаление из коллекции
                }

                PlayerAttackEventHandler.playersInCombat.remove(playerUUID);

                // Сообщение игроку
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);

                if (player != null && message != null) {
                    MessageSender.sendPvpMessageToPlayer(player, message);
                }

                CombatTimerBossBar.stopCombatTimer(playerUUID); // Остановка босс-бара
            }
        }
    }

    public static void resetCombatTimer(UUID playerUUID, Set<UUID> playersInCombat) {
        synchronized (combatStartTimes) {
            // Отменяем предыдущий таймер, если он был запущен
            if (combatTimers.containsKey(playerUUID)) {
                combatTimers.get(playerUUID).cancel();
            }

            combatStartTimes.put(playerUUID, System.currentTimeMillis()); // Время старта

            // Создаем новый таймер
            Timer newTimer = new Timer();
            newTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (playersInCombat.contains(playerUUID)) {
                        stopCombatTimer(playerUUID, IolhPvP.configManager.getConfig("pvpModeEndMessage")); // Безопасное завершение таймера
                    }
                }
            }, COMBAT_DELAY);

            combatTimers.put(playerUUID, newTimer);
        }
    }
}
