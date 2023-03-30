package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.ClientUtil;
import invoker54.invocore.client.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.Stack;

@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestEvent {

//    @SubscribeEvent
//    public static void test(RegisterGuiOverlaysEvent event){
//        event.registerAboveAll("fallen_single_player_screen", (gui, stack, partialTicks, width, height) -> {
//            ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//            ClientUtil.blitColor(stack,(width/2F) - 1, 2, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,0, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,width - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//            ClientUtil.blitColor(stack,width/4F, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,((width/4F) + width/2F) - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//            TextUtil.renderText(stack, Component.literal("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27").withStyle(ChatFormatting.BOLD), true,
//                    width/4F, width/2F, height/5F, height/5F,0, TextUtil.txtAlignment.MIDDLE);
//            ClientUtil.blitColor(stack, width/4F, width/2F, height/5F, height/5F, new Color(0, 84, 7, 158).getRGB());
//
//        });
//    }
}
