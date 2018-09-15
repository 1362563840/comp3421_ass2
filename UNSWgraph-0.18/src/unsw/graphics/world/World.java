package unsw.graphics.world;

import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
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
		System.out.println( "shoudl be seocond" );
		// each 1s, 60 frames, this display should be called
		this.terrain.recursively_draw( gl , CoordFrame3D.identity() );
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
