package dev.hail.create_simple_generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class StressGeneratorRenderer extends KineticBlockEntityRenderer<StressGeneratorEntity> {
    public StressGeneratorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}
