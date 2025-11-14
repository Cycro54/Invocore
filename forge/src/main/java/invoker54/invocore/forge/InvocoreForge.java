package invoker54.invocore.forge;

import dev.architectury.platform.forge.EventBuses;
import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Invocore.MOD_ID)
public final class InvocoreForge {
    public static ModLogger LOGGER = ModLogger.getLogger(InvocoreForge.class, Invocore.debugMode);
    public static IEventBus bus;

    public InvocoreForge() {
        bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::initializeClient);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Invocore.MOD_ID, bus);

        // Run our common setup.
        Invocore.commonInit();
    }

    public void initializeClient(FMLClientSetupEvent event) {
        Invocore.clientInit();
    }
}