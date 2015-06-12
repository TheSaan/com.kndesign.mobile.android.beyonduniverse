package com.thesaan.gameengine.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thesaan.beyonduniverse.gamecontent.Race;
import com.thesaan.beyonduniverse.gamecontent.economy.Item;
import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.City;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Moon;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Planet;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.SolarSystem;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Star;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Handles all content from a universe game.
 */
public class UniverseDatabase extends Database implements DB_Settings{

    public UniverseDatabase(Context context) {
        super(context, DATABASE_NAME_UNIVERSE_GAME);
        this.db = this.getWritableDatabase();
        createTables(db,TABLES_TO_CREATE);

        printTestData();
    }

    public void onCreate(SQLiteDatabase db) {

    }
    /**
     *
     * @param db
     * @param creationStrings
     * An array of the strings to create all tables.
     */
    private void createTables(SQLiteDatabase db, String[] creationStrings){
        for(int i = 0; i < creationStrings.length;i++){
            try {
                db.execSQL(creationStrings[i]);
            }catch (Exception e) {
//                Log.e("Error message", "Table already exists.");
            }
        }
    }
    /*----------------------------------------get all of an object-----------------------------------*/
    public Cursor getCities(){
        return db.query(DATABASE_TABLE_CITIES,null,null,null,null,null,null);
    }
    public Cursor getPlanets(){
        return db.query(DATABASE_TABLE_PLANETS,null,null,null,null,null,null);
    }
    public Cursor getGalaxies(){
        return db.query(DATABASE_TABLE_GALAXIES,null,null,null,null,null,null);
    }
    public Cursor getMoons(){
        return db.query(DATABASE_TABLE_MOONS,null,null,null,null,null,null);
    }
    public Cursor getStars(){
        return db.query(DATABASE_TABLE_STARS,null,null,null,null,null,null);
    }
    public Cursor getSolarSystems(){
        return db.query(DATABASE_TABLE_SOLARSYSTEMS,null,null,null,null,null,null);
    }
    /*----------------------------------------get one object-----------------------------------*/
    public Cursor getGalaxy(int id){
        return db.query(DATABASE_TABLE_GALAXIES,null,COL_ID+"="+id,null,null,null,null);
    }
    public Cursor getSolarSystem(int id){

        return db.query(DATABASE_TABLE_SOLARSYSTEMS, null, COL_ID + "=" + id, null, null, null, null);
    }
    public Cursor getStar(int id){

        return db.query(DATABASE_TABLE_STARS, null, COL_ID + "=" + id, null, null, null, null);
    }
    public Cursor getPlanet(int id){
        return db.query(DATABASE_TABLE_PLANETS, null, COL_ID + "=" + id, null, null, null, null);
    }
    public Cursor getMoon(int id){
        return db.query(DATABASE_TABLE_MOONS, null, COL_ID + "=" + id, null, null, null, null);
    }
    public Market getMarket(String parent,int planetType){
        /*TODO Hier muss ich wieder dekommentieren wenn ich dann die Märkte in der DB habe
        Cursor c = db.query(DATABASE_TABLE_MARKETS, null, COL_PARENT +QM, new String[]{parent}, null, null, null);
        c.moveToFirst();*/

        return new Market(new Race(RandomHandler.createIntegerFromRange(Race.RACE_HUMAN,Race.RACE_CROX,new Random())),parent,planetType);
    }
    /*----------------------------------------get counts-----------------------------------*/
    public int getNumberOfCities(){
        return db.query(DATABASE_TABLE_CITIES,null,null,null,null,null,null).getCount();
    }
    public int getNumberOfPlanets(){
        return db.query(DATABASE_TABLE_PLANETS,null,null,null,null,null,null).getCount();
    }
    public int getNumberOfGalaxies(){
        return db.query(DATABASE_TABLE_GALAXIES,null,null,null,null,null,null).getCount();
    }
    public int getNumberOfMoons(){
        return db.query(DATABASE_TABLE_MOONS,null,null,null,null,null,null).getCount();
    }
    public int getNumberOfStars(){
        return db.query(DATABASE_TABLE_STARS,null,null,null,null,null,null).getCount();
    }
    public int getNumberOfSolarSystems(){
        return db.query(DATABASE_TABLE_SOLARSYSTEMS,null,null,null,null,null,null).getCount();
    }
    /*----------------------------------------get children-----------------------------------*/
    public Moon[] getMoonsOfPlanet(String name) {

            Cursor c = db.query(DATABASE_TABLE_PLANETS, new String[]{COL_MOONS}, COL_NAME + QM, new String[]{name}, null, null, null);
            c.moveToFirst();
            String list = c.getString(c.getColumnIndex(COL_MOONS));

            String[] strIds = list.split(";");
            String selection = COL_ID + QM;

            //add as many as needed "OR" expressions to the selection argument
            if (strIds.length > 1) {
                for (int i = 0; i < strIds.length; i++) {
                    selection += " OR " + COL_ID + QM;
                }
            }
            c = db.query(DATABASE_TABLE_MOONS, null, selection, strIds, null, null, null);

            c.moveToFirst();

            Moon[] moons = new Moon[c.getCount()];

            for (int i = 0; i < c.getCount(); i++) {
                String name1 = c.getString(c.getColumnIndex(COL_NAME));

                Moon moon1 = new Moon(
                        name1,
                        getPositionOfObject(name1, DATABASE_TABLE_MOONS),
                        c.getFloat(c.getColumnIndex(COL_MASS)),
                        c.getFloat(c.getColumnIndex(COL_RADIUS)),
                        c.getFloat(c.getColumnIndex(COL_DEGREES)),
                        UniverseObjectProperties.OBJECT_MOON);

                moons[i] = moon1;
                c.moveToNext();
            }
            c.close();
            return moons;

    }
    public City[] getCitiesOfPlanet(String name){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_CITIES,COL_PlANET_TYPE},COL_NAME+QM,new String[]{name},null,null,null);
        c.moveToFirst();
        int planetType = c.getInt(c.getColumnIndex(COL_PlANET_TYPE));
        String list = c.getString(c.getColumnIndex(COL_CITIES));

        String[] strIds = list.split(";");
        String selection = COL_ID+QM;

        //add as many as needed "OR" expressions to the selection argument
        if(strIds.length > 1) {
            for (int i = 0; i < strIds.length; i++) {
                selection += " OR " + COL_ID + QM;
            }
        }
        c = db.query(DATABASE_TABLE_CITIES,null,selection,strIds,null,null,null);
        c.moveToFirst();
        City[] cities = new City[c.getCount()];

        for(int i= 0; i < c.getCount();i++){

            City city1 = new City(
                    c.getString(c.getColumnIndex(COL_NAME)),
                    c.getLong(c.getColumnIndex(COL_POPULATION)),
                    getMarket(c.getString(c.getColumnIndex(COL_NAME)),planetType),
                    UniverseObjectProperties.OBJECT_CITY);
            cities[i] = city1;
            c.moveToNext();
        }
        c.close();
        return cities;
    }
    public Planet[] getPlanetsOfSolarSystem(String name){
        Cursor c = db.query(DATABASE_TABLE_SOLARSYSTEMS,new String[]{COL_PLANETS},COL_NAME+QM,new String[]{name},null,null,null);
        c.moveToFirst();
        String list = c.getString(c.getColumnIndex(COL_PLANETS));

        String[] strIds = list.split(";");

        String selection = COL_ID+QM;

        //add as many as needed "OR" expressions to the selection argument
        for(int i = 0; i< strIds.length;i++){
            selection += " OR "+ COL_ID+QM;
        }
        c = db.query(DATABASE_TABLE_PLANETS,null,selection,strIds,null,null,null);
        c.moveToFirst();

        Planet[] planets = new Planet[c.getCount()];

        for(int i= 0; i < c.getCount();i++){
            String name1 = c.getString(c.getColumnIndex(COL_NAME));

            Planet planet1 = new Planet(name1,getPositionOfObject(name1,DATABASE_TABLE_PLANETS),
                    c.getFloat(c.getColumnIndex(COL_MASS)),
                    c.getFloat(c.getColumnIndex(COL_RADIUS)),
                    getMoonsOfPlanet(name1),
                    getCitiesOfPlanet(name1),
                    c.getFloat(c.getColumnIndex(COL_DEGREES)),
                    UniverseObjectProperties.OBJECT_PLANET,
                    c.getInt(c.getColumnIndex(COL_PlANET_TYPE)));
            planets[i] = planet1;
            c.moveToNext();
        }
        c.close();
        return planets;
    }
    public SolarSystem[] getSolarSystemsOfGalaxy(String name){
        Cursor c = db.query(DATABASE_TABLE_GALAXIES,new String[]{COL_SOLARSYSTEMS},COL_NAME+QM,new String[]{name},null,null,null);
        c.moveToFirst();
        String list = c.getString(c.getColumnIndex(COL_SOLARSYSTEMS));

        String[] strIds = list.split(";");

        String selection = COL_ID+QM;

        //add as many as needed "OR" expressions to the selection argument
        for(int i = 0; i< strIds.length;i++){
            selection += " OR "+ COL_ID+QM;
        }

        c = db.query(DATABASE_TABLE_SOLARSYSTEMS,null,selection,strIds,null,null,null);
        c.moveToFirst();
        SolarSystem[] solarSystems = new SolarSystem[c.getCount()];
        for(int i= 0; i < c.getCount();i++) {
            String name1 = c.getString(c.getColumnIndex(COL_NAME));

            SolarSystem solarSystem1 = new SolarSystem(
                    name1,
                    c.getFloat(c.getColumnIndex(COL_VOLUME)),
                    getPositionOfObject(name1, DATABASE_TABLE_SOLARSYSTEMS),
                    getStarsOfSolarSystem(name1),
                    getPlanetsOfSolarSystem(name1),
                    //TODO here i take the half of the 3rd part of the volume because in database i dont save the solar system radius
                    (c.getFloat(c.getColumnIndex(COL_VOLUME))/3f)/2f,
                    UniverseObjectProperties.OBJECT_SOLARSYSTEM);

            solarSystems[i] = solarSystem1;
            c.moveToNext();
        }
        c.close();
        return solarSystems;
    }
    public Star[] getStarsOfSolarSystem(String name){
        Cursor c = db.query(DATABASE_TABLE_SOLARSYSTEMS,new String[]{COL_STARS},COL_NAME+QM,new String[]{name},null,null,null);
        c.moveToFirst();
        String list = c.getString(c.getColumnIndex(COL_STARS));

        String[] strIds = list.split(";");

        String selection = COL_ID+QM;

        //add as many as needed "OR" expressions to the selection argument
        for(int i = 0; i< strIds.length;i++){
            selection += " OR "+ COL_ID+QM;
        }
        c = db.query(DATABASE_TABLE_STARS,null,selection,strIds,null,null,null);
        c.moveToFirst();
        Star[] stars = new Star[c.getCount()];

        for(int i= 0; i < c.getCount();i++){

            String name1 = c.getString(c.getColumnIndex(COL_NAME));

            Star star1 = new Star(
                    name1,
                    getPositionOfObject(name1,DATABASE_TABLE_STARS),
                    c.getFloat(c.getColumnIndex(COL_MASS)),
                    c.getFloat(c.getColumnIndex(COL_RADIUS)),
                    c.getFloat(c.getColumnIndex(COL_DEGREES)),
                    UniverseObjectProperties.OBJECT_STAR
                    );

            stars[i] = star1;
            c.moveToNext();
        }
        c.close();
        return stars;
    }
    /*----------------------------------------get properties-----------------------------------*/
    public float getDegreesOfStar(int id){
        Cursor c = db.query(DATABASE_TABLE_STARS,new String[]{COL_DEGREES},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_DEGREES));
    }
    public float getDegreesOfPlanet(int id){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_DEGREES},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_DEGREES));
    }
    public float getRadiusOfPlanet(int id){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_RADIUS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_RADIUS));
    }
    public float getRadiusOfStar(int id){
        Cursor c = db.query(DATABASE_TABLE_STARS,new String[]{COL_RADIUS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_RADIUS));
    }
    public float getRadiusOfMoon(int id){
        Cursor c = db.query(DATABASE_TABLE_MOONS,new String[]{COL_RADIUS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_RADIUS));
    }
    public float getMassOfPlanet(int id){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_MASS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_MASS));
    }
    public float getMassOfStar(int id){
        Cursor c = db.query(DATABASE_TABLE_STARS,new String[]{COL_MASS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_MASS));
    }
    public float getMassOfMoon(int id){
        Cursor c = db.query(DATABASE_TABLE_MOONS,new String[]{COL_MASS},COL_ID+"="+id,null,null,null,null);
        return c.getFloat(c.getColumnIndex(COL_MASS));
    }
    public int getTypeOfPlanet(int id){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_PlANET_TYPE},COL_ID+"="+id,null,null, null, null);
        return c.getInt(c.getColumnIndex(COL_PlANET_TYPE));
    }
    public int[] getDrawableId(Race race, int productType){
        String[] args = {Integer.toString(race.getType()), Integer.toString(productType)};
        int[] ids = null;
        Cursor c = db.query(DATABASE_TABLE_ITEMS,new String[]{COL_DRAWABLE_ID},COL_RACE+QM+AND+COL_ITEM_TYPE+QM,args,null,null,null);

        if(c != null && c.getCount() > 0){
            for(int i = 0;i < c.getCount();i++) {
                c.moveToFirst();
                ids = new int[c.getCount()];

                int id = c.getInt(c.getColumnIndex(COL_DRAWABLE_ID));
                ids[i] = id;

                c.moveToNext();
            }
            return ids;
        }
        return null;
    }
    public int getMaxAmountWhereDrawable(int drawableId){
        Cursor c = db.query(DATABASE_TABLE_ITEMS,new String[]{COL_MAX_AMOUNT},COL_DRAWABLE_ID+"="+drawableId,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_MAX_AMOUNT));
    }
    public String getNameWhereDrawable(int drawableId){
        Cursor c = db.query(DATABASE_TABLE_ITEMS,new String[]{COL_NAME},COL_DRAWABLE_ID+"="+drawableId,null,null, null, null);
        return c.getString(c.getColumnIndex(COL_NAME));
    }

    public int getBitmapArrayIndexOfGalaxyBitmap(String galaxyName){
        Cursor c  = db.query(DATABASE_TABLE_GALAXIES,new String[]{COL_ID},COL_NAME+"="+galaxyName,null,null,null,null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(COL_NAME))-1;
    }

    public MathHandler.Vector getPositionOfObject(String name, String table){
        try {
            Cursor c = db.query(table, new String[]{COL_POS_X, COL_POS_Y, COL_POS_Z}, COL_NAME + QM, new String[]{name}, null, null, null);
            c.moveToFirst();
            MathHandler.Vector pos = new MathHandler.Vector(c.getFloat(c.getColumnIndex(COL_POS_X)), c.getFloat(c.getColumnIndex(COL_POS_Y)), c.getFloat(c.getColumnIndex(COL_POS_Z)));
            return pos;
        }catch (Exception e){
            System.err.println("getPosition Exception..."+e);
            return null;
        }
    }

    public String[] getNamesOfGalaxies(){
        String[] names = new String[getNumberOfGalaxies()];
        Cursor c  = db.query(DATABASE_TABLE_GALAXIES,new String[]{COL_NAME},null,null,null,null,null);
        c.moveToFirst();
        for(int i = 0; i< names.length;i++){
            names[i] = c.getString(c.getColumnIndex(COL_NAME));
//            System.out.println("Galaxy "+(i+1)+": "+names[i]);
            c.moveToNext();
        }

        return names;
    }
    /*----------------------------------------get parents-----------------------------------*/
    public Cursor getCityParent(int id){
        return db.query(DATABASE_TABLE_CITIES,new String[]{COL_PARENT},COL_ID+"="+id,null,null,null,null);
    }
    public Cursor getMoonParent(int id){
        return db.query(DATABASE_TABLE_MOONS,new String[]{COL_PARENT},COL_ID+"="+id,null,null,null,null);
    }
    public Cursor getPlanetParent(int id){
        return db.query(DATABASE_TABLE_PLANETS,new String[]{COL_PARENT},COL_ID+"="+id,null,null,null,null);
    }
    public Cursor getStarParent(int id){
        return db.query(DATABASE_TABLE_STARS,new String[]{COL_PARENT},COL_ID+"="+id,null,null,null,null);
    }
    public Cursor getSolarSystemParent(int id){
        return db.query(DATABASE_TABLE_SOLARSYSTEMS,new String[]{COL_PARENT},COL_ID+"="+id,null,null,null,null);
    }
    /*----------------------------------------get last entry index-----------------------------------*/
    public int getLastGalaxyIndex(){
        return db.query(DATABASE_TABLE_GALAXIES,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastSolarSystemIndex(){
        return db.query(DATABASE_TABLE_SOLARSYSTEMS,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastStarIndex(){
        return db.query(DATABASE_TABLE_STARS,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastPlanetIndex(){
        return db.query(DATABASE_TABLE_PLANETS,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastMoonIndex(){
        return db.query(DATABASE_TABLE_MOONS,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastCityIndex(){
        return db.query(DATABASE_TABLE_CITIES,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    public int getLastMarketIndex(){
        return db.query(DATABASE_TABLE_MARKETS,new String[]{COL_ID},null,null,null,null,null).getCount();
    }
    /*----------------------------------------get id from name-----------------------------------*/
    public int getCityIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_CITIES,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }
    public int getPlanetIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_PLANETS,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }
    public int getMoonCityIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_MOONS,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }
    public int getStarIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_STARS,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }
    public int getSolarsystemIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_SOLARSYSTEMS,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }
    public int getGalaxyIdFromName(String name){
        Cursor c = db.query(DATABASE_TABLE_GALAXIES,new String[]{COL_ID},COL_NAME+"="+name,null,null,null,null);
        return c.getInt(c.getColumnIndex(COL_ID));
    }

    /*----------------------------------------get Object from -----------------------------------*/
    public Item[] createItems(Race race, int planetType, boolean isIllegal){
        int legality;
        if(isIllegal)
            legality = 1;
        else
            legality = 0;

        String selection =
                COL_RACE+QM+AND
                +COL_PlANET_TYPE+QM+AND
                +COL_IS_ILLEGAL+QM;
        String[] args = {
                Integer.toString(race.getType()),
                Integer.toString(planetType),
                Integer.toString(legality)
        };
        Cursor c = db.query(DATABASE_TABLE_ITEMS,null,selection,args,null,null,null);

        if(c != null){
            if(c.getCount()>0){
                Item[] items = new Item[c.getCount()];
                c.moveToFirst();
                for(int i = 0; i < c.getCount();i++) {
                    Item item = new Item(context, race, c.getString(c.getColumnIndex(COL_NAME)), c.getInt(c.getColumnIndex(COL_ITEM_TYPE)));
                    items[i] = item;
                    c.moveToNext();
                }
                return items;
            }
        }

        return null;
    }

    /*----------------------------------------column adderr-----------------------------------*/
    public void addItemColumnToMarket(String table,String newColumn, String afterColumn){

        db.execSQL("ALTER TABLE "+table+" ADD COLUMN "+newColumn+" <INT>  AFTER "+afterColumn);
    }

    /*----------------------------------------world generator-----------------------------------*/



    public void addWorld(Galaxy[] galaxies){
        for(int i = 0; i < galaxies.length;i++){
            addGalaxy(galaxies[i]);
        }
    }

    public void addGalaxy(Galaxy g){
        ContentValues cv = new ContentValues();

        int startSolarSystemIndex = getLastSolarSystemIndex()+1;

        SolarSystem[] systems = g.getSolarsystems();

        for(int i = 0;i < systems.length;i++) {
            addSolarSystem(systems[i],g.getName());
        }

        cv.put(COL_NAME,g.getName());
        cv.put(COL_RADIUS, g.getRadius());
        cv.put(COL_VOLUME, g.getVolume());
        cv.put(COL_SOLARSYSTEMS,createChildrenChain(startSolarSystemIndex,getLastSolarSystemIndex()));
        cv.put(COL_POS_X, g.getX());
        cv.put(COL_POS_Y, g.getY());
        cv.put(COL_POS_Z, g.getZ());
        db.insert(DATABASE_TABLE_GALAXIES, null, cv);
    }
    public void addSolarSystem(SolarSystem s, String parent){
        ContentValues cv = new ContentValues();

        int planetStartIndex = getLastPlanetIndex()+1;
        int starStartIndex = getLastStarIndex()+1;

        Star[] stars = s.getStars();
        for(int i = 0;i < stars.length;i++) {
            addStar(stars[i],s.getName());
        }
        Planet[] planets = s.getPlanets();
        for(int i = 0;i < planets.length;i++) {
            addPlanet(planets[i], s.getName());
        }

        cv.put(COL_NAME,s.getName());
        cv.put(COL_PARENT,parent);
        cv.put(COL_PLANETS,createChildrenChain(planetStartIndex, getLastPlanetIndex()));
        cv.put(COL_STARS,createChildrenChain(starStartIndex, getLastStarIndex()));
        cv.put(COL_VOLUME,s.getVolume());
        cv.put(COL_POS_X,s.getX());
        cv.put(COL_POS_Y,s.getY());
        cv.put(COL_POS_Z,s.getZ());

        db.insert(DATABASE_TABLE_SOLARSYSTEMS, null, cv);
    }
    public void addStar(Star s, String parent){
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME,s.getName());
        cv.put(COL_PARENT,parent);
        cv.put(COL_RADIUS,s.getRadius());
        cv.put(COL_DEGREES,s.getDegrees());
        cv.put(COL_MASS,s.getMass());
        cv.put(COL_POS_X,s.getX());
        cv.put(COL_POS_Y,s.getY());
        cv.put(COL_POS_Z,s.getZ());

        db.insert(DATABASE_TABLE_STARS, null, cv);
    }
    public void addPlanet(Planet p, String parent){
        ContentValues cv = new ContentValues();

        int moonStartIndex = getLastMoonIndex()+1;
        int cityStartIndex = getLastCityIndex()+1;

        Moon[] moons = p.getMoons();

        for(int i = 0;i < moons.length;i++) {
            addMoon(moons[i],p.getName());
        }

        City[] cities = p.getCities();

        if(cities != null) {
            for (int i = 0; i < cities.length; i++) {
                addCity(cities[i],p.getName());
            }

            cv.put(COL_NAME, p.getName());
            cv.put(COL_PARENT,parent);
            cv.put(COL_RADIUS, p.getRadius());
            cv.put(COL_PlANET_TYPE, p.getPlanetType());
            cv.put(COL_DEGREES, p.getDegrees());
            cv.put(COL_POPULATION, p.getPopulation());
            cv.put(COL_MOONS, createChildrenChain(moonStartIndex, getLastMoonIndex()));
            cv.put(COL_CITIES, createChildrenChain(cityStartIndex, getLastCityIndex()));
            cv.put(COL_MASS, p.getMass());
            cv.put(COL_POS_X, p.getX());
            cv.put(COL_POS_Y, p.getY());
            cv.put(COL_POS_Z, p.getZ());
        db.insert(DATABASE_TABLE_PLANETS, null, cv);
        }
    }
    public void addMoon(Moon m, String parent){
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME,m.getName());
        cv.put(COL_PARENT,parent);
        cv.put(COL_RADIUS,m.getRadius());
        cv.put(COL_MASS,m.getMass());
        cv.put(COL_DEGREES,m.getDegrees());
        cv.put(COL_POS_X,m.getX());
        cv.put(COL_POS_Y,m.getY());
        cv.put(COL_POS_Z,m.getZ());

        db.insert(DATABASE_TABLE_MOONS, null, cv);

    }
    public void addCity(City c, String parent){
        ContentValues cv = new ContentValues();

        //insert 1 market for each city
        int marketStartIndex = getLastMarketIndex() + 1;

        //TODO save market
        addMarket(c);

        cv.put(COL_NAME,c.getName());
        cv.put(COL_PARENT,parent);
        cv.put(COL_POPULATION, c.getPopulation());
        cv.put(COL_MARKET, marketStartIndex + ";");
        db.insert(DATABASE_TABLE_CITIES, null, cv);
    }

    public void addMarket(City c){
        ContentValues cv = new ContentValues();


        //TODO Market integrieren
        cv.put(COL_PARENT, c.getName());
//        db.insert(DATABASE_TABLE_MARKETS, null, cv);
    }
    private String createChildrenChain(int start, int end){
        String s = "";
        for( int i = start; i < end;i++){
            s += i+";";
        }
        return s;
    }

    private void setupDrawableIdsToDatabase(){
        String Table = "ITEM";
    }

    private void printTestData(){
        for(int i = 1; i<= getSolarSystems().getCount(); i++) {
            Cursor s = getSolarSystem(i);
            s.moveToFirst();
            System.out.println(s.getString(s.getColumnIndex(COL_NAME)));
        }
    }
}




















































