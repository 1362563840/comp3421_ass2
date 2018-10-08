package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.ColorBuffer;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Point3DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;

/**
 * Displays fireworks using a particle system. Taken from NeHe Lesson #19a:
 * Fireworks
 * 
 * @author Robert Clifton-Everest
 */
public class Rain_particle {
	
	private Random rand;
	
	private Terrain terrian;
	
	private int how_many_rain;
	
	private ArrayList< Point3D > init_local_pos;
		
	private final float offset = 2.0f;
	
	private ArrayList< Cube > rain_cubes;
	
	private final float length = 0.5f;
	
	private float time_regen;
	
	/**
	 * after know the the coordinates of the four angle
	 * @param terrain
	 */
	public Rain_particle ( Terrain terrain ) {
		this.terrian = terrain;
		this.how_many_rain = 5;
		this.init_local_pos = new ArrayList< Point3D >();
		this.rain_cubes = new ArrayList< Cube >();
		this.rand = new Random(1);
		this.time_regen = 0;
	}
	
	public void init( GL3 gl ) {
		float width = this.terrian.getX_length();
		float depth = this.terrian.getZ_length();
		// initial before assigning
		
		for (  int i = 0 ; i < this.how_many_rain ; i++  ) {
			// x is for, y is for z
			
			assert (  width >= 2 );
			assert (  depth >= 2 );

			
			Point2D temp = new Point2D( this.rand.nextFloat() * ( width - 1 ) , this.rand.nextFloat() * ( depth - 1 )  );
			float height = this.terrian.altitude( temp.getX() , temp.getY() );
			
			this.init_local_pos.add( new Point3D( temp.getX() , 
													height + this.offset , 
													temp.getY() 
												) 
									);
			
			Cube temp_c = new Cube( this.length );
			temp_c.init( gl );
			this.rain_cubes.add( temp_c );
			
		}
		
		
	}
	
	public void recreate_rain( GL3 gl ) {
		this.init(gl);
	}
	
	public void remove_finsihed_rain() {
		this.init_local_pos.clear();
		this.rain_cubes.clear();
	}
	
	/**
	 * need to set the texture
	 * @param gl
	 * @param frame
	 */
	public void drawSelf( GL3 gl , CoordFrame3D frame ) {
		
//		System.out.println( " time_regen is " + this.time_regen );
		if ( this.time_regen == 120 ) {
			System.out.println( "regenerate" );
			this.recreate_rain(gl);
		}
		
		if ( this.time_regen == 360 ) {
			System.out.println( "destory and regenerate" );
			this.remove_finsihed_rain();
			this.recreate_rain(gl);
			this.time_regen = 0;
		}
		
		this.time_regen++;
		
		//------------------------------------
		// this part 
		// Creates an additive blend, which looks spectacular on a black
        // background
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);

        // Disable depth testing to get a nice composition
        gl.glDisable(GL.GL_DEPTH_TEST);
		//------------------------------------
        
        
		// draw each cube
		for( int i = 0 ; i < this.how_many_rain ; i++ ) {
			Point3D cube_position = this.init_local_pos.get( i );
			CoordFrame3D temp_f = frame.translate( cube_position );
			this.rain_cubes.get( i ).drawCube( gl, temp_f );
		}
		
		

	}
	

	
   
}
