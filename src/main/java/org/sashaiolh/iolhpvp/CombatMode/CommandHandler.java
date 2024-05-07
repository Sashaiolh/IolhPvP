package org.sashaiolh.iolhpvp.CombatMode;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler;
import org.sashaiolh.iolhpvp.Utils.MessageSender;

@Mod.EventBusSubscriber(modid = IolhPvP.MODID)
public class CommandHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();
            ServerPlayer sourceServerPlayer = source.getPlayer();
            if (sourceServerPlayer!=null) {
                if (PlayerAttackEventHandler.playersInCombat.contains(sourceServerPlayer.getUUID())) {
                    MessageSender.sendPvpMessageToPlayer(sourceServerPlayer, IolhPvP.configManager.getConfig("onCommandMessage"));
                    event.setCanceled(true);
                }
            }
    }
}
