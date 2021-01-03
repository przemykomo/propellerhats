package xyz.przemyk.propellerhats.energy;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderEnergy implements ICapabilityProvider {

    protected final IEnergyStorage energyStorage;
    protected final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public CapabilityProviderEnergy(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
        this.energyStorageLazyOptional = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, energyStorageLazyOptional);
    }
}
