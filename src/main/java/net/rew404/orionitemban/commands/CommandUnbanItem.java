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
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;
import net.rew404.orionitemban.config.BannedItemsConfig;

public class CommandUnbanItem implements Command<CommandSourceStack> {
    private static final CommandUnbanItem CMD = new CommandUnbanItem();
    private static final UnbanAll CMD_ALL = new UnbanAll();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return Commands
                .literal("unban")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("item", ItemArgument.item(context))
                        .executes(CMD))
                .then(Commands.literal("all")
                        .executes(CMD_ALL));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String itemId = ForgeRegistries.ITEMS.getKey(ItemArgument.getItem(context, "item").getItem()).toString();
        try {
            BannedItemsConfig.unbanItem(itemId);
            context.getSource().sendSuccess(() ->
                    Component.literal("El item " + itemId + " ha sido desbaneado!").withStyle(ChatFormatting.AQUA), false);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error al desbanear ese item").withStyle(ChatFormatting.RED));
        }
        return 0;
    }

    public static class UnbanAll implements Command<CommandSourceStack> {
        @Override
        public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            BannedItemsConfig.DROP_FROM_INVENTORY.clear();
            BannedItemsConfig.DELETE_ON_PICKUP.clear();
            BannedItemsConfig.BAN_CRAFTING.clear();
            BannedItemsConfig.REMOVE_FROM_DUNGEONS.clear();
            BannedItemsConfig.saveConfig();

            context.getSource().sendSuccess(() ->
                    Component.literal("Todos los items han sido desbaneados!").withStyle(ChatFormatting.AQUA), false);
            return 0;
        }
    }
}
