package unsw.graphics.world;


import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.LineStrip2D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;

import java.io.IOException;

public class Camera3DWithAva implements KeyListener {

	private Avatar myAva;
	private Terrain myTer;
	
	/**
	 * 1 is first person
	 * 3 is third person
	 */
	private int mode;
	
	public Camera3DWithAva(Avatar ava, Terrain ter) {
		this.myAva = ava;
		this.myTer = ter;
	}
	
	/**
	 * Use frame of avatar
	 * @param gl
	 */
	public void setViewFirst(GL3 gl) {
		float height = myTer.altitude(myAva.getPosition().getX(), myAva.getPosition().getZ());
		CoordFrame3D viewFrame = CoordFrame3D.identity()
				.rotateY(-myAva.getRotateY())
				.translate(-myAva.getPosition().getX(), -height-1f, -myAva.getPosition().getZ());
		Shader.setViewMatrix(gl, viewFrame.getMatrix());
	}
	
	/**
	 * Inverse transformation based on myAva's frame
	 * @param gl
	 */
	public void setViewThird(GL3 gl) {
		CoordFrame3D viewFrame = CoordFrame3D.identity()
				.translate(-2, 0.5f, -2)
				.rotateY(-myAva.getRotateY())
				.translate(-myAva.getPosition().getX(), -myAva.getPosition().getY(), -myAva.getPosition().getZ());
		Shader.setViewMatrix(gl, viewFrame.getMatrix());
	}
	
	public Matrix4 getThirdFrameMatrix() {
		CoordFrame3D viewFrame = CoordFrame3D.identity()
				.translate(-1, 0.5f, -1)
				.rotateY(-myAva.getRotateY())
				.translate(-myAva.getPosition().getX(), -myAva.getPosition().getY(), -myAva.getPosition().getZ());
		return viewFrame.getMatrix();
	}
	
	/**
	 * Press key, avatar goes, camera's position is calculated by transform
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		float tempAngle;
		float height;
		switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            tempAngle = myAva.getRotateY();
            tempAngle += 5;
            myAva.setRotateY(tempAngle);
            
            break;
        case KeyEvent.VK_RIGHT: 
        	tempAngle = myAva.getRotateY();
        	tempAngle -= 5;
        	myAva.setRotateY(tempAngle);
        	
            break;
        case KeyEvent.VK_DOWN:
        	float xAvaD = myAva.getPosition().getX() + (float)Math.sin(Math.toRadians(myAva.getRotateY()));
        	float zAvaD = myAva.getPosition().getZ() + (float)Math.sin(Math.toRadians(myAva.getRotateY()));
        	height = myTer.altitude(xAvaD, zAvaD);
        	myAva.setNewPos(new Point3D(xAvaD, height, zAvaD));
           
            break;
        case KeyEvent.VK_UP:
        	float xAvaU = myAva.getPosition().getX() - (float)Math.sin(Math.toRadians(myAva.getRotateY()));
        	float zAvaU = myAva.getPosition().getZ() - (float)Math.sin(Math.toRadians(myAva.getRotateY()));
        	height = myTer.altitude(xAvaU, zAvaU);
        	myAva.setNewPos(new Point3D(xAvaU, height, zAvaU));
        	
            break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}
}
