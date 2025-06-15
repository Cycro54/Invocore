package invoker54.invocore.client.util;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static invoker54.invocore.client.util.ClientUtil.getMinecraft;

public class TextUtil {
    private static int black = new java.awt.Color(0,0,0, 255).getRGB();
    //    private static final ModLogger LOGGER = ModLogger.getLogger(TextUtil.class, );
    private static CharacterManagerMixer characterMixer;

    public enum txtAlignment{
        LEFT,
        MIDDLE,
        RIGHT
    }


    //deprecated
    public static void renderText(PoseStack stack, Component text, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        renderText(stack, text, shadow, 0, x0, maxWidth, y0, maxHeight, padding, align);
    }

    public static void renderText(PoseStack stack, Component text, boolean shadow, int maxSplits,
                                  InvoZone renderZone, txtAlignment alignment){
        renderText(stack, text, shadow, maxSplits, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), 0, alignment);
    }
    public static void renderText(PoseStack stack, Component text, boolean shadow, int maxSplits,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align){
        if (characterMixer == null) characterMixer = new CharacterManagerMixer();

        java.util.List<FormattedText> list = new ArrayList<>();
        int txtWidth = ClientUtil.getFont().width(text);

        //I took this from the if statement
        // && maxHeight > mC.font.lineHeight
        if (maxSplits != 1) {
            //Let's try this again.
            //I have to make it so the text fits PERFECTLY inside the space provided.
            //What that means is, I have to cut the text at the correct spots.

            //First grab the X Y ratio for the space
            double spaceRatio = maxWidth / maxHeight;
            //Since Y has to be multiples of 9, make the ratio a multiple of 9
            spaceRatio *= 9;

            //Grab the textArea we will be working with
            double textArea = 9 * txtWidth;
            //Do the formula u got from mathSolver to get the multiplier that I can use on the spaceRatio
            double multiplier = textArea / (spaceRatio * 9);
            multiplier = Math.sqrt(multiplier);
            //and FINALLY, multiply spaceRatio with the multiplier, and that should be the cutoff point!
            double cutoffPoint = Math.ceil(multiplier * spaceRatio);
            double neededSplits = ((double) txtWidth /cutoffPoint);

            if (maxSplits != 0 && (neededSplits > maxSplits || maxSplits < 0 && Math.abs(maxSplits) > Math.ceil(neededSplits))){
                neededSplits = Math.abs(maxSplits);
            }

            characterMixer.splitLines(text, (int) (ClientUtil.getFont().width(text)/neededSplits), Style.EMPTY, (A, B) -> list.add(A));

        }
        else {
            list.add(text);
        }


        renderText(stack, list, shadow, x0, maxWidth, y0, maxHeight, padding, align);
    }
    public static void renderText(PoseStack stack, List<FormattedText> textLines, boolean shadow,
                                  InvoZone renderZone, txtAlignment alignment){
        renderText(stack, textLines, shadow, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), 0, alignment);
    }
    public static void renderText(PoseStack stack, List<FormattedText> textLines, boolean shadow,
                                  float x0, float maxWidth, float y0, float maxHeight, int padding, txtAlignment align) {
        Font font = ClientUtil.getFont();

        stack.pushPose();

        float maxTxtHeight = textLines.size() * (7 + 1);
        maxTxtHeight += -2 + textLines.size();
//        LOGGER.info("Max Text Height is " + maxTxtHeight);
//        maxTxtHeight += (padding * 2);
//        LOGGER.info("After padding it is " + maxTxtHeight);

        float maxTxtWidth = 0;
        FormattedText largestComponent = textLines.get(0);
        for (FormattedText textComponent : textLines) {
            int currentWidth = font.width(textComponent);
            if (currentWidth > maxTxtWidth) {
                maxTxtWidth = currentWidth;
                largestComponent = textComponent;
            }
        }
//        LOGGER.info("Max Text Width is " + maxTxtWidth);
//        maxTxtWidth += (padding * 2);
        //There is 1 blank space in front of the last c0, this will remove that.
        maxTxtWidth -= 1;
//        LOGGER.info("After padding it is " + maxTxtWidth);

        float shadowOffset = 0;
        if (shadow) {
            shadowOffset = 1;
//            LOGGER.debug("What is offset? " + offset);
            maxTxtHeight += shadowOffset;
            maxTxtWidth += shadowOffset;
        }

        float heightFillAmount = maxHeight / maxTxtHeight;
//        LOGGER.info("Height Left is " + heightLeft);
        float widthFillAmount = maxWidth / maxTxtWidth;
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

        for (int a = 0; a < textLines.size(); ++a) {
            FormattedText currText = textLines.get(a);

            float y = y0 / scaleFactor;
            y = y + ((((maxHeight - (maxTxtHeight * scaleFactor)) / 2F) + (a * font.lineHeight * scaleFactor)) / scaleFactor);
//            LOGGER.debug("max height is: "+ maxHeight);
//            LOGGER.debug("base empty space is: " + (maxHeight - (maxTxtHeight * scaleFactor)));
//            LOGGER.debug("resulting y spot is: " + (((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.lineHeight * scaleFactor)));

            float x = x0;
            switch (align) {
                case LEFT:
                    x = (x) / scaleFactor;
                    break;
                case MIDDLE:
                    x = ((x + ((maxWidth - ((font.width(currText) - (1 - shadowOffset)) * scaleFactor)) / 2F)) / scaleFactor);
//                    LOGGER.debug("Max Width: " + (maxWidth));
//                    LOGGER.debug("Font Width is now: " + (font.getStringWidth(currText) * scaleFactor));
//                    LOGGER.debug("What's the empty space: " + ((maxWidth) - (font.getStringWidth(currText) * scaleFactor)));
//                    LOGGER.debug("Where will the top left be for the text: " + x);
                    break;
                case RIGHT:
                    x = (((x + maxWidth) / scaleFactor) - (((padding) + ((font.width(currText) - (1 - shadowOffset)) * scaleFactor)) / scaleFactor));
                    break;
            }

            renderText(currText, stack, x, y, shadow);
        }

        stack.popPose();
    }

    public static void renderText(FormattedText text, PoseStack stack, float x, float y, boolean shadow){
        MultiBufferSource.BufferSource irendertypebuffer$impl = getMinecraft().renderBuffers().bufferSource();

//        boolean flag = !player.isDiscrete();
//        float f = player.getBbHeight() * 0.5f;
        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
        Matrix4f matrix4f = stack.last().pose();

        //This is the usual number, so let's keep it like that for now
        int lightCoords = 15728880;

        //float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int)(0 * 255.0F) << 24;
        Font fontrenderer = ClientUtil.getFont();

        RenderSystem.disableDepthTest();
        fontrenderer.drawInBatch(Language.getInstance().getVisualOrder(text), x, y, -1, shadow, matrix4f, irendertypebuffer$impl, Font.DisplayMode.SEE_THROUGH, 0, lightCoords);

        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
    }

    public static class CharacterManagerMixer extends StringSplitter {

        public CharacterManagerMixer() {
            super(ClientUtil.getFont().getSplitter().widthProvider);
        }

        public void splitLines(FormattedText text, int cutoffPoint, Style p_style, BiConsumer<FormattedText, Boolean> splitifier) {
            List<LineComponent> list = Lists.newArrayList();
            text.visit((p_238355_1_, p_238355_2_) -> {
                if (!p_238355_2_.isEmpty()) {
                    LineComponent textComponent = new LineComponent(p_238355_2_, p_238355_1_);
//                    LOGGER.error("I am visiting this: " + textComponent.contents);
                    list.add(textComponent);
                }

                return Optional.empty();
            }, p_style);
            FlatComponents charactermanager$substyledtext = new FlatComponents(list);
            boolean complete = false;
            boolean flag1 = false;
            boolean keepGoing = false;
            float averagePoint = cutoffPoint;
            int count = 1;

            while(!complete) {
                complete = true;
                MultilineProcessorMixer multilineProcessor = new MultilineProcessorMixer(cutoffPoint);

                for(LineComponent charactermanager$styleoverridingtextcomponent : charactermanager$substyledtext.parts) {
//                    LOGGER.debug("Remaining Content: " + charactermanager$styleoverridingtextcomponent.contents);
                    boolean flag3 = StringDecomposer.iterateFormatted(
                            charactermanager$styleoverridingtextcomponent.contents, 0,
                            charactermanager$styleoverridingtextcomponent.style, p_style, multilineProcessor);
                    if (!flag3) {
                        int i = multilineProcessor.getSplitPosition();
                        Style style = multilineProcessor.getSplitStyle();
                        char c0 = charactermanager$substyledtext.charAt(i);
                        boolean shouldReturn = c0 == '\n';
                        boolean shouldBreak = shouldReturn || c0 == ' ';
                        flag1 = shouldReturn;
                        FormattedText FormattedText = charactermanager$substyledtext.splitAt(i, shouldBreak ? 1 : 0, style);
//                        LOGGER.warn("Next Split: " + FormattedText.getString());
                        splitifier.accept(FormattedText, keepGoing);
                        keepGoing = !shouldReturn;
                        complete = false;
                        break;
                    }

                    multilineProcessor.addToOffset(charactermanager$styleoverridingtextcomponent.contents.length());
                }
                averagePoint += multilineProcessor.getWidth();
                count++;
            }

            FormattedText FormattedText1 = charactermanager$substyledtext.getRemainder();
            if (FormattedText1 != null) {
                splitifier.accept(FormattedText1, keepGoing);
            } else if (flag1) {
                splitifier.accept(FormattedText.EMPTY, false);
            }
//            if (FormattedText1 != null) LOGGER.error("What's the remainder: " + FormattedText1.getString());

        }
        public class MultilineProcessorMixer implements FormattedCharSink {
            private final float maxWidth;
            private int lineBreak = -1;
            private Style lineBreakStyle;
            private boolean hadNonZeroWidthChar;
            private float width;
            private int lastSpace;
            private Style lastSpaceStyle;
            private int nextChar;
            private int offset;

            private boolean hasWord = false;
            private boolean wordCaptured = false;
            private float baseShot = 0;
            private int baseSpace = -1;
            private float baseWidth = 0;
            private Style baseStyle;
            private float overShot = 0;

            public MultilineProcessorMixer(float maxWidth) {
                this.lineBreakStyle = Style.EMPTY;
                this.lastSpace = -1;
                this.lastSpaceStyle = Style.EMPTY;
                this.maxWidth = Math.max(maxWidth, 1.0F);
            }

            public boolean accept(int length, Style style, int codeNumber) {
//                    LOGGER.error("Length is: " + length);
//                    LOGGER.error("Offset is: " + this.offset);
//                    LOGGER.error("Code Number is: " + codeNumber);
                int lvt_4_1_ = length + this.offset;
//                    LOGGER.error("Total is: " + (lvt_4_1_));
                boolean isSpace = false;
                switch (codeNumber) {
                    case 10:
//                            LOGGER.info("Finish iteration");
                        return this.finishIteration(lvt_4_1_, style);
                    case 32:
//                            LOGGER.info("This is a space");
                        this.baseSpace = this.lastSpace;
                        this.baseStyle = this.lastSpaceStyle;
                        this.baseWidth = this.getWidth();
                        this.lastSpace = lvt_4_1_;
                        this.lastSpaceStyle = style;
                        isSpace = true;
                        if (width < maxWidth || !wordCaptured){
                            this.baseShot = 0;
                            this.overShot = 0;
                        }
                        if (hasWord) wordCaptured = true;
                    default:
                        float lvt_5_1_ = CharacterManagerMixer.this.widthProvider.getWidth(codeNumber, style);
//                            LOGGER.info("1. width(?): " + lvt_5_1_);
//                            LOGGER.info("2. Previous width(?): " + this.width);
                        if (!isSpace){
                            hasWord = true;
                            if (this.width < this.maxWidth) this.baseShot += lvt_5_1_;
                            else this.overShot += lvt_5_1_;
                        }

                        this.width += lvt_5_1_;

//                            LOGGER.info("3. After width(?): " + this.width);
//                            LOGGER.info("4. NonZeroWidthChar? " + this.hadNonZeroWidthChar);
//                            LOGGER.info("5. curr width > max width: " + (this.width > this.maxWidth));
                        //If there is a character that isn't 0, width is higher than maxWidth
                        //there is a word, and it's currently a space character
                        if (this.hadNonZeroWidthChar && (width >= maxWidth) && isSpace && wordCaptured) {

//                                LOGGER.warn("overShot: " + this.overShot);
//                                LOGGER.warn("baseShot: " + this.baseShot);
//                                LOGGER.warn("baseSpace: " + this.baseSpace);
                            this.width -= lvt_5_1_;
                            //If I should move the current word to the next line or not
                            //If text count that's above the maxWidth is greater than the text count below the maxWidth for the current word
                            //cut it off and move to the next line
//                                if (this.baseShot >= this.overShot){
//                                    LOGGER.warn("Over Shot was less than base Shot, keep it.");
//                                }
//                                else{
//                                    this.lastSpace = this.baseSpace;
//                                    this.lastSpaceStyle = this.baseStyle;
//                                    this.width = this.baseWidth;
//                                    LOGGER.warn("Base shot was less than over shot, cut it off");
//                                }
                            if (this.baseShot < this.overShot) {
                                this.lastSpace = this.baseSpace;
                                this.lastSpaceStyle = this.baseStyle;
                                this.width = this.baseWidth;
                            }
//                                return this.lastSpace != -1 ? this.finishIteration(this.lastSpace, this.lastSpaceStyle) : this.finishIteration(lvt_4_1_, style);
                            return this.finishIteration(this.lastSpace, this.lastSpaceStyle);
                        } else {
                            this.hadNonZeroWidthChar |= lvt_5_1_ != 0.0F;
//                                LOGGER.info("6. What's nonZeroWidthChar now? " + this.hadNonZeroWidthChar);
//                                LOGGER.info("7. Previous nextChar? " + this.nextChar);
                            this.nextChar = lvt_4_1_ + Character.charCount(codeNumber);
//                                LOGGER.info("8. After nextChar? " + this.nextChar);
                            return true;
                        }
                }
            }

            private boolean finishIteration(int p_238388_1_, Style p_238388_2_) {
                this.lineBreak = p_238388_1_;
                this.lineBreakStyle = p_238388_2_;
                return false;
            }

            private boolean lineBreakFound() {
                return this.lineBreak != -1;
            }

            public int getSplitPosition() {
                return this.lineBreakFound() ? this.lineBreak : this.nextChar;
            }

            public Style getSplitStyle() {
                return this.lineBreakStyle;
            }

            public void addToOffset(int p_238387_1_) {
                this.offset += p_238387_1_;
            }

            public float getWidth(){
                return this.width;
            }
        }
    }
}
