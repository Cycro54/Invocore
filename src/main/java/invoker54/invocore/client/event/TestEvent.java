//package invoker54.invocore.client.event;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import invoker54.invocore.Invocore;
//import invoker54.invocore.client.util.ClientUtil;
//import invoker54.invocore.client.util.TextUtil;
//import invoker54.invocore.common.ModLogger;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.text.IFormattableTextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.awt.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT)
//public class TestEvent {
//    private static float aFloat = 0;
//    private static float bFloat = 0;
//    private static ModLogger LOGGER = ModLogger.getLogger(TestEvent.class, new AtomicBoolean(true));
//
//    @SubscribeEvent
//    public static void test(RenderGameOverlayEvent.Post event) {
//        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
////        if (!ClientUtil.mC.options.keyAttack.isDown()) return;
////        ClientUtil.mC.options.keyAttack.setDown(false);
//        MatrixStack stack = event.getMatrixStack();
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//        aFloat += event.getPartialTicks();
//        bFloat += event.getPartialTicks();
//
//        float result = (float) (Math.sin(aFloat/32f)+1)/2F;
//        float result2 = MathHelper.lerp(result, 4,32);
//
//
//        ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//        ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1,1,1, 123).getRGB());
//        ClientUtil.blitItem(stack, 0, 64, 0, 64, new ItemStack(Items.GOLDEN_APPLE));
//        ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
//        ClientUtil.blitItem(stack, 116, result2, MathHelper.lerp(result, 0, height/2F), result2, new ItemStack(Items.GOLD_BLOCK));
//        ClientUtil.blitColor(stack, (width / 2F) - 1, 2, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, 0, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, width - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
//        ClientUtil.blitColor(stack, width / 4F, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, ((width / 4F) + width / 2F) - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
//        ClientUtil.blitColor(stack, width / 4F, 104.75F, height / 5F, 32, new Color(0, 84, 7, 158).getRGB());
//        IFormattableTextComponent txt = new StringTextComponent("Finally, a worthy opponent. Our battle will be legendary, and your blood, a bath.")
//                .withStyle(TextFormatting.RED);
//        TextUtil.renderText(stack, txt, true, 0,
//                width / 4F, 104.75F, height / 5F, 32, 2, TextUtil.txtAlignment.MIDDLE);
//    }
//}