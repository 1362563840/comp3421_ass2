package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.*;

public class Avatar {

    private TriangleMesh model;
    private Texture skin;

    private Point3D position;
    private float rotateY;

    public Avatar(Point3D position, String modelFile) throws IOException {
        this.position = position;
        this.model = new TriangleMesh(modelFile, true, true);
        this.rotateY = -135;
    }

    public Point3D getPosition() { return position; }

    public float getRotateY() { return rotateY; }

    public void setRotateY(float angleY) { rotateY = angleY; }

    public void setNewPos(Point3D newPos) { 
    	position = newPos; 
    }

    public void init(GL3 gl) {
        model.init(gl);
//        Shader shader = new Shader(gl, "shaders/vertex_tex_phong.glsl",
//                "shaders/fragment_tex_phong.glsl");
        skin = new Texture(gl, "res/textures/rock.bmp", "bmp", false);
//        shader.use(gl);
    }

    public void drawSelf(GL3 gl) {
    	Shader.setInt(gl, "mode", 1);
        Shader.setInt(gl, "tex", 4);

        gl.glActiveTexture(GL.GL_TEXTURE4);
        gl.glBindTexture(GL.GL_TEXTURE_2D, skin.getId());
        
        CoordFrame3D modelFrame = CoordFrame3D.identity().translate(position)
                .rotateY(rotateY).scale(1, 1, 1);
        Shader.setModelMatrix(gl, modelFrame.getMatrix());
        model.draw(gl, modelFrame);
    }

    public void destory(GL3 gl) {
        model.destroy(gl);
        skin.destroy(gl);
    }
}