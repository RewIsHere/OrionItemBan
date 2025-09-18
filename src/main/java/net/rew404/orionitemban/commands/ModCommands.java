package net.rew404.orionitemban.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal("orionitemban")
                        .then(CommandBanItem.register(dispatcher, context))
                        .then(CommandUnbanItem.register(dispatcher, context))
                        .then(CommandBanList.register(dispatcher, context))
                        .then(CommandBlockControl.register(dispatcher, context))
                        .then(CommandBlockStatus.register(dispatcher, context)) // <- agregamos aquí
        );

        dispatcher.register(Commands.literal("oib").redirect(cmd)); // alias corto
    }
}
