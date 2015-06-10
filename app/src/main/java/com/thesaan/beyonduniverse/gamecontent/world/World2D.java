package com.thesaan.beyonduniverse.gamecontent.world;

import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * If the player enters a planet. Create a 2D world.
 */
public class World2D {

    private final String DEVIDER = "_";

    public final int WORLD_SIZE_SMALL = 1;
    public final int WORLD_SIZE_NORMAL = 2;
    public final int WORLD_SIZE_BIG = 3;

    public final int FIELD_TYPE_STONE_DEFAULT_1 = 10;
    public final int FIELD_TYPE_STONE_DEFAULT_2 = 11;
    public final int FIELD_TYPE_STONE_DEFAULT_3 = 12;
    public final int FIELD_TYPE_GRASS_FLOOR_1 = 20;
    public final int FIELD_TYPE_GRASS_FLOOR_2 = 21;

    private final int[] fieldTypeIDs = {
            FIELD_TYPE_STONE_DEFAULT_1,//!
            FIELD_TYPE_STONE_DEFAULT_2,//!!
            FIELD_TYPE_STONE_DEFAULT_3,//!!!
            FIELD_TYPE_GRASS_FLOOR_1,//*
            FIELD_TYPE_GRASS_FLOOR_2//**
    };

    private int worldSize;
    private int progress_load_world = 0;

    private final String[] fieldTypes = {
            "!","!!","!!!","*","**"
    };

    private int[] world;



    private  void generateWorld(int size){
        worldSize = 100000*size;

        String[] worldCode = new String[worldSize];
        Random r = new Random();

        //build world map code
        for(int i = 0;i < worldCode.length;i++){
            worldCode[i] = RandomHandler.createIntegerFromRange(1,150,r)+DEVIDER+fieldTypes[RandomHandler.createIntegerFromRange(0,fieldTypes.length,r)];
            progress_load_world = i;
        }
         int worldWidth = worldSize/100;
         int worldHeight = worldSize/1000;
        //build matrix with different lengths

        for(int i = 0; i<worldWidth;i++){

        }

    }
    private int getFieldType(String field){

        for(int i = 0;i < fieldTypeIDs.length;i++){
            String[] str = field.split(DEVIDER);
            String type = str[1];

            //for
        }
        return 0;
    }
    private int getFieldAmount(String field){
            String[] str = field.split(DEVIDER);
            return Integer.parseInt(str[0]);
    }
    public int getWorldSize() {
        return worldSize;
    }

    public int getWorldLoadingProgress(){
        return progress_load_world;
    }
}
