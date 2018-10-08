package unsw.graphics.examples;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.Application2D;
import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Triangle2D;
import unsw.graphics.geometry.Triangle3D;

public class TwoTriangles3D extends Application3D {

    public TwoTriangles3D() {
        super("Two triangles 3D", 600, 600);
        setBackground(new Color(1f,1f,0));
    }
    
    public static void main(String[] args) {
    	TwoTriangles3D example = new TwoTriangles3D();
        example.start();
    }
    
    @Override 
    public void display(GL3 gl) {
        super.display(gl);
        CoordFrame3D frame = CoordFrame3D.identity()
                .translate(0, 0, 0)
                .scale(0.5f, 0.5f, 0.5f);
        Triangle3D t1 = new Triangle3D( -1, -1 , 1,
        		  						1 , -1 , 1, 
        		  						1 , 1 , 1);
       
        Triangle3D t2 = new Triangle3D( 0 , 1 , 0,
				  0 , 0 , 1, 
				  1 , 0 , 0);
        Triangle3D t3 = new Triangle3D( 0 , 1 , 0,
				  1 , 0 , 0, 
				  0 , 0 , 1);
        Triangle2D tri2 = new Triangle2D(0, 0, -1, -1, 1, -1);
        Shader.setPenColor(gl, Color.RED);
        t1.draw(gl , frame);
        t2.draw(gl , frame);
    }
    
     
    public void draw_self(GL3 gl , CoordFrame3D frame ) {
        super.display(gl);
        frame = frame.scale(0.5f, 0.5f, 0.5f);
        Triangle3D t1 = new Triangle3D( -1, -1 , 1,
        		  						1 , -1 , 1, 
        		  						1 , 1 , 1);
       
        Triangle3D t2 = new Triangle3D( 0 , 1 , 0,
				  0 , 0 , 1, 
				  1 , 0 , 0);
        Triangle3D t3 = new Triangle3D( 0 , 1 , 0,
				  1 , 0 , 0, 
				  0 , 0 , 1);
        Triangle2D tri2 = new Triangle2D(0, 0, -1, -1, 1, -1);
        Shader.setPenColor(gl, Color.RED);
        t1.draw(gl , frame);
        t2.draw(gl , frame);
    }
    

}
