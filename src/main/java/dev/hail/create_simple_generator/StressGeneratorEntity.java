package dev.hail.create_simple_generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StressGeneratorEntity extends KineticBlockEntity{
    private final EnergyStorage energyStorage = new EnergyStorage(0);
    protected List<Entity> caughtEntities = new ArrayList<>();
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
        IItemHandler itemHandler = null;
        boolean justExtracted;

        if (level != null) {
            energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(face), face.getOpposite());
            itemStorage = HopperBlockEntity.getContainerAt(level, getBlockPos().relative(face));
            itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, getBlockPos().relative(face), face.getOpposite());
        }
        justExtracted = extractPowerTo(energyStorage);
        if (justExtracted){
            return;
        }
        if (itemStorage != null && Config.canChargeItemInBlock && forItemInStorage(itemStorage)){
            return;
        }
        if (itemHandler != null && Config.canChargeItemInBlock && forItemInStorage(itemHandler)){
            return;
        }
        if (Config.canChargeItemEntity || Config.canChargeItemInEntity){
            caughtEntities.clear();
            caughtEntities = Objects.requireNonNull(getLevel()).getEntities(
                    null, new AABB(getBlockPos().relative(face)).expandTowards(Vec3.atLowerCornerOf(face.getNormal()).scale(0)));
            for (Entity entity : caughtEntities) {
                if (entity instanceof ItemEntity itemEntity && Config.canChargeItemEntity) {
                    energyStorage = itemEntity.getItem().getCapability(Capabilities.EnergyStorage.ITEM);
                    justExtracted = extractPowerTo(energyStorage);
                    if (justExtracted) {
                        return;
                    }
                }
                if (Config.canChargeItemInEntity){
                    if (entity instanceof Player player){
                        itemStorage = player.getInventory();
                        if (forItemInStorage(itemStorage)){
                            return;
                        }
                    }
                    itemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY);
                    if (itemHandler != null && forItemInStorage(itemHandler)){
                        return;
                    }
                }
            }
        }
    }

    public boolean forItemInStorage (IItemHandler itemHandler){
        boolean justExtracted;
        for (int i = 0; i < itemHandler.getSlots(); i++ ){
            IEnergyStorage energyStorage = itemHandler.getStackInSlot(i).getCapability(Capabilities.EnergyStorage.ITEM);
            justExtracted = extractPowerTo(energyStorage);
            if (justExtracted){
                return true;
            }
        }
        return false;
    }
    public boolean forItemInStorage (Container itemStorage){
        boolean justExtracted;
        for (int i = 0; i < itemStorage.getContainerSize(); i++ ){
            IEnergyStorage energyStorage = itemStorage.getItem(i).getCapability(Capabilities.EnergyStorage.ITEM);
            justExtracted = extractPowerTo(energyStorage);
            if (justExtracted){
                return true;
            }
        }
        return false;
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
