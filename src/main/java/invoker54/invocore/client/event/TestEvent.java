//package invoker54.invocore.client.event;
//
//import invoker54.invocore.Invocore;
//import invoker54.invocore.client.util.ClientUtil;
//import invoker54.invocore.client.util.InvoText;
//import invoker54.invocore.client.util.InvoZone;
//import invoker54.invocore.client.util.TextUtil;
//import invoker54.invocore.common.util.MathUtil;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.gui.OverlayRegistry;
//import net.minecraftforge.fml.common.Mod;
//
//import java.awt.*;
//import java.util.Locale;
//
//@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class TestEvent {
//    private static float aFloat = 0;
//    private static float bFloat = 0;
//
//    public static void test(){
//        OverlayRegistry.registerOverlayTop("fallen_single_player_screen", (gui, stack, partialTicks, width, height) -> {
//        aFloat += partialTicks;
//        bFloat += partialTicks;
//
//        float result = (float) (Math.sin(aFloat/32f)+1)/2F;
//        float result2 = MathUtil.lerp(result, 4,32);
//
//
//        ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//        ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1,1,1, 123).getRGB());
//        ClientUtil.blitItem(stack, 0, 64, 0, 64, new ItemStack(Items.GOLDEN_APPLE));
//        ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
//        ClientUtil.blitItem(stack, 116, result2, MathUtil.lerp(result, 0, height/2F), result2, new ItemStack(Items.GOLD_BLOCK));
//        ClientUtil.blitColor(stack, (width / 2F) - 1, 2, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, 0, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, width - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
//        ClientUtil.blitColor(stack, width / 4F, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, ((width / 4F) + width / 2F) - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
//        ClientUtil.blitColor(stack, width / 4F, 104.75F, height / 5F, 32, new Color(0, 84, 7, 158).getRGB());
//        InvoText txt = InvoText.literal("Finally, a worthy opponent. Our battle will be legendary, and your blood, a bath.")
//                .withStyle(true, ChatFormatting.RED);
//        InvoZone theZone = new InvoZone(width / 4F, 104.75F, height / 5F, 32);
//        ClientUtil.blitItem(stack, theZone.copy().shift(-theZone.width()/2F,0), new ItemStack(Items.GOLD_BLOCK));
//        TextUtil.renderText(stack, txt.getText(), true, 0, theZone, TextUtil.txtAlignment.MIDDLE);
//        ClientUtil.blitItem(stack, theZone.copy().shift(theZone.width()/2F,0), new ItemStack(Items.GOLD_BLOCK));
//        });
//    }
//}