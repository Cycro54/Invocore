package invoker54.invocore;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Invocore.MOD_ID)
public class Invocore {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "invocore";

    public Invocore(IEventBus modEventBus, ModContainer modContainer) {
        // Register ourselves for server and other game events we are interested in
//        NeoForge.EVENT_BUS.register(this);
//        modEventBus.addListener();
//        RenderType type;
//        type.pipeline();
//        MultiBufferSource.BufferSource multiBufferSource;
//        multiBufferSource.getBuffer()
//        MultiBufferSource.BufferSource bufferSource;
//        bufferSource.endLastBatch();
    }
}
