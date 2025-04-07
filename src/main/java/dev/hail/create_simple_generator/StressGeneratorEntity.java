package dev.hail.create_simple_generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class StressGeneratorEntity extends KineticBlockEntity{
    public StressGeneratorEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
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
            energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(face), face);
            itemStorage = HopperBlockEntity.getContainerAt(level, getBlockPos().relative(face));
            singleItemStorage = level.getCapability(Capabilities.ItemHandler.BLOCK, getBlockPos().relative(face), face);
        }
        justExtracted = extractPowerTo(energyStorage);
        if (itemStorage != null && !justExtracted) {
            for (int i = 0; i < itemStorage.getContainerSize(); i++ ){
                energyStorage = itemStorage.getItem(i).getCapability(Capabilities.EnergyStorage.ITEM);
                justExtracted = extractPowerTo(energyStorage);
                if (justExtracted){
                    return;
                }
            }
        }
        if (singleItemStorage != null && !justExtracted){
            for (int i = 0; i < singleItemStorage.getSlots(); i++ ){
                energyStorage = singleItemStorage.getStackInSlot(i).getCapability(Capabilities.EnergyStorage.ITEM);
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
}
