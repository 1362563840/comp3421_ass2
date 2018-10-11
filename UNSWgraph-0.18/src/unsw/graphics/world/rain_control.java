package unsw.graphics.world;

import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;

/**
 * so each rain_test is a rain
 * 
 * for each rain_test, we need a life
 * 
 * when time is out, need to destroy it, calling the destroy() in rain_test
 * 
 * 
 * 
 * each texture has slightly different position based on base position
 * 
 * 
 * @author Athos
 *
 */
public class rain_control {
	
	private Random rand;
	private Terrain terrian;
	private float terrian_max_height;
	private float offset_height;
	
	/**
	 * each time , we generate how many rain
	 */
	private int how_many_rain;
	
	private ArrayList< Float > life;
	private ArrayList< Float > current_life;
	private ArrayList< rain_test > rain;
	
	private ArrayList< Point3D > inital = new ArrayList< Point3D >();
 	
	private int counter;
	private int Max_regenerate;
	
	private int how_many_existing;
	
	public rain_control ( Terrain terrian ) {
		this.terrian = terrian;
		this.terrian_max_height = this.terrian.max_height_local();
		
		System.out.println( "terrian's width is " + this.terrian.getWidth() );
		System.out.println( "terrian's depth is " + this.terrian.getDepth() );
		
		System.out.println( "terrian's max heigh is " + this.terrian.max_height_local() );
	
		this.offset_height = 3 + 0;
		this.rand = new Random(0);
		
		this.life = new ArrayList< Float >();
		this.current_life = new ArrayList< Float >();
		this.rain = new ArrayList< rain_test >();
		
		this.how_many_rain = 100;
		
		this.Max_regenerate = 120;
	}
	
	public void init ( GL3 gl ) {
		// generate the first period rain
		this.generate(gl);
	}
	
	/**
	 * 
	 */
	public void generate ( GL3 gl ) {
		float width = this.terrian.getX_length();
		float depth = this.terrian.getZ_length();
		for ( int i = 0 ; i < this.how_many_rain ; i++ ) {
			// first choose a location
			
			assert (  width >= 2 );
			assert (  depth >= 2 );

			Point2D temp = new Point2D( this.rand.nextFloat() * ( width - 1 ) , this.rand.nextFloat() * ( depth - 1 )  );
			float height = this.terrian.altitude( temp.getX() , temp.getY() );
			
			System.out.println( " x is " + temp.getX() + " z is " + temp.getY() );
			System.out.println( "hegiht is " + height );
			
			// t = s / v
			float temp_time = ( terrian_max_height +  this.offset_height - height ) / ( 1f / 60f );
			// consider some error
			temp_time = temp_time - 1f - 0.25f;
			assert ( temp_time > 0 );
			
			rain_test temp_rain = new rain_test( new Point3D( temp.getX() , 
												terrian_max_height + this.offset_height , 
												temp.getY() ) , temp_time );
			// initial rain
			temp_rain.init(gl);
			this.inital.add( new Point3D( temp.getX() , 
												terrian_max_height + this.offset_height , 
												temp.getY() ) );
			
			Point3D debug = new Point3D( temp.getX() , 
											terrian_max_height + this.offset_height , 
											temp.getY() );
//			debug.print_out();
			
			this.rain.add( temp_rain );
			this.life.add( temp_time );
			this.current_life.add( 0f );
			
			this.how_many_existing++;
		}
		
	}
	
	public void draw ( GL3 gl , CoordFrame3D frame ) {
		
		this.counter++;
		if ( this.Max_regenerate == this.counter ) {
			this.generate( gl );
			this.counter = 0;
		}
		
		// check if need to destroy this rain
		for ( int i = 0 ; i < this.rain.size() ; i++ ) {
			
			if ( this.current_life.get( i ) >= this.life.get( i ) ) {
//				this.rain.get( i ).destroy(gl);
				
				this.rain.remove( i );
				this.current_life.remove( i );
				this.life.remove( i );
				this.how_many_existing--;
				
				this.inital.remove(i);
			}
			
		}
		
		// increment the counter for current_life
		for ( int i = 0 ; i < this.rain.size() ; i++ ) {
			this.rain.get( i ).drawself(gl, frame);
			this.current_life.set( i , this.current_life.get( i ) + 1f );
		}
		
		
	}
	
	
}
