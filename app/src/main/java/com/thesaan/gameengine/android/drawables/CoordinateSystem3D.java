package com.thesaan.gameengine.android.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.ui.GameSurface;


/**
 * Calculates a 3 dimensional coordinate system for drawing on a canvas.
 * This class does not contain any @see Canvas objects or drawing methods.
 * Only translation methods.
 */
public class CoordinateSystem3D {


    public CoordinateAxis xAxis,yAxis,zAxis;
    Canvas canvas;
    SurfaceHolder holder;

    public CoordinateSystem3D(CoordinateAxis[] axises, Canvas canvas, SurfaceHolder holder){
        xAxis = axises[0];
        yAxis = axises[1];
        zAxis = axises[2];

        this.canvas = canvas;
        this.holder = holder;
    }

    public CoordinateAxis[] getAxises(){
        return new CoordinateAxis[]{xAxis,yAxis,zAxis};
    }

    /**
     * Calls onRotate for every axis.
     * @param distance
     *  Simulates the angle for the rotation
     * @param axis
     */
    public void onRotate(float distance,int direction,int axis){
        setToScreenOrigin();
        xAxis.onRotate(distance, direction, axis);
        yAxis.onRotate(distance, direction, axis);
        zAxis.onRotate(distance, direction, axis);
        setOriginToCenter();
    }

    /**
     * Set all 3 axises to the screen origin at the top left cornet
     */
    public void setToScreenOrigin(){
        yAxis.setToScreenOrigin();
        xAxis.setToScreenOrigin();
        zAxis.setToScreenOrigin();
    }

    /**
     * Set all 3 axises to the canvas center ( or to the center of your choise)
     */
    public void setOriginToCenter(){
        yAxis.setOriginToCenter();
        xAxis.setOriginToCenter();
        zAxis.setOriginToCenter();
    }

    /**
     * Returns all positioning data in an array with the format
     * <p>In this class the axises are different:</p>
     * <p>x - depth</p>
     * <p>y - horizontal (x on paper)</p>
     * <p>z - vertical (y on paper)</p>
     * <p>0: X-Axis-Start-Vector(X-Value)</p>
     * <p>1: X-Axis-Start-Vector(Z-Value)</p>
     * <p>2: X-Axis-End-Vector(X-Value)</p>
     * <p>3: X-Axis-End-Vector(Z-Value)</p>
     * <p>4: Y-Axis-Start-Vector(X-Value)</p>
     * <p>5: Y-Axis-Start-Vector(Z-Value)</p>
     * <p>6: Y-Axis-End-Vector(X-Value)</p>
     * <p>7: Y-Axis-End-Vector(Z-Value)</p>
     * <p>8: Z-Axis-Start-Vector(X-Value)</p>
     * <p>9: Z-Axis-Start-Vector(Z-Value)</p>
     * <p>10: Z-Axis-End-Vector(X-Value)</p>
     * <p>11: Z-Axis-End-Vector(Z-Value)</p>
     * @return
     */
    public float[] getCoordinateDataAsFloat2D(){
        float[] coord = {
                 xAxis.startVec.getmFloatVec()[0],
                 xAxis.startVec.getmFloatVec()[2],
                 xAxis.endVec.getmFloatVec()[0],
                 xAxis.endVec.getmFloatVec()[2],
                 yAxis.startVec.getmFloatVec()[0],
                 yAxis.startVec.getmFloatVec()[2],
                 yAxis.endVec.getmFloatVec()[0],
                 yAxis.endVec.getmFloatVec()[2],
                 zAxis.startVec.getmFloatVec()[0],
                 zAxis.startVec.getmFloatVec()[2],
                 zAxis.endVec.getmFloatVec()[0],
                 zAxis.endVec.getmFloatVec()[2],
        };
        return coord;
    }

    /**
     * Returns the y values of xyz-axises as layer level value
     * 0 - x start
     * 1 - x end
     * 2 - y start
     * 3 - y end
     * 4 - z start
     * 5 - z end
     * @return
     */
    public float[] getLayerValues(){
        float[] coord = {
                 xAxis.startVec.getmFloatVec()[1],
                 xAxis.endVec.getmFloatVec()[1],
                 yAxis.startVec.getmFloatVec()[1],
                 yAxis.endVec.getmFloatVec()[1],
                 zAxis.startVec.getmFloatVec()[1],
                 zAxis.endVec.getmFloatVec()[1],
        };
        return coord;
    }
    public float[] getZValues(){
        float[] coord = {
                 xAxis.startVec.getmFloatVec()[2],
                 xAxis.endVec.getmFloatVec()[2],
                 yAxis.startVec.getmFloatVec()[2],
                 yAxis.endVec.getmFloatVec()[2],
                 zAxis.startVec.getmFloatVec()[2],
                 zAxis.endVec.getmFloatVec()[2],
        };
        return coord;
    }
    public float[] getXValues(){
        float[] coord = {
                 xAxis.startVec.getmFloatVec()[0],
                 xAxis.endVec.getmFloatVec()[0],
                 yAxis.startVec.getmFloatVec()[0],
                 yAxis.endVec.getmFloatVec()[0],
                 zAxis.startVec.getmFloatVec()[0],
                 zAxis.endVec.getmFloatVec()[0],
        };
        return coord;
    }
    public Paint getXPaint(){
        return xAxis.getMyPaint();
    }
    public Paint getYPaint(){
        return yAxis.getMyPaint();
    }
    public Paint getZPaint(){
        return zAxis.getMyPaint();
    }


    /**
     * Created by mknoe on 21.04.2015.
     */
    public static class CoordinateAxis {

        //defines the pivot between start and end vector
        private MathHandler.Vector translationPivot;

        private final int X= 0;
        private final int Y = 1;
        private final int Z = 2;
        private int myType;
        //Vectors of the start and end position
        MathHandler.Vector startVec,endVec;

        public final static String[] axisNames = {
                "x-axis","y-axis","z-axis"
        };

        private float myStartX,myStartZ,myStartY,myEndX,myEndZ,myEndY;

        //to get the touch event
        private GameSurface myScreen;

        float top,bottom,left,right;
        float centerX;
        float centerY;
        float centerZ;
        private String myAxis;

        Paint xPaint, yPaint, zPaint;

        private Paint myPaint;

        //horizontal or vertical
        private int movingDirection;
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/

        /**
         * The z&x-axises  y-value are set 0 to to have a canvas. Only the y-axis gets an y-value.
         *
         * Please note that the Screen y axis is the z axis in this class. So the y axis is the room depth.
         * @param start
         * @param end
         * @param edges
         * @param surfaceView
         */
        public CoordinateAxis(Coordinate start, Coordinate end, float[] edges, GameSurface surfaceView, int axis){

            synchronized(this){
                myScreen = surfaceView;
            }

            top = edges[0];
            bottom = edges[1];
            left = edges[2];
            right = edges[3];

            centerZ = bottom/2;
            centerX = right/2;
            //shows the 3d center of the y axis
            centerY = 0;
            myEndX = right-(right-end.getMyX());

            myStartX = right-(right-start.getMyX());
            myStartY = checkYAxisValue(axis);
            myStartZ = bottom-(bottom-start.getMyY());




            //method also defines other axises
            myEndY = myStartY*-1;
            myEndZ = bottom-(bottom-end.getMyY());


            //start vector
            startVec = new MathHandler.Vector(myStartX,myStartY,myStartZ);
            //end vector
            endVec = new MathHandler.Vector(myEndX,myEndY,myEndZ);
        }

        private float checkYAxisValue(int axis) {
            switch (axis){
                case MathHandler.TranslationMatrix.X_AXIS:
                    setAxisName(axisNames[0]);
                    myType = axis;
                    xPaint = new Paint();
                    xPaint.setColor(Color.RED);
                    setPaint(xPaint);
                    return (myEndX/4);
                case MathHandler.TranslationMatrix.Y_AXIS:
                    setAxisName(axisNames[1]);
                    myType = axis;
                    yPaint = new Paint();
                    yPaint.setColor(Color.GREEN);
                    setPaint(yPaint);
                    return 0;
                case MathHandler.TranslationMatrix.Z_AXIS:
                    setAxisName(axisNames[2]);
                    myType = axis;
                    zPaint = new Paint();
                    zPaint.setColor(Color.BLUE);
                    setPaint(zPaint);
                    return 0;
                default:
                    return -1;
            }
        }
        public void printVectorData(){
            System.out.println(getMyAxisName()+"\n"+
                    "StartX "+myStartX+
                    " StartZ "+myStartZ+
                    " EndX "+myEndX+
                    " EndZ "+myEndZ
            );

        }
        /*----------------------------------------SETTERS-----------------------------------*/

        public void setPaint(Paint p){
            myPaint = p;
        }
        private void setAxisName(String name){
            myAxis = name;
        }
        public void setMyScreen(GameSurface s){
            myScreen = s;
        }


        /*----------------------------------------BOOLERS-----------------------------------*/

        /*----------------------------------------GETTERS-----------------------------------*/

        public int getMyType() {
            return myType;
        }

        public MathHandler.Vector getEndVec() {
            return endVec;
        }

        public MathHandler.Vector getStartVec() {
            return startVec;
        }

        public Paint getMyPaint(){
            return myPaint;
        }
        public String getMyAxisName() {
            return myAxis;
        }

        /*----------------------------------------HANDLERS-----------------------------------*/
        private void rotate(float angle, int axis, MathHandler.Vector vector){
            MathHandler.getTranslationMatrix().rotateVector3D(vector,angle,axis);
        }
        public static int getDirection(int direction){
            switch (direction){
                case GameSurface.DIRECTION_LEFT:
                case GameSurface.DIRECTION_UP:{
                    return  -1;
                }
                case GameSurface.DIRECTION_RIGHT:
                case GameSurface.DIRECTION_DOWN:{
                    return  1;
                }
                default:
                    return  1;
            }
        }
        public void onRotate(float angle,int direction, int axis){
            int directionSelector = getDirection(direction);
            if(myScreen != null) {
                setToScreenOrigin();
                rotate(angle * directionSelector, axis, startVec);
                rotate(angle * directionSelector, axis, endVec);
                setOriginToCenter();
            } else {
                System.err.println("SurfaceView myScreen is null");
            }
        }
        public void setToScreenOrigin(){
            MathHandler.Vector changeVec = new MathHandler.Vector(250.0f,0f,250.0f);
            startVec = startVec.subtractWith(changeVec);
            endVec = endVec.subtractWith(changeVec);
        }
        public void setOriginToCenter(){
            MathHandler.Vector changeVec = new MathHandler.Vector(250.0f,0f,250.0f);
            startVec.addWith(changeVec);
            endVec.addWith(changeVec);
        }

//        public void setToScreenOrigin(){
//            translationPivot = new MathHandler.Vector(0f,0f,0f);
//            //TODO check if the coordinates are equal
//            if(startVec.getXf() < endVec.getXf()){
//                translationPivot.setX((endVec.getXf()-startVec.getXf())/2);
//            }else{
//                translationPivot.setX((startVec.getXf()-endVec.getXf())/2);
//            }
//            if(startVec.getYf() < endVec.getYf()){
//                translationPivot.setY((endVec.getYf() - startVec.getYf()) / 2);
//            }else{
//                translationPivot.setY((startVec.getYf() - endVec.getYf()) / 2);
//            }
//            if(startVec.getZf() < endVec.getZf()){
//                translationPivot.setZ((endVec.getZf() - startVec.getZf()) / 2);
//            }else{
//                translationPivot.setZ((startVec.getZf() - endVec.getZf()) / 2);
//            }
//
//            startVec.subtractWith(translationPivot);
//            endVec.subtractWith(translationPivot);
//        }
//        public void setOriginToCenter(){
//            startVec.addWith(translationPivot);
//            endVec.addWith(translationPivot);
//        }
        public void setStartVec(MathHandler.Vector startVec) {
            this.startVec = startVec;
        }

        public void setEndVec(MathHandler.Vector endVec) {
            this.endVec = endVec;
        }

        /**
         * Created by mknoe on 21.04.2015.
         */
        public static class Coordinate{

            private float myX,myY, myZ;



            public Coordinate(float x,float y){

                myX = x;
                myY = y;
            }
            public Coordinate(float x, float y, float z){
                myX = x;
                myY = y;
                myZ = z;
            }

            public float getMyX() {
                return myX;
            }

            public float getMyY() {
                return myY;
            }

            public float getMyZ() {
                return myZ;
            }


        }
    }
}
