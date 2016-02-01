package com.thesaan.beyonduniverse.gamecontent.world.models;

import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.threed.jpct.Primitives;

/**
 * Created by Michael on 30.01.2016.
 */
public class ModelGalaxy extends BUObject3D {
    public ModelGalaxy(){
        super(Primitives.getPyramide(10,4f));
    }
}
