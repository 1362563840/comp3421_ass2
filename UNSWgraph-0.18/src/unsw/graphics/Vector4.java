/**
 * 
 */
package unsw.graphics;

import java.util.Arrays;

import unsw.graphics.geometry.Point3D;

/**
 * A vector of rank 4.
 * 
 * This can be used to represent homogenous coordinates in 3D.
 * 
 * This class is immutable.
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Vector4 {
    private float[] values;
    
    /**
     * Construct a vector from the given array of float.
     * 
     * @throws IllegalArgumentException if values.length != 4
     * @param values
     */
    public Vector4(float[] values) {
        if (values.length != 4)
            throw new IllegalArgumentException("Vector4 constructor passed an array of length " + values.length);
        
        this.values = Arrays.copyOf(values, 4);
    }
    
    /**
     *
     * This one is only working for local coordinate 
     * 
     * Based on the coordinate of the point3D 
     * 
     * rotate given the parameter
     * 
     * you still need to transfer it to the global by multiply the global transformation
     * 
     * this gloabl transformation should just be the frame passed from draw() and then translate the offset. This offset is just the 
     * 
     * bezier coordinate
     * 
     * this is used to calcuate the rotation in xz coordinate 
     * 
     * not in y coordinate
     * 
     * It should be just used to draw the roads
     */
    public Vector4 rotateY( float degree ) {
    	float temp = 90 - degree;
    	
    	assert ( temp <= 180 );
    	assert ( temp >= -180 );

    	
    	assert ( temp <= 180 );
    	assert ( temp >= -180 );
    	
    	
    	Matrix4 temp_new_coord = Matrix4.identity().rotationY( temp );
    	Vector4 temp_v = temp_new_coord.multiply( this );
    	
    	
    	// normalize the angle, make sure negative x always clockwise rotate, 
    	// and positive x always anti-clockwise rotate
    	
    	return temp_v;
    }
    
    /**
     * Construct a vector from the given x, y and z values.
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public Vector4(float x, float y, float z, float w) {
        this.values = new float[] { x, y, z, w };
    }
    
    /**
     * Compute the dot product of this vector with the given vector.
     * @param b
     * @return
     */
    public float dotp(Vector4 b) {
        float r = 0;
        for (int i = 0; i < values.length; i++) {
            r += this.values[i] * b.values[i];
        }
        return r;
    }
    
    /**
     * Ignores the final value to create a 2D point
     * @return
     */
    public Point3D asPoint3D() {
        return new Point3D(values[0], values[1], values[2]);
    }

    /** 
     * Subtract v from this vector.
     * @param v
     * @return
     */
    public Vector4 minus(Vector4 v) {
        return new Vector4(values[0] - v.values[0], values[1] - v.values[1], 
                values[2] - v.values[2], values[3] - v.values[3]);
    }

    /**
     * Remove the 4th (w) component
     * @return
     */
    public Vector3 trim() {
        return new Vector3(values[0], values[1], values[2]);
    }
}
