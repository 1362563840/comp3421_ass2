package unsw.graphics.world;



import java.util.ArrayList;
import java.util.List;

import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;



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
    	for( int i = 0 ; i < this.width ; i++ ) {
    		for( int j = 0 ; j < this.depth ; j++ ) {
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
