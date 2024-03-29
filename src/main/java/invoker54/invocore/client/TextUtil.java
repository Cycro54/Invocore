package invoker54.invocore.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static invoker54.invocore.client.ClientUtil.mC;

public class TextUtil {
    private static int black = new Color(0,0,0, 255).getRGB();
    private static final Logger LOGGER = LogManager.getLogger();

    public enum txtAlignment{
        LEFT,
        MIDDLE,
        RIGHT
    }

    public static void renderText(MatrixStack stack, ITextComponent text, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        java.util.List<ITextComponent> list = new ArrayList<>();
        list.add(text);
        renderText(stack, list, shadow, x0, maxWidth, y0, maxHeight, padding, align);
    }
    public static void renderText(MatrixStack stack, List<ITextComponent> textLines, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        FontRenderer font = mC.fontRenderer;

        stack.push();

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();

        float maxTxtHeight = textLines.size() * (7 + 1);
        maxTxtHeight -= 1;
//        LOGGER.info("Max Text Height is " + maxTxtHeight);
        maxTxtHeight += (padding * 2);
//        LOGGER.info("After padding it is " + maxTxtHeight);

        float maxTxtWidth = 0;
        for (ITextComponent textComponent : textLines){
            int currentWidth = font.getStringWidth(textComponent.getString());
            if (currentWidth > maxTxtWidth){
                maxTxtWidth = currentWidth;
            }
        }
//        LOGGER.info("Max Text Width is " + maxTxtWidth);
        maxTxtWidth += (padding * 2);
//        LOGGER.info("After padding it is " + maxTxtWidth);

        if (shadow){
            maxTxtHeight += 1;
            maxTxtWidth += 1;
        }

        float heightLeft = maxHeight - maxTxtHeight;
//        LOGGER.info("Height Left is " + heightLeft);
        float widthLeft = maxWidth - maxTxtWidth;
//        LOGGER.info("Width Left is " + widthLeft);
        float scaleFactor = 0;


        if (heightLeft < widthLeft || heightLeft == widthLeft) {
//                LOGGER.info("HeightLeft was smaller than WidthLeft");
            //example: maxHeight is 70, txtMaxHeight is 60.
            //That means maxHeight is 1.16 times larger than the txtMaxHeight
            scaleFactor = (maxHeight / maxTxtHeight);
        } else if (heightLeft > widthLeft) {
//                LOGGER.info("WidthLeft was smaller than HeightLeft");
            //example: maxWidth is 50, txtMaxWidth is 25.
            //That means maxWidth is 2 times larger than the txtMaxWidth
            scaleFactor = (maxWidth / maxTxtWidth);
        }
//        LOGGER.debug("What's the scale factor? " + scaleFactor);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);

        //Since I changed the Scale of the text, I have to recalculate the maxTxtHeight and maxTxtWidth
//        maxTxtHeight = (maxTxtHeight/scaleFactor);

        for (int a = 0; a < textLines.size(); ++a){
            ITextComponent currText = textLines.get(a);

            float y = y0/scaleFactor;
            y = y + ((((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.FONT_HEIGHT * scaleFactor))/scaleFactor);
//            LOGGER.debug("max height is: "+ maxHeight);
//            LOGGER.debug("base empty space is: " + (maxHeight - (maxTxtHeight * scaleFactor)));
//            LOGGER.debug("resulting y spot is: " + (((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.lineHeight * scaleFactor)));

//            float y = y0 + (a * font.lineHeight) + (padding + (padding * a));
//            y += ((maxHeight - maxTxtHeight)/2F);
//            y = (y/scaleFactor);

            float x = x0/scaleFactor;
//            LOGGER.debug("What's x0: " + x);
            switch (align){
                case LEFT:
                    x = (x + padding)/scaleFactor;
                    break;
                case MIDDLE:
                    x = x + (((maxWidth - ((font.getStringPropertyWidth(currText) - 1) * scaleFactor))/2F)/scaleFactor);
//                    LOGGER.debug("Max Width: " + (maxWidth));
//                    LOGGER.debug("Font Width is now: " + (font.getStringWidth(currText) * scaleFactor));
//                    LOGGER.debug("What's the empty space: " + ((maxWidth) - (font.getStringWidth(currText) * scaleFactor)));
//                    LOGGER.debug("Where will the top left be for the text: " + x);
                    break;
                case RIGHT:
                    x = (((x + maxWidth)/scaleFactor) - ((padding/scaleFactor) + (font.getStringPropertyWidth(currText) * scaleFactor)));
                    break;
            }

            renderText(currText, stack, x, y, shadow);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.enableRescaleNormal();

        stack.pop();
    }

    public static void renderText(ITextComponent text, MatrixStack stack, float x, float y, boolean shadow){
        RenderSystem.disableDepthTest();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

//        boolean flag = !player.isDiscrete();
//        float f = player.getBbHeight() * 0.5f;
        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
        Matrix4f matrix4f = stack.getLast().getMatrix();

        //This is the usual number, so let's keep it like that for now
        int lightCoords = 15728880;

        //float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int)(0 * 255.0F) << 24;
        FontRenderer fontrenderer = mC.fontRenderer;

        fontrenderer.func_243247_a(text, x, y, -1, shadow, matrix4f, irendertypebuffer$impl, true, 0, lightCoords);

//        fontrenderer.drawInBatch(text, x, y, -1, shadow, matrix4f, irendertypebuffer$impl, true, j, lightCoords);
//        if (flag) {
//            fontrenderer.drawInBatch(text.getVisualOrderText(), x, y, -1, shadow, matrix4f, irendertypebuffer$impl, false, 0, lightcoords);
//        }

        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
    }
}
