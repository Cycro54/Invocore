package invoker54.invocore.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static invoker54.invocore.client.ClientUtil.mC;

public class TextUtil {
    private static int black = new java.awt.Color(0,0,0, 255).getRGB();
    private static final Logger LOGGER = LogManager.getLogger();

    public enum txtAlignment{
        LEFT,
        MIDDLE,
        RIGHT
    }


    //deprecated
    public static void renderText(MatrixStack stack, IFormattableTextComponent text, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        renderText(stack, text, 0, shadow, x0, maxWidth, y0, maxHeight, padding, align);
    }
    public static void renderText(MatrixStack stack, IFormattableTextComponent text, int maxSplits, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        java.util.List<ITextProperties> list = new ArrayList<>();
        //I took this from the if statement
        // && maxHeight > mC.font.lineHeight
        if (mC.fontRenderer.getStringPropertyWidth(text) > maxWidth) {
            //Let's try this again.
            //I have to make it so the text fits PERFECTLY inside the space provided.
            //What that means is, I have to cut the text at the correct spots.

            //First grab the X Y ratio for the space
            double spaceRatio = maxWidth / maxHeight;
            //Since Y has to be multiples of 9, make the ratio a multiple of 9
            spaceRatio *= 9;

            //Grab the textArea we will be working with
            double textArea = 9 * mC.fontRenderer.getStringPropertyWidth(text);
            //Do the formula u got from mathSolver to get the multiplier that I can use on the spaceRatio
            double multiplier = textArea / (spaceRatio * 9);
            multiplier = Math.sqrt(multiplier);
            //and FINALLY, multiply spaceRatio with the multiplier, and that should be the cutoff point!
            int cutoffPoint = (int) Math.round(multiplier * spaceRatio);

            list.addAll(mC.fontRenderer.getCharacterManager().func_238362_b_(text, cutoffPoint, Style.EMPTY));

            if (maxSplits != 0 && list.size() > maxSplits) {
                list.clear();

                list.addAll(mC.fontRenderer.getCharacterManager().func_238362_b_(text,
                        (int) (Math.ceil((double) mC.fontRenderer.getStringPropertyWidth(text) / maxSplits)), Style.EMPTY));
                if (list.size() > maxSplits) {
                    ITextProperties part1 = list.get(list.size() - 2);
                    ITextProperties part2 = list.get(list.size() - 1);

                    list.remove(part1);
                    list.remove(part2);

                    list.add(ITextProperties.func_240655_a_(part1, ITextProperties.func_240652_a_(" "),
                            part2));
                }
            }

        }
        else {
            list.add(text);
        }
        renderText(stack, list, shadow, x0, maxWidth, y0, maxHeight, padding, align);
    }
    public static void renderText(MatrixStack stack, List<ITextProperties> textLines, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        FontRenderer font = mC.fontRenderer;

        stack.push();

        float maxTxtHeight = textLines.size() * (7 + 1);
        maxTxtHeight += -2 + textLines.size();
//        LOGGER.info("Max Text Height is " + maxTxtHeight);
//        maxTxtHeight += (padding * 2);
//        LOGGER.info("After padding it is " + maxTxtHeight);

        float maxTxtWidth = 0;
        ITextProperties largestComponent = textLines.get(0);
        for (ITextProperties textComponent : textLines){
            int currentWidth = font.getStringPropertyWidth(textComponent);
            if (currentWidth > maxTxtWidth){
                maxTxtWidth = currentWidth;
                largestComponent = textComponent;
            }
        }
//        LOGGER.info("Max Text Width is " + maxTxtWidth);
//        maxTxtWidth += (padding * 2);
        //There is 1 blank space in front of the last character, this will remove that.
        maxTxtWidth -= 1;
//        LOGGER.info("After padding it is " + maxTxtWidth);

        float shadowOffset = 0;
        if (shadow){
            shadowOffset = 1;
//            LOGGER.debug("What is offset? " + offset);
            maxTxtHeight += shadowOffset;
            maxTxtWidth += shadowOffset;
        }

        float heightFillAmount = maxHeight/maxTxtHeight;
//        LOGGER.info("Height Left is " + heightLeft);
        float widthFillAmount = maxWidth/maxTxtWidth;
//        LOGGER.info("Width Left is " + widthLeft);
        float scaleFactor = 0;

        if (heightFillAmount < widthFillAmount || heightFillAmount == widthFillAmount) {
//                LOGGER.info("heightFillAmount was smaller than widthFillAmount");
            //example: maxHeight is 70, txtMaxHeight is 60.
            //That means maxHeight is 1.16 times larger than the txtMaxHeight
            scaleFactor = ((maxHeight - (align == txtAlignment.MIDDLE ? (padding * 2) : padding)) / maxTxtHeight);
        } else if (heightFillAmount > widthFillAmount) {
//                LOGGER.info("widthFillAmount was smaller than heightFillAmount");
            //example: maxWidth is 50, txtMaxWidth is 25.
            //That means maxWidth is 2 times larger than the txtMaxWidth
            scaleFactor = ((maxWidth - (align == txtAlignment.MIDDLE ? (padding * 2) : padding)) / maxTxtWidth);
        }
//        LOGGER.debug("What's padding amount to remove? " + ((1F/maxWidth) * padding * 2));
//        scaleFactor -= ((1F/maxWidth) * padding * 2);
//        LOGGER.debug("What's the scale factor? " + scaleFactor);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);

        //Since I changed the Scale of the text, I have to recalculate the maxTxtHeight and maxTxtWidth
//        maxTxtHeight = (maxTxtHeight/scaleFactor);

        for (int a = 0; a < textLines.size(); ++a){
            ITextProperties currText = textLines.get(a);

            float y = y0/scaleFactor;
            y = y + ((((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.FONT_HEIGHT * scaleFactor))/scaleFactor);
//            LOGGER.debug("max height is: "+ maxHeight);
//            LOGGER.debug("base empty space is: " + (maxHeight - (maxTxtHeight * scaleFactor)));
//            LOGGER.debug("resulting y spot is: " + (((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.lineHeight * scaleFactor)));

            float x = x0;
            switch (align){
                case LEFT:
                    x = (x)/scaleFactor;
                    break;
                case MIDDLE:
                    x = ((x + ((maxWidth - ((font.getStringPropertyWidth(currText) - (1 - shadowOffset)) * scaleFactor))/2F))/scaleFactor);
//                    LOGGER.debug("Max Width: " + (maxWidth));
//                    LOGGER.debug("Font Width is now: " + (font.getStringWidth(currText) * scaleFactor));
//                    LOGGER.debug("What's the empty space: " + ((maxWidth) - (font.getStringWidth(currText) * scaleFactor)));
//                    LOGGER.debug("Where will the top left be for the text: " + x);
                    break;
                case RIGHT:
                    x = (((x + maxWidth)/scaleFactor) - (((padding) + ((font.getStringPropertyWidth(currText) - (1 - shadowOffset)) * scaleFactor))/scaleFactor));
                    break;
            }

            renderText(currText, stack, x, y, shadow);
        }

        stack.pop();
    }

    public static void renderText(ITextProperties text, MatrixStack stack, float x, float y, boolean shadow){
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
        RenderSystem.disableDepthTest();

        fontrenderer.drawEntityText(LanguageMap.getInstance().func_241870_a(text), x, y, -1, shadow, matrix4f, irendertypebuffer$impl, true, 0, lightCoords);

//        fontrenderer.drawInBatch(text, x, y, -1, shadow, matrix4f, irendertypebuffer$impl, true, j, lightCoords);
//        if (flag) {
//            fontrenderer.drawInBatch(text.getVisualOrderText(), x, y, -1, shadow, matrix4f, irendertypebuffer$impl, false, 0, lightcoords);
//        }

        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
    }
}
