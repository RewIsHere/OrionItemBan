package net.rew404.orionitemban.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.rew404.orionitemban.config.BannedItemsConfig;

public class CommandBlockControl {

    private static final CommandBlockControl CMD = new CommandBlockControl();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return Commands.literal("block")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("enchanting_table")
                        .then(Commands.literal("enable").executes(ctx -> CMD.enableEnchanting(ctx)))
                        .then(Commands.literal("disable").executes(ctx -> CMD.disableEnchanting(ctx)))
                )
                .then(Commands.literal("anvil")
                        .then(Commands.literal("enable").executes(ctx -> CMD.enableAnvil(ctx)))
                        .then(Commands.literal("disable").executes(ctx -> CMD.disableAnvil(ctx)))
                );
    }

    private int enableEnchanting(CommandContext<CommandSourceStack> context) {
        BannedItemsConfig.setDisableEnchantingTable(false);
        context.getSource().sendSuccess(() -> Component.literal("Mesa de encantamientos ACTIVADA ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
        return 1;
    }

    private int disableEnchanting(CommandContext<CommandSourceStack> context) {
        BannedItemsConfig.setDisableEnchantingTable(true);
        context.getSource().sendSuccess(() -> Component.literal("Mesa de encantamientos DESACTIVADA ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        return 1;
    }

    private int enableAnvil(CommandContext<CommandSourceStack> context) {
        BannedItemsConfig.setDisableAnvil(false);
        context.getSource().sendSuccess(() -> Component.literal("Yunque ACTIVADO ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
        return 1;
    }

    private int disableAnvil(CommandContext<CommandSourceStack> context) {
        BannedItemsConfig.setDisableAnvil(true);
        context.getSource().sendSuccess(() -> Component.literal("Yunque DESACTIVADO ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        return 1;
    }
}
