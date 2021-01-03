package xyz.przemyk.propellerhats.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemEnergyStorage implements IEnergyStorage {

    private final ItemStack itemStack;
    private final int capacity;

    public ItemEnergyStorage(ItemStack itemStack, int capacity) {
        this.itemStack = itemStack;
        this.capacity = capacity;
    }

    private CompoundNBT getTag() {
        if (!itemStack.hasTag()) {
            CompoundNBT tag = new CompoundNBT();
            itemStack.setTag(tag);
        }
        return itemStack.getTag();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        CompoundNBT tag = getTag();
        int energy = tag.getInt("Energy");
        int energyReceived = Math.min(capacity - energy, maxReceive);

        if (!simulate) {
            tag.putInt("Energy", energy + energyReceived);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        CompoundNBT tag = getTag();
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
