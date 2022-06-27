package xyz.przemyk.propellerhats.items;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.client.PropellerHatRenderProperties;
import xyz.przemyk.propellerhats.energy.CapabilityProviderEnergy;
import xyz.przemyk.propellerhats.energy.ItemEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class PropellerHatItem extends ArmorItem {

    public final int energyCapacity;
    public final int energyUsage;
    public final float speed;

    public PropellerHatItem(ArmorMaterial materialIn, Properties builderIn, int energyCapacity, int energyUsage, float speed) {
        super(materialIn, EquipmentSlot.HEAD, builderIn);
        this.energyCapacity = energyCapacity;
        this.energyUsage = energyUsage;
        this.speed = speed;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(PropellerHatRenderProperties.INSTANCE);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this);
        if (registryName == null) {
            return null;
        }
        return registryName.getNamespace() + ":textures/models/armor/" + registryName.getPath() + ".png";
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityProviderEnergy(new ItemEnergyStorage(stack, energyCapacity));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00000BD5;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> {
            int capacity = energy.getMaxEnergyStored();
            return 13 - (int)((capacity - energy.getEnergyStored()) * 13.0f) / capacity;
        }).orElse(0);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!player.isSpectator() && PropHatsMod.isHoldingUp(player) && (energyUsage == 0 || stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> energy.extractEnergy(energyUsage, false) > 0).orElse(false))) {
            Vec3 motion = player.getDeltaMovement();
            if (motion.y < speed + 0.2) {
                player.setDeltaMovement(motion.x, motion.y + speed, motion.z);
                player.fallDistance = 0;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY != null) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> tooltip.add(Component.translatable("text." + PropHatsMod.MODID + ".energy", energy.getEnergyStored())));
        }
    }
}
