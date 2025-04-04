package dev.hail.create_simple_generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class StressGeneratorEntity extends KineticBlockEntity{
    public StressGeneratorEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @Override
    public void tick() {
        super.tick();
        Direction face = getBlockState().getValue(StressGeneratorBlock.FACING);
        IEnergyStorage energyStorage = null;
        if (level != null) {
            energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(face), face);
        }
        if (energyStorage == null) {
            CreateSimpleGenerator.LOGGER.debug("NULL STORAGE");
            return;
        }
        energyStorage.receiveEnergy((int) Math.abs(getSpeed()), false);
    }
    public void blockInFrontChanged() {
    }
}
