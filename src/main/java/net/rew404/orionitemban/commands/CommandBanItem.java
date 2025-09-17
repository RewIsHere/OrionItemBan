package net.rew404.orionitemban.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.rew404.orionitemban.config.BannedItemsConfig;

public class CommandBanItem implements Command<CommandSourceStack> {
    private static final CommandBanItem CMD = new CommandBanItem();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return Commands
                .literal("ban")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("item", ItemArgument.item(context))
                        .then(Commands.argument("category", StringArgumentType.word())
                                .suggests((c, b) -> {
                                    b.suggest("drop");
                                    b.suggest("pickup");
                                    b.suggest("crafting");
                                    b.suggest("trades");
                                    b.suggest("dungeon");
                                    return b.buildFuture();
                                })
                                .executes(CMD)))
                .then(Commands.literal("hand")
                        .then(Commands.argument("category", StringArgumentType.word())
                                .suggests((c, b) -> {
                                    b.suggest("drop");
                                    b.suggest("pickup");
                                    b.suggest("crafting");
                                    b.suggest("trades");
                                    b.suggest("dungeon");
                                    return b.buildFuture();
                                })
                                .executes(ctx -> {
                                    ItemStack stack = ctx.getSource().getPlayerOrException().getMainHandItem();
                                    int i = banItem(ctx, stack.getItem(), StringArgumentType.getString(ctx, "category"));
                                    stack.setCount(0);
                                    return i;
                                })));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return banItem(
                context,
                ItemArgument.getItem(context, "item").getItem(),
                StringArgumentType.getString(context, "category")
        );
    }

    private static int banItem(CommandContext<CommandSourceStack> context, Item item, String category) {
        if (item == Items.AIR)
            return 1;

        String itemId = ForgeRegistries.ITEMS.getKey(item).toString();
        BannedItemsConfig.banItem(itemId, category);

        context.getSource().sendSuccess(() ->
                Component.literal("Item " + itemId + " banned in category [" + category + "]!"), false);

        // Si la categoría es "drop", limpiamos inventarios
        if (category.equalsIgnoreCase("drop")) {
            PlayerList playerList = context.getSource().getServer().getPlayerList();
            for (ServerPlayer player : playerList.getPlayers()) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack slot = player.getInventory().getItem(i);
                    if (BannedItemsConfig.isDropFromInventory(ForgeRegistries.ITEMS.getKey(slot.getItem()).toString())) {
                        slot.setCount(0);
                    }
                }
            }
        }
        return 0;
    }
}
