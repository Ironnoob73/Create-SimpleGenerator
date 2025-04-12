package dev.hail.create_simple_generator;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CreateSimpleGenerator.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER_S = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue GENERATOR_GENERATES_ENERGY_MULTIPLIER = BUILDER_S
            .defineInRange("generator_generates_energy_multiplier", 1D, 0D, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue GENERATOR_CONSUMPTION_STRESS_MULTIPLIER = BUILDER_S
            .defineInRange("generator_consumption_stress_multiplier", 1D, 0D, Double.MAX_VALUE);
    static final ModConfigSpec SPEC_S = BUILDER_S.build();

    public static double generatorGeneratesEnergyMultiplier;
    public static double generatorConsumptionStressMultiplier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event)
    {
        if (event.getConfig().getSpec() == SPEC_S) {
            generatorGeneratesEnergyMultiplier = GENERATOR_GENERATES_ENERGY_MULTIPLIER.get();
            generatorConsumptionStressMultiplier = GENERATOR_CONSUMPTION_STRESS_MULTIPLIER.get();
        }
    }
}
