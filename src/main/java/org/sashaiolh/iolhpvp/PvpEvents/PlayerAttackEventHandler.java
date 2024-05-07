package org.sashaiolh.iolhpvp.PvpEvents;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sashaiolh.iolhpvp.CombatMode.CombatModeManager;
import org.sashaiolh.iolhpvp.CombatMode.CombatTimerBossBar;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.Utils.MessageSender;
import org.sashaiolh.iolhpvp.Utils.NbtUtils;
import org.sashaiolh.iolhpvp.Utils.PlayerUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = IolhPvP.MODID)
public class PlayerAttackEventHandler {
    // Уникальные идентификаторы игроков в бою
    public static final Set<UUID> playersInCombat = new HashSet<>();

    public static final String pvpProtectKey = "iolhPvpProtection";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerAttack(AttackEntityEvent event) {
        Entity attacker = event.getEntity();
        Entity target = event.getTarget();

        if (attacker instanceof Player && target instanceof Player) {
            Player attackerPlayer = (Player) attacker;
            Player targetPlayer = (Player) target;


            // Проверка на режим выживания
            if(!PlayerUtils.isPlayerInSurvival((ServerPlayer) attackerPlayer) || !PlayerUtils.isPlayerInSurvival((ServerPlayer) targetPlayer)){
                event.setCanceled(true);
                return;
            }


            // Проверка защиты от PvP атакующего
            if(NbtUtils.hasPersistentTag(attackerPlayer, pvpProtectKey)) {
                if (NbtUtils.getPersistentBoolean(attackerPlayer, pvpProtectKey, false)) {
                    MessageSender.sendPvpMessageToPlayer(attackerPlayer, IolhPvP.configManager.getConfig("turnOffPvpMode"));
                    event.setCanceled(true);
                    return;
                }
            }
            else {
                NbtUtils.setPersistentBoolean(attackerPlayer, pvpProtectKey, false);
            }


            // Проверка защиты от PvP жертвы
            if(NbtUtils.hasPersistentTag(targetPlayer, pvpProtectKey)) {
                if (NbtUtils.getPersistentBoolean(targetPlayer, pvpProtectKey, false)) {
                    MessageSender.sendPvpMessageToPlayer(attackerPlayer, IolhPvP.configManager.getConfig("playerIsProtected"));
                    MessageSender.sendPvpMessageToPlayer(targetPlayer, IolhPvP.configManager.getConfig("youAreProtected"));
                    event.setCanceled(true);
                    return;
                }
            }
            else {
                NbtUtils.setPersistentBoolean(targetPlayer, pvpProtectKey, false);
            }



            UUID attackerUUID = attackerPlayer.getUUID();
            UUID targetUUID = targetPlayer.getUUID();

            CombatModeManager.enablePvpMode(attackerPlayer);
            CombatModeManager.enablePvpMode(targetPlayer);

//            // Сообщаем игрокам, что они в боевом режиме, если они не были уже добавлены
//            if (!playersInCombat.contains(attackerUUID)) {
//                playersInCombat.add(attackerUUID);
//                MessageSender.sendPvpMessageToPlayer(attackerPlayer, IolhPvP.configManager.getConfig("pvpModeOnForAttacker"));
//            }
//
//            if (!playersInCombat.contains(targetUUID)) {
//                playersInCombat.add(targetUUID);
//                MessageSender.sendPvpMessageToPlayer(targetPlayer, IolhPvP.configManager.getConfig("pvpModeOnForTarget"));
//            }
//
//            // Добавляем игроков в боевой режим
//            CombatTimerBossBar.startCombatTimer(attackerUUID);  // Запускаем босс-бар для атакующего
//            CombatTimerBossBar.startCombatTimer(targetUUID);  // Запускаем босс-бар для цели
//
//
//            // Используем новый класс CombatTimers для сброса таймеров
//            CombatTimers.resetCombatTimer(attackerUUID, playersInCombat);
//            CombatTimers.resetCombatTimer(targetUUID, playersInCombat);
        }
    }
}
