package xyz.przemyk.propellerhats.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class PropellerHatRenderProperties implements IClientItemExtensions {

    public static final PropellerHatRenderProperties INSTANCE = new PropellerHatRenderProperties();

    private PropellerHatRenderProperties() {}

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        PropellerHatModel.INSTANCE.rotate(livingEntity, itemStack);
        return PropellerHatModel.INSTANCE;
    }
}
