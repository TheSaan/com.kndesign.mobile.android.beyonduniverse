package com.thesaan.gameengine.android.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.World;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.TestHandler;
import com.thesaan.gameengine.android.opengl.shapes.Triangle;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 30.12.2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Vertex mPoint;

    Context mContext;

    /**
     * Stands for the users input origin coordinates
     */
    MathHandler.Vector origin;

    private float[] mRotationMatrix = new float[16];

    /**
     * Store the accumulated rotation.
     */
    private final float[] mAccumulatedRotation = new float[16];


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public volatile float mAngle;
    public volatile float mLastAngle;

    //the distance the finger already moved
    //to calculate a certain angle for direct moved transistion
    public float x_moved;
    public float y_moved;

    public float mDeltaX;
    public float mDeltaY;

    //defines the speed, the finger moves on the screen
    public float fingerSpeed;

    //axices which rotating
    private int xAxis, yAxis, zAxis;

    //just getting the screen dimension
    public int screenWidth;
    public int screenHeigth;

    Galaxy[] galaxies;


    Map map;
    TestHandler test;

    public boolean objectsLoaded = false;


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        test = new TestHandler();

        //reset axices
        xAxis = yAxis = zAxis = 0;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // initialize a triangle
        mTriangle = new Triangle();

        // Initialize the accumulated rotation matrix
//        Matrix.setIdentityM(mAccumulatedRotation, 0);


    }


    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        float[] scratch = new float[16];

        mAngle = -0.05f * (float) getDegreesFromTouchEvent();
//        test.startTimer();
//        Matrix.setRotateM(mRotationMatrix,
//                0,
//                (float)getDegreesFromTouchEvent(),
//                xAxis,
//                yAxis,
//                zAxis
//        );
//        System.out.print("[1]");
//        for (float m : mRotationMatrix) {
//            System.out.print(m);
//        }
//        Matrix.setRotateM(mRotationMatrix, 0, (float)getDegreesFromTouchEvent(), 0, 0, 1);
//        test.stopTimer("OpenGLES20 setRotationMatrix");

        MathHandler.Matrix rotation = new MathHandler.Matrix();

        rotation.setMatrix(MathHandler.Matrix.getRotationMatrix(
                        mAngle,
                        xAxis,
                        yAxis,
                        zAxis,
                        origin
                )
        );
//        rotation.setMatrix(MathHandler.Matrix.getRotationMatrix(
//                        mAngle,
//                        0,
//                        0,
//                        1,
//                        null
//                )
//        );
//
        mRotationMatrix = rotation.toOpenGLESMatrix();

//        System.out.print("\n[2]");
//        for (float m : mRotationMatrix) {
//            System.out.print(m);
//        }
        setProjectionAndCameraView(scratch);


        mPoint.draw(scratch);

        System.out.println();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        setScreenDim(width, height);

        map = World.getMap(galaxies);
        map.setScreenSize(width, height);
        map.makeVertex();

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);//TODO do not set 7th arg above 3

    }

    public void setProjectionAndCameraView(float[] view) {

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //Combine the rotation matrix with the projection and camera view
        //Note that the mMVPMatrix factor *must be first* in order
        //for the matrix multiplication product to be correct.
        Matrix.multiplyMM(view, 0, mMVPMatrix, 0, mRotationMatrix, 0);

    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public boolean hasOrigin() {
        if (origin != null)
            return true;
        else
            return false;
    }

    public void setLastAngle(float mLastAngle) {
        this.mLastAngle = mLastAngle;
//        System.out.println("MyGLRenderer: mLastAngle: "+this.mLastAngle);
    }

    public void setFingerSpeed(float fingerSpeed) {
        this.fingerSpeed = fingerSpeed;
    }

    public void setAngle(float angle, float speed) {
//        System.out.println("Speed: "+ speed);
        long time = SystemClock.uptimeMillis() % 4000L;
//        mAngle = mLastAngle -(angle * speed * 0.009f * ((int) time));
        mAngle = mLastAngle - angle;
//        mAngle = angle *  0.00009f * ((int) time);
    }

    public void setOrigin(MathHandler.Vector origin) {
        this.origin = origin;
    }

    /**
     * Defines the axis multiplicators for the rotation method
     * of the Matrix class.
     */
    public void setAxices(int x, int y, int z) {
        xAxis = x;
        yAxis = y;
        zAxis = z;
    }

    public void setMovedDistance(float x, float y) {
        x_moved = x;
        y_moved = y;
    }

    public void setScreenDim(int width, int height) {
        screenWidth = width;
        screenHeigth = height;
    }

    public double getDegreesFromTouchEvent() {
        double delta_x = x_moved - (screenWidth) / 2;
        double delta_y = (screenHeigth) / 2 - y_moved;
        double radians = Math.atan2(delta_y, delta_x);

        double r = mLastAngle - Math.toDegrees(radians);
//        System.out.println("r = "+r);
        return r;
    }


    public double getDegreesFromTouchEvent(float x, float y) {
        double delta_x = x - (screenWidth) / 2;
        double delta_y = (screenHeigth) / 2 - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(radians);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setGalaxies(Galaxy[] galaxies) {
        this.galaxies = galaxies;
    }

    public void rotate(float[] mCurrentRotation) {

        float[] mTemporaryMatrix = new float[16];
        float[] mModelMatrix = new float[16];

        // Draw a cube.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.8f, -3.5f);

        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mRotationMatrix, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mRotationMatrix, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;

        // Multiply the current rotation by the accumulated rotation, and then set the accumulated
        // rotation to the result.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

        // Rotate the cube taking the overall rotation into account.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
    }

    public void setMap(Map map) {
        this.map = map;
    }
}