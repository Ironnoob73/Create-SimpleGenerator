package dev.hail.create_simple_generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSGCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateSimpleGenerator.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("create_simple_generator_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_simple_generator"))
            .icon(CSGContents.STRESS_GENERATOR_BLOCK::asStack)
            .displayItems((parameters, output) -> output.accept(CSGContents.STRESS_GENERATOR_BLOCK))
            .build());

    public static void init(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }

}
