package unsw.graphics.world;

import java.awt.Color;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private Point3D position;
    private TriangleMesh tree;
    private Texture texture;
    
    public Tree(float x, float y, float z) {
        position = new Point3D(x, y, z);
        
        // Our code
        
        this.read();
    }
    
    public Point3D getPosition() {
        return position;
    }
    
    public void init( GL3 gl ) {
    	System.out.println( "initial for tree texture" );
    	this.tree.init( gl );
    	this.texture = new Texture( gl, "res/textures/grass.bmp", "bmp", false );
    }
    
    /**
     * read the ply before displaying in order to increase the spped
     * 
     * 
     */
    public void read() {
    	try {
    		this.tree = new TriangleMesh( "res/models/tree.ply" , true , true );
    	}
    	catch ( IOException e ) {
    		System.out.println( "failure to read tree.ply" );
    		e.printStackTrace();
    		System.exit(1);
    	}
    }
    
    /**
     * provide texture things
     * provide vertices
     * @param gl
     * @param frame
     */
    // TODO
    public void drawSelf( GL3 gl , CoordFrame3D frame ) {	
    	
    	if ( texture == null ) {
    		System.out.println( "fuckyou" );
    	}
    	Shader.setInt(gl, "tex", 0);
    	gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture.getId());
//        Shader.setPenColor( gl , Color.WHITE );
        this.tree.draw( gl , frame );
    }
    
    public void destroy( GL3 gl ) {
    	this.tree.destroy( gl );
    }
}
