package org.sashaiolh.iolhpvp.CombatMode;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.sashaiolh.iolhpvp.CombatTimers;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler;
import org.sashaiolh.iolhpvp.Utils.PlayerUtils;

import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = IolhPvP.MODID)
public class PlayerLogoutEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerUUID = player.getUUID();

//        for(ServerPlayer serverPlayer : server.getPlayerList().getPlayers()){
//            serverPlayer.displayClientMessage(Component.literal(player.getName().getString()+" ливнул с позором"), false);
//        }

        // Завершаем боевой режим при отключении игрока
        if (PlayerAttackEventHandler.playersInCombat.contains(playerUUID)) {
            CombatTimers.stopCombatTimer(playerUUID, null);
        }

        PlayerUtils.dropAllItems(player);

        PlayerUtils.killPlayer(player);


        event.setCanceled(true);
    }
}
