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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class PolygonTransform {

    static ArrayList<Polygon> polygons = new ArrayList<>();
    
    //program starts with reading the coordinates file. 
    public static void main(String[] args) {
        try {
            File myObj = new File("coordinates.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String value = myReader.nextLine();
                String[] str = value.split(" ");

                switch (str[0]) {
                    case "P":
                        polygons.add(new Polygon(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3])));
                        break;
                    case "T":
                        break;
                    case "r":
                        polygons.get(polygons.size()-1).getTransforms().add(new Rotate(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3])));
                        break;
                    case "s":
                        polygons.get(polygons.size()-1).getTransforms().add(new Scale(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]), Float.parseFloat(str[4])));
                        break;
                    case "t":
                        polygons.get(polygons.size()-1).getTransforms().add(new Translate(Float.parseFloat(str[1]), Float.parseFloat(str[2])));
                        break;
                    default:
                        polygons.get(polygons.size()-1).addPoints(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
                        break;
                }
            }
            myReader.close();
            PolygonTransform  app = new PolygonTransform();
            app.start();
        }
        catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
    
    
    public void start(){
        try{
            createWindow();
            initL();
            render();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640,480));
        Display.setTitle("Polygon Transformation");
        Display.create();
    }

    private void initL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glOrtho(-320, 320, -240, 240, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    private void render() {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                glPointSize(2);
                
//               testing case
//                for(int i =0; i< polygons.size();i++){
//                    glPushMatrix();
//                    polygons.get(i).transform();
//                    glBegin(GL_POLYGON);
//                    polygons.get(i).getPoints().forEach(p ->glVertex2f (p.getX(), p.getY()));
//                    glEnd();
//                    glPopMatrix();
//                }
                for(int i =0; i< polygons.size();i++){
                    glPushMatrix();
                    polygons.get(i).transform();
                    glBegin(GL_POINTS);
                    polygons.get(i).fill();
                    glEnd();
                    glPopMatrix();
                }
                
                Display.update();
                Display.sync(60);
            }catch(Exception e){
                
            }
        }
        Display.destroy();
    }

}
