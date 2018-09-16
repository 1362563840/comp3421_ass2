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

    public Camera3D() {
        myPos = new Point3D(0, 0, 0);
        myAngle = 0;
        myScale = 10;
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
        CoordFrame3D viewFrame = CoordFrame3D.identity()
                .scale(1/myScale, 1/myScale , 1/myScale )
                .rotateY(-myAngle)   //.rotateY(-myAngle).rotateX(-myAngle).
                .translate(-myPos.getX(), -myPos.getY() , -myPos.getZ() );
        Shader.setViewMatrix(gl, viewFrame.getMatrix());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            if (e.isShiftDown()) {
//                myAngle += 5;
            }
            else {
            	myAngle += 5;
//            	myPos = new Point3D(myPos.getX() - 1, myPos.getY() , 0 );      
            }
            break;
            
        case KeyEvent.VK_RIGHT:
            if (e.isShiftDown()) {
//                myAngle -= 5;
            }
            else {
            	myAngle -= 5;
//            	myPos = new Point3D(myPos.getX() + 1, myPos.getY() ,  myPos.getZ() );         
            }
            break;

        case KeyEvent.VK_DOWN:
            
            myPos = new Point3D(myPos.getX(), myPos.getY() , myPos.getZ() + 1 );
            break;

        case KeyEvent.VK_UP:
        
            myPos = new Point3D(myPos.getX(), myPos.getY()  , myPos.getZ() - 1 );
            break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
