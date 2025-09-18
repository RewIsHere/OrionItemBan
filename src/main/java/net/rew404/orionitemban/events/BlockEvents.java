package net.rew404.orionitemban.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rew404.orionitemban.OrionItemBan;
import net.rew404.orionitemban.config.BannedItemsConfig;

@Mod.EventBusSubscriber(modid = OrionItemBan.MOD_ID)
public class BlockEvents {

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Player player = event.getEntity();

        if ((BannedItemsConfig.DISABLE_ENCHANTING_TABLE && state.is(Blocks.ENCHANTING_TABLE)) ||
                (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.ANVIL)) || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.CHIPPED_ANVIL))
                || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.DAMAGED_ANVIL))) {
            event.setCanceled(true);
            player.displayClientMessage(
                    Component.literal(state.is(Blocks.ENCHANTING_TABLE) ?
                            "Mesa de encantamientos deshabilitada ❌" :
                            "Yunque deshabilitado ❌").withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if ((BannedItemsConfig.DISABLE_ENCHANTING_TABLE && state.is(Blocks.ENCHANTING_TABLE)) ||
                (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.ANVIL)) || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.CHIPPED_ANVIL))
                || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.DAMAGED_ANVIL))) {
            event.setCanceled(true);
            event.getPlayer().displayClientMessage(
                    Component.literal(state.is(Blocks.ENCHANTING_TABLE) ?
                            "Mesa de encantamientos deshabilitada ❌" :
                            "Yunque deshabilitado ❌").withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        BlockState state = event.getPlacedBlock();
        if ((BannedItemsConfig.DISABLE_ENCHANTING_TABLE && state.is(Blocks.ENCHANTING_TABLE)) ||
                (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.ANVIL)) || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.CHIPPED_ANVIL))
                || (BannedItemsConfig.DISABLE_ANVIL && state.is(Blocks.DAMAGED_ANVIL))) {
            event.setCanceled(true);
            if (event.getEntity() instanceof Player player) {
                player.displayClientMessage(
                        Component.literal(state.is(Blocks.ENCHANTING_TABLE) ?
                                "Mesa de encantamientos deshabilitada ❌" :
                                "Yunque deshabilitado ❌").withStyle(ChatFormatting.RED),
                        true
                );
            }
        }
    }
}
