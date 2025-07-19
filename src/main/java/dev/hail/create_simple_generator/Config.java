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
    private static final ModConfigSpec.BooleanValue CAN_CHARGE_ITEM_IN_BLOCK = BUILDER_S
            .define("can_charge_item_in_block", true);
    private static final ModConfigSpec.BooleanValue CAN_CHARGE_ITEM_ENTITY = BUILDER_S
            .define("can_charge_item_entity", true);
    private static final ModConfigSpec.BooleanValue CAN_CHARGE_ITEM_IN_ENTITY = BUILDER_S
            .define("can_charge_item_in_entity", true);
    static final ForgeConfigSpec SPEC_S = BUILDER_S.build();

    public static double generatorGeneratesEnergyMultiplier;
    public static double generatorConsumptionStressMultiplier;
    public static boolean canChargeItemInBlock;
    public static boolean canChargeItemEntity;
    public static boolean canChargeItemInEntity;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event)
    {
        if (event.getConfig().getSpec() == SPEC_S) {
            generatorGeneratesEnergyMultiplier = GENERATOR_GENERATES_ENERGY_MULTIPLIER.get();
            generatorConsumptionStressMultiplier = GENERATOR_CONSUMPTION_STRESS_MULTIPLIER.get();
            canChargeItemInBlock = CAN_CHARGE_ITEM_IN_BLOCK.get();
            canChargeItemEntity = CAN_CHARGE_ITEM_ENTITY.get();
            canChargeItemInEntity = CAN_CHARGE_ITEM_IN_ENTITY.get();
        }
    }
}