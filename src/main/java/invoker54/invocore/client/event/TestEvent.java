package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.ClientUtil;
import invoker54.invocore.client.TextUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestEvent {

//    public static void test(){
//        OverlayRegistry.registerOverlayTop("fallen_single_player_screen", (gui, stack, partialTicks, width, height) -> {
//            ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
////            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1,1,1, 123).getRGB());
////            ClientUtil.blitItem(stack, 0, 40, 0, 40, new ItemStack(Items.GOLDEN_APPLE));
////            ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitColor(stack,(width/2F) - 1, 2, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,0, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,width - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//            ClientUtil.blitColor(stack,width/4F, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,((width/4F) + width/2F) - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//            MutableComponent txt = new TextComponent("Don't you understand that I am trying to help you?");
//            TextUtil.renderText(stack, txt, 3,true,
//                    width/4F, width/4F, height/5F, height/8F,0, TextUtil.txtAlignment.LEFT);
//            ClientUtil.blitColor(stack, width/4F, width/4F, height/5F, height/8F, new Color(0, 84, 7, 158).getRGB());
//
//        });
//    }
}