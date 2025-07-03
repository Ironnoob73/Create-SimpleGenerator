package dev.hail.create_simple_generator;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.catnip.config.ui.ConfigScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateSimpleGenerator.MODID)
public class CreateSimpleGenerator {
    public static final String MODID = "create_simple_generator";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    @SuppressWarnings("removal")
    public CreateSimpleGenerator() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC_S);

        REGISTRATE.registerEventListeners(modEventBus);

        modEventBus.addListener(this::commonSetup);
        REGISTRATE.setCreativeTab(CSGCreativeTab.TAB);
        CSGContents.init();
        CSGCreativeTab.init(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new CSGClient(modEventBus));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            BaseConfigScreen.setDefaultActionFor(CreateSimpleGenerator.MODID, screen -> screen
                    .withButtonLabels(null, null, "Gameplay Settings")
                    .withSpecs(null, null, Config.SPEC_S));
        }
    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
