package org.sashaiolh.iolhpvp.CombatMode;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sashaiolh.iolhpvp.CombatTimers;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler;
import org.sashaiolh.iolhpvp.Utils.PlayerUtils;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = IolhPvP.MODID)
public class PlayerDeathEventHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer targetPlayer = (ServerPlayer) event.getEntity();
            UUID targetPlayerUUID = targetPlayer.getUUID();

            // Проверяем, в режиме пвп ли игрок
            if (PlayerAttackEventHandler.playersInCombat.contains(targetPlayerUUID)) {
//                Player killerPlayer = (Player) event.getSource().getEntity();
//                String killerName = killerPlayer.getName().getString();

                // Выводим сообщение о том, что игрок был убит
//                Component deathMessage = Component.literal(killerName + " фывфы убил " + targetName);
//                targetPlayer.getLevel().players().forEach(p -> p.displayClientMessage(deathMessage, false));

                PlayerUtils.dropAllItems(targetPlayer);

                CombatTimers.stopCombatTimer(targetPlayerUUID, null);


            }
        }
    }
}