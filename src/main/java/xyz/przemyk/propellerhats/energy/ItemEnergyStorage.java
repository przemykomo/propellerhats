package xyz.przemyk.propellerhats.energy;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import xyz.przemyk.propellerhats.PropHatsMod;

public record ItemEnergyStorage(ItemStack itemStack, int capacity) implements IEnergyStorage {

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = itemStack.getOrDefault(PropHatsMod.ENERGY, 0);
        int energyReceived = Math.min(capacity - energy, maxReceive);

        if (!simulate) {
            itemStack.set(PropHatsMod.ENERGY, energy + energyReceived);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = itemStack.getOrDefault(PropHatsMod.ENERGY, 0);
        int energyExtracted = Math.min(energy, maxExtract);

        if (!simulate) {
            itemStack.set(PropHatsMod.ENERGY, energy - energyExtracted);
        }

        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return itemStack.getOrDefault(PropHatsMod.ENERGY, 0);
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
