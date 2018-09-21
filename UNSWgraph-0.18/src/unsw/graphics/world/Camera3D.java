package unsw.graphics.world;


import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.LineStrip2D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;


/**
 * The camera for the person demo
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class Camera3D implements KeyListener {

    private Point3D myPos;
    private float myAngle;
    private float myScale;
    private float myAngle_X;
    private float myAngle_Z;

    public Camera3D() {
        myPos = new Point3D(0, 0, 0);
        myAngle = 0;
        myScale = 10;
        myAngle_X = 0;
        myAngle_Z = 0;
    }
    
    public void draw(GL3 gl, CoordFrame3D frame) {
        CoordFrame3D cameraFrame = frame.translate( myPos )
                .rotateY( myAngle );

//        //Draw the camera
//        LineStrip2D camera = new LineStrip2D(1,1, -1,1, -1,-1, 1,-1, 1,1);
//        camera.draw(gl, cameraFrame);
    }

    /**
     * Set the view transform
     * 
     * Note: this is the inverse of the model transform above
     * 
     * @param gl
     */
    public void setView(GL3 gl) {
    	
    	// if camera's position is near the hill, then adjust the y direction
//    	this.myPos = new Point3D( myPos.getX() +   ); 
    	
    	
        CoordFrame3D viewFrame = CoordFrame3D.identity()
                .scale(1/myScale, 1/myScale , 1/myScale )
                .rotateX(-myAngle_X).rotateY(-myAngle).rotateZ(-myAngle_Z)   //.rotateY(-myAngle).rotateX(-myAngle).
                .translate(-myPos.getX(), -myPos.getY() , -myPos.getZ() );
        Shader.setViewMatrix(gl, viewFrame.getMatrix());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            myAngle += 5;
            break;
        case KeyEvent.VK_RIGHT: 
        	myAngle -= 5;
            break;
        case KeyEvent.VK_DOWN:
            myPos = new Point3D(myPos.getX(), myPos.getY() ,  myPos.getZ() + 1 );
            break;
        case KeyEvent.VK_UP:
        	float a = 1;
        	float a_z = a * (float)Math.cos( Math.toRadians( this.myAngle ) );
        	float a_x = a * (float)Math.sin( Math.toRadians( this.myAngle ) );
            myPos = new Point3D(myPos.getX() + a_x , myPos.getY()  , myPos.getZ() - a_z );
            break;
            
        case KeyEvent.VK_W:
            myPos = new Point3D(myPos.getX(), myPos.getY() + 1 , myPos.getZ() );
            break;
        
        case KeyEvent.VK_S:
            myPos = new Point3D(myPos.getX(), myPos.getY() - 1  , myPos.getZ() );
            break;
            
        case KeyEvent.VK_A:
            myPos = new Point3D(myPos.getX() - 1, myPos.getY() , myPos.getZ() );
            break;
        
        case KeyEvent.VK_D:
            myPos = new Point3D(myPos.getX() + 1 , myPos.getY()  , myPos.getZ() );
            break;
           
        case KeyEvent.VK_Z:
//        	this.
            break;
        
        case KeyEvent.VK_X:
            break;
          
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
