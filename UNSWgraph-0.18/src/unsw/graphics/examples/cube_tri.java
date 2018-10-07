package unsw.graphics.examples;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * A simple example that draws a cube.
 * 
 * You can use this to play around with rotations and projection.
 * 
 * @author Robert Clifton-Everest
 *
 */
public class cube_tri extends Application3D {
    
    private float rotationX, rotationY;
    
    private Texture text_graph;
    
    public cube_tri() {
        super("Cube", 1200, 800);
        rotationX = 0;
        rotationY = 0;
    }

    @Override
    public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
//        Shader.setProjMatrix(gl, Matrix4.frustum(-1, 1, -1, 1, 1, 10));
        Shader.setProjMatrix(gl, Matrix4.perspective(60, 1, 1, 10));
    }

    public static void main(String[] args) {
    	cube_tri example = new cube_tri();
        example.start();
    }
    
    @Override
    public void display(GL3 gl) {
    	Shader shader = new Shader(gl, "shaders/vertex_tex_3d.glsl",
    			"shaders/fragment_tex_3d.glsl");   	
        shader.use(gl);

        gl.glPointSize(10);
        
        // Turn on the depth buffer
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        // Cull back faces
        gl.glEnable(GL.GL_CULL_FACE);
        super.display(gl);
        
//        CoordFrame3D frame = CoordFrame3D.identity()
//                .translate(0, 0, -2)
//                .scale(0.5f, 0.5f, 0.5f);
        
        CoordFrame3D frame = CoordFrame3D.identity()
                .translate(0, 0, -2)
                .scale(0.5f, 0.5f, 0.5f)
                .rotateX( rotationX )
                .rotateY( rotationY );
        
//        drawCube(gl, frame.rotateY(rotationY).rotateX(rotationX));
        drawCube(gl, frame);
//        rotationX += 1;
        rotationY += 1;
    }

    /**
     * Draw a cube centered around (0,0) with bounds of length 1 in each direction.
     * @param gl
     * @param frame
     */
    private void drawCube(GL3 gl, CoordFrame3D frame) {
    	this.import_texture(gl);
//        TriangleFan3D face = new TriangleFan3D(-1,-1,1, 1,-1,1, 1,1,1, -1,1,1);
    	
    	
//        
//        Shader.setPoint3D(gl, "lightPos", new Point3D( 0 , 0 , 5 ) );
//        
//        Shader.setColor(gl, "lightIntensity", Color.WHITE);
//        Shader.setColor(gl, "ambientIntensity", new Color(0.7f, 0.7f, 0.7f));
//        
//        // Set the material properties
//        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
//        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
//        Shader.setColor(gl, "specularCoeff", new Color(0.3f, 0.3f, 0.3f));
//        Shader.setFloat(gl, "phongExp", 4f);
        ArrayList<Point3D> t1 = new ArrayList<Point3D>();
        t1.add( new Point3D( -1,-1,1 ) );
        t1.add( new Point3D( 1,-1,1 ) );
        t1.add( new Point3D( 1,1,1 ) );
        
        ArrayList<Point2D> t2 = new ArrayList<Point2D>();
        t2.add( new Point2D( 0 , 0 ) );
        t2.add( new Point2D( 1 , 0 ) );
        t2.add( new Point2D( 1 , 1 ) );
        
        
        t1.add( new Point3D( -1,-1,1 ) );
        t1.add( new Point3D( 1,1,1 ) );
        t1.add( new Point3D( -1,1,1 ) );
        
        t2.add( new Point2D( 0 , 0 ) );
        t2.add( new Point2D( 1 , 1 ) );
        t2.add( new Point2D( 0 , 1 ) );
        
        
        TriangleMesh tm1 = new TriangleMesh( t1 , true , t2 );
        tm1.init(gl);
        
        Shader.setInt(gl, "tex", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.text_graph.getId());
        float scale = 0.8f;
        // Front
        Shader.setPenColor(gl, Color.WHITE);
        tm1.draw(gl, frame.scale(scale, scale, scale));
        
        // Left
//        Shader.setPenColor(gl, Color.BLUE);
        tm1.draw(gl, frame.scale(scale, scale, scale).rotateY(-90));
        
        // Right
//        Shader.setPenColor(gl, Color.GREEN);
        tm1.draw(gl, frame.scale(scale, scale, scale).rotateY(90));
        
        // Back
//        Shader.setPenColor(gl, Color.CYAN);
        tm1.draw(gl, frame.scale(scale, scale, scale).rotateY(180));
        
        // Bottom
//        Shader.setPenColor(gl, Color.YELLOW);
        tm1.draw(gl, frame.scale(scale, scale, scale).rotateX(-90));
        
        // Top
//        Shader.setPenColor(gl, Color.MAGENTA);
        tm1.draw(gl, frame.scale(scale, scale, scale).rotateX(90));
    }
    
    public void import_texture( GL3 gl ) {
    	this.text_graph = new Texture( gl, "res/textures/rain.bmp", "bmp", false );
    }
}