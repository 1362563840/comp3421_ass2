package unsw.graphics.world;



import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
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
    TriangleMesh triMesh;
    ArrayList< Point3D > vertices;
    

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
        
    }
    
    public void order_vertics() {
    	int current_x = 0;
    	int current_z = 1;
    	boolean odd_even = true;
    	assert( this.width > 0 );
    	assert( this.depth > 0 );
    	System.out.println( "Width is " + this.width );
    	System.out.println( "depth is " + this.depth );
    	// always counter clock wise
    	// 
    	for ( ; ; ) {
    		
    		if ( odd_even == true ) {
    			float a = altitudes[ current_x ][ current_z ];
    			float b = altitudes[ current_x + 1  ][ current_z - 1 ];
    			float c = altitudes[ current_x ][ current_z - 1 ];
//    			System.out.println( "current_x is " + current_x );
//    			System.out.println( "a is " + a );
//    			System.out.println( "current_z is " + current_z );
    			this.vertices.add( new Point3D( current_x , a , current_z ) );
    			this.vertices.add( new Point3D( current_x + 1 , b , current_z - 1 ) );
    			this.vertices.add( new Point3D( current_x , c , current_z - 1 ) );
    			
    			System.out.println( "this is odd" );
    			this.vertices.get( this.vertices.size() - 3 ).print_out();
    			this.vertices.get( this.vertices.size() - 2 ).print_out();
    			this.vertices.get( this.vertices.size() - 1 ).print_out();
    		}
    		else {
    			float a = altitudes[ current_x ][ current_z ];
    			float b = altitudes[ current_x + 1  ][ current_z ];
    			float c = altitudes[ current_x + 1 ][ current_z - 1 ];
    			this.vertices.add( new Point3D( current_x , a , current_z ) );
    			this.vertices.add( new Point3D( current_x + 1 , b , current_z  ) );
    			this.vertices.add( new Point3D( current_x + 1 , c , current_z - 1 ) );
    			
    			System.out.println( "this is even" );
    			this.vertices.get( this.vertices.size() - 3 ).print_out();
    			this.vertices.get( this.vertices.size() - 2 ).print_out();
    			this.vertices.get( this.vertices.size() - 1 ).print_out();
    			
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
    
    
    
    public void creatMesh() {
    	this.order_vertics();
    	System.out.println( this.vertices.size() );
    	this.triMesh = new TriangleMesh( this.vertices , true );
    }
    
    public void recursively_draw ( GL3 gl , CoordFrame3D frame ) {
    	// if Terrain has offset, need to adjust frame before passing to its children
    	this.drawSelf( gl , frame );
    	for( int i = 0 ; i < this.trees.size() ; i++ ) {
    		this.trees.get( i ).drawSelf( gl , frame );
    	}
    	for( int i = 0 ; i < this.roads.size() ; i++ ) {
    		this.roads.get( i ).drawSelf( gl , frame );
    	}
    }
    
    public void drawSelf( GL3 gl , CoordFrame3D frame ) {
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
    public float altitude(float x, float z) {
    	// debug for array
    	// TODO: Implement this
    	for ( int i = 0 ; i < this.width ; i++ ) {
    		for ( int j = 0 ; j < this.depth ; j++ ) {
    			System.out.print( this.altitudes[ i ][ j ] + "    " );
    		}
    		System.out.println();
    	}
    	float result;
        int isInteger_x = Math.round( x );
        int isInteger_z = Math.round( z );
        boolean result_x =  ( (float)isInteger_x == x );
        boolean result_z =  ( (float)isInteger_z == z );
        if ( result_x == false || result_z == false ) {
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
        	
        	float R1 = ( ( z - z1 ) / ( z3 - z1 ) ) * this.altitudes[ x1 ][ z3 ];
        	R1 = R1 + ( ( z3 - z ) / ( z3 - z1 ) ) * this.altitudes[ x1 ][ z1 ];
        	
        	float R2 = ( ( z - z2 ) / ( z4 - z2 ) ) * this.altitudes[ x2 ][ z4 ];
        	R2 = R2 + ( ( z4 - z ) / ( z4 - z2 ) ) * this.altitudes[ x2 ][ z2 ];
        	
        	result = ( ( x - x1 ) / ( x2 - x1 ) ) * R2;
        	result = result + ( ( x2 - x ) / ( x2 - x1 ) ) * R1;
        }
        else {
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

}
