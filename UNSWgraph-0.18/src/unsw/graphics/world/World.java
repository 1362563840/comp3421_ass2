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
        
        this.DarkMode = 1;
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
		CoordFrame3D frame = CoordFrame3D.identity();
		this.camera3d.setView(gl);
		
		Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.4f, 0.4f, 0.4f));
        
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.3f, 0.3f, 0.3f));
        Shader.setFloat(gl, "phongExp", 4f);
        Shader.setPenColor(gl, Color.WHITE);
		
		
		if ( this.normal_mode == true ) {
			this.terrain.turn_on_normal();
			super.setBackground( Color.WHITE );	
			this.terrain.recursively_draw( gl , frame , this.camera3d.View_trans() );
		}
		else {
			this.terrain.turn_off_normal();
			super.setBackground( new Color( 32 , 32 , 32 ) );
			Shader.setFloat(gl, "cutOff", 10f );
			Shader.setFloat(gl, "constant", 1f );
			Shader.setFloat(gl, "linear", 0.09f );
			Shader.setFloat(gl, "quadratic", 0.032f );
			this.terrain.recursively_draw( gl , frame , this.camera3d.View_trans() );
		}
		
		if( this.torch_on_off == true ) {
			this.terrain.turn_on_flash();
		}
		else {
			this.terrain.turn_off_flash();
		}
		
		if ( this.rain_mode == true ) {
			this.terrain.turn_on_rain();
		}
		else {
			this.terrain.turn_off_rain();
		}
		
		if ( this.sun_mode == true ) {
			this.terrain.turn_on_sun();
		}
		else {
			this.terrain.turn_off_sun();
		}
		

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
	
	private boolean normal_mode = true;
	private boolean torch_mode = false;
	private boolean torch_on_off = false;
	private boolean rain_mode = false;
	private boolean sun_mode = false;
	
	@Override
    public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_N:
			this.torch_on_off = false;
			this.normal_mode = true;
			this.torch_mode = false;
            break;
		
	    case KeyEvent.VK_T:
	    	this.normal_mode = false;
	    	this.torch_mode = true;
	        break;
	    
	    case KeyEvent.VK_SPACE:
	    	if ( this.torch_mode == true ) {
	    		this.torch_on_off = !this.torch_on_off;
	    	}
	        break;
		
		case KeyEvent.VK_R:
			this.rain_mode = !this.rain_mode;
		    break;
		
		case KeyEvent.VK_Q:
			if ( this.normal_mode == true ) {
				this.sun_mode = !this.sun_mode;
			}
		    break;
		
	    case KeyEvent.VK_P:
			if ( this.sun_mode == true ) {
				this.terrain.sun_switch();
			}
		    break;
	    case KeyEvent.VK_O:
			if ( this.sun_mode == true ) {
				this.terrain.color_switch();
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
