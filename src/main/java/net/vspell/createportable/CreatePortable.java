package net.vspell.createportable;

import com.simibubi.create.Create;
import com.tterrag.registrate.Registrate;
import net.vspell.createportable.component.ModComponents;
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

    public static final Registrate REGISTRATE = Registrate.create(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreatePortable(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        registerEverything(modEventBus);


        modEventBus.addListener(this::commonSetup);
    }

    private void registerEverything(IEventBus modEventBus){
        ModComponents.register(modEventBus);
        ModItems.register();
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
