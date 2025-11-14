package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import net.minecraft.nbt.CompoundTag;

import java.awt.*;

public class InvoImageColor extends InvoImage {
    public static final String COLOR_IMAGE = "COLOR_IMAGE";
    public static final String COLOR_INT = "COLOR_INT";
    public Color color;

    public InvoImageColor(CompoundTag tag){
        super(new InvoZone(0,1,0,1));
        this.deserializeNBT(tag);
    }

    public InvoImageColor(InvoZone renderZone, Color color) {
        super(renderZone);
        this.color = color;
    }

    public Color setColor(Color newColor){
        Color oldColor = this.color;
        this.color = newColor;
        return oldColor;
    }

    public Color getColor(){
        return this.color;
    }

    @Override
    public InvoImage copy() {
        return new InvoImageColor(this.mainZone, color);
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone) {
        ClientUtil.blitColor(stack, renderZone, this.color.getRGB());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(COLOR_INT, this.getColor().getRGB());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.color = new Color(tag.getInt(COLOR_INT));
    }
}
