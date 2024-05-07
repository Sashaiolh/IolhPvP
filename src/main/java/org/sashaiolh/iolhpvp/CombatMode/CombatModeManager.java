package org.sashaiolh.iolhpvp.CombatMode;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.sashaiolh.iolhpvp.CombatTimers;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler;
import org.sashaiolh.iolhpvp.Utils.MessageSender;

import java.util.UUID;

public class CombatModeManager {

    public static void enablePvpMode(Player player){
        UUID playerUUID = player.getUUID();
        // Сообщаем игрокам, что они в боевом режиме, если они не были уже добавлены
        if (!PlayerAttackEventHandler.playersInCombat.contains(playerUUID)) {
            PlayerAttackEventHandler.playersInCombat.add(playerUUID);
            MessageSender.sendPvpMessageToPlayer(player, IolhPvP.configManager.getConfig("pvpModeOn"));
        }

//        if (!playersInCombat.contains(targetUUID)) {
//            playersInCombat.add(targetUUID);
//            MessageSender.sendPvpMessageToPlayer(targetPlayer, IolhPvP.configManager.getConfig("pvpModeOnForTarget"));
//        }

        // Добавляем игроков в боевой режим
        CombatTimerBossBar.startCombatTimer(playerUUID);  // Запускаем босс-бар для игрока
//        CombatTimerBossBar.startCombatTimer(attackerUUID);  // Запускаем босс-бар для атакующего
//        CombatTimerBossBar.startCombatTimer(targetUUID);  // Запускаем босс-бар для цели


        // Используем новый класс CombatTimers для сброса таймеров
        CombatTimers.resetCombatTimer(playerUUID, PlayerAttackEventHandler.playersInCombat);
//        CombatTimers.resetCombatTimer(attackerUUID, playersInCombat);
//        CombatTimers.resetCombatTimer(targetUUID, playersInCombat);
    }

}
