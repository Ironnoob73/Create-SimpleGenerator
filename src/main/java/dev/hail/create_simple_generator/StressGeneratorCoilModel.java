package dev.hail.create_simple_generator;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class StressGeneratorCoilModel {
    public static final PartialModel STRESS_GENERATOR_COIL = PartialModel.of(CreateSimpleGenerator.resourceLocation("block/stress_generator/coil"));
    public static void init() {}
}
