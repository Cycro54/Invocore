package invoker54.invocore.client.util;

import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.nbt.CompoundTag;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class InvoZone {
    public static ModLogger LOGGER = ModLogger.getLogger(InvoZone.class, Invocore.debugMode);

    public static String DIMENSION_FLOATS = "DIMENSION_FLOATS";

    private float x0;
    private float width;
    private float y0;
    private float height;

    public InvoZone(float x0, float width, float y0, float height){
        this.x0 = x0;
        this.width = width;
        this.y0 = y0;
        this.height = height;
    }

    public static InvoZone fromCoordinates(Vector2f first, Vector2f second){
        return new InvoZone(first.x(), second.x() - first.x(),
                first.y(), second.y() - first.y());
    }

    public static InvoZone fromTag(CompoundTag tag){
        InvoZone zone = new InvoZone(0,0,0,0);
        zone.deserializeNBT(tag);
        return zone;
    }

    public boolean isSame(InvoZone otherZone) {
        return this.topLeft().equals(otherZone.topLeft(), 0) &&
                this.bottomRight().equals(otherZone.bottomRight(), 0);
    }

    public float x() {return x0;}
    public InvoZone setX(float x0) {
        this.x0 = x0;
        return this;
    }

    public float width() {return width;}
    public InvoZone setWidth(float width) {
        this.width = width;
        return this;
    }
    public InvoZone setWidthConstraint(float width){
        if (this.width == 0) this.width = 1;
        float percentChange = width/this.width;
        this.setWidth(width);
        this.setHeight(this.height * percentChange);
        return this;
    }

    public float y() {return y0;}
    public InvoZone setY(float y0) {
        this.y0 = y0;
        return this;
    }

    public float height() {return height;}
    public InvoZone setHeight(float height) {
        this.height = height;
        return this;
    }
    public InvoZone setHeightConstraint(float height){
        if (this.height == 0) this.height = 1;
        float percentChange = height/this.height;
        this.setHeight(height);
        this.setWidth(this.width * percentChange);
        return this;
    }

    public InvoZone shift(float changeX, float changeY){
        this.x0 += changeX;
        this.y0 += changeY;
        return this;
    }
    public InvoZone inflate(float changeAmount){
        return this.inflate(changeAmount, changeAmount);
    }
    public InvoZone inflate(float changeWidth, float changeHeight){
        this.x0 -= changeWidth;
        this.width += changeWidth * 2;
        this.y0 -= changeHeight;
        this.height += changeHeight * 2;
        return this;
    }

    public InvoZone splitHeight(float divisor, float slices){
        this.height = this.height/divisor;
        this.height *= slices;
        return this;
    }

    public InvoZone splitWidth(float divisor, float slices){
        this.width = this.width/divisor;
        this.width *= slices;
        return this;
    }

    public InvoZone mirrorX(float xOrigin){
        float newX0 = this.x() + ((xOrigin - this.x()) * 2);
        float newX1 = this.right() + ((xOrigin - this.right()) * 2);
        this.x0 = Math.min(newX0, newX1);
        return this;
    }
    public InvoZone invertX(){
        this.x0 = this.right();
        this.width *= -1;
        return this;
    }

    public InvoZone mirrorY(float yOrigin){
        float newY0 = this.y() + ((yOrigin - this.y()) * 2);
        float newY1 = this.down() + ((yOrigin - this.down()) * 2);
        this.y0 = Math.min(newY0, newY1);
        return this;
    }
    public InvoZone invertY(){
        this.y0 = this.down();
        this.height *= -1;
        return this;
    }

    public InvoZone setBound(InvoZone boundZone, boolean constrainSize){
        if(constrainSize){
            this.setWidthConstraint(Math.min(boundZone.width, this.width));
            this.setHeightConstraint(Math.min(boundZone.height, this.height));
        }
        else {
            this.setWidth(Math.min(boundZone.width, this.width));
            this.setHeight(Math.min(boundZone.height, this.height));
        }

        return setBound(boundZone);
    }
    public InvoZone setBound(InvoZone boundZone){
        this.shift(Math.max(boundZone.x() - this.x(), Math.min(boundZone.right() - this.right(),0)),
                Math.max(boundZone.y() - this.y(), Math.min(boundZone.down() - this.down(),0)));
        return this;
    }

    public InvoZone fitBound(InvoZone boundZone, boolean centerX, boolean centerY){
        float boundZoneSmallestDimension = Math.min(boundZone.width, boundZone.height);

        float boundWidthRatio = boundZone.width/boundZone.height;
        float myWidthRatio = this.width/this.height;

        if (myWidthRatio > boundWidthRatio){
            this.setWidthConstraint(boundZoneSmallestDimension);
        }
        else {
            this.setHeightConstraint(boundZoneSmallestDimension);
        }

        if (centerX) this.centerX(boundZone.middleX());
        if (centerY) this.centerY(boundZone.middleY());

        return this;
    }

    public InvoZone center(InvoZone centerZone){
        this.centerX(centerZone.middleX());
        this.centerY(centerZone.middleY());
        return this;
    }

    public InvoZone centerX(float centerX){
        this.x0 = centerX - this.width/2;
        return this;
    }
    public InvoZone centerY(float centerY){
        this.y0 =  centerY - this.height/2;
        return this;
    }

    public float right(){return this.x0 + this.width;}
    public InvoZone setRight(float x1){return this.setX(x1 - this.width);}
    public InvoZone setRightWidth(float x1){return this.setWidth(x1 - this.x());}
    public float middleX(){return this.x0 + this.width/2;}
    public float down(){return this.y0 + this.height;}
    public InvoZone setDown(float y1){return this.setY(y1 - this.height);}
    public InvoZone setDownHeight(float y1){return this.setHeight(y1 - this.y());}
    public float middleY(){return this.y0 + this.height/2;}

    public Vector2f topLeft(){
        return new Vector2f(this.x(), this.y());
    }
    public Vector2f topRight(){
        return new Vector2f(this.right(), this.y());
    }
    public Vector2f bottomLeft(){
        return new Vector2f(this.x(), this.down());
    }
    public Vector2f bottomRight(){
        return new Vector2f(this.right(), this.down());
    }

    public InvoZone intersect(InvoZone otherZone){
        List<Float> xRange = new ArrayList<>(List.of(this.x(), this.right(), otherZone.x(), otherZone.right()));
        xRange.sort(Float::compareTo);

        List<Float> yRange = new ArrayList<>(List.of(this.y(), this.down(), otherZone.y(), otherZone.down()));
        yRange.sort(Float::compareTo);

        return InvoZone.fromCoordinates(new Vector2f(xRange.get(1), yRange.get(1)),
                new Vector2f(xRange.get(2), yRange.get(2)));
    }
    public InvoZone merge(InvoZone otherZone){
        this.setX(Math.min(this.x(), otherZone.x()));
        this.setRightWidth(Math.max(this.right(), otherZone.right()));
        this.setY(Math.min(this.y(), otherZone.y()));
        this.setDownHeight(Math.max(this.down(), otherZone.down()));
        return this;
    }

    public boolean inBounds(Vector2f point){
        boolean inXBounds = MathUtil.isInRange(point.x(), this.x(), this.right());
        boolean inYBounds = MathUtil.isInRange(point.y(), this.y(), this.down());
        return inXBounds && inYBounds;
    }

    public boolean inBounds(InvoZone boundZone, boolean fullyInBounds){
        boolean isTopLeftInBounds = boundZone.inBounds(this.topLeft());
        boolean isBottomRightInBounds = boundZone.inBounds(this.bottomRight());

        if (fullyInBounds) return isTopLeftInBounds && isBottomRightInBounds;
        return isTopLeftInBounds || isBottomRightInBounds;
    }

    //TODO: Split into pieces, reuse code!
    public InvoZone grid(float pointX, float pointY){
        float distance = pointX - this.x();
        int steps = (int) (distance/this.width());
        if (steps < 0) steps -= 1;
        this.shift(steps * this.width(), 0);

        distance = pointY - this.y();
        steps = (int) (distance/this.height());
        if (steps < 0) steps -= 1;
        this.shift(0, steps * this.height());

        return this;
    }

    public ScreenRectangle rect(){
        return new ScreenRectangle((int) this.x(), (int) this.y(), (int) this.width, (int) this.height);
    }

    public InvoZone copy(){
        return new InvoZone(this.x0, this.width, this.y0, this.height);
    }
    public InvoZone copy(InvoZone otherZone){
        this.x0 = otherZone.x();
        this.width = otherZone.width();
        this.y0 = otherZone.y();
        this.height = otherZone.height();
        return this;
    }

    public InvoZone multiply(float multiplier){
        InvoZone originalZone = this.copy();
        this.width = this.width * multiplier;
        this.height = this.height * multiplier;
        this.center(originalZone);
        return this;
    }

    public InvoZone changeRelative(InvoZone startZone, InvoZone endZone){
        float xPercentage = (this.x() - startZone.x())/startZone.width;
        float widthPercentage = this.width/startZone.width;
        float yPercentage = (this.y() - startZone.y())/startZone.height;
        float heightPercentage = this.height/startZone.height;

        this.setX(endZone.x() + (xPercentage * endZone.width));
        this.setWidth(widthPercentage * endZone.width);
        this.setY(endZone.y() + (yPercentage * endZone.height));
        this.setHeight(heightPercentage * endZone.height);

        return this;
    }

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        StringBuilder builder = new StringBuilder();
        builder.append(this.x())
                .append(":").append(this.width())
                .append(":").append(this.y())
                .append(":").append(this.height());
        tag.putString(DIMENSION_FLOATS, builder.toString());

        return tag;
    }

    public void deserializeNBT(CompoundTag tag){
        if (!tag.contains(DIMENSION_FLOATS)) return;
        String[] stringArray = tag.getString(DIMENSION_FLOATS).split(":");
        if (stringArray.length != 4){
            LOGGER.error("[Invocore] Invozone string is malformed!: " + tag.getString(DIMENSION_FLOATS));
            return;
        }
        this.setX(Float.parseFloat(stringArray[0]))
                .setWidth(Float.parseFloat(stringArray[1]))
                .setY(Float.parseFloat(stringArray[2]))
                .setHeight(Float.parseFloat(stringArray[3]));
    }

    @Override
    public String toString() {
        return "[x:"+ this.x() + ", width:" + this.width
        + ", y:" + this.y() + ", height:" +  this.height + "]";
    }
}