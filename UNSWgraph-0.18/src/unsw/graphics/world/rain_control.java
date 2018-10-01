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
		this.offset_height = 3;
		this.rand = new Random(0);
		
		this.life = new ArrayList< Float >();
		this.current_life = new ArrayList< Float >();
		this.rain = new ArrayList< rain_test >();
		
		this.how_many_rain = 10;
		
		this.Max_regenerate = 180;
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
			
			// t = s / v
			float temp_time = ( terrian_max_height +  this.offset_height - height ) / ( 1f / 60f );
			
			rain_test temp_rain = new rain_test( new Point3D( temp.getX() , 
												terrian_max_height + this.offset_height , 
												temp.getY() ) );
//			System.out.println( "terrian_max_height + this.offset_height is " + ( terrian_max_height + this.offset_height ) );
			System.out.println( "??" );
			// initial rain
			temp_rain.init(gl);
			this.inital.add( new Point3D( temp.getX() , 
												terrian_max_height + this.offset_height , 
												temp.getY() ) );
			this.rain.add( temp_rain );
			System.out.println( "time is " + temp_time );
			this.life.add( temp_time );
			this.current_life.add( 0f );
			
			this.how_many_existing++;
			System.out.println( "how " + how_many_existing );
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
			this.inital.get(i).print_out();
			this.rain.get( i ).drawself(gl, frame);
			this.current_life.set( i , this.current_life.get( i ) + 1f );
		}
		
		
	}
	
	
}
