package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import unsw.graphics.ColorBuffer;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Point3DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.examples.TwoTriangles3D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle2D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * initial each rain position is set by Rain_particles
 * so local position is just ( 0 , 0 , 0 )
 * @author Athos
 *
 */
public class Cube {
	
	private Random rand;
	
	private Texture text_graph;
	
	private ArrayList< TriangleMesh > triMesh;
	
	private ArrayList< Point3D > vertices;
	
	private ArrayList< Point2D > textCoord;
	
	private float length;
	
	
	// for particle
	
	private Point3DBuffer velocities;
	private Point3D velocities_Point3D;
    private ColorBuffer colors;
    private Color color_v4;
    
    private int velocitiesName;
    private int colorsName;
    
    private final float[][] colors_preDefine = { // rainbow of 12 colors
            { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f },
            { 1.0f, 1.0f, 0.5f }, { 0.75f, 1.0f, 0.5f },
            { 0.5f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.75f },
            { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f },
            { 0.5f, 0.5f, 1.0f }, { 0.75f, 0.5f, 1.0f },
            { 1.0f, 0.5f, 1.0f }, { 1.0f, 0.5f, 0.75f } };
	
    
    private float r, g, b; // color
    private float speedX, speedY, speedZ; // speed in the direction
    
 // Pull forces in each direction
    private static float gravityY = -0.0008f; // gravity

    // Initial speed for all the particles
    private static float speedYGlobal = 0.1f;
    
    private float life; // how alive it is
    
    private int time;
    
    private Point3D Mypos;
	
	public Cube( float length ) {

		this.length = length;
		this.vertices = new ArrayList< Point3D >();
		this.textCoord = new ArrayList< Point2D >();
		this.triMesh = new ArrayList< TriangleMesh >(); 
		
		
		this.rand = new Random(1);
		this.time = 0;

	}
	
	public void init( GL3 gl ) {
		
		this.import_texture(gl);
		this.Mypos = new Point3D( 0 , 0 , 0 );
		
		Point3D p1 = new Point3D( - this.length , - this.length , this.length );
		Point3D p2 = new Point3D(   this.length , - this.length , this.length );
		Point3D p3 = new Point3D( - this.length ,   this.length , this.length );
		ArrayList< Point3D > temp_vertices = new ArrayList< Point3D >();
		temp_vertices.add( p1 );
		temp_vertices.add( p2 );
		temp_vertices.add( p3 );
		
		Point2D text_p1 = new Point2D( 0 , 0 );
		Point2D text_p2 = new Point2D( 1 , 0 );
		Point2D text_p3 = new Point2D( 0 , 1 );
		
		ArrayList< Point2D > temp_text_Coor = new ArrayList< Point2D >();
		temp_text_Coor.add( text_p1 );
		temp_text_Coor.add( text_p2 );
		temp_text_Coor.add( text_p3 );
		
		TriangleMesh botttom_left = new TriangleMesh( temp_vertices , true , temp_text_Coor );
		botttom_left.init( gl );
		this.triMesh.add( botttom_left );
		
		// for second triangle
		Point3D p4 = new Point3D(   this.length , - this.length , this.length );
		Point3D p5 = new Point3D(   this.length ,   this.length , this.length );
		Point3D p6 = new Point3D( - this.length ,   this.length , this.length );
		ArrayList< Point3D > temp_vertices_second = new ArrayList< Point3D >();
		temp_vertices_second.add( p4 );
		temp_vertices_second.add( p5 );
		temp_vertices_second.add( p6 );
		
		Point2D text_p4 = new Point2D( 1 , 0 );
		Point2D text_p5 = new Point2D( 0 , 1 );
		Point2D text_p6 = new Point2D( 1 , 1 );
		
		ArrayList< Point2D > second_temp_text_coor = new ArrayList< Point2D >();
		second_temp_text_coor.add( text_p4 );
		second_temp_text_coor.add( text_p5 );
		second_temp_text_coor.add( text_p6 );
		
		TriangleMesh top_right = new TriangleMesh( temp_vertices_second , true , second_temp_text_coor );
		top_right.init( gl );
		this.triMesh.add( top_right );
		
		this.partical_init(gl);
	}
	
	public void partical_init( GL3 gl ) {
		
		//------------------------------------------------ 
		// from inner class
		float maxSpeed = 0.1f;
        float speed = 0.02f + (rand.nextFloat() - 0.5f) * maxSpeed;
        float angle = (float) Math.toRadians(rand.nextInt(360));
		
		
        this.speedX = speed * (float) Math.cos(angle);
        this.speedY = speed * (float) Math.sin(angle) + speedYGlobal;
        this.speedZ = (rand.nextFloat() - 0.5f) * maxSpeed;

        int colorIndex = (int) (((speed - 0.02f) + maxSpeed)
                / (maxSpeed * 2) * this.colors_preDefine.length) % this.colors_preDefine.length;
        // Pick a random color
        this.r = this.colors_preDefine[colorIndex][0];
        this.g = this.colors_preDefine[colorIndex][1];
        this.b = this.colors_preDefine[colorIndex][2];

        // Initially it's fully alive
        life = 1.0f;
        
        //------------------------------------------------
        
        
        //------------------------------------------------ or here
		// Allocate the buffers
		// this class is just one rain
//        velocities = new Point3DBuffer(1);
        colors = new ColorBuffer(1);
        
        int[] names = new int[2];
//        int[] names = new int[3];
//        gl.glGenBuffers(3, names, 0);
        gl.glGenBuffers(2, names, 0);

//        this.velocitiesName = names[0];
        this.colorsName = names[1];
        
        
//        this.velocities.put(0, this.speedX, this.speedY, this.speedZ);
        this.colors.put(0, this.r, this.g, this.b,
        		this.life);
        
 
//        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, velocitiesName);
//        gl.glBufferData(GL.GL_ARRAY_BUFFER, 1 * 3 * Float.BYTES,
//                velocities.getBuffer(), GL.GL_DYNAMIC_DRAW);
//        gl.glVertexAttribPointer(Shader.POSITION, 3, GL.GL_FLOAT, false, 0, 0);
        
        // this one shall correspond the variable in glsl in vertex : "color"
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorsName);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, 1 * 4 * Float.BYTES,
                colors.getBuffer(), GL.GL_DYNAMIC_DRAW);
        gl.glVertexAttribPointer(Shader.COLOR, 4, GL.GL_FLOAT, false, 0, 0);
        //------------------------------------------------
        
        //------------------------------------------------
        // either here 
//        this.velocities_Point3D = new Point3D( this.speedX, this.speedY, this.speedZ );
//        Shader.setPoint3D(gl, "position", this.velocities_Point3D);
//        
//        this.color_v4 = new Color( this.r, this.g, this.b,
//        		this.life );
//        Shader.setColorWithAlpha(gl, "color", this.color_v4);
//        
//        
        //------------------------------------------------
        // Set the point size
        gl.glPointSize(50);
        
        Shader.setFloat(gl, "gravity", gravityY);
    
        
	}
	

	/**
     * Draw a cube centered around (0,0) with bounds of length 1 in each direction.
     * @param gl
     * @param frame
     */
    public void drawCube(GL3 gl, CoordFrame3D frame) {
    	
    	// explain :
    	// the position of rain is based on the terrain position
    	// so when the global transformaiton of terrain is parsed in
    	// the position should be good, since the local position of rain is parsed to the trainglemesh when creating
    	
    	            
        this.Mypos = new Point3D( this.Mypos.getX() + this.speedX , 
        							this.Mypos.getY() + this.speedY ,
        							this.Mypos.getZ() + this.speedZ );   
        
        frame = frame.translate( this.Mypos );
        
        colors.put(0, this.r, this.g, this.b,
            		this.life);

        this.life = this.life - 0.001f;

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorsName);
        //for itself, just one rain
        gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0,
                1 * 4 * Float.BYTES, colors.getBuffer());

        // Draw the particles
        Shader.setInt(gl, "tex", 3);
        gl.glActiveTexture(GL.GL_TEXTURE3);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, text_graph.getId());
    	

    	
    	// terrain 0
    	// tree 1
    	// road 2
    	Shader.setInt(gl, "tex", 3);
        gl.glActiveTexture(GL.GL_TEXTURE3);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.text_graph.getId());
//        Shader.setInt(gl, "time", this.time);
//        this.time--;
    	
        // Front
        Shader.setPenColor(gl, Color.RED);
        
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
        	this.triMesh.get( i ).draw(gl, frame);
        }
        this.triMesh.get( 0 ).draw(gl, frame);
        this.triMesh.get( 1 ).draw(gl, frame);
//        face.draw(gl, frame);
        
        // Left
        Shader.setPenColor(gl, Color.BLUE);
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
        	this.triMesh.get( i ).draw(gl, frame.rotateY(-90));
        }
        this.triMesh.get( 0 ).draw(gl, frame.rotateY(-90));
        this.triMesh.get( 1 ).draw(gl, frame.rotateY(-90));
//        face.draw(gl, frame.rotateY(-90));
        
        // Right
        Shader.setPenColor(gl, Color.GREEN);
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
            this.triMesh.get( i ).draw(gl, frame.rotateY(90));
        }
        this.triMesh.get( 0 ).draw(gl, frame.rotateY(90));
        this.triMesh.get( 1 ).draw(gl, frame.rotateY(90));
//        face.draw(gl, frame.rotateY(90));
        
        // Back
        Shader.setPenColor(gl, Color.CYAN);
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
            this.triMesh.get( i ).draw(gl, frame.rotateY(180));
        }
        this.triMesh.get( 0 ).draw(gl, frame.rotateY(180));
        this.triMesh.get( 1 ).draw(gl, frame.rotateY(180));
//        face.draw(gl, frame.rotateY(180));
        
        // Bottom
        Shader.setPenColor(gl, Color.YELLOW);
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
            this.triMesh.get( i ).draw(gl, frame.rotateX(-90));
        }
        this.triMesh.get( 0 ).draw(gl, frame.rotateX(-90));
        this.triMesh.get( 1 ).draw(gl, frame.rotateX(-90));
//        face.draw(gl, frame.rotateX(-90));
        
        // Top
        Shader.setPenColor(gl, Color.MAGENTA);
        for( int i = 0 ; i < this.triMesh.size() ; i++ ) {
        	assert( this.triMesh.size() == 2 || this.triMesh.size() == 4 );
            this.triMesh.get( 0 ).draw(gl, frame.rotateX(90));
        }
        this.triMesh.get( 0 ).draw(gl, frame.rotateX(90));
        this.triMesh.get( 1 ).draw(gl, frame.rotateX(90));
//        face.draw(gl, frame.rotateX(90));
    }
    
    public void import_texture( GL3 gl ) {
    	this.text_graph = new Texture( gl, "res/textures/rain_1.bmp", "bmp", false );
    }
}
