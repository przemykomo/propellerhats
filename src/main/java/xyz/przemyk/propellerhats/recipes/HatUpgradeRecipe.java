package xyz.przemyk.propellerhats.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public class HatUpgradeRecipe extends ShapedRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    public HatUpgradeRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack output =  super.assemble(inv);
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemStack = inv.getItem(i);
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
        public HatUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe shapedRecipe = super.fromJson(recipeId, json);

            return new HatUpgradeRecipe(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
        }

        @Override
        public HatUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe shapedRecipe = super.fromNetwork(recipeId, buffer);
            if (shapedRecipe == null) {
                return null;
            }

            return new HatUpgradeRecipe(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
        }
    }
}
