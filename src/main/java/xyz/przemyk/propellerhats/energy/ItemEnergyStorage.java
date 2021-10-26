package xyz.przemyk.propellerhats.energy;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.IEnergyStorage;

public record ItemEnergyStorage(ItemStack itemStack, int capacity) implements IEnergyStorage {

    private CompoundTag getTag() {
        if (!itemStack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            itemStack.setTag(tag);
        }
        return itemStack.getTag();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        CompoundTag tag = getTag();
        int energy = tag.getInt("Energy");
        int energyReceived = Math.min(capacity - energy, maxReceive);

        if (!simulate) {
            tag.putInt("Energy", energy + energyReceived);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        CompoundTag tag = getTag();
        int energy = tag.getInt("Energy");
        int energyExtracted = Math.min(energy, maxExtract);

        if (!simulate) {
            tag.putInt("Energy", energy - energyExtracted);
        }

        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return getTag().getInt("Energy");
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
