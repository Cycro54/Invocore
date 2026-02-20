package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import invoker54.invocore.client.util.InvoZone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.util.*;

public class InvoImageCanvas extends InvoImage{
    public static final String CANVAS_IMAGE = "CANVAS_IMAGE";
    public static final String IMAGE_LIST = "IMAGE_LIST";
//    public static final String FULL_ZONE = "FULL_ZONE";
    private final List<InvoImage> imageList;

    public InvoImageCanvas(InvoZone renderZone) {
        this(renderZone, Collections.emptyList());
    }

    public InvoImageCanvas(CompoundTag tag){
        this(new InvoZone(0,1,0,1));
        this.deserializeNBT(tag);
    }

    public InvoImageCanvas(InvoZone renderZone, List<InvoImage> imageList) {
        super(renderZone);
        this.imageList = imageList;
    }

    @Override
    public void setMainZone(InvoZone updatedZone, boolean movePivot) {
        InvoZone oldZone = this.mainZone.copy();
        super.setMainZone(updatedZone, movePivot);

        for (InvoImage image : this.imageList){
            image.setMainZone(image.getMainZoneCopy().changeRelativeAdd(oldZone, updatedZone,
                    InvoZone.ANCHORPOINT.TOP_LEFT, InvoZone.ANCHORPOINT.BOTTOM_RIGHT), true);
        }
    }

    public InvoZone getFullZone(){
        InvoZone fullZone = this.mainZone.copy();
        for (InvoImage image : this.imageList){
            fullZone.merge(image.getMainZoneCopy());
        }

        return fullZone;
    }

    public void setFullZone(InvoZone updatedZone){
        InvoZone fullZone = this.getFullZone();

        this.setMainZone(this.getMainZoneCopy().changeRelativeAdd(fullZone, updatedZone,
                InvoZone.ANCHORPOINT.TOP_LEFT, InvoZone.ANCHORPOINT.BOTTOM_RIGHT), true);
    }

    @Override
    public InvoImageCanvas copy() {
        return new InvoImageCanvas(this.getMainZoneCopy(), this.imageList.stream().map(InvoImage::copy).toList());
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone) {
        if (this.mainZone.isZero()) return;


        this.rotate(stack, renderZone);
        for (InvoImage image : this.imageList){
            image.render(stack, image.mainZone.copy().changeRelativeAdd(this.getMainZoneCopy(), renderZone,
                    InvoZone.ANCHORPOINT.TOP_LEFT, InvoZone.ANCHORPOINT.BOTTOM_RIGHT));
        }
        stack.popPose();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        ListTag tagList = new ListTag();
        this.imageList.forEach(image -> tagList.add(image.serializeNBT()));
        tag.put(IMAGE_LIST, tagList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.imageList.clear();
        tag.getList(IMAGE_LIST, Tag.TAG_COMPOUND).forEach(imageTag ->
                this.imageList.add(InvoImage.fromTag((CompoundTag) imageTag)));
    }
}
