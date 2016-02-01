package com.thesaan.gameengine.android.jpct_extend;

import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import java.util.Enumeration;

/**
 * Created by Michael on 28.01.2016.
 */
public class BUWorld extends World {

    private int fps;

    /**
     * Sets to each object the world the fps parameter
     * @param fps
     */
    public void setFps(int fps) {
        this.fps = fps;

        BUObject3D[] objects = getObjectsArray();

        for(BUObject3D obj:objects){
            obj.setFps(fps);
        }
    }

    /**
     * Get a normal Array of all Objects in the World instead of
     * an Enumeration
     * @return
     * Array of all Objects in this world
     */
    public BUObject3D[] getObjectsArray(){
        Enumeration<Object3D> objects = getObjects();

        BUObject3D[] buObject3Ds = new BUObject3D[getSize()-1];

        for(BUObject3D obj:buObject3Ds){
            obj = new BUObject3D(objects.nextElement());
        }

        return buObject3Ds;
    }
}
