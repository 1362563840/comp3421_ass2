package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.opengl.GL3;

import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.*;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class World extends Application3D {

    private Terrain terrain;

    public World(Terrain terrain) {
    	super("Assignment 2", 1200, 1000);
        this.terrain = terrain;
   
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
                .translate(0, 0, -10)
                .scale(0.5f, 0.5f, 0.5f);
		// each 1s, 60 frames, this display should be called
		this.terrain.recursively_draw( gl , frame );
		Triangle3D t1 = new Triangle3D( 0 , 0 , 0,
				  1 , 1 , 0, 
				  -1 , 1 , 0);

		Shader.setPenColor( gl , Color.YELLOW);
		t1.draw( gl , frame);
	}

	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		
		// Terrain recursively destory
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
		
		System.out.println( "shoudl be first " );
		// Our codes :
		this.terrain.init( gl );
		System.out.println( "shoudl be third " );
		//TODO terrian init() need to be called
		
	}

	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, 1, 100));
	}
}
