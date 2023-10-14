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

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Comparator;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Point;

// create polygon object to manage data
class Polygon {
    
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Edge> allEdges = new ArrayList<>();
    private ArrayList<Edge> global = new ArrayList<>();
    private ArrayList<Edge> active = new ArrayList<>();
    private float[] rgb = new float[3];
    private ArrayList<Transform> transforms = new ArrayList<>();
    private float xMax, xMin, yMax, yMin;
    
    Polygon(float r, float g, float b) {
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }

    void addPoints(int x, int y) {
        points.add(new Point(x, y));
    }

    //getters and setters
    
    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public float[] getRgb() {
        return rgb;
    }

    public void setRgb(float[] rgb) {
        this.rgb = rgb;
    }

    public ArrayList<Transform> getTransforms() {
        return transforms;
    }

    public ArrayList<Edge> getAllEdges() {
        return allEdges;
    }

    public ArrayList<Edge> getGlobal() {
        return global;
    }

    public ArrayList<Edge> getActive() {
        return active;
    }
    
    public void setTransforms(ArrayList<Transform> transforms) {
        this.transforms = transforms;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public float getxMin() {
        return xMin;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public float getyMin() {
        return yMin;
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }
    
    
    //start and print edge tables for reference
    public void iniEdgeTables(){
        
        System.out.println();
        this.getPoints().forEach(val -> System.out.print("["+val.getX() + ", " + val.getY()+"] "));
        System.out.println();
        this.getTransforms().forEach(val -> System.out.println(val.getType()+val));
        this.setAllEdges();
        this.setGlobal();
        System.out.println("All Edges");
        this.getAllEdges().forEach(val -> System.out.println(val));
        System.out.println("Global Edges");
        this.getGlobal().forEach(val->System.out.println(val));
        System.out.println();
    }

    //update all_edges table
    private void setAllEdges() {
        this.allEdges.clear();
        for(int i =0; i<this.getPoints().size()-1;i++){
            Point p0 = this.getPoints().get(i);
            Point p1 = this.getPoints().get(i+1);
            float yMinL=min(p0.getY(),p1.getY()), yMaxL=max(p0.getY(),p1.getY());
            
            float xVal;
            if(p0.getY()==yMinL){
                xVal = p0.getX();
            }
            else{
                xVal = p1.getX();
            }
            
            if((p0.getY()-p1.getY())!=0){
                float m_1 = (float) (p1.getX()-p0.getX())/(p1.getY()-p0.getY());
                //m_1 = (float) (Math.round(m_1*10.0)/10.0);
                Edge e1 = new Edge(yMinL, yMaxL, xVal, m_1);
                this.allEdges.add(e1);
            }
        }
    }

    //update global edges table by shalow copy of all edge table
    // get x min and max of the polygon to make scanning easier
    // sort global table by yMin > xVal > yMax
    private void setGlobal() {
        
        this.global= new ArrayList(this.getAllEdges());
        
        Comparator<Edge> sort = Comparator.comparing(Edge::getxVal);
        
        this.global.sort(sort);
        this.setxMin(this.getGlobal().get(0).xVal);
        this.setxMax(this.getGlobal().get(this.getGlobal().size()-1).getxVal());
        Comparator<Edge> sortY = Comparator.comparing(Edge::getyMax);
        this.global.sort(sortY);
        this.setyMax(this.getGlobal().get(this.getGlobal().size()-1).getyMax());
        Comparator<Edge> sortComp = Comparator.comparing(Edge::getyMin)
                                            .thenComparing(Edge::getxVal)
                                            .thenComparing(Edge::getyMax)
                                            .thenComparing(Edge::getM_1);
        
        this.global.sort(sortComp);
        this.setyMin(this.getGlobal().get(0).yMin);
    }
    
    //=========================================================================
    //fill method
    // set Color
    // update edge tables
    // init scanLine and Active table
    public void fill() {
        
        Comparator<Edge> sort = Comparator.comparing(Edge::getxVal);
        glColor3f(getRgb()[0], getRgb()[1], getRgb()[2]);
        this.iniEdgeTables();
        float scanLine = this.getGlobal().get(0).getyMin();
        this.getActive().add(this.getGlobal().remove(0));
        while(!this.getActive().isEmpty()){
            //
            int parity = 0;
            for(int i=0; i<this.getActive().size(); i++){
                if ((this.getActive().get(i).getyMax())==scanLine){
                    this.getActive().remove(i);
                    i--;
                }
            }
            System.out.print(scanLine);
            for(int i=0; i<this.getGlobal().size(); i++){
                if ((this.getGlobal().get(i).getyMin())==scanLine){
                    this.getActive().add(this.getGlobal().remove(i));
                    i--;
                }
            }
            this.getActive().sort(sort);
            System.out.println("Global Edges");
            this.getGlobal().forEach(val->System.out.println(val));
            System.out.println("Active Edges");
            this.getActive().forEach(val->System.out.println(val));
            for(int x = (int) (xMin-1); x <= xMax+1; x++){
                if(parity==1){
                    glVertex2f(x, scanLine);
                }
                for (Edge e : this.getActive()) {
                    if(Math.round(e.getxVal())==x){
                        parity = 1 - parity;
                    }
                }
            }
            for (Edge e : this.getActive()) {
                e.setxVal(e.getxVal()+e.getM_1());
            }
            this.getActive().sort(sort);
            
            scanLine++;
        } 
    }
    
    // transformation method
    // call the tranform objects backward to render
    void transform(){
//        boolean toOrigin = false;
        for (int i=this.getTransforms().size()-1; i>=0; i-- ){
            Transform t = this.getTransforms().get(i);
            t.render();
//            if(t.getType().equals("Rotate") || t.getType().equals("Scale")){
//                if(!toOrigin){
//                    toOrigin = true;
//                    //glTranslatef(xMin+((xMax-xMin)/2),yMin+((yMax-yMin)/2),0f); // moves polygon back
//                    //System.out.print(xMin+((xMax-xMin)/2) + " " + yMin+((yMax-yMin)/2));
//                }
//                t.render();
//            }
//            else if(t.getType().equals("Translate")){
//                if(toOrigin){
//                    toOrigin = false;
//                    //glTranslatef(-xMin-((xMax-xMin)/2),-yMin-((yMax-yMin)/2),0f); // moves polygon to origin 
//                }
//                t.render();
//            }
        }
//        if(toOrigin){
//            //glTranslatef(-xMin-((xMax-xMin)/2),-yMin-((yMax-yMin)/2),0f); // moves polygon to origin 
//        }
    }
    
    
    //=========================================================================
    //Edge Class to manage Edge values
    private static class Edge {

        float yMin, yMax, xVal, m_1;
        public Edge() {
        }
        public Edge(Edge e) {
            this.yMin = e.getyMin();
            this.yMax = e.getyMax();
            this.xVal = e.getxVal();
            this.m_1 = e.getM_1();
        }

        private Edge(float yMin, float yMax, float xVal, float m_1) {
            this.yMin=yMin;
            this.yMax=yMax;
            this.xVal=xVal;
            this.m_1=m_1;
        }

        // getters and setters
        
        public float getyMin() {
            return yMin;
        }

        public void setyMin(float yMin) {
            this.yMin = yMin;
        }

        public float getyMax() {
            return yMax;
        }

        public void setyMax(float yMax) {
            this.yMax = yMax;
        }

        public float getxVal() {
            return xVal;
        }

        public void setxVal(float xVal) {
            this.xVal = xVal;
        }

        public float getM_1() {
            return m_1;
        }

        public void setM_1(float m_1) {
            this.m_1 = m_1;
        }
        
        @Override
        public String toString(){
            return "[" +yMin +" " + yMax +" " + xVal +" " + m_1+"]";
        }
    }

    
}
