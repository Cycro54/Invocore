package invoker54.invocore.client.util;

public class InvoZone {
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

    public InvoZone mirrorY(float yOrigin){
        float newY0 = this.y() + ((yOrigin - this.y()) * 2);
        float newY1 = this.down() + ((yOrigin - this.down()) * 2);
        this.y0 = Math.min(newY0, newY1);
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
        this.shift(Math.max(boundZone.x() - this.x(), Math.min(boundZone.right() - this.right(),0)),
                Math.max(boundZone.y() - this.y(), Math.min(boundZone.down() - this.down(),0)));

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
    public float middleX(){return this.x0 + this.width/2;}
    public float down(){return this.y0 + this.height;}
    public InvoZone setDown(float y1){return this.setY(y1 - this.height);}
    public float middleY(){return this.y0 + this.height/2;}

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
}