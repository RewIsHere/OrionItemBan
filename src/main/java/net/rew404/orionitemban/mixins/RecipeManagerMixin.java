package net.rew404.orionitemban.mixins;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.rew404.orionitemban.config.BannedItemsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(
            at = @At("RETURN"),
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
            cancellable = true
    )
    private <C extends Container, T extends Recipe<C>> void onGetRecipe(
            RecipeType<T> recipeType, C inventory, Level level, CallbackInfoReturnable<Optional<T>> cir) {

        cir.getReturnValue().ifPresent(recipe -> {
            ItemStack result = recipe.assemble(inventory, level.registryAccess()); // <-- nivel agregado
            String id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(result.getItem()).toString();

            if (BannedItemsConfig.isBanCrafting(id)) {
                cir.setReturnValue(Optional.empty());
            }
        });
    }

    @Inject(
            at = @At("RETURN"),
            method = "getRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/List;",
            cancellable = true
    )
    private <C extends Container, T extends Recipe<C>> void onGetRecipes(
            RecipeType<T> recipeType, C inventory, Level level, CallbackInfoReturnable<List<T>> cir) {

        List<T> filtered = cir.getReturnValue().stream()
                .filter(recipe -> {
                    ItemStack result = recipe.assemble(inventory,  level.registryAccess()); // <-- nivel agregado
                    String id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(result.getItem()).toString();
                    return !BannedItemsConfig.isBanCrafting(id);
                })
                .collect(Collectors.toList());

        cir.setReturnValue(filtered);
    }
}
