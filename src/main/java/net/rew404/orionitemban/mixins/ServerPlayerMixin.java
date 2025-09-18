package net.rew404.orionitemban.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.rew404.orionitemban.config.BannedItemsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(
            method = "doTick",
            at = @At(value = "HEAD")
    )
    public void onPlayerTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        Inventory inv = player.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            String itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString();

            if (BannedItemsConfig.isDropFromInventory(itemId)) {
                // Dropear el item en la posición del jugador
                ItemEntity dropped = player.drop(stack.copy(), true);
                if (dropped != null) {
                    dropped.setPickUpDelay(40); // 2 segundos antes de poder recogerlo
                }
                // Limpiar el slot
                stack.setCount(0);

                // Mensaje al jugador
                player.displayClientMessage(
                        Component.literal("Este item está baneado y se ha dropeado.").withStyle(
                        ChatFormatting.RED
                        ),
                        true
                );
            }
        }
    }
}
