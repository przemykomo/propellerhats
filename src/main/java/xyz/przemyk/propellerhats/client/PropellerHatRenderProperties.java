package xyz.przemyk.propellerhats.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

public final class PropellerHatRenderProperties implements IItemRenderProperties {

    public static final PropellerHatRenderProperties INSTANCE = new PropellerHatRenderProperties();

    private PropellerHatRenderProperties() {}

    @Override
    public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        PropellerHatModel.INSTANCE.rotate(entityLiving, itemStack);
        return PropellerHatModel.INSTANCE;
    }
}
