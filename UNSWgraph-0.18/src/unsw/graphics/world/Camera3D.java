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
 * The 3Dcamera for the assignment2
 *
 * @Modified Xudong Shi, Liu Yixiong
 * @author Robert Clifton-Everest
 */
public class Camera3D implements KeyListener {

    private Point3D myPos;
    private float myAngle;
    private float myScale;
    private float myAngle_X;
    private float myAngle_Z;

    public Camera3D() {
        myPos = new Point3D(0, 0, 10);
        myAngle = 0;
<<<<<<< HEAD
        myScale = 0;
=======
        myScale = 10;
        myAngle_X = 0;
        myAngle_Z = 0;
>>>>>>> 1b2f090a9e281ee1ccb00ca05359977a22eb32b9
    }
    
    public Point3D CameraNormal() {
    	return new Point3D( (float)Math.sin( Math.toRadians( this.myAngle ) ) ,
    						0 ,
    						- (float)Math.cos( Math.toRadians( this.myAngle ) ) 
    						);
    }
    
    public Point3D CameraPostion() {
    	return this.myPos;
    }
    
    public void draw(GL3 gl, CoordFrame3D frame) {
        CoordFrame3D cameraFrame = frame.translate(myPos)
                .rotateY(myAngle);

        //Draw the camera
//        LineStrip2D camera = new LineStrip2D(1,1, -1,1, -1,-1, 1,-1, 1,1);
//        camera.draw(gl, cameraFrame);
    }

    /**
     * Set the view transform
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
<<<<<<< HEAD
        	// If shift is down, camera should translate left
            if (e.isShiftDown()) {
            	myPos = new Point3D(myPos.getX()-1, myPos.getY(), myPos.getZ());
            }
            // if not, turn left
            else {
            	myAngle += 5;
            }
            break;
        case KeyEvent.VK_RIGHT:
        	// If shift is down, camera should translate right
            if (e.isShiftDown()) {
            	myPos = new Point3D(myPos.getX()+1, myPos.getY(), myPos.getZ());
            }
            // If not, camera turn right
            else {
            	myAngle -= 5;
            	//myPos = new Point3D(myPos.getX() + 1, myPos.getY() ,  myPos.getZ() );         
            }
            break;
        case KeyEvent.VK_DOWN:
        	// camera go backward
        	if (e.isShiftDown()) {
        		myPos = new Point3D(myPos.getX(), myPos.getY(), myPos.getZ()-1);
        	}
        	// camera falls along Y axis
        	else {
                myPos = new Point3D(myPos.getX(), myPos.getY()-1, myPos.getZ());
        	}
            break;
        case KeyEvent.VK_UP:
        	// camera go forward
        	if (e.isShiftDown()) {
                myPos = new Point3D(myPos.getX(), myPos.getY(), myPos.getZ() - 1 );
        	}
        	// camera elevates
        	else {
        		myPos = new Point3D(myPos.getX(), myPos.getY()+1, myPos.getZ());
        	}
=======
            myAngle += 5;
            break;
        case KeyEvent.VK_RIGHT: 
        	myAngle -= 5;
            break;
        case KeyEvent.VK_DOWN:
        	float x = this.myPos.getX() + (float)Math.sin( Math.toRadians( this.myAngle ) );
        	float z = this.myPos.getZ() + (float)Math.cos( Math.toRadians( this.myAngle ) );
            myPos = new Point3D(x , myPos.getY()  , z );
            break;
        case KeyEvent.VK_UP:
        	float x_1 = this.myPos.getX() - (float)Math.sin( Math.toRadians( this.myAngle ) );
        	float z_1 = this.myPos.getZ() - (float)Math.cos( Math.toRadians( this.myAngle ) );
            myPos = new Point3D(x_1 , myPos.getY()  , z_1 );
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
>>>>>>> 1b2f090a9e281ee1ccb00ca05359977a22eb32b9
            break;
          
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
