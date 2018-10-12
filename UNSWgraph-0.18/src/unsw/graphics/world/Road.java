package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Point2D> points;
    private float width;
    
    private float step_increment;
    private float division_factor;
    private float distance;
    
    private float my_step_increment;
    
    private ArrayList< Point3D > left;
    private ArrayList< Point3D > middle;
    private ArrayList< Point3D > right;
    
    private ArrayList< Point3D > left_global;
    private ArrayList< Point3D > middle_global;
    private ArrayList< Point3D > right_global;
    
    private ArrayList< Matrix4 > this_layer_global_trans;
    
    private Terrain terrian;
    
    
    private ArrayList< Point3D > vertices;
    private ArrayList< Point2D > textCoord;
    private TriangleMesh triMesh;
    private Texture text_graph;
    
    /**
     * change the width of road in order to have a good looking
     */
    private float true_width;

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(float width, List<Point2D> spine) {
        this.width = width;
        this.points = spine;
        
        this.division_factor = 25;
        
        this.left = new ArrayList< Point3D >();
        this.middle = new ArrayList< Point3D >();
        this.right = new ArrayList< Point3D >();
        
        this.left_global = new ArrayList< Point3D >();
        this.middle_global = new ArrayList< Point3D >();
        this.right_global = new ArrayList< Point3D >();
        
        this.this_layer_global_trans = new ArrayList< Matrix4 >();
        
        this.my_step_increment = 1f / 50;
        
        this.vertices = new ArrayList< Point3D >();
        this.textCoord = new ArrayList< Point2D >();
        
        this.true_width = ( this.width / 1f ) * 0.25f;
        
    }
    
    public void setTerrian( Terrain terrian ) {
    	this.terrian = terrian;
    }
    
    public void init(GL3 gl) {
    	
    	// algorithm is provided in function point
    	// there are several curves perhaps, but the algorithm is
    	// you only need to let t start from 0 to this.size
		for ( float j = 0 ; j < this.size() ; j = j + my_step_increment ) { 
			// important here, you get the point in curve
			// you get the slope ratio and calculate the angle
					
			Point2D temp_middle_2 = this.point( j );
			
			Point2D temp_middle_2_tangnet = this.point_tangent( j );

			float slope_angle = this.calculateDegree( temp_middle_2_tangnet.getX() , temp_middle_2_tangnet.getY() );
			
			float altitude_middle = this.terrian.altitude( temp_middle_2.getX() , temp_middle_2.getY() );
			Matrix4 temp_this_layer = Matrix4.identity().translation( temp_middle_2.getX() , altitude_middle , temp_middle_2.getY() );
			Point3D temp_middle_3 = new Point3D( 0 , 0 , 0 );
			
			// this one is just translate towards positive x with width / 2
			Point3D temp_right_3 = new Point3D( this.true_width , 0 , 0 );
			Point3D temp_right_3_rotate = temp_right_3.asHomogenous().rotateY( slope_angle ).asPoint3D();
			
			// this one is just translate towards positive x with - width / 2
			Point3D temp_left_3 = new Point3D( - this.true_width , 0 , 0 );
			Point3D temp_left_3_rotate = temp_left_3.asHomogenous().rotateY( slope_angle ).asPoint3D();
			
			this.middle.add( temp_middle_3 );
			this.left.add( temp_left_3_rotate );
			this.right.add( temp_right_3_rotate );
			this.this_layer_global_trans.add( temp_this_layer );		
			
		}
    		

    	
    	this.import_texture( gl );
    }
    
    // TODO
    public void drawSelf( GL3 gl , CoordFrame3D frame ) {
    	this.renewCoord( CoordFrame3D.identity() );
    	
    	this.create_mesh( gl );
    	Shader.setModelMatrix( gl , CoordFrame3D.identity().getMatrix() );
        Shader.setInt(gl, "tex", 2);
        gl.glActiveTexture(GL.GL_TEXTURE2);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.text_graph.getId());
        this.triMesh.draw( gl , frame.translate(0f, 0.008f, 0f) ); 
//        this.triMesh.draw( gl , frame ); 

        
        this.clear();
    	
    }
    
    /**
     * read texture file
     * @param gl
     */
    public void import_texture( GL3 gl ) {
    	this.text_graph = new Texture( gl, "res/textures/rock.bmp", "bmp", false );
    }
    
    /**
     * after renew the coordinate 
     * create the meash
     */
    public void create_mesh( GL3 gl ) {
    	this.triMesh = new TriangleMesh( this.vertices , true , this.textCoord );
    	this.triMesh.init( gl );
    }
    
    
    int debug_1 = 0;
    /**
     * after parsed in the global frame
     * renew the global coordinate
     * @param global_frame
     */
    public void renewCoord( CoordFrame3D global_frame ) {

    	
    	//multiply global_frame by this_layer_global_trans to get global frame
    	//then multiply the left coord and right coord to get the coordinate in global
    	assert ( this.this_layer_global_trans.size() == this.middle.size() );
    	assert ( this.middle.size() == this.left.size() );
    	assert ( this.left.size() == this.right.size() );
    	    	
    	for( int i = 0 ; i < this.this_layer_global_trans.size() ; i++ ) {
    		Matrix4 temp_global = global_frame.getMatrix().multiply( this.this_layer_global_trans.get( i ) );
    		
    		// add left point
    		Vector4 temp_v4_left = temp_global.multiply( this.left.get( i ).asHomogenous() );
    		
    		Point3D temm_p3_left = temp_v4_left.asPoint3D();

    		
    		this.left_global.add( temm_p3_left );
    		
    		// add right point
			Vector4 temp_v4_right = temp_global.multiply( this.right.get( i ).asHomogenous() );
			
			Point3D temm_p3_right = temp_v4_right.asPoint3D();

    		
    		this.right_global.add( temm_p3_right );
    	}
    	
    	
    	// anticlock or clock to draw road vary
    	// one can see from above
    	// one can see from below
    	for( int i = 0 ; i < this.left_global.size() - 1 ; i++ ) {   		
    		
    		this.vertices.add( this.left_global.get( i ) );
    		this.vertices.add( this.right_global.get( i + 1 ) );
    		this.vertices.add( this.right_global.get( i ) );
    		
    		this.textCoord.add( new Point2D( 0 , 0 ) );
    		this.textCoord.add( new Point2D( 1 , 0 ) );
    		this.textCoord.add( new Point2D( 1 , 1 ) );
    		
    		this.vertices.add( this.left_global.get( i  ) );
    		this.vertices.add( this.left_global.get( i + 1 ) );
    		this.vertices.add( this.right_global.get( i + 1 ) );
    		
    		this.textCoord.add( new Point2D( 0 , 0 ) );
    		this.textCoord.add( new Point2D( 1 , 1 ) );
    		this.textCoord.add( new Point2D( 0 , 1 ) );

    	}
    	
    }
    
    public void clear() {
    	this.left_global.clear();
    	this.right_global.clear();
    	this.vertices.clear();
    	this.textCoord.clear();
    }
    
    
    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return width;
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return points.size() / 3;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public Point2D controlPoint(int i) {
        return points.get(i);
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public Point2D point(float t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 3;
        
        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);
        

        float x = b(0, t) * p0.getX() + b(1, t) * p1.getX() + b(2, t) * p2.getX() + b(3, t) * p3.getX();
        float y = b(0, t) * p0.getY() + b(1, t) * p1.getY() + b(2, t) * p2.getY() + b(3, t) * p3.getY();     
        
        return new Point2D(x, y);
    }
    
    /**
     * this one calculates the tangent
     * if slope is 5
     * then y : x = 5
     * @param t
     * @return
     */
    public Point2D point_tangent(float t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 3;
        
        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);
           
        float x = b_tangent(0, t) * p0.getX() + b_tangent(1, t) * p1.getX() + b_tangent(2, t) * p2.getX() + b_tangent(3, t) * p3.getX();
        float y = b_tangent(0, t) * p0.getY() + b_tangent(1, t) * p1.getY() + b_tangent(2, t) * p2.getY() + b_tangent(3, t) * p3.getY();  
        
        return new Point2D(x, y);
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private float b(int i, float t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
    public float b_tangent( int i , float t ) {
    	switch(i) {
        
        case 0:
            return -3 * (1-t) * (1-t);

        case 1:
            return -6 * ( 1 - t ) * t  +  3 * ( 1 - t ) * ( 1 - t );
            
        case 2:
            return -3 * t * t  +  6 * t * ( 1 - t );

        case 3:
            return 3 * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
//    public Point2D calculaten_normal_to_tangent( float t ) {
//    	
//    }
    
    public void road_distance() {
    	this.step_increment = ( ( float )( this.size() ) ) / this.division_factor;
    	this.distance = this.size() -  (float)( 1.0 / 3.0 ) - ( 2 * this.step_increment );
    }
    
    int debug = 0;
    
    /**
     * know x and z, find the angle
     */
    public float calculateDegree( float x , float y ) {
    	float degree = (float)Math.toDegrees( Math.atan2( y , x ) );

    	return degree;

    }
    
   
    
}
