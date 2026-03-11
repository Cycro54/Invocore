package invoker54.invocore.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import invoker54.invocore.client.invoimage.ImageOperation;
import invoker54.invocore.client.invoimage.InvoImage;
import invoker54.invocore.client.invoimage.InvoImageColor;
import invoker54.invocore.client.invoimage.InvoImageTexture;
import invoker54.invocore.client.util.*;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import invoker54.invocore.common.util.ResourceUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Vector2f;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestEventCommon implements ClientGuiEvent.RenderHud {
    public static final ModLogger LOGGER = ModLogger.getLogger(TestEventCommon.class, new AtomicBoolean(true));
    private static float aFloat = 0;
    private static float bFloat = 0;
    private static InvoImageTexture sprite;

    @Override
    public void renderHud(GuiGraphics guiGraphics, float tickDelta) {
        if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) aFloat += tickDelta / 2;
        if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) bFloat += tickDelta;
        float resulta = (float) (Math.sin(aFloat / 16f) + 1) / 2F;
        float resultb = (float) (Math.cos(bFloat / 32f) + 1) / 2F;
        float result2 = Mth.lerp(resulta, 4, 32);
        PoseStack stack = guiGraphics.pose();
        float width = guiGraphics.guiWidth();
        float height = guiGraphics.guiHeight();
        InvoZone colorZone = new InvoZone(0, width, 0, height);
        ClientUtil.blitColor(stack, colorZone, new Color(94, 94, 94, 142).getRGB());
            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1, 1, 1, 123).getRGB());
        ClientUtil.blitItem(stack, 180, -64, 16, 64, new ItemStack(Items.ITEM_FRAME));
        ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
        ClientUtil.blitItem(stack, 116, result2, Mth.lerp(resulta, 0, height / 2F), result2, new ItemStack(Items.GOLD_BLOCK));
        ClientUtil.blitColor(stack, (width / 2F) - 1, 2, 0, height, new Color(1, 1, 1, 255).getRGB());
        ClientUtil.blitColor(stack, 0, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
        ClientUtil.blitColor(stack, width - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());

        InvoZone paddedZone = new InvoZone(142.33334f, 142.33333f, 72.0f, 22.5f, false);

//        String s = "Lorem &lipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget metus ultrices, interdum neque non, sodales diam. Morbi hendrerit urna lorem, sed faucibus nisl venenatis a. Cras rutrum felis accumsan lacinia tempus. Nunc egestas, magna ac sagittis vehicula, neque quam ultricies augue, ut gravida quam ipsum at est.";
//      String s = "Lorem&l ipsum dolor sit amet, consectetur adipiscing elit. ac sagittis vehicula, neque quam ultricies augue, ut gravida quam ipsum at est.";
//        String s = "dsadakjdnkjadhkjasdafjn  ";
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa    ";
//        String s = "dsadakjdnkjadhkjasdafja   ";
        InvoImage.fromColor(Color.BLACK).render(stack, paddedZone);
        guiGraphics.drawWordWrap(ClientUtil.getFont(), InvoText.literal(s).getText(false),
                (int) paddedZone.x(), (int) paddedZone.y(), (int) paddedZone.width(), new Color(61, 135, 135, 233).getRGB());

//        InvoImage.fromColor(Color.RED).render(stack, paddedZone.copy().setWidth(30).setHeight(10));
        InvoText text = new InvoText.Properties().setTxtAlignment(TextUtil.TextAlign.BOT_LEFT)
                .setShadow(false).setTextSize(9).text(InvoText.literal(s));

        TextViewer textViewer = text.getTextViewer(true, paddedZone);
        InvoZone textZone = textViewer.getZoneCopy();
        InvoImage.fromColor(new Color(255, 0, 0, 86)).render(stack, textZone);
//        text.renderWithActualZone(stack, paddedZone, textZone.setDown(paddedZone.down()), true);
        InvoZone pointZone = InvoZone.fromPoint(new Vector2f(MathUtil.lerp(resultb, textZone.x()-1, textZone.right()+1),
//        InvoZone pointZone = InvoZone.fromPoint(new Vector2f(textZone.right(),
                textViewer.getLineY(1))).inflate(0.5f);
//                MathUtil.lerp(resulta, textZone.y(), textZone.down()))).inflate(1);
//        MathUtil.lerp(resulta, textZone.y(), textZone.down())
        InvoImage.fromColor(Color.green).render(stack, textViewer.getTextZone(textViewer.getMaxDisplayIndex()).setWidth(29));
        List<InvoZone> zoneList = textViewer.getTextZones(0, textViewer.getDisplayIndex(pointZone.middleX(), pointZone.middleY()));
//                            if (ClientUtil.getWorld().getGameTime() % 40 == 0) {
//                                LOGGER.error("What's index: " + textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//                            }
        text.render(stack, textViewer.getZoneCopy(), true);
//        if (ClientUtil.getWorld().getGameTime() % 10 == 0) {
//            LOGGER.error("What's index? " + textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//        }
//        InvoImage.fromColor(Color.green).render(stack, textViewer.getTextZone(textViewer.getMaxIndex()).setWidth(29));

        zoneList.forEach(zone -> {
                    InvoImage.fromColor(new Color(244, 84, 244, 121)).render(stack, zone.minWidth(2));

//                    if (ClientUtil.getWorld().getGameTime() % 10 == 0) {
//                    }
                }
        );
        InvoImage.fromColor(Color.BLUE).render(stack, pointZone);

//        text.render(stack, paddedZone.copy().setX(paddedZone.right()));
//        if (ClientUtil.getWorld().getGameTime() % 10 == 0){
//            LOGGER.error("list size: " + textViewer.textList().size());
//            textViewer.textList().forEach(thing ->{
//                LOGGER.error("is empty? " + thing.getString().isEmpty() + " : string: '" + thing.getString()+"'");
//            });
//            LOGGER.error("index: " + textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//            LOGGER.error("Text length: " + s.length());
//        }
//        new InvoText.Properties().setShadow(false).setTxtAlignment(TextUtil.TextAlign.MID).setShadow(false).setTextSize(8)
//                .text(InvoText.literal(s)).render(stack, paddedZone);

//        LOGGER.warn("what's width: " + ClientUtil.getFont().width("blah blah"));

        InvoImageTexture texture = InvoImage.fromTexture(ResourceUtil.create("dirt"));
        texture.setPivot(texture.getMainZoneCopy().middle());
        texture.setMainZone(texture.getMainZoneCopy().setXY(new Vector2f(180, 180)),true);
        texture.setTintColor(new Color((int) MathUtil.clampPingPong(MathUtil.lerp(resulta, 0, 400), 0, 255), 52, 255, 155));
        texture.setSubImageZone(texture.getSubImageZoneCopy().inflate(-3).shiftXY(3,-3));
        texture.setOuterOperation(ImageOperation.TILE);
        InvoZone pointZonething = InvoZone.fromPoint(texture.getMainZoneCopy().middle().add(40,0)).shiftXY(0, MathUtil.lerp(resulta, -30, 30));

        InvoImage.fromColor(Color.RED).render(stack, pointZonething);

        texture.setRotation((float) MathUtil.lookRotation(texture.getMainZoneCopy().middle(), pointZonething.middle()));
        texture.setImageZone(texture.getImageZoneCopy().shiftXY(-aFloat,0));
        texture.render(stack, texture.getMainZoneCopy().inflate(20));

        InvoImageColor colorBlack = InvoImage.fromColor(Color.white);
        colorBlack.setMainZone(InvoZone.fromPoint(texture.getMainZoneCopy().middle()).shiftXY(1,1).
                setWidth(pointZonething.middle().distance(texture.getMainZoneCopy().middle())).setHeight(1),false);
        colorBlack.setPivot(colorBlack.getMainZoneCopy().topLeft().lerp(colorBlack.getMainZoneCopy().bottomLeft(), 0.5F));
        colorBlack.setRotation((float) MathUtil.lookRotation(colorBlack.getPivotPoint(), pointZonething.middle()));
        colorBlack.render(stack);
    }
}