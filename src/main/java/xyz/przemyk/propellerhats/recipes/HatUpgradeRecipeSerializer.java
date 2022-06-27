package xyz.przemyk.propellerhats.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class HatUpgradeRecipeSerializer extends ShapedRecipe.Serializer {

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
