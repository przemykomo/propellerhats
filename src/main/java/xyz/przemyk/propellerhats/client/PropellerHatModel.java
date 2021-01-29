package xyz.przemyk.propellerhats.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import xyz.przemyk.propellerhats.PropHatsMod;

public class PropellerHatModel extends BipedModel<LivingEntity> {

    public static final PropellerHatModel INSTANCE = new PropellerHatModel();

    private final ModelRenderer main;
    private final ModelRenderer bone;

    public PropellerHatModel() {
        super(1.0F);
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 16.0F, 0.0F);
        main.setTextureOffset(0, 0).addBox(-0.5F, -12.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        main.setTextureOffset(20, 15).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 3.0F, 1.0F, 0.0F, false);
        main.setTextureOffset(20, 11).addBox(-5.0F, -9.0F, 4.0F, 10.0F, 3.0F, 1.0F, 0.0F, false);
        main.setTextureOffset(10, 14).addBox(4.0F, -9.0F, -4.0F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        main.setTextureOffset(0, 11).addBox(-5.0F, -9.0F, -4.0F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 3.0F, 0.0F);
        main.addChild(bone);
        bone.setTextureOffset(22, 22).addBox(-0.5F, -16.0F, 0.5F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone.setTextureOffset(0, 22).addBox(-0.5F, -16.0F, -6.5F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone.setTextureOffset(0, 9).addBox(-6.5F, -16.0F, -0.5F, 13.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        main.rotationPointY = bipedHead.rotationPointY;
        main.rotateAngleZ = bipedHead.rotateAngleZ;
        main.rotateAngleX = bipedHead.rotateAngleX;
        main.rotateAngleY = bipedHead.rotateAngleY;
        return ImmutableList.of(main);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of();
    }

    public void rotate(LivingEntity entity, ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        if (entity instanceof PlayerEntity && PropHatsMod.isFlyingIgnoreItemType((PlayerEntity) entity, itemStack)) {
            bone.rotateAngleY = (entity.ticksExisted + (minecraft.isGamePaused() ? 0 : minecraft.getRenderPartialTicks())) / 2f;
        }
    }
}
