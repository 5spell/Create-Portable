package net.vspell.createportable;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.vspell.createportable.block.ModBlockEntities;
import net.vspell.createportable.block.ModBlocks;
import net.vspell.createportable.component.ModComponents;
import net.vspell.createportable.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CreatePortable.MOD_ID)
public class CreatePortable {
    public static final String MOD_ID = "createportable";
    public static final String NAME = Create.NAME + ": Portable";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreatePortable(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        REGISTRATE.registerEventListeners(modEventBus);
        registerEverything(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void registerEverything(IEventBus modEventBus){
        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModComponents.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
