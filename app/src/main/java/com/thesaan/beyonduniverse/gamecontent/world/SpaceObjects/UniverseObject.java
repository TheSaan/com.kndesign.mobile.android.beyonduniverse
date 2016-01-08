package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.opengl.Matrix;
import android.widget.ImageView;

import com.thesaan.beyonduniverse.gamecontent.Drawables;
import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.drawables.CoordinateSystem3D;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;
import com.thesaan.gameengine.android.ui.StarMapSurface;

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

    Vertex opengl_position;


    public long population;
    public float radius;
    public float scope;
    public float mass;
    public float degrees;
    public float volume;
    public String name;
    public int type;
    public int planetType;
    public MathHandler.Vector myPosition;
    public UniverseObject parent;
    public String typeName;

    //stored seed of this object
    protected int myRandomSeed;
    //use the private seed;
    protected Random random;


    /*----------------------------------------CONSTRUCTORS-----------------------------------*/
    public UniverseObject() {

    }

    /**
     * To create a city
     */
    public UniverseObject(String name, long population, Market market, int type, int seed) {

        myRandomSeed = seed;

        random = new Random(myRandomSeed);

        this.type = type;
        this.market = market;

        //name set
        if (name != "")
            this.name = name;
        else
            setRandomName(random);
        this.population = population;

    }


    /**
     * To create Stars, asteroids, comets.
     *
     * @param name     If the name is empty, an unique random name will be set to this object.
     *                 Please note, that only objects should have explicit names which are inside the story.
     * @param position The universal position of the object.
     * @param mass
     * @param degrees
     * @param type
     * @param seed     seed based random object
     */
    public UniverseObject(String name, MathHandler.Vector position, float mass, float degrees, int type, int seed) {

        myRandomSeed = seed;

        random = new Random(myRandomSeed);

        //set the stars color
        starPaint = new Paint();

        this.type = type;

        if (isStar())
            setRandomColor(random);

        //name set
        if (name != "")
            this.name = name;
        else
            setRandomName(random);

        myPosition = position;

        this.degrees = degrees;
        this.mass = mass;
        this.radius = setRandomRadius(random);
        scope = 2f * radius * (float) Math.PI;

    }

    /**
     * This one is only for creating solar systems.
     *
     * @param name
     * @param radius
     * @param type
     */
    public UniverseObject(String name, UniverseObject parent, float radius, int type, int seed) {

        myRandomSeed = seed;

        random = new Random(myRandomSeed);

        System.out.println("Solarsystem Constructor");
        this.parent = parent;
        //name set
        if (name != "")
            this.name = name;
        else
            setRandomName(random);

        setType(type);


        myPosition = setRandomPosition(random);

        this.radius = radius;

    }

    /**
     * This one is only for creating galaxies. The position gets calculated internally
     *
     * @param name
     * @param type
     */
    public UniverseObject(String name, int type, int seed) {

        myRandomSeed = seed;

        random = new Random(myRandomSeed);

        //type set
        if (type == OBJECT_GALAXY)
            this.type = type;
        else
            System.err.println("Tried to create " + getTypeName() + " with Galaxy constructor!");

        //name set
        if (name != "")
            this.name = name;
        else
            setRandomName(random);


        this.volume = UniverseObject.setRandomVolume(type,random);

        this.radius = (float) Math.pow(volume, (1.0 / 3.0));

        //position set
        myPosition = setRandomPosition(random);

        setVertexPosition();

    }

    /*----------------------------------------SETTERS-----------------------------------*/

    public void setType(int type) {
        this.type = type;
    }

    public void setMyPosition(MathHandler.Vector position) {
        this.myPosition = position;
    }

    /**
     * If the object is a star, choose its color for selecting the corrent bitmap then.
     */
    public void setRandomColor(Random r) {
        starPaint.setColor(getColor(RandomHandler.createIntegerFromRange(0, 3, r) + 101010));
    }

    /**
     * Creates a random image out of the arrays filled with the ids of the object drawables.
     *
     * @return
     */
    public int getRandomImageResource(Random r) {
        if (isStar()) {
            return Drawables.drawablesStars[RandomHandler.createIntegerFromRange(0, Drawables.drawablesStars.length, r)];
        } else if (isPlanet()) {
            return Drawables.drawablesPlanets[RandomHandler.createIntegerFromRange(0, Drawables.drawablesPlanets.length, r)];
        } else if (isMoon()) {
            return Drawables.drawablesMoons[RandomHandler.createIntegerFromRange(0, Drawables.drawablesMoons.length, r)];
        } else if (isAsteroid()) {
            return Drawables.drawablesAsteroids[RandomHandler.createIntegerFromRange(0, Drawables.drawablesAsteroids.length, r)];
        } else if (isComet()) {
            return Drawables.drawablesComets[RandomHandler.createIntegerFromRange(0, Drawables.drawablesComets.length, r)];
        } else if (isGalaxy()) {
            return Drawables.drawablesGalaxies[RandomHandler.createIntegerFromRange(0, Drawables.drawablesGalaxies.length, r)];
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
    public static int setRandomPlanetType(Random r) {
        return RandomHandler.createIntegerFromRange(100010, 100012, r);
    }

    /**
     * Sets a random mass value for either galaxies or solarsystems.
     * This value is actual to get the side lengths of the object for
     * relating the new positions of its children when open it {@link StarMapSurface.GameThread#openObject()}
     *
     * @param type
     * @return
     */
    public static float setRandomVolume(int type, Random r) {
        float x, y, z, h, i, j;
        switch (type) {
            case OBJECT_GALAXY: {
                x = GALAXY_MAX_X;
                y = GALAXY_MAX_Y;
                z = GALAXY_MAX_Z;
                h = GALAXY_MIN_X;
                i = GALAXY_MIN_Y;
                j = GALAXY_MIN_Z;
                break;
            }
            case OBJECT_SOLARSYSTEM: {
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
        x = RandomHandler.createFloatFromRange(h, x, r);
        y = RandomHandler.createFloatFromRange(i, y, r);
        z = RandomHandler.createFloatFromRange(j, z, r);
        return x * y * z;
    }

    /**
     * Sets a random temperature value for either stars, planets or moons
     *
     * @param type
     * @return
     */
    public static float setRandomTemperature(int type, Random r) {

        switch (type) {
            case OBJECT_STAR: {
                return Math.round(RandomHandler.createFloatFromRange(4 * (float) Math.pow(10, 6), 23 * (float) Math.pow(10, 6), r));
            }
            case OBJECT_PLANET: {
                return Math.round(RandomHandler.createFloatFromRange(-250, 500, r));
            }
            case OBJECT_MOON: {
                return Math.round(RandomHandler.createFloatFromRange(-250, 30, r));
            }
            default:
                return Math.round(25);
        }

    }

    /**
     * Sets a random mass value for either stars, planets or moons
     *
     * @param type
     * @return
     */
    public static float setRandomMass(int type, Random r) {
        float mass;
        switch (type) {
            case OBJECT_STAR: {
                return RandomHandler.createFloatFromRange(4f * (float) Math.pow(10f, 6f), 23f * (float) Math.pow(10f, 6f), r);
            }
            case OBJECT_PLANET: {
                return RandomHandler.createFloatFromRange(0.2f, 150f, r);
            }
            case OBJECT_MOON: {
                return RandomHandler.createFloatFromRange(0.001f, 0.8f, r);
            }
            default:
                return 0.2f;
        }

    }

    /**
     * Sets a random postion for a galaxy or solar system.
     * If the object is a solar system, the position is related to the parent galaxies
     * position, so that it is guaranteed that the solar system is really within
     * the galaxy.
     *
     * @return
     */
    public MathHandler.Vector setRandomPosition(Random r) {
        float devider = 3333f;
        float x, y, z;
        float borderMinDistance = ((UNIVERSE_X / devider) / 2);
        switch (type) {
            case OBJECT_GALAXY: {
                x = (GALAXY_MAX_X + (UNIVERSE_X / RandomHandler.createFloatFromRange(borderMinDistance, devider, r)));
                y = (GALAXY_MAX_Y + (UNIVERSE_Y / RandomHandler.createFloatFromRange(borderMinDistance, devider, r)));
                z = (GALAXY_MAX_Z + (UNIVERSE_Z / RandomHandler.createFloatFromRange(borderMinDistance, devider, r)));
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

                x = (SOLARSYSTEM_MAX_X + (GALAXY_MAX_X / RandomHandler.createFloatFromRange(pPivotX - (pWidth / 2), pPivotX + (pWidth / 2), r)));
                y = (SOLARSYSTEM_MAX_Y + (GALAXY_MAX_Y / RandomHandler.createFloatFromRange(pPivotY - (pHeight / 2), pPivotY + (pHeight / 2), r)));
                z = (SOLARSYSTEM_MAX_Z + (GALAXY_MAX_Z / RandomHandler.createFloatFromRange(pPivotZ - ((pWidth / 2) / 2), pPivotZ + ((pWidth / 2) / 2), r)));
                break;
            }
            default:
                x = 0;
                y = 0;
                z = 0;
        }
        return new MathHandler.Vector(x, y, z);
    }

    /**
     * Sets a random position for a planet object
     *
     * @param solarSystemX
     * @param solarSystemY
     * @param solarSystemZ
     * @return
     */
    public static MathHandler.Vector setRandomPlanetPosition(float solarSystemX, float solarSystemY, float solarSystemZ, Random r) {
        float x, y, z;
        int xDir = 1;
        int yDir = 1;
        //no z because it should be alwas a plate

        //random direction in the solar system
        if (RandomHandler.createIntegerFromRange(0, 1, r) != 1) xDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, r) != 1) yDir = -1;


        x = solarSystemX + (RandomHandler.createFloatFromRange(solarSystemX / 8, solarSystemX / 3, r) * xDir);
        y = solarSystemY + (RandomHandler.createFloatFromRange(solarSystemY / 8, solarSystemY / 3, r) * yDir);
        z = solarSystemZ + (RandomHandler.createFloatFromRange(solarSystemY / 8, solarSystemY / 3, r));

        return new MathHandler.Vector(x, y, z);
    }

    /**
     * Sets a random position for the moon from the position data of its parent planet
     *
     * @param planetX
     * @param planetY
     * @param planetZ
     * @return
     */
    public static MathHandler.Vector setRandomMoonPosition(float planetX, float planetY, float planetZ, Random r) {
        float x, y, z;
        int xDir = 1;
        int yDir = 1;
        int zDir = 1;

        //random direction in the solar system
        if (RandomHandler.createIntegerFromRange(0, 1, r) != 1) xDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, r) != 1) yDir = -1;
        if (RandomHandler.createIntegerFromRange(0, 1, r) != 1) zDir = -1;


        x = planetX + (RandomHandler.createFloatFromRange(planetX / 8, planetX / 3, r) * xDir);
        y = planetY + (RandomHandler.createFloatFromRange(planetY / 8, planetY / 3, r) * yDir);
        z = planetZ + (RandomHandler.createFloatFromRange(planetZ / 8, planetZ / 3, r) * zDir);

        return new MathHandler.Vector(x, y, z);
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
    public float setRandomRadius(Random r) {
        switch (type) {
            case OBJECT_PLANET: {
                return (EARTH_RADIUS * RandomHandler.createFloatFromRange(0.01f, 20.0f, r));
            }
            case OBJECT_STAR: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(3000.0f, 10000000000.0f, r);
            }
            case OBJECT_MOON: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.00001f, 0.4f, r);
            }
            case OBJECT_ASTEROID:
            case OBJECT_COMET: {
                return EARTH_RADIUS * RandomHandler.createFloatFromRange(0.0000001f, 0.01f, r);
            }
            default:
                return EARTH_RADIUS;
        }
    }

    /**
     * Create a random name
     * like if its a planet: P.UZDMDK.574865
     */
    public void setRandomName(Random r) {
        String prefix = "";
        String postfix = "";
        String randName = "";

        //create middle
        for (int i = 0; i < 6; i++) {
            randName += ALPHABET[RandomHandler.createIntegerFromRange(0, ALPHABET.length - 1, r)];
        }
        for (int i = 0; i < 6; i++) {
            postfix += RandomHandler.createIntegerFromRange(0, 9, r);
        }

        if (type == OBJECT_SOLARSYSTEM)
            System.out.println("Solarsystem name should be set to SS." + randName + "." + postfix);

        if (type == 0) {
            System.out.println("Type not set!");
        }else {
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
            case OBJECT_WORLD:
                return "World";
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
    public float getDegrees() {
        return degrees;
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
    public Vertex getVertex(){
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
    public void setVertexPosition(){
        opengl_position = new Vertex(myPosition.getXf(),myPosition.getYf(),myPosition.getZf());
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }




   /*----------------------------------------RUNNABLES-----------------------------------*/

}
