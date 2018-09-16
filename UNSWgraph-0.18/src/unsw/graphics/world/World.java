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
    
    public World(Terrain terrain) {
    	super("Assignment 2", 1200, 1000);
        this.terrain = terrain;
        this.clockwise = 0;
        this.anticlockwise = 0;
        this.camera3d = new Camera3D();
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
		// adjust frustrum
		// if 0 , 0 , 0 , then the default camera is at same z coordinate with object
		CoordFrame3D frame = CoordFrame3D.identity()
//                .translate(0, 0, -3 + this.z )
                .translate(0, 0, -15  )
//                .scale(0.7f, 0.7f, 0.7f)
                .rotateY( this.clockwise )
                .rotateY( this.anticlockwise );
		//------------------------------------------
		this.camera3d.setView(gl);
		
		// each 1s, 60 frames, this display should be called
		this.terrain.recursively_draw( gl , frame );
		
		// Debug -------------------------------------
		
		Triangle3D t1 = new Triangle3D( 0 , 0 , 0,
				  1 , 1 , 0, 
				  -1 , 1 , 0);

//		Shader.setPenColor( gl , Color.RED);
////		t1.draw( gl , frame);
//		
//		ArrayList< Point3D > a = new ArrayList< Point3D >();
//		a.add( new Point3D(  -5 , 0 , 0 ) );
//		a.add( new Point3D(  5 , 0 , 0 ) );
//		a.add( new Point3D(  0 , 5 , 0 ) );
//		
//		ArrayList< Point2D > b = new ArrayList< Point2D >();
//		b.add( new Point2D( 0 , 0 ) );
//		b.add( new Point2D( 0.5f , 1 ) );
//		b.add( new Point2D( 1 , 0 ) );
//		
////		this.main_frame.traslate
////		CoordFrame3D frame1 = CoordFrame3D.identity().translate( 0 , 0 , -15 );
//		this.main_frame.translate(0, 0, this.z);
//		TriangleMesh b_mesh = new TriangleMesh( a , true , b ); 
//		b_mesh.init( gl );
//		
//		Texture texture = new Texture(gl, "res/textures/canLabel.bmp", "bmp", false);
//		Shader.setInt(gl, "tex", 0);
//        gl.glActiveTexture(GL.GL_TEXTURE0);
//        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getId());
        
//		b_mesh.draw( gl , main_frame );
//		b_mesh.draw( gl , frame );
	}

	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		
		// Terrain recursively destory
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
//		this.getWindow().addKeyListener( this );
		this.getWindow().addKeyListener( this.camera3d );
		this.z = -15;
		this.z = -15;
		this.main_frame = CoordFrame3D.identity().translate( 0 , 0 , z );
		System.out.println( "shoudl be first " );
		// Our codes :
		this.terrain.init( gl );
		System.out.println( "shoudl be third " );
		//TODO terrian init() need to be called
		
	}
	@Override
    public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
//			case KeyEvent.VK_UP:
//            	this.z--;
//	            break;
//			case KeyEvent.VK_DOWN:
//            	this.z++;
//	            break;    
//			case KeyEvent.VK_LEFT:
//            	this.clockwise++;
//	            break;    
//			case KeyEvent.VK_RIGHT:
//            	this.anticlockwise--;
//	            break;        
	        
		}
	}

	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, 1, 100));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}
}
