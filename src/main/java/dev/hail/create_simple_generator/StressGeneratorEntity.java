package dev.hail.create_simple_generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StressGeneratorEntity extends KineticBlockEntity{
    private final EnergyStorage energyStorage = new EnergyStorage(0);
    LazyOptional<IEnergyStorage> lazyHandler;
    public StressGeneratorEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY)
            return lazyHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        super.tick();
        if (getSpeed() == 0){
            return;
        }
        Direction face = getBlockState().getValue(StressGeneratorBlock.FACING);
        IEnergyStorage energyStorage = null;
        Container itemStorage = null;
        IItemHandler singleItemStorage = null;
        boolean justExtracted;

        if (level != null) {
            energyStorage = level.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).orElse(null);
            itemStorage = HopperBlockEntity.getContainerAt(level, getBlockPos().relative(face));
            singleItemStorage = level.getCapability(ForgeCapabilities.ITEM_HANDLER, face.getOpposite()).orElse(null);
        }
        justExtracted = extractPowerTo(energyStorage);
        if (itemStorage != null && !justExtracted) {
            for (int i = 0; i < itemStorage.getContainerSize(); i++ ){
                energyStorage = itemStorage.getItem(i).getCapability(ForgeCapabilities.ENERGY).orElse(null);
                justExtracted = extractPowerTo(energyStorage);
                if (justExtracted){
                    return;
                }
            }
        }
        if (singleItemStorage != null && !justExtracted){
            for (int i = 0; i < singleItemStorage.getSlots(); i++ ){
                energyStorage = singleItemStorage.getStackInSlot(i).getCapability(ForgeCapabilities.ENERGY).orElse(null);
                justExtracted = extractPowerTo(energyStorage);
                if (justExtracted){
                    return;
                }
            }
        }
    }

    public boolean extractPowerTo (IEnergyStorage energyStorage){
        if (energyStorage != null && (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored() || energyStorage.getMaxEnergyStored() == 0)){
            energyStorage.receiveEnergy((int) Math.abs(getSpeed() * Config.generatorGeneratesEnergyMultiplier), false);
            return true;
        }
        return false;
    }

    public EnergyStorage getEnergyStored() {
        return energyStorage;
    }
}
