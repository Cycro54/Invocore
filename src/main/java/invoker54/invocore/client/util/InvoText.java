package invoker54.invocore.client.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class InvoText {
    private final MutableComponent textComponent;

    public static InvoText translate(String txt, Object... args){
        return new InvoText(Component.translatable(txt, args));
    }

    public static InvoText literal(String txt){
        return new InvoText(Component.literal(txt));
    }

    public static InvoText component(MutableComponent txt){
        return new InvoText(txt);
    }

    private InvoText (MutableComponent textComponent){
        this.textComponent = textComponent;
    }

    public MutableComponent getText(){
        return this.textComponent;
    }

    public InvoText setArgs(Object... args){
        if (!(this.textComponent.getContents() instanceof TranslatableContents)) {
            return InvoText.literal("This is not a translation component! [InvoText]");
        }
        InvoText newText = InvoText.translate(((TranslatableContents)this.textComponent.getContents()).getKey(), args);
        newText.textComponent.getSiblings().addAll(this.textComponent.getSiblings());
        newText.textComponent.setStyle(this.textComponent.getStyle());
        return newText;
    }

    public String getString(){
        return this.textComponent.getString();
    }

    public InvoText withStyle(boolean clear, ChatFormatting... styles) {
        if (clear) this.textComponent.setStyle(Style.EMPTY);
        this.textComponent.withStyle(styles);
        return this;
    }

    public InvoText append(InvoText text) {
        this.textComponent.append(text.getText());
        return this;
    }

    public InvoText append(Component component){
        this.textComponent.append(component);
        return this;
    }

    public Style getStyle() {
        return this.textComponent.getStyle();
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
        return new InvoText(this.textComponent.copy());
    }

    public FormattedCharSequence getVisualOrderText() {
        return this.textComponent.getVisualOrderText();
    }
}
