package dev.hail.create_simple_generator;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;

import static dev.hail.create_simple_generator.CreateSimpleGenerator.REGISTRATE;

public class CSGContents {
    public static final BlockEntry<StressGeneratorBlock> STRESS_GENERATOR_BLOCK =
            REGISTRATE.block("stress_generator", StressGeneratorBlock::new)
            .initialProperties(SharedProperties::stone)
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> Config.generatorConsumptionStressMultiplier))
            .simpleItem()
            .register();
    public static final BlockEntityEntry<StressGeneratorEntity> STRESS_GENERATOR_ENTITY = REGISTRATE
            .blockEntity("stress_generator", StressGeneratorEntity::new)
            .visual(() -> StressGeneratorVisual::new, true)
            .validBlocks(STRESS_GENERATOR_BLOCK)
            .renderer(() -> StressGeneratorRenderer::new)
            .register();
    public static void init(){}
}
