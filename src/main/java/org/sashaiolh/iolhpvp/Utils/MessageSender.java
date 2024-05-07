package org.sashaiolh.iolhpvp.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.sashaiolh.iolhpvp.IolhPvP;

public class MessageSender {
    public static void sendPvpMessageToPlayer(Player player, String message){
        if(message!="") {
            String messagePrefix = IolhPvP.configManager.getConfig("messagePrefix");
            String resultMessage = messagePrefix + message;
            player.displayClientMessage(Component.literal(resultMessage), false);
        }
    }
}
