package com.thesaan.gameengine.android.opengl.shapes;

import android.opengl.GLES20;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Michael on 31.12.2015.
 */
public class Shape {

    private FloatBuffer vertexBuffer;


    int screenWidth;
    int screenHeight;

    //for the own MathHandler class
    public MathHandler.Vector mVector;


    // number of coordinates per vertex in this array
    public static final int COORDS_PER_VERTEX = 3;

    private float posX;
    private float posY;
    private float posZ;

    float pointCoords[];

//    private final String vertexShaderCode =
//            "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_Position = vPosition;" +
//                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private int mProgram;

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private int mPositionHandle;
    private int mColorHandle;

    int vertexCount;
    int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Shape() {

    }

    /**
     * A default {@link Shape}
     */
    public Shape(float[] map_coordinates) {


//        pointCoords = new float[map_coordinates.length];
        pointCoords = map_coordinates;
//        copyCoordinates(pointCoords, map_coordinates);
        vertexCount = pointCoords.length / COORDS_PER_VERTEX;


        init();
    }

    /**
     * For a single Point ({@link Vertex})
     *
     * @param x
     * @param y
     * @param z
     */
    public Shape(float x, float y, float z) {
        posX = x;
        posY = y;
        posZ = z;


        pointCoords = new float[]{x, y, z};
        vertexCount = pointCoords.length / COORDS_PER_VERTEX;

        init();
    }

    /**
     * @param mvpMatrix
     * @param shape     <p>{@link GLES20#GL_POINTS}</p>
     *                  <p>{@link GLES20#GL_TRIANGLE_FAN}</p>
     *                  <p>{@link GLES20#GL_TRIANGLE_STRIP}</p>
     *                  <p>{@link GLES20#GL_TRIANGLES}</p>
     *                  <p>{@link GLES20#GL_LINES}</p>
     */
    public void draw(float[] mvpMatrix, int shape) {
        updateBuffer();

        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(shape, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    /**
     * Set the coordinates buffer. Also call for re-init
     */
    public void init() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                pointCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(pointCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * @param current
     * @param coords
     */
    public void copyCoordinates(float[] current, float[] coords) {
        int length = coords.length;
        for (int i = 0; i < length; i++) {
            current[i] = coords[i];
        }
    }


    /**
     * @return
     */
    public float[] getPointCoords() {
        return pointCoords;
    }

    public void setPointCoords(float[] coords) {
        this.pointCoords = coords;
    }


    /**
     * Setup the vertexBuffer with the new Coordinates
     */
    public void updateBuffer() {

        //reset pointCoords from Mathhandler.Vektor
//        mVector.definePositionAsScreenPercentage();

//        applyVectorTranslation();

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                pointCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

//        vertexBuffer = null;

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(pointCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }


    /**
     * Set the current screen dimension
     *
     * @param width
     * @param height
     */
    public void setDimension(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void convertToScreenPercentage() {
        MathHandler.Matrix m = new MathHandler.Matrix();
        m.setScreenDimension(screenWidth, screenHeight);

        for (int i = 0; i < pointCoords.length; i += 3) {
            for (int k = 0; k < 3; k++) {

                switch (k) {
                    case 0://x
                        pointCoords[i + k] = (float) m.getWidthPercentage(
                                pointCoords[i + k], screenWidth) / 100;
                        break;
                    case 1://y
                        pointCoords[i + k] = (float) m.getHeightPercentage(
                                pointCoords[i + k], screenHeight) / 100;
                        break;
                    case 2://z
                        pointCoords[i + k] = (float) m.getWidthPercentage(
                                pointCoords[i + k], screenWidth * 2) / 100;
                        break;
                }

            }
        }
    }
}

