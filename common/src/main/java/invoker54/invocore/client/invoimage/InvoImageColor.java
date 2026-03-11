package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import net.minecraft.nbt.CompoundTag;

import java.awt.*;

public class InvoImageColor extends InvoImage {
    public static final String COLOR_IMAGE = "COLOR_IMAGE";

    public InvoImageColor(CompoundTag tag){
        super(new InvoZone(0,1,0,1));
        this.deserializeNBT(tag);
    }

    public InvoImageColor(InvoZone renderZone, Color color) {
        super(renderZone);
        this.tintColor = color;
    }

//    public Color setColor(Color newColor){
//        Color oldColor = this.color;
//        this.color = newColor;
//        return oldColor;
//    }

//    public Color getColor(){
//        return this.color;
//    }

    @Override
    public InvoImage copy() {
        return new InvoImageColor(this.mainZone, this.getTintColor());
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone, boolean movePivot) {
        if (renderZone.isZero()){
//            LOGGER.error("Mainzone is zero: ");
            return;
        }

        this.rotate(stack, renderZone, movePivot);
        this.changeTintForRender(true);
        ClientUtil.blitColor(stack, renderZone.copy().absolute(), this.tintColor.getRGB());
        this.changeTintForRender(false);
        stack.popPose();
    }
}
