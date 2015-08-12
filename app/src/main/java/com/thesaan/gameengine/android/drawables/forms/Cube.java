package com.thesaan.gameengine.android.drawables.forms;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.ui.StarMapSurface;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Cube implements Form{
    
    float width,height,depth;
    
    int myColor;

    Paint p;
    
    MathHandler.Vector pivot;
    
    //the single coordinates ids for the array
    public final static int v1Point = 0;
    public final static int v2Point = 1;
    public final static int v3Point = 2;
    public final static int v4Point = 3;
    public final static int v5Point = 4;
    public final static int v6Point = 5;
    public final static int v7Point = 6;
    public final static int v8Point = 7;
    public final static int X = 0;
    public final static int Y = 1;
    public final static int Z = 2;
    

   
    //the corner coordinates
    MathHandler.Vector
        v1,v2,v3,v4,v5,v6,v7,v8;

    MathHandler.Vector[] points = {
            v1,v2,v3,v4,v5,v6,v7,v8
    };
    
    public Cube(float width,float height,float depth,int color,MathHandler.Vector pivot){
        this.width = width;
        this.height = height;
        this.depth = depth;
        
        this.myColor = color;

        p = new Paint();

        p.setColor(myColor);

        this.pivot = pivot;

        if(pivot != null)
            buildCube(width,height,depth,pivot);
        else
            System.err.println("Pivot is null");
    }

    private void buildCube(float width, float height, float depth, MathHandler.Vector pivot) {
        float x = pivot.getmFloatVec()[0];
        float y = pivot.getmFloatVec()[1];
        float z = pivot.getmFloatVec()[2];

        //get the first point 
        v1 = new MathHandler.Vector(x-width/2,y-height/2,z*2);
        
        x = v1.getmFloatVec()[0];
        y = v1.getmFloatVec()[1];
        z = v1.getmFloatVec()[2];
        
        //now calculate all other points from the first
        v2 = new MathHandler.Vector(x+width,y,depth);
        v3 = new MathHandler.Vector(x,y+height,depth);
        v4 = new MathHandler.Vector(x+width,y+height,depth);
        
        v5 = new MathHandler.Vector(x+width/2,y-height/2,depth/2);
        v6 = new MathHandler.Vector(x+width+(width/2),y-height/2,depth/2);
        v7 = new MathHandler.Vector(x+width/2,y+height/2,depth/2);
        v8 = new MathHandler.Vector(x+width+(width/2),y+height/2,depth/2);
    }
    
    
    public float[][] getCubeData(){
        float[][] data = {
                v1.getmFloatVec(),
                v2.getmFloatVec(),
                v3.getmFloatVec(),
                v4.getmFloatVec(),
                v5.getmFloatVec(),
                v6.getmFloatVec(),
                v7.getmFloatVec(),
                v8.getmFloatVec(),
        };
        
        return data;
    }
    
    public void draw(Canvas canvas, int look){
        float[][] data =  getCubeData();
        Paint textColor = new Paint();
        textColor.setColor(Color.BLACK);

        canvas.drawText("1",data[v1Point][X]-10,data[v1Point][Y]+10,textColor);
        canvas.drawText("2",data[v2Point][X],data[v2Point][Y]+10,textColor);
        canvas.drawText("3",data[v3Point][X]-10,data[v3Point][Y]+10,textColor);
        canvas.drawText("4",data[v4Point][X],data[v4Point][Y]+10,textColor);
        canvas.drawText("5",data[v5Point][X]-10,data[v5Point][Y],textColor);
        canvas.drawText("6",data[v6Point][X],data[v6Point][Y],textColor);
        canvas.drawText("7",data[v7Point][X]-10,data[v7Point][Y]+10,textColor);
        canvas.drawText("8", data[v8Point][X], data[v8Point][Y] + 10, textColor);


        switch (look) {
            case WIREFRAME:{
                canvas.drawLine(data[v1Point][X], data[v1Point][Y], data[v2Point][X], data[v2Point][Y], p);
                canvas.drawLine(data[v1Point][X], data[v1Point][Y], data[v3Point][X], data[v3Point][Y], p);
                canvas.drawLine(data[v3Point][X], data[v3Point][Y], data[v4Point][X], data[v4Point][Y], p);
                canvas.drawLine(data[v2Point][X], data[v2Point][Y], data[v4Point][X], data[v4Point][Y], p);
                canvas.drawLine(data[v3Point][X], data[v3Point][Y], data[v7Point][X], data[v7Point][Y], p);
                canvas.drawLine(data[v4Point][X], data[v4Point][Y], data[v8Point][X], data[v8Point][Y], p);
                canvas.drawLine(data[v7Point][X], data[v7Point][Y], data[v8Point][X], data[v8Point][Y], p);
                canvas.drawLine(data[v5Point][X], data[v5Point][Y], data[v6Point][X], data[v6Point][Y], p);
                canvas.drawLine(data[v1Point][X], data[v1Point][Y], data[v5Point][X], data[v5Point][Y], p);
                canvas.drawLine(data[v2Point][X], data[v2Point][Y], data[v6Point][X], data[v6Point][Y], p);
                canvas.drawLine(data[v6Point][X], data[v6Point][Y], data[v8Point][X], data[v8Point][Y], p);
                canvas.drawLine(data[v5Point][X], data[v5Point][Y], data[v7Point][X], data[v7Point][Y], p);

                break;
            }
            case SURFACED:{
                Rectangle r1,r2,r3,r4,r5,r6;

                r1 = new Rectangle(
                        data[v1Point][X], data[v1Point][Y],
                        data[v3Point][X], data[v3Point][Y],
                        data[v2Point][X], data[v2Point][Y],
                        data[v4Point][X], data[v4Point][Y],p);
                r2 = new Rectangle(
                        data[v1Point][X], data[v1Point][Y],
                        data[v3Point][X], data[v3Point][Y],
                        data[v5Point][X], data[v5Point][Y],
                        data[v7Point][X], data[v7Point][Y],p);
                r3 = new Rectangle(
                        data[v5Point][X], data[v5Point][Y],
                        data[v7Point][X], data[v7Point][Y],
                        data[v6Point][X], data[v6Point][Y],
                        data[v8Point][X], data[v8Point][Y],p);
                r4 = new Rectangle(
                        data[v2Point][X], data[v2Point][Y],
                        data[v4Point][X], data[v4Point][Y],
                        data[v6Point][X], data[v6Point][Y],
                        data[v8Point][X], data[v8Point][Y],p);
                r5 = new Rectangle(
                        data[v5Point][X], data[v5Point][Y],
                        data[v1Point][X], data[v1Point][Y],
                        data[v6Point][X], data[v6Point][Y],
                        data[v2Point][X], data[v2Point][Y],p);
                r6 = new Rectangle(
                        data[v7Point][X], data[v7Point][Y],
                        data[v3Point][X], data[v3Point][Y],
                        data[v8Point][X], data[v8Point][Y],
                        data[v4Point][X], data[v4Point][Y],p);

                Rectangle[] rects = {r1,r2,r3,r4,r5,r6};

                for(int i = 0; i < rects.length;i++){
                    rects[i].draw(canvas);
                }

            }
            case RENDERED:{

            }
        }


    }

    @Override
    public void makeWireframe(Canvas canvas,int look) {
        //canvas.drawColor(Color.WHITE);
        draw(canvas,look);
    }

    @Override
    public void makeSurface(Canvas canvas,int look) {
        //canvas.drawColor(Color.WHITE);
        draw(canvas,look);
    }

    @Override
    public void makeRendered(Canvas canvas,int look) {
        //canvas.drawColor(Color.WHITE);
        draw(canvas,look);
    }

    @Override
    public void onRotate(Canvas canvas,float angle, int mode, int direction, int axis) {

        int directionSelector;

        switch (direction){
            case StarMapSurface.DIRECTION_LEFT:
            case StarMapSurface.DIRECTION_UP:{
                directionSelector = -1;
                break;
            }
            case StarMapSurface.DIRECTION_RIGHT:
            case StarMapSurface.DIRECTION_DOWN:{
                directionSelector = 1;
                break;
            }
            default:
                directionSelector = 1;
                break;
        }

        for(int i = 0;i < points.length; i++) {
            MathHandler.getTranslationMatrix().rotateVector3D(points[i], angle, axis);
        }
    }

    public class Rectangle{
        float x1,x2,x3,x4,y1,y2,y3,y4;
        Paint myPaint;
        /////////////////v1 x       v1 y        v2 x    v2 y        v3 x    v3 y        v4 x        v4y
        public Rectangle(float topleftX, float topleftY,
                         float bottomleftX, float bottomleftY,
                         float toprightX, float toprightY,
                         float bottomrightX, float bottomrightY,
                         Paint p){
            this.x1 = topleftX;
            this.x2 = bottomleftX;
            this.x3 = toprightX;
            this.x4 = bottomrightX;
            this.y1 = topleftY;
            this.y2 = bottomleftY;
            this.y3 = toprightY;
            this.y4 = bottomrightY;

            myPaint = p;
        }

        public void draw(Canvas canvas){
           for(float i = 0; i < x3;i++){
               canvas.drawLine(x1,y1,x2,y2,myPaint);

               //r1
               if(x1<x3)x1++;
               if(x2<x4)x2++;
               if(y1>y3)y1--;
               if(y2>y4)y2--;



           }

        }
    }
}
