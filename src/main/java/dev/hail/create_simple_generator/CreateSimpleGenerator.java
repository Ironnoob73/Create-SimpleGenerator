package dev.hail.create_simple_generator;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(CreateSimpleGenerator.MODID)
public class CreateSimpleGenerator {
    public static final String MODID = "create_simple_generator";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final BlockEntry<StressGeneratorBlock> STRESS_GENERATOR_BLOCK = REGISTRATE.block("stress_generator", StressGeneratorBlock::new)
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> Config.generatorConsumptionStressMultiplier))
            .initialProperties(SharedProperties::stone)
            .register();
    public static final BlockEntityEntry<StressGeneratorEntity> STRESS_GENERATOR_ENTITY = REGISTRATE
            .blockEntity("stress_generator", StressGeneratorEntity::new)
            .visual(() -> StressGeneratorVisual::new, false)
            .validBlocks(STRESS_GENERATOR_BLOCK)
            .renderer(() -> StressGeneratorRenderer::new)
            .register();
    public static final DeferredItem<BlockItem> STRESS_GENERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("stress_generator", STRESS_GENERATOR_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("create_simple_generator_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_simple_generator"))
            .icon(() -> STRESS_GENERATOR_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> output.accept(STRESS_GENERATOR_BLOCK_ITEM.get())).build());

    public CreateSimpleGenerator(IEventBus modEventBus)
    {
        modEventBus.addListener(this::commonSetup);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        REGISTRATE.registerEventListeners(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::registerCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, Config.SPEC_S);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
            StressGeneratorCoilModel.init();
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                STRESS_GENERATOR_ENTITY.get(),
                (blockEntity,side)-> blockEntity.getEnergyStored()
        );
    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
