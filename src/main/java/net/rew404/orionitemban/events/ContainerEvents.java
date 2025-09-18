package net.rew404.orionitemban.events;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rew404.orionitemban.OrionItemBan;
import net.rew404.orionitemban.config.BannedItemsConfig;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = OrionItemBan.MOD_ID)
public class ContainerEvents {

    @SubscribeEvent
    public static void onPlayerContainerEvent(PlayerContainerEvent event) {
        var container = event.getContainer();

        // Ignorar inventario del propio jugador (ej: inventario base sin contenedores)
        if (event.getEntity() != null && container == event.getEntity().inventoryMenu) {
            return;
        }

        // Recorrer todos los slots
        for (Slot slot : container.slots) {
            // Ignorar slots que pertenecen al inventario del jugador
            if (slot.container == event.getEntity().getInventory()) {
                continue;
            }

            // Aquí sí: son slots de cofres, mochilas, hornos, etc.
            ItemStack stack = slot.getItem();
            if (stack.isEmpty()) continue;

            String itemId = Objects.requireNonNull(
                    stack.getItem().builtInRegistryHolder().key().location()
            ).toString();

            if (BannedItemsConfig.isRemoveFromDungeons(itemId)) {
                slot.set(ItemStack.EMPTY); // Borra solo en inventarios externos
                OrionItemBan.LOGGER.info("Eliminado item baneado de inventario externo: {}", itemId);
            }
        }
    }
}
