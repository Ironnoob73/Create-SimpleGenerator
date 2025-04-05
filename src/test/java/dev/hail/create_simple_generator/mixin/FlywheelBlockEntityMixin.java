package dev.hail.create_simple_generator.mixin;

import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlockEntity;
import dev.hail.create_simple_generator.CreateSimpleGenerator;
import dev.hail.create_simple_generator.StressGeneratorBlock;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(FlywheelBlockEntity.class)
public abstract class FlywheelBlockEntityMixin implements SelfGetter<FlywheelBlockEntity>{
    @Inject(method = "tick()V", at = @At("HEAD"))
    private void injected(CallbackInfo ci) {
        CreateSimpleGenerator.LOGGER.debug("DO");
        IEnergyStorage energyStorage = null;
        if (self().getLevel() != null) {
            energyStorage = Objects.requireNonNull(self().getLevel()).getCapability(Capabilities.EnergyStorage.BLOCK, self().getBlockPos().above(1),null);
        }
        if (energyStorage == null) {
            CreateSimpleGenerator.LOGGER.debug("NULL STORAGE");
            return;
        }
        energyStorage.receiveEnergy((int) Math.abs(self().getSpeed()), false);
    }
}
