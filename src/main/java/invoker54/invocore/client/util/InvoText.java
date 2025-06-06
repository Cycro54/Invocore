package invoker54.invocore.client.util;

import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;

import java.util.List;

public class InvoText {
    private final IFormattableTextComponent textComponent;

    public static InvoText translate(String txt, Object... args){
        return new InvoText(new TranslationTextComponent(txt, args));
    }

    public static InvoText literal(String txt){
        return new InvoText(new StringTextComponent(txt));
    }

    public static InvoText component(IFormattableTextComponent txt){
        return new InvoText(txt);
    }

    private InvoText (IFormattableTextComponent textComponent){
        this.textComponent = textComponent;
    }

    public IFormattableTextComponent getText(){
        return this.textComponent;
    }

    public InvoText setArgs(Object... args){
        if (!(this.textComponent instanceof TranslationTextComponent)) {
            return InvoText.literal("This is not a translation component! [InvoText]");
        }
        InvoText newText = InvoText.translate(((TranslationTextComponent)this.textComponent).getKey(), args);
        newText.textComponent.getSiblings().addAll(this.textComponent.getSiblings());
        newText.textComponent.setStyle(this.textComponent.getStyle());
        return newText;
    }

    public String getString(){
        return this.textComponent.getString();
    }

    public InvoText withStyle(boolean clear, TextFormatting... styles) {
        if (clear) this.textComponent.setStyle(Style.EMPTY);
        this.textComponent.withStyle(styles);
        return this;
    }

    public InvoText append(InvoText text) {
        this.textComponent.append(text.getText());
        return this;
    }

    public InvoText append(ITextComponent component){
        this.textComponent.append(component);
        return this;
    }

    public Style getStyle() {
        return this.textComponent.getStyle();
    }

    public String getContents() {
        return this.textComponent.getContents();
    }

    public List<ITextComponent> getSiblings() {
        return this.textComponent.getSiblings();
    }

    public InvoText copyRaw() {
        return new InvoText(this.textComponent.plainCopy());
    }

    public InvoText deepCopy() {
        return new InvoText(this.textComponent.copy());
    }

    public IReorderingProcessor getVisualOrderText() {
        return this.textComponent.getVisualOrderText();
    }
}
