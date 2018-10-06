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
    private Terrain myTer;

    public Camera3D(Terrain TerUsed) {
        myPos = new Point3D(-1, 0, -1);
        myAngle = -135;
        myScale = 1;
        myAngle_X = 0;
        myAngle_Z = 0;
        myTer = TerUsed;
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
    	
    	float altitude = myTer.altitude(myPos.getX(), myPos.getZ());
        CoordFrame3D viewFrame = CoordFrame3D.identity()
                .scale(1/myScale, 1/myScale , 1/myScale )
                //.rotateX(-myAngle_X).rotateY(-myAngle).rotateZ(-myAngle_Z)   //.rotateY(-myAngle).rotateX(-myAngle).
                .rotateY(-myAngle)
                .translate(-myPos.getX(), -altitude - 1f , -myPos.getZ() );
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
        	float x = this.myPos.getX() + (float)Math.sin( Math.toRadians( this.myAngle ) );
        	float z = this.myPos.getZ() + (float)Math.cos( Math.toRadians( this.myAngle ) );
            myPos = new Point3D(x , myPos.getY()  , z );
            //System.out.println(throughWall());
            break;
        case KeyEvent.VK_UP:
        	float x_1 = this.myPos.getX() - (float)Math.sin( Math.toRadians( this.myAngle ) );
        	float z_1 = this.myPos.getZ() - (float)Math.cos( Math.toRadians( this.myAngle ) );

            myPos = new Point3D(x_1, myPos.getY(), z_1);
            break;
            
        case KeyEvent.VK_W:
        	if (e.isShiftDown()) myPos = new Point3D(myPos.getX(), myPos.getY() + 1f , myPos.getZ() );
        	//myPos = new Point3D(myPos.getX(), myPos.getY() + 1f , myPos.getZ() );
            break;
        
        case KeyEvent.VK_S:
            myPos = new Point3D(myPos.getX(), myPos.getY() - 1f  , myPos.getZ() );
            break;
            
//        case KeyEvent.VK_A:
//            myPos = new Point3D(myPos.getX() - (float)0.1, myPos.getY() , myPos.getZ() );
//            break;
//        
//        case KeyEvent.VK_D:
//            myPos = new Point3D(myPos.getX() + (float)0.1 , myPos.getY()  , myPos.getZ() );
//            break;
//           
        }

    }

    /**
     * Move camera along the hill
     * @param tempPoint
     * @return
     */
    public Point3D climb(Point3D tempPoint) {
        // calculate how much height
        float facingHeight = myTer.altitude(tempPoint.getX(), tempPoint.getZ());
        System.out.println(facingHeight);
        facingHeight += 0.5;
        System.out.println(facingHeight);
        return new Point3D(tempPoint.getX(), facingHeight, tempPoint.getZ());
    }

    /**
     *
     * QUESTION TO ASK: HOW TO DETERMINE CAMERA VIEW HAS GONE BEYOND TERRAIN
     *                  IT DOESN'T BEYOND WHEN LOOK AT COORDINATE, BUT ACTUALLY BEYOND FROM VIEW
     * Determine if camera in the wall
     * Check if it is on the terrain
     * Then compare the altitude
     * @return
     */
    public Boolean throughWall(Point3D tempPoint) {
        Boolean insideTerrain = false;
        Boolean throughWall = false;
        // Check if it on the terrain
        if (tempPoint.getX() >= 0 && tempPoint.getX() < myTer.getWidth() &&
                tempPoint.getZ() >= 0 && tempPoint.getZ() < myTer.getDepth()) {
            insideTerrain = true;
        }
        // if on, check altitude
        if (insideTerrain) {
            if (myTer.altitude(tempPoint.getX(), tempPoint.getZ()) >= tempPoint.getY()) {
                throughWall = true;
            }
        }
        return throughWall;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
