package dev.hail.create_simple_generator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class StressGeneratorVisual extends KineticBlockEntityVisual<StressGeneratorEntity> implements SimpleDynamicVisual {
    protected final RotatingInstance shaft;
    public StressGeneratorVisual(VisualizationContext context, StressGeneratorEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        var axis = rotationAxis();
        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT)).createInstance();
        shaft.setup(blockEntity).setPosition(getVisualPosition()).rotateToFace(axis).setChanged();
    }

    @Override
    public void beginFrame(Context ctx) {

    }
    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
    }
    @Override
    public void updateLight(float partialTick) {
        relight(shaft);
    }
    @Override
    protected void _delete() {
        shaft.delete();
    }
}
