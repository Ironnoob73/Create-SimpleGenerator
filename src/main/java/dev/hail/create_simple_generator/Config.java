package dev.hail.create_simple_generator;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER_S = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue GENERATOR_GENERATES_ENERGY_MULTIPLIER = BUILDER_S
            .defineInRange("generator_generates_energy_multiplier", 1D, 0D, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue GENERATOR_CONSUMPTION_STRESS_MULTIPLIER = BUILDER_S
            .defineInRange("generator_consumption_stress_multiplier", 1D, 0D, Double.MAX_VALUE);
    static final ForgeConfigSpec SPEC_S = BUILDER_S.build();

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