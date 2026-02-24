package invoker54.invocore.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.common.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class InvoText {
    public static final String TEXT_COMPONENT = "TEXT_COMPONENT";
    public static final String PROPERTIES = "PROPERTIES";

    private MutableComponent textComponent;
    private final InvoText.Properties properties;

    public static InvoText translate(String txt, Object... args) {
        return new InvoText(Component.translatable(txt, args));
    }

    public static InvoText literal(String txt) {
        return new InvoText(Component.literal(txt));
    }

    public static InvoText component(MutableComponent txt) {
        return new InvoText(txt);
    }

    public InvoText(CompoundTag tag) {
        this(Component.literal(""));
        this.deserializeNBT(tag);
    }

    private InvoText(MutableComponent textComponent) {
        this.textComponent = textComponent;
        this.properties = new Properties();
    }

    public InvoText.Properties getProperties() {
        return this.properties;
    }

    public MutableComponent getText(boolean keepFormatCodes) {
        return StringUtil.formatText(this, keepFormatCodes).setStyle(this.getStyle()).textComponent;
    }

    public MutableComponent getUnformattedText() {
        return this.textComponent;
    }

    public InvoText setArgsAndCopy(Object... args) {
        if (!(this.textComponent.getContents() instanceof TranslatableContents)) {
            return InvoText.literal("This is not a translation component! [InvoText]");
        }
        InvoText newText = InvoText.translate(((TranslatableContents) this.textComponent.getContents()).getKey(), args);
        newText.textComponent.getSiblings().addAll(this.textComponent.getSiblings());
        newText.textComponent.setStyle(this.textComponent.getStyle());
        return newText;
    }

    public String getString() {
        return this.textComponent.getString();
    }

    public InvoText withStyle(boolean clear, ChatFormatting... styles) {
        if (clear) this.textComponent.setStyle(Style.EMPTY);
        this.textComponent.withStyle(styles);
        return this;
    }

    public InvoText append(InvoText text) {
        this.textComponent.append(text.getText(true));
        return this;
    }

    public InvoText append(Component component) {
        this.textComponent.append(component);
        return this;
    }

    public Style getStyle() {
        return this.textComponent.getStyle();
    }

    public InvoText setStyle(Style style){
        this.textComponent.setStyle(style.applyTo(this.textComponent.getStyle()));
        return this;
    }

    public ComponentContents getContents() {
        return this.textComponent.getContents();
    }

    public List<Component> getSiblings() {
        return this.textComponent.getSiblings();
    }

    public InvoText copyRaw() {
        return new InvoText(this.textComponent.plainCopy());
    }

    public InvoText deepCopy() {
        return new InvoText(this.serializeNBT());
    }

    public FormattedCharSequence getVisualOrderText() {
        return this.textComponent.getVisualOrderText();
    }

    public TextViewer getTextViewer(boolean keepFormatCodes, InvoZone textZone) {
        Properties props = this.getProperties();
        return new TextViewer(this.getText(keepFormatCodes).getString(), props, textZone, false, keepFormatCodes);
    }

//    public void renderWithActualZone(PoseStack stack, InvoZone textZone, InvoZone renderZone, boolean keepFormatCodes) {
//        InvoZone actualTextZone = TextUtil.renderText(null, this.getText(keepFormatCodes), textZone,
//                this.getProperties(), false).textZone();
//        this.render(stack, textZone.changeRelativeMultiply(actualTextZone, renderZone), keepFormatCodes);
//    }

    public void render(PoseStack stack, InvoZone textZone, boolean keepFormatCodes) {
        Properties props = this.getProperties();

        TextUtil.renderText(stack, textZone, this.getText(keepFormatCodes), props, true);
    }

    public void setProperties(Properties properties) {
        this.getProperties().deserializeNBT(properties.serializeNBT());
    }

    public void copyProperties(InvoText invoText) {
        this.getProperties().deserializeNBT(invoText.getProperties().serializeNBT());
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TEXT_COMPONENT, Component.Serializer.toJson(this.textComponent));
        tag.put(PROPERTIES, this.getProperties().serializeNBT());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.textComponent = Component.Serializer.fromJson(tag.getString(TEXT_COMPONENT));
        this.getProperties().deserializeNBT(tag.getCompound(PROPERTIES));
    }

    public TextViewer.RenderInfo getRenderInfo(InvoZone textZone, boolean keepFormatCodes) {
        return TextUtil.renderText(null, textZone, this.getText(keepFormatCodes), this.getProperties(), false);
    }

    public static class Properties {
        public static final String IS_SHADOW_BOOL = "IS_SHADOW_BOOL";
        public static final String MAX_SPLITS_INT = "MAX_SPLITS_INT";
        public static final String PADDING_INT = "PADDING_INT";
        public static final String TXT_ALIGNMENT_ENUM = "TXT_ALIGNMENT_ENUM";
        public static final String MAX_TEXT_SIZE_FLOAT = "MAX_TEXT_SIZE_FLOAT";
        public static final String MIN_TEXT_SIZE_FLOAT = "MIN_TEXT_SIZE_FLOAT";

        private boolean shadow = true;
        private int maxSplits = 0;
        private int padding = 0;
        private TextUtil.TextAlign TextAlign = TextUtil.TextAlign.MID_LEFT;
        private float maxTextSize = 27F;
        private float minTextSize = 5F;

        public InvoText text(String text) {
            return this.text(InvoText.literal(text));
        }

        public InvoText text(InvoText text) {
            text.setProperties(this);
            return text;
        }

        public boolean isShadow() {
            return shadow;
        }

        public Properties setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        public int getMaxSplits() {
            return maxSplits;
        }

        public Properties setMaxSplits(int maxSplits) {
            this.maxSplits = maxSplits;
            return this;
        }

        public int getPadding() {
            return padding;
        }

        public Properties setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public TextUtil.TextAlign getTxtAlignment() {
            return TextAlign;
        }

        public Properties setTxtAlignment(TextUtil.TextAlign TextAlign) {
            this.TextAlign = TextAlign;
            return this;
        }

        public Properties setTextSize(float textSize) {
            this.maxTextSize = textSize;
            this.minTextSize = textSize;
            return this;
        }

        public float getMaxTextSize() {
            return maxTextSize;
        }

        public Properties setMaxTextSize(float maxTextSize) {
            this.maxTextSize = maxTextSize;
            this.minTextSize = Math.min(this.minTextSize, this.maxTextSize);
            return this;
        }

        public float getMinTextSize() {
            return minTextSize;
        }

        public Properties setMinTextSize(float minTextSize) {
            this.minTextSize = minTextSize;
            this.maxTextSize = Math.max(this.maxTextSize, this.minTextSize);
            return this;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(IS_SHADOW_BOOL, this.isShadow());
            tag.putInt(MAX_SPLITS_INT, this.getMaxSplits());
            tag.putInt(PADDING_INT, this.getPadding());
            tag.putString(TXT_ALIGNMENT_ENUM, this.getTxtAlignment().name());
            tag.putFloat(MAX_TEXT_SIZE_FLOAT, this.getMaxTextSize());
            tag.putFloat(MIN_TEXT_SIZE_FLOAT, this.getMinTextSize());
            return tag;
        }

        public void deserializeNBT(CompoundTag tag) {
            this.setShadow(tag.getBoolean(IS_SHADOW_BOOL));
            this.setMaxSplits(tag.getInt(MAX_SPLITS_INT));
            this.setPadding(tag.getInt(PADDING_INT));
            this.setTxtAlignment(TextUtil.TextAlign.valueOf(tag.getString(TXT_ALIGNMENT_ENUM)));
            this.setMaxTextSize(tag.getFloat(MAX_TEXT_SIZE_FLOAT));
            this.setMinTextSize(tag.getFloat(MIN_TEXT_SIZE_FLOAT));
        }
    }
}
