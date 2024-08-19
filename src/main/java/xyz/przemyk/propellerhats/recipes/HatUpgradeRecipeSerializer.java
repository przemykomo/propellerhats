package xyz.przemyk.propellerhats.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class HatUpgradeRecipeSerializer implements RecipeSerializer<HatUpgradeRecipe> {

    @SuppressWarnings("DataFlowIssue")
    public static final MapCodec<HatUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
            kind -> kind.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(null)),
                            Codec.BOOL.optionalFieldOf("show_notification", Boolean.TRUE).forGetter(ShapedRecipe::showNotification)
                    )
                    .apply(kind, HatUpgradeRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HatUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
            HatUpgradeRecipeSerializer::toNetwork, HatUpgradeRecipeSerializer::fromNetwork
    );

    @Override
    public MapCodec<HatUpgradeRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, HatUpgradeRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static HatUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
        String s = byteBuf.readUtf();
        CraftingBookCategory craftingbookcategory = byteBuf.readEnum(CraftingBookCategory.class);
        ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(byteBuf);
        ItemStack itemstack = ItemStack.STREAM_CODEC.decode(byteBuf);
        boolean flag = byteBuf.readBoolean();
        return new HatUpgradeRecipe(s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
    }

    @SuppressWarnings("DataFlowIssue")
    private static void toNetwork(RegistryFriendlyByteBuf byteBuf, HatUpgradeRecipe recipe) {
        byteBuf.writeUtf(recipe.getGroup());
        byteBuf.writeEnum(recipe.category());
        ShapedRecipePattern.STREAM_CODEC.encode(byteBuf, recipe.pattern);
        ItemStack.STREAM_CODEC.encode(byteBuf, recipe.getResultItem(null));
        byteBuf.writeBoolean(recipe.showNotification());
    }
}