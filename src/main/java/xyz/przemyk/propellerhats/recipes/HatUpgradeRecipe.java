package xyz.przemyk.propellerhats.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public class HatUpgradeRecipe extends ShapedRecipe {

    public HatUpgradeRecipe(String pGroup, CraftingBookCategory pCategory, ShapedRecipePattern pPattern, ItemStack pResult, boolean pShowNotification) {
        super(pGroup, pCategory, pPattern, pResult, pShowNotification);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PropHatsMod.HAT_UPGRADE_RECIPE_SERIALIZER.get();
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack output =  super.assemble(craftingInput, provider);
        for (int i = 0; i < craftingInput.size(); ++i) {
            ItemStack itemStack = craftingInput.getItem(i);
            if (itemStack.getItem() instanceof PropellerHatItem) {
                output.set(PropHatsMod.ENERGY, itemStack.getOrDefault(PropHatsMod.ENERGY, 0));
                break;
            }
        }

        return output;
    }

}
