package dev.hail.create_simple_generator;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = CreateSimpleGenerator.MODID, dist = Dist.CLIENT)
public class CSGClient {
    public CSGClient(IEventBus modEventBus) {
        StressGeneratorCoilModel.init();
        modEventBus.addListener(CSGClient::init);
    }

    public static void init(final FMLClientSetupEvent event) {
    }
}
