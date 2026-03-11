package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;

import java.awt.*;

public class InvoImageItem extends InvoImage {
    public static final String ITEM_IMAGE = "ITEM_IMAGE";
    public static final String STACK_ITEMSTACK = "STACK_ITEMSTACK";
    public ItemStack stack;

    public InvoImageItem(CompoundTag tag){
         super(new InvoZone(0,1,0,1));
         this.deserializeNBT(tag);
    }

    public InvoImageItem(InvoZone renderZone, ItemStack stack) {
        super(renderZone);
        this.stack = stack;
    }

    public ItemStack setStack(ItemStack newStack){
        ItemStack oldStack = this.stack;
        this.stack = newStack;
        return oldStack;
    }

    public ItemStack getStack(){
        return this.stack;
    }

    @Override
    public InvoImage copy() {
        return new InvoImageItem(this.mainZone.copy(), stack.copy());
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone, boolean movePivot) {
        if (this.mainZone.isZero()) return;

        this.rotate(stack, renderZone, movePivot);
        this.changeTintForRender(true);
        ClientUtil.blitItem(stack, renderZone, this.stack);
        this.changeTintForRender(false);
        stack.popPose();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        CompoundTag stackTag = new CompoundTag();
        this.stack.save(stackTag);
        tag.put(STACK_ITEMSTACK, stackTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.stack = ItemStack.of(tag.getCompound(STACK_ITEMSTACK));
    }
}
