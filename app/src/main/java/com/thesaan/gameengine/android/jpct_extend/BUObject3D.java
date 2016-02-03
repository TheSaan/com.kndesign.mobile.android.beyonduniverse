package com.thesaan.gameengine.android.jpct_extend;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.threed.jpct.Object3D;
import com.threed.jpct.Polyline;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

/**
 * Created by Michael on 28.01.2016.
 */
public class BUObject3D extends Object3D {

    SimpleVector preScalePosition;

    //sets the own list position in level
    private int levelIndex;

    private World world;

    private int fps = 60;

    private int modelType;

    public static final int STATIC = 1;
    public static final int NON_STATIC = 2;
    public static final int NPC = 3;
    public static final int PLAYER = 4;
    public static final int OTHER = 5;
    public static final int ALL = 6;

    private boolean isNPC;
    private boolean isPlayer;
    private boolean isStaticObject;
    private boolean isNonStaticObject;
    private boolean isOther;

    public BUObject3D(Object3D obj,int type) {
        super(obj);
        setModelType(type);
    }

    public void setPreScalePosition(SimpleVector preScalePosition) {
        this.preScalePosition = preScalePosition;
    }

    public SimpleVector getPreScalePosition() {
        return preScalePosition;
    }

    public void resetScaleTranslation() {
        translate(preScalePosition);
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getModelType(){
        return modelType;
    }
    public void setModelType(int type){
        switch (type){
            case NON_STATIC:
                isNonStaticObject = true;
                break;
            case STATIC:
                isStaticObject = true;
                break;
            case NPC:
                isNPC = true;
                break;
            case PLAYER:
                isPlayer = true;
                break;
            default:
                isOther = true;
                break;
        }
    }
    public void onScalePosition(boolean scaleUp, float frameStep) {


        float x, y, z = frameStep;
        y = x = z;

        if (scaleUp) {
            //if a value is negative make the translation positive
            if (x < 0) x *= -1;
            if (y < 0) y *= -1;
            if (z < 0) z *= -1;
        } else {
            if (x > 0) x *= -1;
            if (y > 0) y *= -1;
            if (z > 0) z *= -1;
        }
        translate(x, y, z);
    }

    /**
     * @param world
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * When the Object is too far away from the
     * camera, it will be drawn as point to
     * reduce memory
     */
    public void setDrawMode(boolean isNear) {
        Polyline point = null;
        if (world != null) {
            if (isNear) {
                if (point != null) {
                    //draw the Model again
                    world.removePolyline(point);
                    world.addObject(this);
                }
            } else {
                //remove the actual model
                world.removeObject(this);

                //create a Point of the own center via a PolyLine
                //and add it to the world again
                point = new Polyline(
                        new SimpleVector[]{getTransformedCenter()}
                        , new RGBColor(1, 1, 1)
                );

                world.addPolyline(point);
            }
        }
    }


}
