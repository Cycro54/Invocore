package invoker54.invocore.client;

import invoker54.invocore.Invocore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID)
public class Ticker {
    private static long ticksInGame = 0;
    private static float partialTicks = 0;
    private static float delta = 0;
    private static float total = 0;
    private static final Logger LOGGER = LogManager.getLogger();

    private static void calcDelta() {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    protected static void renderTick(EntityTickEvent.Pre event) {
            partialTicks = ClientUtil.getMinecraft().getTimer().getGameTimeDeltaPartialTick(true);
    }

    @SubscribeEvent
    protected static void clientTickEnd(ClientTickEvent.Post event) {
        ticksInGame++;
        partialTicks = 0;

        calcDelta();
    }

    //returns how much actual time has passed (in ticks)
    public static float getDelta(boolean canPause, boolean inTicks){
        return (canPause ? (ClientUtil.getMinecraft().isPaused() ? 0 : delta) : delta)/(inTicks ? 1 : 20);
    }
}
