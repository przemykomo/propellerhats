package xyz.przemyk.propellerhats.items;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import xyz.przemyk.propellerhats.PropHatsMod;

import java.util.List;

public class PropellerHatItem extends ArmorItem {

    public final int energyCapacity;
    public final int energyUsage;
    public final float speed;

    public PropellerHatItem(Holder<ArmorMaterial> pMaterial, Properties builderIn, int energyCapacity, int energyUsage, float speed) {
        super(pMaterial, Type.HELMET, builderIn);
        this.energyCapacity = energyCapacity;
        this.energyUsage = energyUsage;
        this.speed = speed;
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        return ResourceLocation.fromNamespaceAndPath(registryName.getNamespace(), "textures/models/armor/" + registryName.getPath() + ".png");
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00000BD5;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null) {
            return 0;
        }
        int capacity = energyStorage.getMaxEnergyStored();
        return 13 - (int) ((capacity - energyStorage.getEnergyStored()) * 13.0f) / capacity;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pSlotId == 39 && pEntity instanceof Player player && !player.isSpectator() && PropHatsMod.isHoldingUp(player)) {
            if (energyUsage > 0) {
                IEnergyStorage energyStorage = pStack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (energyStorage != null) {
                    if (energyStorage.extractEnergy(energyUsage, false) == 0) {
                        return;
                    }
                }
            }
            Vec3 motion = player.getDeltaMovement();
            if (motion.y < speed + 0.2) {
                player.setDeltaMovement(motion.x, motion.y + speed, motion.z);
                player.fallDistance = 0;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        IEnergyStorage energyStorage = pStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            pTooltipComponents.add(Component.translatable("text." + PropHatsMod.MODID + ".energy", energyStorage.getEnergyStored()));
        }
    }
}
