package dev.hail.create_simple_generator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class CSGClient {
    public CSGClient(IEventBus modEventBus) {
        StressGeneratorCoilModel.init();
        modEventBus.addListener(CSGClient::init);
    }

    public static void init(final FMLClientSetupEvent event) {
    }
}
