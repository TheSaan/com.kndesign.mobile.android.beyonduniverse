package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.Drawables;
import com.thesaan.beyonduniverse.gamecontent.Race;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.beyonduniverse.gamecontent.world.models.ModelGalaxy;
import com.thesaan.gameengine.android.opengl.text.GameFont;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;
import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

import java.util.Random;

/**
 * Defines a universal object for extending galaxies, solar systems, stars, planets, moons or cities
 */
public class UniverseObject implements UniverseObjectProperties, ObjectProperties {

    Vertex opengl_position;

    int mapWidth, mapHeight;

    public BUObject3D myObject;
    public int race;

    /*
    Detects wheter the map objects are already
    linked with a line to the nearest neighbor
    or not
     */
    boolean isLinked;
    public long population;
    public float radius;
    public float scope;
    public float mass;
    public float temperature;
    public float volume;
    public String name;
    public int type;
    public int planetType;
    public MathHandler.Vector myPosition;
    public UniverseObject parent;

    //stored seed of this object
    protected int myRandomSeed;
    //use the private seed;
    public Random random;

    /**
     * stores the position in the position draw array for opengl
     */
    int indexPosition;
    /**
     * <p>
     * Defines a small area in virtual space where
     * this object stands.
     * This variable is needed to get just a part
     * of the current object array which has to be drawn
     * onto the surface which reduces the processing
     * requirements.
     * </p>
     * <p>The Quadrants can be from 0 to 63 (4^3)</p>
     */
    public int positionQuadrant;

    /**
     * Defines a virtual Position relative to the real
     * position on Screen in relation to the kind of object
     */
    public String virtualPosition;

    public Map map;

    /*----------------------------------------CONSTRUCTORS-----------------------------------*/
    /**
     * For creating the world object and initializing the map
     */
    public UniverseObject(int type, int seed) {
        random = new Random(seed);

        this.type = type;
    }

    /**
     * Create all Universe Children
     */
    public UniverseObject(String name, int type, UniverseObject parent, Map map, int seed) {

        myRandomSeed = seed;

        random = new Random(seed);

        this.parent = parent;

        this.race = parent.race;

        this.map = map;

        this.type = type;

        if (this.map != null) {
            setScreenDimension(map.getScreenWidth(), map.getScreenHeight());
        } else {
            System.err.println("Map Object null in " + getTypeName() + " " + getName() + " Constructor UniverseObject.class");
        }

        //init different types of this class (Galaxy, Solarsystem, ...)
        int error = init();

        //if every property was set correct
        if (error == 0) {
            myObject = getModelByType();//TODO set objects in UniverseObject Constructor
            //set name
            if (name == "") {
                setRandomName();
            } else {
                this.name = name;
            }

        /*
            - todo Market init
            - todo Planet init
            - todo Asteroid init
            - todo Comet init
            - todo Solarsystem init
            - todo Moon init
         */
        }

    }


    /*----------------------------------------SETTERS-----------------------------------*/

    public void writeInfo(int infoType, GameFont font){
        if(font != null){
            float[] pos = myPosition.getmFloatVec();
            float x = pos[0]+20f;
            float y = pos[1]+20f;
            float z = pos[2];
            switch (infoType){
                case INFO_POSITION:

                    font.write(getPosInfoText(),x,y,z,0);
                    break;
                default:
                    font.write(getName(),x,y,z,0);
                    break;
            }
        }
    }

    /**
     * Get the Model Object by the defines object type
     * @return
     * Object3D Model
     */
    private BUObject3D getModelByType(){
        switch (type){
            case OBJECT_GALAXY:
                return new ModelGalaxy();
            default:
                return new BUObject3D(Primitives.getCone(10f),BUObject3D.OTHER);
        }
    }

    public void resetModel(){
        myObject = getModelByType();
    }
    private String getPosInfoText(){
        return
                "X: "+myPosition.getXf()+"\n"+
                        "Y: "+ myPosition.getYf()+"\n"+
                        "Z: "+ myPosition.getZf();
    }
    /**
     * A method that includes every random property
     * setter method which need all objects
     */
    public int setRandomProperties() {
        try {
            if (type != OBJECT_GALAXY && type != OBJECT_WORLD) {
                setRandomMass();
                setRandomTemperature();
                setRandomRadius();
                setRandomVolume();
            }
            setRandomPosition();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int init() {
        switch (type) {
            case OBJECT_UNIVERSE:

                return 0;
            case OBJECT_GALAXY:

                return setRandomProperties();
            case OBJECT_SOLARSYSTEM:

                return 0;
            case OBJECT_STAR:
                return setRandomProperties();
            case OBJECT_PLANET:
                setRandomPlanetPosition();
                return setRandomProperties();
            case OBJECT_MOON:
                setRandomMoonPosition();
                return setRandomProperties();
            case OBJECT_CITY:
                setRandomPopulation();
                return 0;
            case OBJECT_COMET:
                return setRandomProperties();
            case OBJECT_ASTEROID:
                return setRandomProperties();
            default:
                return 1;
        }


    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMyPosition(MathHandler.Vector position) {
        this.myPosition = position;
    }

    /**
     * Creates a random image out of the arrays filled with the ids of the object drawables.
     *
     * @return
     */
    public int getRandomImageResource() {
        if (isStar()) {
            return Drawables.drawablesStars[RandomHandler.createIntegerFromRange(
                    0, Drawables.drawablesStars.length, random)];
        } else if (isPlanet()) {
            return Drawables.drawablesPlanets[RandomHandler.createIntegerFromRange(
                    0, Drawables.drawablesPlanets.length, random)];
        } else if (isMoon()) {
            return Drawables.drawablesMoons[RandomHandler.createIntegerFromRange(
                    0, Drawables.drawablesMoons.length, random)];
        } else if (isAsteroid()) {
            return Drawables.drawablesAsteroids[RandomHandler.createIntegerFromRange(
                    0, Drawables.drawablesAsteroids.length, random)];
        } else if (isComet()) {
            return Drawables.drawablesComets[RandomHandler.createIntegerFromRange(
                    0, Drawables.drawablesComets.length, random)];
        } else if (isGalaxy()) {
            return Drawables.drawablesGalaxies[RandomHandler.createIntegerFromRange(0, Drawables.drawablesGalaxies.length, random)];
        } else {
            return 0;
        }
    }

    /**
     * If the planet is habitabel create either a grass, - water or jungle planet. the Rest kinds are selected by the
     * temperature
     *
     * @return
     */
    public void setRandomPlanetType() {
        setRandomTemperature();
        planetType = RandomHandler.createIntegerFromRange(
                PLANET_TYPE_GRASS, PLANET_TYPE_SNOW, random);
    }


    /**
     * Sets a random mass value for either galaxies or solarsystems.
     * This value is actual to get the side lengths of the object for
     * relating the new positions of its children when open it {@link
     * //     * StarMapSurface.GameThread#openObject()}
     *
     * @return
     */
    public float setRandomVolume() {
        float x, y, z, h, i, j;
        switch (type) {
            case OBJECT_GALAXY: {
                x = mapWidth;
                y = mapHeight;
                z = mapWidth;
                h = 0;
                i = 0;
                j = 0;
                break;
            }
            case OBJECT_SOLARSYSTEM: {
                x = mapWidth;
                y = mapHeight;
                z = mapWidth;
                h = 0;
                i = 0;
                j = 0;
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
        x = RandomHandler.createFloatFromRange(h, x, random);
        y = RandomHandler.createFloatFromRange(i, y, random);
        z = RandomHandler.createFloatFromRange(j, z, random);
        return x * y * z;
    }

    /**
     * Sets a random temperature value for either stars, planets or moons
     *
     * @return
     */
    public void setRandomTemperature() {

        switch (type) {
            case OBJECT_STAR: {
                temperature = Math.round(RandomHandler.createFloatFromRange(
                        4 * (float) Math.pow(10, 6), 23 * (float) Math.pow(10, 6), random));
            }
            case OBJECT_PLANET: {
                temperature = Math.round(RandomHandler.createFloatFromRange(-250, 500, random));
            }
            case OBJECT_MOON: {
                temperature = Math.round(RandomHandler.createFloatFromRange(-250, 5, random));
            }
            default:
                temperature = Math.round(15);
        }

    }

    public void setRandomPopulation() {
        population = RandomHandler.createIntegerFromRange(50, 35000000, random);
    }

    /**
     * Sets a random mass value for either stars, planets or moons
     *
     * @return
     */
    public void setRandomMass() {
        switch (type) {
            case OBJECT_STAR: {
                mass = RandomHandler.createFloatFromRange(
                        4f * (float) Math.pow(10f, 6f), 23f * (float) Math.pow(10f, 6f), random);
            }
            case OBJECT_PLANET: {
                mass = RandomHandler.createFloatFromRange(0.2f, 15f, random);
            }
            case OBJECT_MOON: {
                mass = RandomHandler.createFloatFromRange(0.001f, 0.8f, random);
            }
            default:
                mass = 1f;
        }

    }

    public int getNameNumber() {
        return Integer.getInteger(name.split(".")[2]);
    }

    public void setVirtualPosition(int quadrant) {

        positionQuadrant = quadrant;

        //beginning letters of greek letters names
        //alpha, beta, gamme, delta
        String[] xline = {"A", "B", "G", "D"};
        //epsilon, zeta, eta, theta
        String[] zline = {"EP", "ZE", "ET", "TH"};
        //iota, kappa, lambda, my
        String[] yline = {"I", "K", "L", "M"};

        int z = 0;
        int y = 0;
        int x = 0;

        //set quadrant name
        for (int i = 0; i < quadrant; i++) {

            if (y == 4) {
                z++;
                y = 0;
            } else if (x == 4) {
                y++;
                x = 0;
            } else {
                x++;
            }
        }

        //creates a static number out of the name
        //and the quadrant value
        int pos = getNameNumber() / quadrant;

        virtualPosition =
                xline[x] + "." +
                        yline[y] + "." +
                        zline[z] + "." + pos;

    }


    /**
     * Sets a random postion for a galaxy or solar system.
     * If the object is a solar system, the position is related to the parent galaxies
     * position, so that it is guaranteed that the solar system is really within
     * the galaxy.
     *
     * @return
     */
    public void setRandomPosition() {
        float x, y, z;
        float w = mapWidth;
        float h = mapHeight;

        float minOffsetW = RandomHandler.createFloatFromRange(-w, 0f, random);
        float maxOffsetW = RandomHandler.createFloatFromRange(0f, w, random);
        float maxOffsetH = RandomHandler.createFloatFromRange(-h, 0f, random);
        float minOffsetH = RandomHandler.createFloatFromRange(0f, h, random);

        switch (type) {
            case OBJECT_GALAXY: {
                x = RandomHandler.createFloatFromRange(minOffsetW, w + maxOffsetW, random);
                y = RandomHandler.createFloatFromRange(minOffsetH, h + maxOffsetH, random);
                z = RandomHandler.createFloatFromRange(minOffsetW, w + maxOffsetW, random);
//                x = RandomHandler.createFloatFromRange(0f, w, random);
//                y = RandomHandler.createFloatFromRange(0f, h, random);
//                z = RandomHandler.createFloatFromRange(0f, w, random);
                break;
            }
            case OBJECT_SOLARSYSTEM: {
                //parent dimensions
                float pWidth = getGalaxyWidth();
                float pHeight = getGalaxyHeight();
                //parent position
                float pPivotX = parent.getX();
                float pPivotY = parent.getY();
                float pPivotZ = parent.getZ();

                x = (w + (w / RandomHandler.createFloatFromRange(
                        pPivotX - (pWidth / 2), pPivotX + (pWidth / 2), random)));
                y = (h + (h / RandomHandler.createFloatFromRange(
                        pPivotY - (pHeight / 2), pPivotY + (pHeight / 2), random)));
                z = (w + (w / RandomHandler.createFloatFromRange
                        (pPivotZ - ((pWidth / 2) / 2), pPivotZ + ((pWidth / 2) / 2), random)));
                break;
            }
            default:
                x = 0;
                y = 0;
                z = 0;
        }
        myPosition = new MathHandler.Vector(x, y, z);
    }

    /**
     * Sets a random position for a planet object
     *
     * @return
     */
    public void setRandomPlanetPosition() {
        float x, y, z;
        int xDir = 1;
        int yDir = 1;

        float px = parent.getX();
        float py = parent.getY();
        float pz = parent.getZ();
        //no z because it should be alwas a plate

        //random direction in the solar system
        if (RandomHandler.createIntegerFromRange(0, 1, random) != 1) xDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, random) != 1) yDir = -1;


        x = px + (RandomHandler.createFloatFromRange(px / 8, px / 3, random) * xDir);
        y = py + (RandomHandler.createFloatFromRange(py / 8, py / 3, random) * yDir);
        z = pz + (RandomHandler.createFloatFromRange(pz / 8, pz / 3, random));

        myPosition = new MathHandler.Vector(x, y, z);
    }

    /**
     * Sets a random position for the moon from the position data of its parent planet
     *
     * @return
     */
    public void setRandomMoonPosition() {
        float x, y, z;
        int xDir = 1;
        int yDir = 1;
        int zDir = 1;
        float px = parent.getX();
        float py = parent.getY();
        float pz = parent.getZ();
        //random direction in the solar system
        if (RandomHandler.createIntegerFromRange(0, 1, random) != 1) xDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, random) != 1) yDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, random) != 1) zDir = -1;


        x = px + (RandomHandler.createFloatFromRange(px / 8, px / 3, random) * xDir);
        y = py + (RandomHandler.createFloatFromRange(py / 8, py / 3, random) * yDir);
        z = pz + (RandomHandler.createFloatFromRange(pz / 8, pz / 3, random) * zDir);

        myPosition = new MathHandler.Vector(x, y, z);
    }

    /**
     * Sets a random radius value for this object
     * <p>For Planet: {@link #EARTH_RADIUS} x (0.001f up to 20.0f)</p>
     * <p>For Star: {@link #EARTH_RADIUS} x (3000.0f up to 10000000000.0f)</p>
     * <p>For Moon: {@link #EARTH_RADIUS} x (0.00001f up to 0.4f)</p>
     * <p>For Asteroids & Comets: {@link #EARTH_RADIUS} x (0.0000001f up to 0.01f)</p>
     * <p>For Planet: {@link #EARTH_RADIUS} x (0.001f up to 20.0f)</p>
     *
     * @return <p>default - {@link #EARTH_RADIUS}</p>
     */
    public float setRandomRadius() {
        switch (type) {
            case OBJECT_PLANET: {
                return (EARTH_RADIUS * RandomHandler.createFloatFromRange(0.01f, 20.0f, random));
            }
            case OBJECT_STAR: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(3000.0f, 10000000000.0f, random);
            }
            case OBJECT_MOON: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.00001f, 0.4f, random);
            }
            case OBJECT_ASTEROID:
            case OBJECT_COMET: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.0000001f, 0.01f, random);
            }
            default:
                return EARTH_RADIUS;
        }
    }

    /**
     * Create a random name
     * like if its a planet: P.UZDMDK.574865
     */
    public void setRandomName() {
        String prefix = "";
        String postfix = "";
        String randName = "";

        //create middle
        for (int i = 0; i < 6; i++) {
            randName += ALPHABET[RandomHandler.createIntegerFromRange(0, ALPHABET.length - 1, random)];
        }
        for (int i = 0; i < 6; i++) {
            postfix += RandomHandler.createIntegerFromRange(0, 9, random);
        }

        if (type == OBJECT_SOLARSYSTEM)
            System.out.println("Solarsystem name should be set to SS." + randName + "." + postfix);

        if (type == 0) {
            System.out.println("Type not set!");
        } else {
            switch (type) {
                case OBJECT_CITY:
                    prefix = "C";
                    break;
                case OBJECT_STAR:
                    prefix = "ST";
                    break;
                case OBJECT_PLANET:
                    prefix = "P";
                    break;
                case OBJECT_MOON:
                    prefix = "M";
                    break;
                case OBJECT_COMET:
                    prefix = "CO";
                    break;
                case OBJECT_ASTEROID:
                    prefix = "A";
                    break;
                case OBJECT_GALAXY:
                    prefix = "G";
                    break;
                case OBJECT_SOLARSYSTEM:
                    prefix = "SS";
                    break;
                default:
                    prefix = "NONE";
            }
            this.name = prefix + "." + randName + "." + postfix;
        }
    }

    public void setRandomRace() {
        for (int r : Race.RACES) {
            if (parent.race == r) {
                race = r;
            }
        }

        race = RandomHandler.createIntegerFromRange(
                Race.RACES[0],
                Race.RACES[Race.RACES.length - 1],
                random
        );
    }

    public int getRandomPlanetAmount() {
        return RandomHandler.createIntegerFromRange(0, MAX_PLANETS_IN_SOLAR_SYSTEM, random);
    }

    protected int getRandomSolarSystemAmount() {
        return RandomHandler.createIntegerFromRange(0, MAX_SOLAR_SYSTEMS_IN_GALAXY, random);
    }
    /*----------------------------------------BOOLERS-----------------------------------*/

    public boolean isStar() {
        if (type == OBJECT_STAR) return true;
        else return false;
    }

    public boolean isCity() {
        if (type == OBJECT_CITY) return true;
        else return false;
    }

    public boolean isPlanet() {
        if (type == OBJECT_PLANET) return true;
        else return false;
    }

    public boolean isMoon() {
        if (type == OBJECT_MOON) return true;
        else return false;
    }

    public boolean isAsteroid() {
        if (type == OBJECT_ASTEROID) return true;
        else return false;
    }

    public boolean isComet() {
        if (type == OBJECT_COMET) return true;
        else return false;
    }

    public boolean isSolarSystem() {
        if (type == OBJECT_SOLARSYSTEM) return true;
        else return false;
    }

    public boolean isGalaxy() {
        if (type == OBJECT_SOLARSYSTEM) return true;
        else return false;
    }

    public boolean isUniverse() {
        if (type == OBJECT_UNIVERSE) return true;
        else return false;
    }

    /*----------------------------------------GETTERS-----------------------------------*/
    private int getColor(int color) {

        switch (color) {
//            case STAR_BLUE: return R.color.star_blue;
//            case STAR_RED: return R.color.star_red;
//            case STAR_ORANGE: return R.color.star_orange;
//            case STAR_YELLOW: return R.color.star_yellow;
            default:
                return 0;
        }
    }

    public String getTypeName() {
        switch (type) {
            case OBJECT_UNIVERSE:
                return "Universe";
            case OBJECT_GALAXY:
                return "Galaxy";
            case OBJECT_SOLARSYSTEM:
                return "Solarsystem";
            case OBJECT_STAR:
                return "Star";
            case OBJECT_PLANET:
                return "Planet";
            case OBJECT_MOON:
                return "Moon";
            case OBJECT_ASTEROID:
                return "Asteroid";
            case OBJECT_COMET:
                return "Comet";

            default:
                return "No type";
        }
    }

    public int getPlanetType() {
        return planetType;
    }

    private String getDatabaseTableFromType() {
        switch (type) {
            case OBJECT_GALAXY:
                return DB_Settings.DATABASE_TABLE_GALAXIES;
            case OBJECT_SOLARSYSTEM:
                return DB_Settings.DATABASE_TABLE_SOLARSYSTEMS;
            case OBJECT_STAR:
                return DB_Settings.DATABASE_TABLE_STARS;
            case OBJECT_PLANET:
                return DB_Settings.DATABASE_TABLE_PLANETS;
            case OBJECT_MOON:
                return DB_Settings.DATABASE_TABLE_MOONS;
            case OBJECT_CITY:
                return DB_Settings.DATABASE_TABLE_CITIES;
            default:
                System.err.println("No type found to return a database table!");
                return null;
        }
    }

//    public int getId() {
//        Cursor c = uDb.getReadableDatabase().query(getDatabaseTableFromType(), new String[]{DB_Settings.COL_ID}, DB_Settings.COL_NAME + "=" + this.name, null, null, null, null);
//        return c.getInt(c.getColumnIndex(DB_Settings.COL_ID));
//    }
/*    private float getGalaxyWidth()
    private float getGalaxyHeight
    private float getGalaxyDepth*/
    /*----------------------------------------HANDLERS-----------------------------------*/

    @Override
    public void onRotate(float angle, int xAxis, int yAxis, int zAxis) {

        myPosition.rotate3D(
                angle,
                xAxis,
                yAxis,
                zAxis,
                null,
                null
        );

        if(myObject != null) {
            if (xAxis == 1) myObject.rotateX(angle);
        }

        if(myObject != null) {
            if (yAxis == 1) myObject.rotateY(angle);
        }

        if(myObject != null) {
            if (zAxis == 1) myObject.rotateZ(angle);
        }
    }

    @Override
    public void onMove(float x, float y, float z) {
        myPosition.addWith(new MathHandler.Vector(x, y, z));
    }

    @Override
    public void onZoom(float factor) {
        myPosition.scale(factor);
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
    public float getTemperature() {
        return temperature;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    /**
     * Calculates the dimensions as a galaxy disk format. And not as
     * a cube.
     *
     * @return
     */
    public float getGalaxyWidth() {
        return ((parent.getVolume() * 0.33f) / 2f);
    }

    /**
     * Calculates the dimensions as a galaxy disk format. And not as
     * a cube.
     *
     * @return
     */
    public float getGalaxyHeight() {
        return parent.getVolume() * 0.33f;
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

    public Map getMap() {
        return map;
    }

    /**
     *
     * @return
     */
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     *
     * @return
     */
    public int getMapWidth() {
        return mapWidth;
    }

    @Override
    public Vertex getVertex() {
        return opengl_position;
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

    @Override
    public void setVertexPosition() {
        opengl_position = new Vertex(myPosition.getXf(), myPosition.getYf(), myPosition.getZf());
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setScreenDimension(int width, int height) {
        mapWidth = width;
        mapHeight = height;
    }

    public void setObject(BUObject3D myObject) {
        this.myObject = myObject;
        this.myObject.translate(
                myPosition.getXf(),
                myPosition.getYf(),
                myPosition.getZf()
        );
    }

    public BUObject3D getObject() {
        return myObject;
    }


    /*----------------------------------------RUNNABLES-----------------------------------*/

}
