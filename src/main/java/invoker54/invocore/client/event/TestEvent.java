package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.ClientUtil;
import invoker54.invocore.client.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.plaf.multi.MultiTableUI;
import java.awt.*;
import java.util.ArrayList;
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
//            MutableComponent txt = Component.literal("A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A " +
//                    "A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A " +
//                    "A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A A ");
//            TextUtil.renderText(stack, txt, true,
//                    width/4F, width/4F, height/5F, height/8F,0, TextUtil.txtAlignment.LEFT);
//            ClientUtil.blitColor(stack, width/4F, width/4F, height/5F, height/8F, new Color(0, 84, 7, 158).getRGB());
//
//        });
//    }
}