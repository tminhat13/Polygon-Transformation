/***************************************************************
* file: JavaApplication1.java
* author: Nhat Minh Tran
* class: CS 4450.01 Computer Graphics
*
* assignment: program 2
* date last modified: 9/30/2023
*
* purpose: This program uses the LWJGL library to draw a window of 640x480
* program should then read in coordinates from a file titled coordinates.txt 
* draw, fill, and transform polygon
****************************************************************/
package polygontransform;

// Create Transform object

import static org.lwjgl.opengl.GL11.*;

class Transform {
    void render(){};
    public String getType(){
        return "Transform";
    };
    
}

//create Rotate object inherit from Transform
class Rotate extends Transform {

    private float angle, pivotPointX, pivotPointY;
    Rotate(float angle, float pivotPointX, float pivotPointY) {
        this.angle=angle;
        this.pivotPointX=pivotPointX;
        this.pivotPointX=pivotPointY;
    }

    //getters and setters
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getPivotPointX() {
        return pivotPointX;
    }

    public void setPivotPointX(float pivotPointX) {
        this.pivotPointX = pivotPointX;
    }

    public float getPivotPointY() {
        return pivotPointY;
    }

    public void setPivotPointY(float pivotPointY) {
        this.pivotPointY = pivotPointY;
    }
    @Override
    public String getType() {
        return "Rotate";
    }
    @Override
    public String toString(){
        return "[" +angle+" " + pivotPointX+" " + pivotPointY +"]";
    }
    @Override
    public void render(){
        //glTranslatef(220+((420-220)/2),150f,0f); // moves polygon back
        glRotatef(this.getAngle(),this.getPivotPointX(),this.getPivotPointY(),1f);
        //glTranslatef(-220-((420-220)/2),-150,0f); // moves polygon to origin
    }
}

class Scale extends Transform {

    private float factorX, factorY, pivotPointX, pivotPointY;
    Scale(float factorX, float factorY, float pivotPointX, float pivotPointY) {
        this.factorX=factorX;
        this.factorY=factorY;
        this.pivotPointX=pivotPointX;
        this.pivotPointY=pivotPointY;
    }

    //getters and setters
    public float getFactorX() {
        return factorX;
    }

    public void setFactorX(float factorX) {
        this.factorX = factorX;
    }

    public float getFactorY() {
        return factorY;
    }

    public void setFactorY(float factorY) {
        this.factorY = factorY;
    }

    public float getPivotPointX() {
        return pivotPointX;
    }

    public void setPivotPointX(float pivotPointX) {
        this.pivotPointX = pivotPointX;
    }

    public float getPivotPointY() {
        return pivotPointY;
    }

    public void setPivotPointY(float pivotPointY) {
        this.pivotPointY = pivotPointY;
    }
    
    @Override
    public String getType() {
        return "Scale";
    }
    @Override
    public String toString(){
        return "[" +factorX+" " + factorY+" " + pivotPointX+" " + pivotPointY +"]";
    }
    
    @Override
    void render(){
        glScalef(this.factorX,this.factorY, 0);
    }
}

class Translate extends Transform {

    private float xDirection, yDirection;
    Translate(float xDirection, float yDirection) {
        this.xDirection=xDirection;
        this.yDirection=yDirection;
    }

    // getters and setters
    public float getxDirection() {
        return xDirection;
    }

    public void setxDirection(float xDirection) {
        this.xDirection = xDirection;
    }

    public float getyDirection() {
        return yDirection;
    }

    public void setyDirection(float yDirection) {
        this.yDirection = yDirection;
    }
    
    @Override
    public String getType() {
        return "Translate";
    }
    @Override
    public String toString(){
        return "[" +xDirection+" " + yDirection+"]";
    }
    @Override
    void render(){
        glTranslatef(this.xDirection,this.yDirection,0);
    }
}