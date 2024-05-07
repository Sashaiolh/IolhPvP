package org.sashaiolh.iolhpvp.Commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.sashaiolh.iolhpvp.Utils.MessageSender;
import org.sashaiolh.iolhpvp.Utils.NbtUtils;

import java.util.ArrayList;

import static org.sashaiolh.iolhpvp.PvpEvents.PlayerAttackEventHandler.pvpProtectKey;


public class PvpProtectionCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("pvp")
                        .requires(source -> source.hasPermission(4))
                        .executes(context -> {
                            ServerPlayer source = context.getSource().getPlayerOrException();
                            boolean currentPvpProtection = NbtUtils.getPersistentBoolean(source, pvpProtectKey, false);
                            NbtUtils.setPersistentBoolean(source, pvpProtectKey, !currentPvpProtection);
                            MessageSender.sendPvpMessageToPlayer(source, "Protection now is "+!currentPvpProtection);
                            return 1;
                        })
        );
    }
}
