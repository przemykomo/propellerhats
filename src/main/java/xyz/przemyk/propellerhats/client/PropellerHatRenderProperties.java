package xyz.przemyk.propellerhats.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public final class PropellerHatRenderProperties implements IItemRenderProperties {

    public static final PropellerHatRenderProperties INSTANCE = new PropellerHatRenderProperties();

    private PropellerHatRenderProperties() {}

    @SuppressWarnings("unchecked")
    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        PropellerHatModel.INSTANCE.rotate(entityLiving, itemStack);
        return (A) PropellerHatModel.INSTANCE;
    }
}
