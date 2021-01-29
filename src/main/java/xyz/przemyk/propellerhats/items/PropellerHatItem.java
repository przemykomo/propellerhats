package xyz.przemyk.propellerhats.items;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.client.PropellerHatModel;
import xyz.przemyk.propellerhats.energy.CapabilityProviderEnergy;
import xyz.przemyk.propellerhats.energy.ItemEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class PropellerHatItem extends ArmorItem {

    public final int energyCapacity;
    public final int energyUsage;
    public final float speed;

    public PropellerHatItem(IArmorMaterial materialIn, Properties builderIn, int energyCapacity, int energyUsage, float speed) {
        super(materialIn, EquipmentSlotType.HEAD, builderIn);
        this.energyCapacity = energyCapacity;
        this.energyUsage = energyUsage;
        this.speed = speed;
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        PropellerHatModel.INSTANCE.rotate(entityLiving, itemStack);
        return (A) PropellerHatModel.INSTANCE;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        ResourceLocation registryName = getRegistryName();
        if (registryName == null) {
            return null;
        }
        return registryName.getNamespace() + ":textures/models/armor/" + registryName.getPath() + ".png";
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergy(new ItemEnergyStorage(stack, energyCapacity));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x00000BD5;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> {
            int capacity = energy.getMaxEnergyStored();
            return ((double) (capacity - energy.getEnergyStored())) / capacity;
        }).orElse(0.0);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (PropHatsMod.isHoldingUp(player) && stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> energy.extractEnergy(energyUsage, false) > 0).orElse(false)) {
            Vector3d motion = player.getMotion();
            if (motion.y < 0.3) {
                player.setMotion(motion.x, motion.y + speed, motion.z);
                player.fallDistance = 0;
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY != null) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> tooltip.add(new StringTextComponent("Energy: " + energy.getEnergyStored() + "FE")));
        }
    }
}
