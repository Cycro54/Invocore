package invoker54.invocore;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Invocore.MOD_ID)
public class Invocore {

    // Directly reference a log4j logger.
    public static final String MOD_ID = "invocore";

    public Invocore() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
