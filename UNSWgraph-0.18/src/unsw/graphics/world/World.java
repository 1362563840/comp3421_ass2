package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.*;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class World extends Application3D implements KeyListener{

    private Terrain terrain;
    private CoordFrame3D main_frame;
    private float z;
    private float clockwise;
    private float anticlockwise;
    private Camera3D camera3d;
    
    private int DarkMode;
    
    public World(Terrain terrain) {
    	super("Assignment 2", 1200, 1200);
        this.terrain = terrain;
        this.clockwise = 0;
        this.anticlockwise = 0;
        this.camera3d = new Camera3D(terrain);
        
        this.DarkMode = 0;
    }
   
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        World world = new World(terrain);
        world.start();
    }

	@Override
	public void display(GL3 gl) {
		super.display(gl);
		
		// if 0 , 0 , 0 , then the default camera is at same z coordinate with object
		
		//  --------------------------------- for torch light
		Shader.setInt(gl, "isDay", this.DarkMode );
		
		if ( this.DarkMode == 0 ) {
			super.setBackground( new Color( 32 , 32 , 32 ) );
		}
		else {
			super.setBackground( Color.WHITE );
		}
		
//		Shader.setPoint3D(gl, "lightPos", this.camera3d.CameraPostion() );
		Shader.setFloat(gl, "x", this.camera3d.CameraPostion().getX() );
		Shader.setFloat(gl, "y", this.camera3d.CameraPostion().getY() );
		Shader.setFloat(gl, "z", this.camera3d.CameraPostion().getZ() );
//		Shader.setPoint3D(gl, "lightPos", this.terrain.getSunlight().asPoint3D() );
//		Shader.setPoint3D(gl, "normal_light_outside" , this.camera3d.CameraNormal() );
		
		Shader.setFloat(gl, "cutOff", 10f );
		Shader.setFloat(gl, "constant", 1f );
		Shader.setFloat(gl, "linear", 0.09f );
		Shader.setFloat(gl, "quadratic", 0.032f );
		
        Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.7f, 0.7f, 0.7f));
        
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.3f, 0.3f, 0.3f));
        Shader.setFloat(gl, "phongExp", 4f);
        //  --------------------------------- for torch light
	
		CoordFrame3D frame = CoordFrame3D.identity()
//                .translate(0, 0, -3 + this.z )
//                .translate(0, 0, -18  )
//                .scale(0.7f, 0.7f, 0.7f)
                .rotateY( this.clockwise )
//                .translate( 0 , 0 , 2 )
//                .scale(2f, 2f, 2f)
                ;
		//------------------------------------------
//		this.camera3d.setView(gl);
		// each time camera view is changed, need to adjust the new normal of the light
		
		this.camera3d.setView(gl);		
		
		
		// each 1s, 60 frames, this display should be called
		this.terrain.recursively_draw( gl , frame );
//		this.clockwise += 1;
		
//		this.terrain.drawSelf(gl, frame);
//		
//		super.s_shader2(gl);
//
//		this.terrain.drawRoad(gl, frame);
//		
//		super.s_shader1(gl);
//		
//		this.terrain.drawTree(gl, frame);
//		

	}

	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		
		// Terrain recursively destory
		this.terrain.destroy( gl );
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);	

		
		this.getWindow().addKeyListener( this );
		this.getWindow().addKeyListener( this.camera3d );
		this.z = -15;
		this.z = -15;
		this.main_frame = CoordFrame3D.identity().translate( 0 , 0 , z );
		// Our codes :
		this.terrain.init( gl );
		//TODO terrian init() need to be called
		
		
		
		
	}
	@Override
    public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_V:
			if ( this.DarkMode == 0 ) {
				this.DarkMode = 1;
			}
			else {		
				this.DarkMode = 0;
			}
            break;
		}
	}

	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, 1, 100));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 
		
	}
}
