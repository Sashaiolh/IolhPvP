package org.sashaiolh.iolhpvp.PvpEvents;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.sashaiolh.iolhpvp.CombatMode.CombatModeManager;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.Utils.PlayerUtils;

@Mod.EventBusSubscriber(modid = IolhPvP.MODID)
public class PlayerHitByArrowHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity(); // Сущность, которая получила урон
        DamageSource damageSource = event.getSource(); // Источник урона
        Entity attacker = damageSource.getEntity(); // Сущность, которая нанесла урон


        // Проверяем, был ли урон нанесен стрелой
        if (attacker instanceof ServerPlayer && target instanceof ServerPlayer && target.getUUID() != damageSource.getEntity().getUUID()) {
            ServerPlayer attackerPlayer = (ServerPlayer) attacker;
            ServerPlayer targetPlayer = (ServerPlayer) target;
            if(PlayerUtils.isPlayerInSurvival(attackerPlayer) && PlayerUtils.isPlayerInSurvival(targetPlayer)) {
                CombatModeManager.enablePvpMode(attackerPlayer);
                CombatModeManager.enablePvpMode(targetPlayer);
            }
        }
    }
}


