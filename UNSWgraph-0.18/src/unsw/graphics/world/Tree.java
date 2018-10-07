package unsw.graphics.world;

import java.awt.Color;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
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
    
    private Terrain terrian;
    
    public Tree(float x, float y, float z) {
        position = new Point3D(x, y, z);
        
        // Our code
        
        this.read();
    }
    
    public Point3D getPosition() {
        return position;
    }
    
    public void init( GL3 gl ) {
    	this.tree.init( gl );
    	this.texture = new Texture( gl, "res/textures/canLabel.bmp", "bmp", false );
    	this.setHeight();
    }
    
    public void setHeight() {
        float tempAlt = 0.5f+this.terrian.altitude(  this.position.getX() ,  this.position.getZ());
    	this.position = new Point3D( 
    						this.position.getX() , 
    						tempAlt,
    						this.position.getZ()
    						);
    }

   

    
    public void setTerrian( Terrain terrian ) {
    	this.terrian = terrian;
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
        	
    	
    	Shader.setInt(gl, "tex", 1);
    	gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture.getId());
        this.tree.draw( gl , frame.translate( this.position ).scale(0.5f, 0.1f, 0.1f) );
    }
    
    public void destroy( GL3 gl ) {
    	this.tree.destroy( gl );
    }
}
