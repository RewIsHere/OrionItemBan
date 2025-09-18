package net.rew404.orionitemban.events;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.rew404.orionitemban.OrionItemBan;
import net.rew404.orionitemban.config.BannedItemsConfig;

@Mod.EventBusSubscriber(modid = OrionItemBan.MOD_ID)
public class PlayerItemEvents {

    private static String getItemId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        return key != null ? key.toString() : "unknown";
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItem().getItem();
        String id = getItemId(stack.getItem());

        if (BannedItemsConfig.isDeleteOnPickup(id)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("No puedes recoger este item, ya que esta desactivado"), true);
        }
    }
}
