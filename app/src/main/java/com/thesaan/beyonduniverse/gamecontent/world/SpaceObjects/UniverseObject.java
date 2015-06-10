package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.widget.ImageView;

import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.Drawables;
import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.database.UniverseDatabase;
import com.thesaan.gameengine.android.drawables.CoordinateSystem3D;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;
import com.thesaan.gameengine.android.ui.GameSurface;

import java.util.Random;

/**
 * Defines a universal object for extending galaxies, solar systems, stars, planets, moons or cities
 */
public class UniverseObject implements UniverseObjectProperties, ObjectProperties {

    /**
     * The actual image resource
     */
    Bitmap mBitmap;
    /**
     * To choose the correct star bitmap
     */
    Paint starPaint;
    public MathHandler.Vector myPosition;

    ImageView mImageView;

    SolarSystem[] solarSystems;
    Star[] stars;
    Planet[] planets;
    Moon[] moons;
    City[] cities;
    //all on the planet
    Market[] markets;
    //for each city
    Market market;


    public long population;
    public float radius;
    public float scope;
    public float mass;
    public float degrees;
    public float volume;
    public String name;
    public int type;
    public int planetType;
    public String parent;

    UniverseDatabase uDb;


    /*----------------------------------------CONSTRUCTORS-----------------------------------*/
    public UniverseObject(){

    }

    /**
     * To create a city
     */
    public UniverseObject(String name, long population, Market market,int type){

        this.type = type;
        this.market = market;

        //name set
        if(name != "")
            this.name = name;
        else
            setRandomName(type);
        this.population = population;
        this.markets = markets;

    }


    /**
     * To create asteroids, comets.
     * @param name
     *  If the name is empty, an unique random name will be set to this object.
     *  Please note, that only objects should have explicit names which are inside the story.
     * @param position
     *  The universal position of the object.
     * @param mass
     * @param radius
     * @param degrees
     * @param type
     */
    public UniverseObject(String name, MathHandler.Vector position, float mass, float radius,float degrees, int type){
        //set the stars color
        starPaint = new Paint();

        this.type = type;

        if(isStar())
            setRandomColor();

        //name set
        if(name != "")
            this.name = name;
        else
            setRandomName(type);

        myPosition = position;

        this.degrees = degrees;
        this.mass = mass;
        this.radius = radius;
        scope = 2f*radius*(float) Math.PI;

    }

    /**
     * This one is only for creating solar systems.
     * @param name
     * @param position
     * @param radius
     * @param type
     */
    public UniverseObject(String name, MathHandler.Vector position, float radius, int type){


        //name set
        if(name != "")
            this.name = name;
        else
            setRandomName(type);
        if(type == OBJECT_SOLARSYSTEM)
            this.type = type;
        else
            System.err.println("Tried to create "+getTypeName(type)+" with Solarsystem constructor!");

        myPosition = position;

        this.radius = radius;

    }/**
     * This one is only for creating galaxies.
     * @param name
     * @param position
     * @param type
     */
    public UniverseObject(String name, MathHandler.Vector position, int type){

        //type set
        if(type == OBJECT_GALAXY)
            this.type = type;
        else
            System.err.println("Tried to create "+getTypeName(type)+" with Galaxy constructor!");

        //name set
        if(name != "")
            this.name = name;
        else
            setRandomName(type);

        //position set
        myPosition = position;

    }

    /*----------------------------------------SETTERS-----------------------------------*/

    public void setType(int type) {
        this.type = type;
    }

    /**
     * If the object is a star, choose its color for selecting the corrent bitmap then.
     */
    public void setRandomColor(){
        starPaint.setColor(getColor(RandomHandler.createIntegerFromRange(0, 3, new Random()) + 101010));
    }

    /**
     * Creates a random image out of the arrays filled with the ids of the object drawables.
     * @return
     */
    public int getRandomImageResource(){
        if(isStar()){
            return Drawables.drawablesStars[RandomHandler.createIntegerFromRange(0,Drawables.drawablesStars.length, new Random())];
        }else
        if(isPlanet()){
            return Drawables.drawablesPlanets[RandomHandler.createIntegerFromRange(0,Drawables.drawablesPlanets.length, new Random())];
        }else
        if(isMoon()){
            return Drawables.drawablesMoons[RandomHandler.createIntegerFromRange(0,Drawables.drawablesMoons.length, new Random())];
        }else
        if(isAsteroid()){
            return Drawables.drawablesAsteroids[RandomHandler.createIntegerFromRange(0,Drawables.drawablesAsteroids.length, new Random())];
        }else
        if(isComet()){
            return Drawables.drawablesComets[RandomHandler.createIntegerFromRange(0,Drawables.drawablesComets.length, new Random())];
        }else
        if(isGalaxy()){
            return Drawables.drawablesGalaxies[RandomHandler.createIntegerFromRange(0,Drawables.drawablesGalaxies.length, new Random())];
        }else{
            return 0;
        }
    }

    /**
     * If the planet is habitabel create either a grass, - water or jungle planet. the Rest kinds are selected by the
     * temperature
     * @return
     */
    public static int setRandomPlanetType(){
        return RandomHandler.createIntegerFromRange(100010,100012,new Random());
    }
    /**
     * Sets a random mass value for either galaxies or solarsystems.
     * This value is actual to get the side lengths of the object for
     * relating the new positions of its children when open it {@link GameSurface.GameThread#openObject()}
     * @param type
     * @return
     */
    public static float setRandomVolume(int type){
        Random r = new Random();
        float x,y,z,h,i,j;
        switch (type){
            case OBJECT_GALAXY:{
                x = GALAXY_MAX_X;
                y = GALAXY_MAX_Y;
                z = GALAXY_MAX_Z;
                h = GALAXY_MIN_X;
                i = GALAXY_MIN_Y;
                j = GALAXY_MIN_Z;
                break;
            }
            case OBJECT_SOLARSYSTEM:{
                x = SOLARSYSTEM_MAX_X;
                y = SOLARSYSTEM_MAX_Y;
                z = SOLARSYSTEM_MAX_Z;
                h = SOLARSYSTEM_MIN;
                i = SOLARSYSTEM_MIN;
                j = SOLARSYSTEM_MIN;
                break;
            }
            default:
                x = 0;
                y = 0;
                z = 0;
                h = 0;
                i = 0;
                j = 0;
        }
        x = RandomHandler.createFloatFromRange(h, x,r);
        y = RandomHandler.createFloatFromRange(i, y,r);
        z = RandomHandler.createFloatFromRange(j, z,r);
        return x*y*z;
    }
    /**
     * Sets a random temperature value for either stars, planets or moons
     * @param type
     * @return
     */
    public static float setRandomTemperature(int type){
        Random r = new Random();
        switch (type){
            case OBJECT_STAR:{
                return Math.round(RandomHandler.createFloatFromRange(4 * (float) Math.pow(10, 6), 23 * (float) Math.pow(10, 6), r));
            }
            case OBJECT_PLANET:{
                return Math.round(RandomHandler.createFloatFromRange(-250, 500, r));
            }
            case OBJECT_MOON:{
                return Math.round(RandomHandler.createFloatFromRange(-250, 30, r));
            }
            default:
                return Math.round(25);
        }

    }

    /**
     * Sets a random mass value for either stars, planets or moons
     * @param type
     * @return
     */
    public static float setRandomMass(int type){
        Random r = new Random();
        float mass;
        switch (type){
            case OBJECT_STAR:{
                return RandomHandler.createFloatFromRange(4f * (float) Math.pow(10f, 6f), 23f * (float) Math.pow(10f, 6f), r);
            }
            case OBJECT_PLANET:{
                return RandomHandler.createFloatFromRange(0.2f, 150f, r);
            }
            case OBJECT_MOON:{
                return RandomHandler.createFloatFromRange(0.001f, 0.8f, r);
            }
            default:
                return 0.2f;
        }

    }

    /**
     * Sets a random postion for a galaxy or solar system.
     * If the object is a solar system, the position is related to the parent galaxys
     * position
     * @param type
     * @return
     */
    public static MathHandler.Vector setRandomPosition(int type){
        Random r = new Random();
        float x,y,z;
        float borderMinDistance = ((UNIVERSE_X /3333f)/2);
        switch (type){
            case OBJECT_GALAXY:{
                x = (GALAXY_MAX_X +(UNIVERSE_X /RandomHandler.createFloatFromRange(borderMinDistance,3333,r)));
                y = (GALAXY_MAX_Y +(UNIVERSE_Y /RandomHandler.createFloatFromRange(borderMinDistance,3333,r)));
                z = (GALAXY_MAX_Z +(UNIVERSE_Z /RandomHandler.createFloatFromRange(borderMinDistance,3333,r)));
                break;
            }
            case OBJECT_SOLARSYSTEM:{
                x = (SOLARSYSTEM_MAX_X +(GALAXY_MAX_X /RandomHandler.createFloatFromRange(1,100,r)));
                y = (SOLARSYSTEM_MAX_Y +(GALAXY_MAX_Y /RandomHandler.createFloatFromRange(1,100,r)));
                z = (SOLARSYSTEM_MAX_Z +(GALAXY_MAX_Z /RandomHandler.createFloatFromRange(1,100,r)));
                break;
            }
            default:
                x = 0;
                y = 0;
                z = 0;
        }
        return new MathHandler.Vector(x,y,z);
    }

    /**
     * Sets a random position for a planet object
     * @param solarSystemX
     * @param solarSystemY
     * @param solarSystemZ
     * @return
     */
    public static MathHandler.Vector setRandomPlanetPosition(float solarSystemX,float solarSystemY,float solarSystemZ){
        Random r = new Random();
        float x,y,z;
        int xDir = 1;
        int yDir = 1;
        //no z because it should be alwas a plate

        //random direction in the solar system
        if(RandomHandler.createIntegerFromRange(0,1,r) != 1) xDir = -1;
        if(RandomHandler.createIntegerFromRange(0,1,r) != 1) yDir = -1;


        x = solarSystemX+(RandomHandler.createFloatFromRange(solarSystemX/8,solarSystemX/3,r)*xDir);
        y = solarSystemY+(RandomHandler.createFloatFromRange(solarSystemY/8,solarSystemY/3,r)*yDir);
        z = solarSystemZ+(RandomHandler.createFloatFromRange(solarSystemY/8,solarSystemY/3,r));

        return new MathHandler.Vector(x,y,z);
    }

    /**
     * Sets a random position for the moon from the position data of its parent planet
     * @param planetX
     * @param planetY
     * @param planetZ
     * @return
     */
    public static MathHandler.Vector setRandomMoonPosition(float planetX,float planetY,float planetZ){
        Random r = new Random();
        float x,y,z;
        int xDir = 1;
        int yDir = 1;
        int zDir = 1;

        //random direction in the solar system
        if(RandomHandler.createIntegerFromRange(0,1,r) != 1) xDir = -1;
        if(RandomHandler.createIntegerFromRange(0,1,r) != 1) yDir = -1;
        if(RandomHandler.createIntegerFromRange(0,1,r) != 1) zDir = -1;


        x = planetX+(RandomHandler.createFloatFromRange(planetX/8,planetX/3,r)*xDir);
        y = planetY+(RandomHandler.createFloatFromRange(planetY/8,planetY/3,r)*yDir);
        z = planetZ+(RandomHandler.createFloatFromRange(planetZ/8,planetZ/3,r)*zDir);

        return new MathHandler.Vector(x,y,z);
    }

    /**
     * Sets a random radius value for this object
     * <p>For Planet: {@link #EARTH_RADIUS} x (0.001f up to 20.0f)</p>
     * <p>For Star: {@link #EARTH_RADIUS} x (3000.0f up to 10000000000.0f)</p>
     * <p>For Moon: {@link #EARTH_RADIUS} x (0.00001f up to 0.4f)</p>
     * <p>For Asteroids & Comets: {@link #EARTH_RADIUS} x (0.0000001f up to 0.01f)</p>
     * <p>For Planet: {@link #EARTH_RADIUS} x (0.001f up to 20.0f)</p>
     * @param type
     * @return
     *  <p>default - {@link #EARTH_RADIUS}</p>
     */
    public static float setRandomRadius(int type){
        Random r = new Random();
        switch (type){
            case OBJECT_PLANET:{
                return (EARTH_RADIUS * RandomHandler.createFloatFromRange(0.01f,20.0f,r));
            }
            case OBJECT_STAR:{
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(3000.0f,10000000000.0f,r);
            }
            case OBJECT_MOON:{
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.00001f,0.4f,r);
            }
            case OBJECT_ASTEROID:
            case OBJECT_COMET:{
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.0000001f,0.01f,r);
            }
            default:return EARTH_RADIUS;
        }
    }

    /**
     * Create a random name
     * like if its a planet: P.UZDMDK.574865
     * @param type
     */
    public void setRandomName(int type){
        String prefix = "";
        String postfix ="";
        String randName ="";
        Random r = new Random();

        //create middle
        for(int i = 0; i<6;i++){
            randName += alphabet[RandomHandler.createIntegerFromRange(0,alphabet.length-1, r)];
        }
        for(int i = 0; i<6;i++){
            postfix += RandomHandler.createIntegerFromRange(0,9, r);
        }
        switch (type){
            case OBJECT_CITY:prefix = "C";break;
            case OBJECT_STAR:prefix = "ST";break;
            case OBJECT_PLANET:prefix = "P";break;
            case OBJECT_MOON:prefix = "M";break;
            case OBJECT_COMET:prefix = "CO";break;
            case OBJECT_ASTEROID:prefix = "A";break;
            case OBJECT_GALAXY:prefix = "G";break;
            case OBJECT_SOLARSYSTEM:prefix = "SS";break;
            default: prefix = "NONE";
        }
        this.name = prefix+"."+randName+"."+postfix;
    }
    /*----------------------------------------BOOLERS-----------------------------------*/

    public boolean isStar(){
        if(type == OBJECT_STAR)return true;
        else return false;
    }
    public boolean isCity(){
        if(type == OBJECT_CITY)return true;
        else return false;
    }
    public boolean isPlanet(){
        if(type == OBJECT_PLANET)return true;
        else return false;
    }
    public boolean isMoon(){
        if(type == OBJECT_MOON)return true;
        else return false;
    }
    public boolean isAsteroid(){
        if(type == OBJECT_ASTEROID)return true;
        else return false;
    }
    public boolean isComet(){
        if(type == OBJECT_COMET)return true;
        else return false;
    }
    public boolean isSolarSystem(){
        if(type == OBJECT_SOLARSYSTEM)return true;
        else return false;
    }
    public boolean isGalaxy(){
        if(type == OBJECT_SOLARSYSTEM)return true;
        else return false;
    }


    /*----------------------------------------GETTERS-----------------------------------*/
    private int getColor(int color){

        switch (color){
            case STAR_BLUE: return R.color.star_blue;
            case STAR_RED: return R.color.star_red;
            case STAR_ORANGE: return R.color.star_orange;
            case STAR_YELLOW: return R.color.star_yellow;
            default: return 0;
        }
    }
    private String getTypeName(int type){
        switch(type){
            case OBJECT_WORLD: return "World";
            case OBJECT_GALAXY: return "Galaxy";
            case OBJECT_SOLARSYSTEM: return "Solarsystem";
            case OBJECT_STAR: return "Star";
            case OBJECT_PLANET: return "Planet";
            case OBJECT_MOON: return "Moon";
            case OBJECT_ASTEROID: return "Asteroid";
            case OBJECT_COMET: return "Comet";

            default: return "No type";
        }
    }
    private String getDatabaseTableFromType(){
        switch (type){
            case OBJECT_GALAXY: return DB_Settings.DATABASE_TABLE_GALAXIES;
            case OBJECT_SOLARSYSTEM: return DB_Settings.DATABASE_TABLE_SOLARSYSTEMS;
            case OBJECT_STAR: return DB_Settings.DATABASE_TABLE_STARS;
            case OBJECT_PLANET: return DB_Settings.DATABASE_TABLE_PLANETS;
            case OBJECT_MOON: return DB_Settings.DATABASE_TABLE_MOONS;
            case OBJECT_CITY: return DB_Settings.DATABASE_TABLE_CITIES;
            default:
                System.err.println("No type found to return a database table!");
                return null;
        }
    }
    public int getId(){
        Cursor c = uDb.getReadableDatabase().query(getDatabaseTableFromType(),new String[]{DB_Settings.COL_ID},DB_Settings.COL_NAME+"="+this.name,null,null,null,null);
        return c.getInt(c.getColumnIndex(DB_Settings.COL_ID));
    }
/*    private float getGalaxyWidth()
    private float getGalaxyHeight
    private float getGalaxyDepth*/
    /*----------------------------------------HANDLERS-----------------------------------*/

    @Override
    public void onRotate(float angle, int mode, int direction, int axis){

         MathHandler.getTranslationMatrix().rotateVector3D(myPosition,
                 angle * CoordinateSystem3D.CoordinateAxis.getDirection(direction),
                 axis);
    }

    @Override
    public void onMove( float x, float y, float z) {
        MathHandler.Vector changeVec = new MathHandler.Vector(x,y,z);
        myPosition.addWith(changeVec);
    }

    @Override
    public void onScale(float factor){
        myPosition = MathHandler.getTranslationMatrix().scaleVector(myPosition,factor);
    }

    @Override
    public float getX() {
        return myPosition.getmFloatVec()[0];
    }

    @Override
    public float getY() {
        return myPosition.getmFloatVec()[1];
    }

    @Override
    public float getZ() {
        return myPosition.getmFloatVec()[2];
    }

    @Override
    public float getScope() {
        return scope;
    }

    @Override
    public float getDegrees() {
        return degrees;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public MathHandler.Vector getPosition() {
        return myPosition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getPopulation() {
        return population;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public SolarSystem[] getSolarsystems() {
        return solarSystems;
    }

    @Override
    public Planet[] getPlanets() {
        return planets;
    }

    @Override
    public Moon[] getMoons() {
        return moons;
    }

    @Override
    public Star[] getStars() {
        return stars;
    }

    @Override
    public City[] getCities() {
        return cities;
    }

    @Override
    public void setX(float x) {
        myPosition.setX(x);
    }

    @Override
    public void setY(float y) {
        myPosition.setY(y);
    }

    @Override
    public void setZ(float z) {
        myPosition.setZ(z);
    }






   /*----------------------------------------RUNNABLES-----------------------------------*/

}
