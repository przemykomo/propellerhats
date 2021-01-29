package xyz.przemyk.propellerhats.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public class HatUpgradeRecipe extends ShapedRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    public HatUpgradeRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack output =  super.getCraftingResult(inv);
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (itemStack.getItem() instanceof PropellerHatItem) {
                output.setTag(itemStack.getOrCreateTag().copy());
                break;
            }
        }

        return output;
    }

    public static class Serializer extends ShapedRecipe.Serializer {

        public Serializer() {
            setRegistryName(PropHatsMod.MODID, "hat_upgrade");
        }

        @Override
        public HatUpgradeRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe shapedRecipe = super.read(recipeId, json);

            return new HatUpgradeRecipe(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getRecipeOutput());
        }

        @Override
        public HatUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            ShapedRecipe shapedRecipe = super.read(recipeId, buffer);
            if (shapedRecipe == null) {
                return null;
            }

            return new HatUpgradeRecipe(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getRecipeOutput());
        }
    }
}
