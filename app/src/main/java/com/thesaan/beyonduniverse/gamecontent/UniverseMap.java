package com.thesaan.beyonduniverse.gamecontent;

import com.thesaan.gameengine.android.database.UniverseDatabase;
import com.thesaan.gameengine.android.ui.GameSurface;

/**
 * Created by mknoe on 04.05.2015.
 */
public class UniverseMap {

    GameSurface gameSurface;

    StarMapBuilder mapBuilder;


    /**
     * Quiet redundant at the moment
     * @param db
     */
    public UniverseMap(UniverseDatabase db){
        mapBuilder = new StarMapBuilder(db);

    }

    public StarMapBuilder getMapBuilder() {
        return mapBuilder;
    }
}
