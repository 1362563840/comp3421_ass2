package unsw.graphics.world;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleMesh;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

	private int width;
    private int depth;
    private float[][] altitudes;
    private List<Tree> trees;
    private List<Road> roads;
    private Vector3 sunlight;
    // from on, they are mine
    private TriangleMesh triMesh;
    private ArrayList< Point3D > vertices;
    
    private Texture text_graph;
    private ArrayList< Point2D > texCoords;
    

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth, Vector3 sunlight) {
        this.width = width;
        this.depth = depth;
        altitudes = new float[width][depth];
        trees = new ArrayList<Tree>();
        roads = new ArrayList<Road>();
        this.sunlight = sunlight;
        this.vertices = new ArrayList< Point3D >();
        
        this.texCoords = new ArrayList< Point2D >();
        
    }
    
    public void init( GL3 gl ) {
    	
    	this.order_vertics();

    	this.import_texture( gl );
    	this.creatMesh();
    	this.triMesh.init( gl );
    	for ( int i = 0 ; i < this.trees.size() ; i++ ) {
    		this.trees.get( i ).setTerrian( this );
    		this.trees.get( i ).init( gl );
    	}
    	
    	for( int j = 0 ; j < this.roads().size() ; j++ ) {
    		this.roads().get( j ).setTerrian( this );
    		this.roads.get( j ).init( gl );
    	}
    	
    	// Question Do I need to texture init()
    	// TODO need to init road init()
    }
    
    /**
     * These two method is for camera usage
     * @return
     */
    public int getWidth() { return this.width; }
    public int getDepth() { return this.depth; }
     
    public void order_vertics() {
    	int current_x = 0;
    	int current_z = 1;
    	boolean odd_even = true;
    	assert( this.width > 0 );
    	assert( this.depth > 0 );

    	// always counter clock wise
    	// 
    	for ( ; ; ) {
    		
    		if ( odd_even == true ) {
    			float a = altitudes[ current_x ][ current_z ];
    			float b = altitudes[ current_x + 1  ][ current_z - 1 ];
    			float c = altitudes[ current_x ][ current_z - 1 ];

    			this.vertices.add( new Point3D( current_x , a , current_z ) );
    			this.vertices.add( new Point3D( current_x + 1 , b , current_z - 1 ) );
    			this.vertices.add( new Point3D( current_x , c , current_z - 1 ) );

    			this.texCoords.add( new Point2D( 0 , 0 ) );
    			this.texCoords.add( new Point2D( 0.5f , 1 ) );
    			this.texCoords.add( new Point2D( 1 , 0 ) );

    		}
    		else {
    			float a = altitudes[ current_x ][ current_z ];
    			float b = altitudes[ current_x + 1  ][ current_z ];
    			float c = altitudes[ current_x + 1 ][ current_z - 1 ];
    			this.vertices.add( new Point3D( current_x , a , current_z ) );
    			this.vertices.add( new Point3D( current_x + 1 , b , current_z  ) );
    			this.vertices.add( new Point3D( current_x + 1 , c , current_z - 1 ) );

    			this.texCoords.add( new Point2D( 0 , 0 ) );
    			this.texCoords.add( new Point2D( 0.5f , 1 ) );
    			this.texCoords.add( new Point2D( 1 , 0 ) );
    			

    			current_x = current_x + 1;
    		}
    		
    		if ( current_x == this.width - 1 && current_z == this.depth - 1  ) {
    			break;
    		}
    		
    		if ( current_x == this.width - 1 ) {
    			current_x = 0;
    			current_z = current_z + 1;
    		}
    		
    		odd_even = !odd_even;
    		
    	}
    }
    
    /**
     * read texture file
     * @param gl
     */
    public void import_texture( GL3 gl ) {
    	this.text_graph = new Texture( gl, "res/textures/grass.bmp", "bmp", false );
    }
    
    public void creatMesh() {

    	this.triMesh = new TriangleMesh( this.vertices , true , this.texCoords );
    }
    
    public void recursively_draw ( GL3 gl , CoordFrame3D frame ) {
    	
    	
    	Vector4 temp_light_v4 = new Vector4( this.getSunlight().getX() , this.getSunlight().getY() , this.getSunlight().getZ() , 1 );
    	Point3D temp_light = frame.getMatrix().multiply( temp_light_v4 ).asPoint3D();
    	
    	Shader.setPoint3D(gl, "lightPos", temp_light );
    	
    	
        Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.4f, 0.4f, 0.4f));
        
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.3f, 0.3f, 0.3f));
        Shader.setFloat(gl, "phongExp", 4f);
        Shader.setPenColor( gl , Color.WHITE);
    	
    	
    	// if Terrain has offset, need to adjust frame before passing to its children
    	this.drawSelf( gl , frame);
    	
    	
    	for ( int i = 0 ; i < this.trees.size() ; i++ ) {
    		this.trees.get( i ).drawSelf( gl , frame );
    	}
    	for ( int i = 0 ; i < this.roads.size() ; i++ ) {
    		this.roads.get( i ).drawSelf( gl , frame );
    	}
    }
    
    public void drawSelf( GL3 gl , CoordFrame3D frame ) {
    	
    
    	Shader.setInt(gl, "tex", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.text_graph.getId());
        this.triMesh.draw( gl , frame );

    }
    

    public List<Tree> trees() {
        return trees;
    }

    public List<Road> roads() {
        return roads;
    }

    public Vector3 getSunlight() {
        return sunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        sunlight = new Vector3(dx, dy, dz);      
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return altitudes[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, float h) {
        altitudes[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * @param x
     * @param z
     * @return
     */
    int debug = 0;
    public float altitude(float x, float z) {
    	// debug for array
    	// TODO: Implement this
		if (x >= ( this.width - 1 ) || x < 0 || z >= ( this.depth - 1 ) || z < 0 ) return 0;


    	float result;
        int isInteger_x = Math.round( x );
        int isInteger_z = Math.round( z );
        boolean result_x =  ( (float)isInteger_x == x );
        boolean result_z =  ( (float)isInteger_z == z );
        if ( result_x == false && result_z == false ) {
        	// using interpolated
        	
        	float y = z;
        	int x1 = (int)Math.floor( x );
        	int x2 = (int)Math.ceil( x );
        	int x3 = x1;
        	int x4 = x2;
        	int z1 = (int)Math.floor( z );
        	int z2 = z1;
        	int z3 = (int)Math.ceil( z );
        	int z4 = z3;
        	
        	float diff = ( x - x1 ) + ( z - z1 );
        	
        	// in the right triangle
        	if ( diff >= 1 ) {
        		float R2 = ( ( z - z2 ) / ( z4 - z2 ) ) * this.altitudes[ x2 ][ z4 ];
            	R2 = R2 + ( ( z4 - z ) / ( z4 - z2 ) ) * this.altitudes[ x2 ][ z2 ];
            	
//            	result = R2;
            	
//            	float R1 = ( ( x - x1 ) / ( x2 - x1 ) ) * this.altitudes[ x2 ][ z3 ];
//            	R1 = R1 + ( ( x2 - x ) / ( x2 - x1 ) ) * this.altitudes[ x1 ][ z3 ];
            	
            	Point2D v1 = new Point2D( x2 , z3 );
            	float alt_v1 = this.altitude( v1.getX() , v1.getY() );
            	Point2D v2 = new Point2D( x1 , z1 );
            	float alt_v2 = this.altitude( v2.getX() , v2.getY() );
            	Point2D v3 = new Point2D( x2 , z1 );
            	float alt_v3 = this.altitude( v3.getX() , v3.getY() );
            	
            	float numerator = ( v2.getY() - v3.getY() ) * ( x - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( z - v3.getY() );
            	float Denominato = ( v2.getY() - v3.getY() ) * ( v1.getX() - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( v1.getY() - v3.getY() );
            	
            	float w_v1 = numerator / Denominato;
            	
            	float numerator_1 = ( v3.getY() - v1.getY() ) * ( x - v3.getX() ) + ( v1.getX() - v3.getX() ) * ( z - v3.getY() );
            	float Denominato_2 = ( v2.getY() - v3.getY() ) * ( v1.getX() - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( v1.getY() - v3.getY() );
            	
            	float w_v2 = numerator_1 / Denominato_2;
            	
            	float w_v3 = 1 - w_v1 - w_v2;
            	
            	result = w_v1 * alt_v1 + w_v2 * alt_v2 + w_v3 * alt_v3;
        	}
        	// in the left triangle
        	else {
        		float R1 = ( ( z - z1 ) / ( z3 - z1 ) ) * this.altitudes[ x1 ][ z3 ];
            	R1 = R1 + ( ( z3 - z ) / ( z3 - z1 ) ) * this.altitudes[ x1 ][ z1 ];
            	
//            	result = R1;
//            	
//            	float R2 = ( ( x - x1 ) / ( x2 - x1 ) ) * this.altitudes[ x2 ][ z3 ];
//            	R1 = R1 + ( ( x2 - x ) / ( x2 - x1 ) ) * this.altitudes[ x1 ][ z3 ];
            	
            	Point2D v1 = new Point2D( x1 , z3 );
            	float alt_v1 = this.altitude( v1.getX() , v1.getY() );
            	Point2D v2 = new Point2D( x1 , z1 );
            	float alt_v2 = this.altitude( v2.getX() , v2.getY() );
            	Point2D v3 = new Point2D( x2 , z3 );
            	float alt_v3 = this.altitude( v3.getX() , v3.getY() );
            	
            	float numerator = ( v2.getY() - v3.getY() ) * ( x - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( z - v3.getY() );
            	float Denominato = ( v2.getY() - v3.getY() ) * ( v1.getX() - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( v1.getY() - v3.getY() );
            	
            	float w_v1 = numerator / Denominato;
            	
            	float numerator_1 = ( v3.getY() - v1.getY() ) * ( x - v3.getX() ) + ( v1.getX() - v3.getX() ) * ( z - v3.getY() );
            	float Denominato_2 = ( v2.getY() - v3.getY() ) * ( v1.getX() - v3.getX() ) + ( v3.getX() - v2.getX() ) * ( v1.getY() - v3.getY() );
            	
            	float w_v2 = numerator_1 / Denominato_2;
            	
            	float w_v3 = 1 - w_v1 - w_v2;
            	
            	result = w_v1 * alt_v1 + w_v2 * alt_v2 + w_v3 * alt_v3;
            	
        	}
        	
        	
        	assert( z3 < this.depth );
        	assert( x2 < this.width );
        	
        	
        }
        else if ( result_x == true && result_z == false ) {
        	int z1 = (int)Math.floor( z );
        	int z3 = (int)Math.ceil( z );
        	
        	float R1 = ( ( z - z1 ) / ( z3 - z1 ) ) * this.altitudes[ isInteger_x ][ z3 ];
        	R1 = R1 + ( ( z3 - z ) / ( z3 - z1 ) ) * this.altitudes[ isInteger_x ][ z1 ];
        	
        	result = R1;
        }
        else if( result_x == false && result_z == true ) {
        	int x1 = (int)Math.floor( x );
        	int x2 = (int)Math.ceil( x );
        	
        	result = ( ( x - x1 ) / ( x2 - x1 ) ) * this.altitudes[ x2 ][ isInteger_z ];
        	result = result + ( ( x2 - x ) / ( x2 - x1 ) ) * this.altitudes[ x1 ][ isInteger_z ];
        }
        else {
        	assert( result_x == true && result_z == true );
        	result = this.altitudes[ isInteger_x ][ isInteger_z ];
        }


        
        return result;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(float x, float z) {
        float y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        trees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(float width, List<Point2D> spine) {
        Road road = new Road(width, spine);
        roads.add(road);        
    }
    
    public void destroy(GL3 gl) {
    	
    }

}
