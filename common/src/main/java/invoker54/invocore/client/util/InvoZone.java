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
    private boolean stretch = false;
    
    public enum ANCHORPOINT{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    public InvoZone(){
        this(0, 1, 0, 1, false);
//        LOGGER.error("What's the zone? " + this);
    }

    public InvoZone(float x0, float width, float y0, float height){
        this(x0, width, y0, height, false);
//        LOGGER.error("What's the zone? " + this);
    }

    public InvoZone(float x0, float width, float y0, float height, boolean stretch){
        this.x0 = x0;
        this.width = width;
        this.y0 = y0;
        this.height = height;
        this.stretch = stretch;
    }

    public InvoZone(CompoundTag tag){
        this.deserializeNBT(tag);
    }

    public static InvoZone fromPoints(Vector2f first, Vector2f second){
        float x0 = Math.min(first.x, second.x);
        float width = Math.max(first.x, second.x) - x0;
        float y0 = Math.min(first.y, second.y);
        float height = Math.max(first.y, second.y) - y0;
        return new InvoZone(x0, width, y0, height);
//        return new InvoZone(x0, width, y0, height).setXY(new Vector2f(x0, y0)).setWidth(width).setHeight(height);
    }

    public static InvoZone fromPoint(Vector2f point){
        return new InvoZone(point.x, 1, point.y, 1);
    }

    public static InvoZone fromTag(CompoundTag tag){
        InvoZone zone = new InvoZone(0,0,0,0);
        zone.deserializeNBT(tag);
        return zone;
    }

    public boolean isZero(){
        return this.width == 0 || this.height == 0;
    }
    
    public InvoZone stretch(boolean stretch){
        this.stretch = stretch;
        return this;
    }

    public Vector2f rotate(Vector2f origin, ANCHORPOINT anchorPoint, float rotation){
        Vector2f point = this.getAnchorPoint(anchorPoint);

        // "Liberated" from: https://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
        Vector2f rotatedPoint = new Vector2f(
                (float) (Math.cos(rotation) * (point.x - origin.x) - Math.sin(rotation) * (point.y - origin.y) + origin.x),
                (float) (Math.sin(rotation) * (point.x - origin.x) + Math.cos(rotation) * (point.y - origin.y) + origin.y)
        );
        return rotatedPoint;
//        p'x = cos(theta) * (px-ox) - sin(theta) * (py-oy) + ox
//
//        p'y = sin(theta) * (px-ox) + cos(theta) * (py-oy) + oy
    }

    public boolean isSame(InvoZone otherZone) {
        return this.topLeft().equals(otherZone.topLeft(), 0) &&
                this.bottomRight().equals(otherZone.bottomRight(), 0);
    }

    public float x() {return x0;}
    public InvoZone setX(float x0) {
        float oldRight = this.right();
        this.x0 = x0;
        if (stretch) this.width += oldRight - this.right();
        if (Float.isNaN(this.x0)) this.x0 = 0;
        return this;
    }

    public float width() {return width;}
    public InvoZone setWidth(float width) {
        this.width = width;
        if (Float.isNaN(this.width)) this.width = 0;
        return this;
    }
    public InvoZone setWidthConstraint(float width){
        if (this.width == 0) this.setWidth(1);
        float percentChange = width/this.width;
        this.setWidth(width);
        this.setHeight(this.height * percentChange);
        return this;
    }

    public InvoZone minWidth(float minWidth){
        return this.setWidth(Math.max(this.width, minWidth));
    }

    public InvoZone maxWidth(float maxWidth){
        return this.setWidth(Math.min(this.width, maxWidth));
    }

    public float y() {return y0;}
    public InvoZone setY(float y0) {
        float oldDown = this.down();
        this.y0 = y0;
        if (stretch) this.height += oldDown - this.down();
        if (Float.isNaN(this.y0)) this.y0 = 0;
        return this;
    }

    public float height() {return height;}
    public InvoZone setHeight(float height) {
        this.height = height;
        if (Float.isNaN(this.height)) this.height = 0;
        return this;
    }
    public InvoZone setHeightConstraint(float height){
        if (this.height == 0) this.setHeight(1);
        float percentChange = height/this.height;
        this.setHeight(height);
        this.setWidth(this.width * percentChange);
        return this;
    }

    public InvoZone minHeight(float minHeight){
        return this.setHeight(Math.max(this.height, minHeight));
    }

    public InvoZone maxHeight(float maxHeight){
        return this.setHeight(Math.min(this.height, maxHeight));
    }

    public InvoZone minSize(float minSize){
        return this.minWidth(minSize).minHeight(minSize);
    }

    public InvoZone maxSize(float maxSize){
        return this.maxWidth(maxSize).maxHeight(maxSize);
    }

    public InvoZone setXY(Vector2f vector){
        return this.setX(vector.x()).setY(vector.y());
    }

    public InvoZone setRightY(Vector2f vector){
        return this.setRight(vector.x()).setY(vector.y());
    }

    public InvoZone setXDown(Vector2f vector){
        return this.setX(vector.x()).setDown(vector.y());
    }

    public InvoZone setRightDown(Vector2f vector){
        return this.setRight(vector.x()).setDown(vector.y());
    }

    public InvoZone shiftWH(float changeW, float changeH){
        this.setWidth(this.width() + changeW);
        this.setHeight(this.height() + changeH);
        return this;
    }

    public InvoZone shiftXY(float changeX, float changeY){
        this.setX(this.x() + changeX);
        this.setY(this.y() + changeY);
        return this;
    }
    public InvoZone inflate(float changeAmount){
        return this.inflate(changeAmount, changeAmount);
    }
    public InvoZone inflate(float changeWidth, float changeHeight){
        this.setX(this.x0 - changeWidth);
        this.setWidth(this.width + (changeWidth * 2));
        this.setY(this.y0 - changeHeight);
        this.setHeight(this.height + (changeHeight * 2));
        return this;
    }

    public InvoZone splitHeight(float divisor, float slices){
        this.setHeight(this.height/divisor);
        this.setHeight(this.height * slices);
        return this;
    }

    public InvoZone splitWidth(float divisor, float slices){
        this.setWidth(this.width/divisor);
        this.setWidth(this.width * slices);
        return this;
    }

    public InvoZone mirrorX(float xOrigin){
        float newX0 = this.x() + ((xOrigin - this.x()) * 2);
        float newX1 = this.right() + ((xOrigin - this.right()) * 2);
        this.setX(Math.min(newX0, newX1));
        return this;
    }
    public InvoZone invertX(){
        this.setX(this.right());
        this.setWidth(this.width * -1);
        return this;
    }

    public InvoZone floor(){
        this.setX((int)this.x());
        this.setY((int)this.y());
        this.setWidth((int)this.width());
        this.setHeight((int)this.height());
        return this;
    }

    public InvoZone absolute(){
        if (Math.signum(this.width) <= -0) this.invertX();
        if (Math.signum(this.height) <= -0) this.invertY();
        return this;
    }

    public InvoZone mirrorY(float yOrigin){
        float newY0 = this.y() + ((yOrigin - this.y()) * 2);
        float newY1 = this.down() + ((yOrigin - this.down()) * 2);
        this.setY(Math.min(newY0, newY1));
        return this;
    }
    public InvoZone invertY(){
        this.setY(this.down());
        this.setHeight(this.height * -1);
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
        this.shiftXY(Math.max(boundZone.x() - this.x(), Math.min(boundZone.right() - this.right(),0)),
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
        this.setX(centerX - this.width/2);
        return this;
    }
    public InvoZone centerY(float centerY){
        this.setY(centerY - this.height/2);
        return this;
    }

    public float right(){return this.x0 + this.width;}
    public InvoZone setRight(float x1){
        if (stretch) return this.setWidth(x1 - this.x());
        return this.setX(x1 - this.width);
    }
    public float middleX(){return this.x0 + this.width/2;}
    public float down(){return this.y0 + this.height;}
    public InvoZone setDown(float y1){
        if (stretch) return this.setHeight(y1 - this.y());
        else return this.setY(y1 - this.height);
    }
    public float middleY(){return this.y0 + this.height/2;}

    public Vector2f middle(){
        return new Vector2f(this.middleX(), this.middleY());
    }

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

        return InvoZone.fromPoints(new Vector2f(xRange.get(1), yRange.get(1)),
                new Vector2f(xRange.get(2), yRange.get(2)));
    }
    public InvoZone merge(InvoZone otherZone){
        otherZone = otherZone.absolute();

        this.setX(Math.min(this.x(), otherZone.x()));
        this.stretch(true).setRight(Math.max(this.right(), otherZone.right())).stretch(false);
        this.setY(Math.min(this.y(), otherZone.y()));
        this.stretch(true).setDown(Math.max(this.down(), otherZone.down())).stretch(false);
        return this;
    }

    public boolean inBounds(Vector2f point){
        boolean inXBounds = MathUtil.isInRange(point.x(), this.x(), this.right());
        boolean inYBounds = MathUtil.isInRange(point.y(), this.y(), this.down());
        return inXBounds && inYBounds;
    }

    public boolean inBounds(InvoZone boundZone, boolean fullyInBounds){
        boolean isTopLeftInBounds = boundZone.inBounds(this.topLeft());
        boolean isTopRightInBounds = boundZone.inBounds(this.topRight());
        boolean isBottomLeftInBounds = boundZone.inBounds(this.bottomLeft());
        boolean isBottomRightInBounds = boundZone.inBounds(this.bottomRight());

        if (fullyInBounds) return isTopLeftInBounds && isTopRightInBounds && isBottomLeftInBounds && isBottomRightInBounds;
        return isTopLeftInBounds || isTopRightInBounds || isBottomLeftInBounds || isBottomRightInBounds;
    }

    //TODO: Split into pieces, reuse code!
    public InvoZone gridPoint(float pointX, float pointY){
        float distance = pointX - this.x();
        int steps = (int) (distance/this.width());
        if (steps < 0) steps -= 1;
        this.shiftXY(steps * this.width(), 0);

        distance = pointY - this.y();
        steps = (int) (distance/this.height());
        if (steps < 0) steps -= 1;
        this.shiftXY(0, steps * this.height());

        return this;
    }

    public InvoZone gridShift(float xMultiplier, float yMultiplier){
        return this.shiftXY(this.width() * xMultiplier,
                this.height() * yMultiplier);
    }

    public ScreenRectangle rect(){
        return new ScreenRectangle((int) this.x(), (int) this.y(), (int) this.width, (int) this.height);
    }

    public InvoZone copy(){
        return new InvoZone(this.x0, this.width, this.y0, this.height, this.stretch);
    }
    public InvoZone copy(InvoZone otherZone){
        this.setX(otherZone.x());
        this.setWidth(otherZone.width());
        this.setY(otherZone.y());
        this.setHeight(otherZone.height());
        this.stretch(otherZone.stretch);
        return this;
    }

    public InvoZone multiply(float multiplier){
//        InvoZone originalZone = this.copy();
        this.setWidth(this.width * multiplier);
        this.setHeight(this.height * multiplier);
//        this.center(originalZone);
        return this;
    }

    public static Vector2f changeRelativeMultiply(Vector2f point, InvoZone startZone, InvoZone endZone){
        return new InvoZone(point.x, 1, point.y, 1).changeRelativeMultiply(startZone, endZone).topLeft();
    }

    public InvoZone changeRelativeMultiply(InvoZone startZone, InvoZone endZone){
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

    public static Vector2f changeRelativeAdd(Vector2f point, InvoZone startZone, InvoZone endZone, ANCHORPOINT topLeftAnchor) {
        return new InvoZone(point.x, 1, point.y, 1).changeRelativeAdd(startZone, endZone, topLeftAnchor, topLeftAnchor).topLeft();
    }

    public InvoZone changeRelativeAdd(InvoZone startZone, InvoZone endZone, ANCHORPOINT topLeftAnchor){
        return this.changeRelativeAdd(startZone, endZone, topLeftAnchor, topLeftAnchor);
    }

    public InvoZone changeRelativeAdd(InvoZone startZone, InvoZone endZone, ANCHORPOINT topLeftAnchor, ANCHORPOINT botRightAnchor){
        Vector2f topLeftDistance = this.topLeft().sub(startZone.getAnchorPoint(topLeftAnchor));
        Vector2f botRightDistance = this.bottomRight().sub(startZone.getAnchorPoint(botRightAnchor));

        boolean stretchState = this.stretch;
        this.stretch(true);
        this.setXY(endZone.getAnchorPoint(topLeftAnchor).add(topLeftDistance));
        this.setRightDown(endZone.getAnchorPoint(botRightAnchor).add(botRightDistance));
        this.stretch = stretchState;

        return this;
    }
    
    public Vector2f getAnchorPoint(ANCHORPOINT anchorpoint){
        switch (anchorpoint){
            case TOP_LEFT -> {
                return topLeft();
            }
            case TOP_RIGHT -> {
                return topRight();
            }
            case BOTTOM_LEFT -> {
                return bottomLeft();
            }
            case BOTTOM_RIGHT -> {
                return bottomRight();
            }
        }
        return topLeft();
    }

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        StringBuilder builder = new StringBuilder();
        builder.append(this.x())
                .append(":").append(this.width())
                .append(":").append(this.y())
                .append(":").append(this.height())
                .append(":").append(this.stretch);
        tag.putString(DIMENSION_FLOATS, builder.toString());

        return tag;
    }

    public void deserializeNBT(CompoundTag tag){
        if (!tag.contains(DIMENSION_FLOATS)) return;
        String[] stringArray = tag.getString(DIMENSION_FLOATS).split(":");
        if (stringArray.length != 5){
            LOGGER.error("[Invocore] Invozone string is malformed!: " + tag.getString(DIMENSION_FLOATS));
            return;
        }

        this.stretch(false).setX(Float.parseFloat(stringArray[0]))
                .setWidth(Float.parseFloat(stringArray[1]))
                .setY(Float.parseFloat(stringArray[2]))
                .setHeight(Float.parseFloat(stringArray[3])).stretch(Boolean.parseBoolean(stringArray[4]));
    }

    @Override
    public String toString() {
        return "[x:"+ this.x() + ", width:" + this.width()
        + ", y:" + this.y() + ", height:" +  this.height() + ", stretch:"  + (this.stretch) + "]";
    }
}