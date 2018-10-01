package unsw.graphics.world;

import java.awt.Color;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.ColorBuffer;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Point3DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point3D;

/**
 * each rain contains x texture
 * 
 * each texture has slightly different position based on base position
 * 
 * 
 * @author Athos
 *
 */
public class rain_test {
	
	private final int how_many = 2;

	private Point3D init_pos;
		
	private Texture text_graph;
	
	private static float gravityY = -0.0008f; // gravity
	
	private static float speedYGlobal = 0.1f;
	
	private float time;
	private float life; // how alive it is
	private float r, g, b; // color
	private float[] each_y_pos; // each rain_bit has its own position, but x z are same, different is y
	private float speedX, speedY, speedZ; // speed in the direction, each rain_bit share same speed, color
	
	private Point3DBuffer velocities;
	private Point3DBuffer inital_position;
    private ColorBuffer colors;
    
    private int velocitiesName;
    private int colorsName;
    private int initial_position_name;
	
	private final float[][] colors_init = { // rainbow of 12 colors
            { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f },
            { 1.0f, 1.0f, 0.5f }, { 0.75f, 1.0f, 0.5f },
            { 0.5f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.75f },
            { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f },
            { 0.5f, 0.5f, 1.0f }, { 0.75f, 0.5f, 1.0f },
            { 1.0f, 0.5f, 1.0f }, { 1.0f, 0.5f, 0.75f } };
	
	private Random rand;
	
	public rain_test(  Point3D initial_pos ) {
		this.init_pos = initial_pos;
		// debug
//		init_pos.print_out();
		
		
		
		this.each_y_pos = new float[ this.how_many ];
		
		this.rand = new Random(1); 
		this.time = 0f;
		
	}
	
	public void init(GL3 gl) {
		this.import_texture(gl);
		this.init_y_pos();
		this.init_speed_color();
		
		
		this.velocities = new Point3DBuffer(this.how_many); 
		this.colors = new ColorBuffer(this.how_many); 
		this.inital_position = new Point3DBuffer(this.how_many); 
		
		int[] names = new int[ 3 ];
        gl.glGenBuffers(3, names, 0);
        
        this.velocitiesName = names[0];
        this.colorsName = names[1];
        this.initial_position_name = names[2];
        
        // despite that there are 100 rain bits
        // but each rain_bit color and velocity are same
        // now add each particle's velocity and color into Buffer
        // after adding it, we should only need to change the time without 
        // without altering the velocity 
        System.out.println( "x is " + this.init_pos.getX() + " z is " + this.init_pos.getZ() );
        for ( int i = 0 ; i < this.how_many ; i++ ) {
        	// each
//        	assert ( this.speedX == 0 &&  this.speedY == 0 && this.speedZ == 0 );
        	this.velocities.put( i , this.speedX , this.speedY , this.speedZ );
//        	this.velocities.put( i , 0 , 0 , 0 );
        	
        	this.colors.put(i, r , g, b,
                    life);
        	this.inital_position.put( i , this.init_pos.getX() , this.each_y_pos[ i ] , this.init_pos.getZ() );
        }
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, velocitiesName);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, this.how_many * 3 * Float.BYTES,
                velocities.getBuffer(), GL.GL_DYNAMIC_DRAW);
        //										3 is because the point3D has three pints
        gl.glVertexAttribPointer(Shader.POSITION, 3, GL.GL_FLOAT, false, 0, 0);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorsName);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, this.how_many * 4 * Float.BYTES,
                colors.getBuffer(), GL.GL_DYNAMIC_DRAW);
        //									4 is because the color has 4 float points
        gl.glVertexAttribPointer(Shader.COLOR, 4, GL.GL_FLOAT, false, 0, 0);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, initial_position_name);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, this.how_many * 3 * Float.BYTES,
        		inital_position.getBuffer(), GL.GL_DYNAMIC_DRAW);
        //									4 is because the color has 4 float points
        gl.glVertexAttribPointer(Shader.INIT_POSITION, 3, GL.GL_FLOAT, false, 0, 0);
        
     // Set the point size
        gl.glPointSize(50);
        
        Shader.setFloat(gl, "gravity", rain_test.gravityY);
        
	}
	
	/**
	 * 
	 */
	public void drawself( GL3 gl , CoordFrame3D frame ) {
//		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
//
//        // Disable depth testing to get a nice composition
//        gl.glDisable(GL.GL_DEPTH_TEST);
		Shader.setPenColor(gl, Color.YELLOW);
		
//		Shader.setPoint3D( gl , "debug_v" , new Point3D( 0 , 0.5f , 0 ) );
//		Shader.setFloat( gl , "y_speed" , 0.5f );
        
        Shader.setFloat(gl, "time", this.time);
//        System.out.println( "here" );
        // Draw the particles
        Shader.setInt(gl, "tex", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.text_graph.getId());
//        System.out.println( "there" );
        gl.glDrawArrays(GL.GL_POINTS, 0, this.how_many);
        this.time = this.time + 0.05f;
	}
	
	public void destroy(GL3 gl) {
        gl.glDeleteBuffers(3, new int[] { velocitiesName, colorsName, this.initial_position_name }, 0);
        this.text_graph.destroy(gl);
    }
	
	
	/**
	 * the distance between first rain bit and the last rain bit is one in local scale
	 */
	public void init_y_pos() {
		assert( this.init_pos != null );
		for ( int i = 0 ; i < this.how_many ; i++ ) {
			this.each_y_pos[ i ] = this.init_pos.getY() - 0.01f * i;
		}		
	}
	
	
	public void init_speed_color() {
		float maxSpeed = 0.1f;
        float speed = 0.02f + (rand.nextFloat() - 0.5f) * maxSpeed;
        float angle = (float) Math.toRadians(rand.nextInt(360));
        
        speedX = 0f;
//        speedY = speed * (float) Math.sin(angle) + speedYGlobal;
        speedY =  -0.1f;
        speedZ = 0f;
//        assert ( speedY < 0 );
        
        int colorIndex = (int) (((speed - 0.02f) + maxSpeed)
                / (maxSpeed * 2) * colors_init.length) % colors_init.length;
        // Pick a random color
        r = colors_init[colorIndex][0];
        g = colors_init[colorIndex][1];
        b = colors_init[colorIndex][2];

        // Initially it's fully alive
        life = 1.0f;
	}
	
	
	
	public void import_texture( GL3 gl ) {
    	this.text_graph = new Texture( gl, "res/textures/rain.bmp", "bmp", false );
    }
}
