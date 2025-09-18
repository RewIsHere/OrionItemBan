package net.rew404.orionitemban.commands;

import com.mojang.brigadier.Command;
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

public class CommandBlockStatus implements Command<CommandSourceStack> {

    private static final CommandBlockStatus CMD = new CommandBlockStatus();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return Commands.literal("status")
                .requires(cs -> cs.hasPermission(2))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean enchantingDisabled = BannedItemsConfig.DISABLE_ENCHANTING_TABLE;
        boolean anvilDisabled = BannedItemsConfig.DISABLE_ANVIL;

        context.getSource().sendSuccess(() -> Component.literal(
                "Estado actual:\n" +
                        "Mesa de encantamientos: " + (enchantingDisabled ? "Deshabilitada ❌" : "Habilitada ✅") + "\n" +
                        "Yunque: " + (anvilDisabled ? "Deshabilitado ❌" : "Habilitado ✅")
        ).withStyle(ChatFormatting.GREEN), false);

        return 1;
    }
}
