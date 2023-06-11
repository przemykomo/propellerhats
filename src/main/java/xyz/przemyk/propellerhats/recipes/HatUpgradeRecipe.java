package xyz.przemyk.propellerhats.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public class HatUpgradeRecipe extends ShapedRecipe {

    public HatUpgradeRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, CraftingBookCategory.MISC, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PropHatsMod.HAT_UPGRADE_RECIPE_SERIALIZER.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack output =  super.assemble(inv, registryAccess);
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack.getItem() instanceof PropellerHatItem) {
                output.setTag(itemStack.getOrCreateTag().copy());
                break;
            }
        }

        return output;
    }

}
