package org.sashaiolh.iolhpvp.Commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.sashaiolh.iolhpvp.IolhPvP;
import org.sashaiolh.iolhpvp.Utils.MessageSender;

import java.util.ArrayList;

public class ReloadCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("pvp")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("reload")
                                .requires(source -> source.hasPermission(4))
                                .executes(context -> {
                                    IolhPvP.updateConfigs();
                                    MessageSender.sendPvpMessageToPlayer(context.getSource().getPlayerOrException(), IolhPvP.configManager.getConfig("reloadConfigMessage"));
                                    return 1;
                                })
                        )
        );
    }
}
