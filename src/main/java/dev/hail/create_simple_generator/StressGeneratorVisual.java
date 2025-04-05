package dev.hail.create_simple_generator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

import static dev.hail.create_simple_generator.CreateSimpleGenerator.MODID;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class StressGeneratorVisual extends KineticBlockEntityVisual<StressGeneratorEntity> {
    protected final RotatingInstance shaft;
    protected final RotatingInstance coil;
    final Direction direction;
    private final Direction opposite;

    public StressGeneratorVisual(VisualizationContext context, StressGeneratorEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();
        shaft = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();
        coil = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, Models.partial(StressGeneratorCoilModel.STRESS_GENERATOR_COIL))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();

        coil.setup(blockEntity, getFanSpeed())
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();
    }

    private float getFanSpeed() {
        float speed = blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update(float pt) {
        shaft.setup(blockEntity)
                .setChanged();
        coil.setup(blockEntity, getFanSpeed())
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, coil);
    }

    @Override
    protected void _delete() {
        shaft.delete();
        coil.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(coil);
    }
}
