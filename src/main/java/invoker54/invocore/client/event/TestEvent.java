package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.ClientUtil;
import invoker54.invocore.client.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.Stack;

//@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class TestEvent {
//
//    @SubscribeEvent
//    public static void test(RegisterGuiOverlaysEvent event){
//        event.registerAboveAll("fallen_single_player_screen", (gui, stack, partialTicks, width, height) -> {
//            ClientUtil.blitColor(stack,(width/2F) - 1, 2, 0, height, new Color(1,1,1,255).getRGB());
//
//            TextUtil.renderText(stack, Component.literal("AA"), false,
//                    width/2F, width/2F, 0, height,0, TextUtil.txtAlignment.MIDDLE);
//
//        });
//    }
//}
