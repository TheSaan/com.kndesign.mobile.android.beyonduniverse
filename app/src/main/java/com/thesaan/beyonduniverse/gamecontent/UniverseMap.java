package com.thesaan.beyonduniverse.gamecontent;

import com.thesaan.gameengine.android.database.AppDatabase;
import com.thesaan.gameengine.android.ui.StarMapSurface;

/**
 * Created by mknoe on 04.05.2015.
 */
public class UniverseMap {

    StarMapSurface gameSurface;

    StarMapBuilder mapBuilder;


    /**
     * Quiet redundant at the moment
     * @param db
     */
    public UniverseMap(AppDatabase db){
        mapBuilder = new StarMapBuilder(db);

    }

    public StarMapBuilder getMapBuilder() {
        return mapBuilder;
    }
}
