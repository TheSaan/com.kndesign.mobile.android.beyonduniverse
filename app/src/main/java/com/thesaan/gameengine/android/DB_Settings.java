package com.thesaan.gameengine.android;

import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by mknoe on 17.04.2015.
 */
public interface DB_Settings {

    int REQUEST_TAKE_PICTURE = 1;

    /**
     * FOLDERS
     */

    String FOLDER_BEYOND_UNIVERSE = "/beyond-universe/";

    /**
     * File names
     */
    String FILENAME_INDEX ="index.php";
    String FILENAME_INSERT_RECORD ="insert_object.php";

    String STD_LOCALHOST = "192.168.1.2";
    String STRATO_HOST = "85.214.213.58";
    String LOCALHOST_EMULATED_ANDROID_DEVICE = "10.0.2.2";
    String LOCALHOST = "127.0.0.1";

    String HTTP = "http://";
    String HTTPS = "https://";
    String FTP = "ftp://";

    //IMAGE PREFIX
    String IMAGE_SPLIT_POINT = "IMG_";


    //TEXT TEXTS
    String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    String TEST_DETAILS = "Ich programmiere diese Applikation";



    //INTENTS
    Intent takePictureIntent = new Intent(
            MediaStore.ACTION_IMAGE_CAPTURE);


    // DATABASE SETTINGS
    String QM = " = ? ";
    String AND = " AND ";

    //Application Package
    String GAME_ENGINE_PACKAGE = "com.thesaan.gameengine.android";
    String APP_PACKAGE = "com.thesaan.gameengine.android";

    String DATABASE_NAME_UNIVERSE_GAME = "UniverseGameDatabase.db";
    String DATABASE_NAME_MEMBERS = "MemberDatabase.db";
    String DATABASE_NAME_MEMBERS_GAME_DATA = "MemberGameDataDatabase.db";
    String DATABASE_NAME_BACKUP = "GameDatabaseBackup.db";

    public static int DATABASE_VERSION = 1;

    //Database Backup Name
    String DATABASE_PATH = "//data//" + APP_PACKAGE + "//databases//";

    int DATABASE_EXISTS = 1000;
    int DATABASE_DONT_EXIST = 1001;
    int DATABASE_BACKUP_PROCESS_FAILED = 1002;
    int DATABASE_BACKUP_PROCESS_COMPLETED = 1003;
    int DATABASE_RESTORE_PROCESS_FAILED = 1004;
    int DATABASE_RESTORE_PROCESS_COMPLETED = 1005;
    int DATABASE_BACKUP_EXISTS = 1006;
    int DATABASE_BACKUP_DONT_EXIST = 1007;
    int DATABASE_NOT_WRITABLE = 1008;
    int DATABASE_BACKUP_NOT_WRITABLE = 1009;
    int DATABASE_BACKUP_FILE_COPYING_FAILED = 1010;
    int DATABASE_BACKUP_FILE_COPYING_COMPLETED = 1011;
    int DATABASE_BACKUP_FILE_COPYING_PROCESS_FAILED = 1012;
    int DATABASE_RESTORE_PROCESS_NOT_FULLY_COMPLETED = 1013;
    int DATABASE_NEW_VERSION_AVAILABLE = 1014;
    int DATABASE_NO_UPDATE_REQUIRED = 1015;
    int DATABASE_STATUS_OK = 1016;

    int ENTRY_GOT_DELETED = 3000;
    int APPLICATION_RESTART_BACKUP = 3001;
    int DATABASE_IS_EMPTY_AND_BACKUP_CONTAINS_DATA = 3002;

    //search configurations
    int STANDARD_SEARCH = 1017;
    int SEARCH_WITH_INTENT_ID = 1018;

    //STANDARD COLUMNS

    public String COL_ID = "_id";
    public String COL_NAME = "name";

    //CREATE TABLE STRINGS
    public String AUTO_INCR = " AUTOINCREMENT ";
    public String PRIM_KEY = " PRIMARY KEY ";
    public String INT = " INTEGER ";
    public String NUM = " NUMERIC ";
    public String CREATE_TABLE = " CREATE TABLE ";
    public String OPEN = " ( ";
    public String CLOSE = " )";
    public String NEXT = " , ";
    public String NN = " NOT NULL ";

    //CREATE TABLE COMBINATIONS
    public String AUTO_KEY_INT = INT + PRIM_KEY + AUTO_INCR;

    //TABLES
    /*
     String DATABASE_TABLE_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
     String COL_ = "";
    */
    //Password table
    String DATABASE_TABLE_PASSWORDS = "logins";
    String COL_USERNAME = "username";
    String COL_PASSWORD = "key";
    String COL_RANK = "rang";

    String RANK_STANDARD = "Standard";
    String RANK_STAFF = "Personal";
    String RANK_MASTER = "Chef";
    String RANK_ADMIN = "Administrator";

    int RANK_UNKNOWN_ID = 5000;
    int RANK_STANDARD_ID = 5001;
    int RANK_STAFF_ID = 5002;
    int RANK_MASTER_ID = 5003;
    int RANK_ADMIN_ID = 5004;

    String[] COLUMNS_LOGIN = {COL_ID, COL_USERNAME, COL_PASSWORD, COL_RANK};

    String CREATE_PASSWORD_TABLE = CREATE_TABLE + DATABASE_TABLE_PASSWORDS
            + OPEN
            + COL_ID + AUTO_KEY_INT
            + COL_USERNAME + " VARCHAR(50)" + NN + NEXT
            + COL_PASSWORD + " VARCHAR(50)" + NN + NEXT
            + COL_RANK + " VARCHAR(50)" + NN +
            CLOSE;

    String DATABASE_TABLE_MEMBERS = "users";
    String COL_EMAIL = "email";
    String COL_COUNTRY = "country";
    String COL_AGE = "age";
    String COL_BIRTHDATE = "birthdate";
    String COL_USER_TABLE = "user_table";
    String COL_TIME_SINCE_LAST_ONLINE = "time_since_last_on";
    String COL_TIME_PLAYED = "time_played";
    String COL_REG_DATE = "registration_timestamp";
    String COL_REAL_FIRST_NAME = "first_name";
    String COL_REAL_LAST_NAME = "last_name";
    String COL_GAME_CHARACTERS = "game_characters";
    String COL_LAST_LOGOUT = "timestamp_last_logout";

    String CREATE_MEMBER_TABLE = CREATE_TABLE + DATABASE_TABLE_MEMBERS
            + OPEN
            + COL_ID + AUTO_KEY_INT
            + COL_USERNAME + " VARCHAR(50)" + NN + NEXT
            + COL_PASSWORD + " VARCHAR(50)" + NN + NEXT
            + COL_EMAIL + " VARCHAR(70)" + NN + NEXT
            + COL_COUNTRY + " VARCHAR(70)" + NN + NEXT
            + COL_AGE + INT + NN + NEXT
            + COL_BIRTHDATE + " VARCHAR(70)" + NN + NEXT
            + COL_USER_TABLE + " VARCHAR(70)" + NN + NEXT
            + COL_REG_DATE + " VARCHAR(70)" + NN + NEXT
            + COL_REAL_FIRST_NAME + " VARCHAR(70)" + NN + NEXT
            + COL_REAL_LAST_NAME + " VARCHAR(70)" + NN + NEXT
            + COL_TIME_SINCE_LAST_ONLINE + " VARCHAR(70)" + NN + NEXT
            + COL_TIME_PLAYED + " VARCHAR(70)" + NN + NEXT
            + COL_LAST_LOGOUT + " VARCHAR(30)"
            + COL_GAME_CHARACTERS + INT + NN +
            CLOSE;

    //Universe object tables
    String DATABASE_TABLE_MARKETS = "markets";
    String DATABASE_TABLE_CITIES = "cities";
    String DATABASE_TABLE_PLANETS = "planets";
    String DATABASE_TABLE_MOONS = "moons";
    String DATABASE_TABLE_STARS = "stars";
    String DATABASE_TABLE_GALAXIES = "galaxies";
    String DATABASE_TABLE_SOLARSYSTEMS = "solar_systems";
    String DATABASE_TABLE_ITEMS = "items";


    String COL_SOLARSYSTEMS = "solar_systems";
    String COL_STARS = "stars";
    String COL_PLANETS = "planets";
    String COL_CITIES = "cities";
    String COL_MARKET = "markets";
    String COL_MOONS = "moons";
    String COL_RADIUS = "radius";
    String COL_PlANET_TYPE = "planet_type";
    String COL_DEGREES = "degrees";
    String COL_MASS = "mass";
    String COL_POS_X = "positionX";
    String COL_POS_Y = "positionY";
    String COL_POS_Z = "positionZ";
    String COL_VOLUME = "volume";
    String COL_RACE = "race";
    String COL_DEFAULT_PRICE = "default_price";
    String COL_MAX_AMOUNT = "max_amount";
    String COL_CONTAINER_SPACE = "container_space";
    String COL_ITEM_TYPE = "item_type";
    String COL_DRAWABLE_ID = "drawable_id";
    String COL_IS_ILLEGAL = "is_illegal";
    String COL_POPULATION = "population";
    String COL_PALETTE = "productpalette";

    //defines the objects parent like
    //planets parent is solarsystem
    String COL_PARENT = "parent";


    /*----------------------------------------HUMAN MARKET-----------------------------------*/
    String CREATE_HUMAN_MARKET_TABLE = CREATE_TABLE + DATABASE_TABLE_MARKETS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RACE + INT + NEXT
            + COL_PALETTE + " VARCHAR(600)"
            + CLOSE;
    String[] HUMAN_MARKET_COLUMNS = {
            COL_ID,
            COL_PARENT,
            COL_RACE,
            COL_PALETTE};
    /*----------------------------------------HIGH MARKET-----------------------------------*/
    String CREATE_HIGHS_MARKET_TABLE = CREATE_TABLE + DATABASE_TABLE_MARKETS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RACE + INT + NEXT
            + COL_PALETTE + " VARCHAR(600)"
            + CLOSE;

    /*----------------------------------------CROX MARKET-----------------------------------*/
    String CREATE_CROX_MARKET_TABLE = CREATE_TABLE + DATABASE_TABLE_MARKETS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RACE + INT + NEXT
            + COL_PALETTE + " VARCHAR(600)"
            + CLOSE;
    /*----------------------------------------OTHER TABLES-----------------------------------*/
    String CREATE_CITIES_TABLE = CREATE_TABLE + DATABASE_TABLE_CITIES
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_PARENT + INT + NEXT
            + COL_POPULATION + INT + NEXT
            + COL_MARKET + INT
            + CLOSE;
    String[] CITIES_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_PARENT,
            COL_POPULATION,
            COL_MARKET
    };
    String CREATE_PLANET_TABLE = CREATE_TABLE + DATABASE_TABLE_PLANETS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RADIUS + NUM + NN + NEXT
            + COL_PlANET_TYPE + INT + NN + NEXT
            + COL_DEGREES + NUM + NN + NEXT
            + COL_POPULATION + INT + NEXT
            + COL_MOONS + INT + NEXT
            + COL_CITIES + INT + NEXT
            + COL_MASS + NUM + NN + NEXT
            + COL_POS_X + NUM + NN + NEXT
            + COL_POS_Y + NUM + NN + NEXT
            + COL_POS_Z + NUM + NN
            + CLOSE;
    String[] PLANETS_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_PARENT,
            COL_RADIUS,
            COL_PlANET_TYPE,
            COL_DEGREES,
            COL_POPULATION,
            COL_MOONS,
            COL_CITIES,
            COL_MASS,
            COL_POS_X,
            COL_POS_Y,
            COL_POS_Z
    };
    String CREATE_MOON_TABLE = CREATE_TABLE + DATABASE_TABLE_MOONS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RADIUS + NUM + NN + NEXT
            + COL_DEGREES + NUM + NN + NEXT
            + COL_MASS + NUM + NN + NEXT
            + COL_POS_X + NUM + NN + NEXT
            + COL_POS_Y + NUM + NN + NEXT
            + COL_POS_Z + NUM + NN
            + CLOSE;
    String[] MOONS_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_PARENT,
            COL_RADIUS,
            COL_MASS,
            COL_DEGREES,
            COL_POS_X,
            COL_POS_Y,
            COL_POS_Z
    };
    String CREATE_STAR_TABLE = CREATE_TABLE + DATABASE_TABLE_STARS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_PARENT + INT + NEXT
            + COL_RADIUS + NUM + NN + NEXT
            + COL_DEGREES + NUM + NN + NEXT
            + COL_MASS + NUM + NN + NEXT
            + COL_POS_X + NUM + NN + NEXT
            + COL_POS_Y + NUM + NN + NEXT
            + COL_POS_Z + NUM + NN
            + CLOSE;

    String[] STARS_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_PARENT,
            COL_RADIUS,
            COL_DEGREES,
            COL_MASS,
            COL_POS_X,
            COL_POS_Y,
            COL_POS_Z
    };
    String CREATE_GALAXY_TABLE = CREATE_TABLE + DATABASE_TABLE_GALAXIES
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_RADIUS + NUM + NN + NEXT
            + COL_SOLARSYSTEMS + " VARCHAR(5000)" + NEXT
            + COL_VOLUME + NUM + NEXT
            + COL_POS_X + NUM + NN + NEXT
            + COL_POS_Y + NUM + NN + NEXT
            + COL_POS_Z + NUM + NN
            + CLOSE;

    String[] GALAXIES_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_RADIUS,
            COL_SOLARSYSTEMS,
            COL_VOLUME,
            COL_POS_X,
            COL_POS_Y,
            COL_POS_Z
    };

    String CREATE_SOLARSYSTEM_TABLE = CREATE_TABLE + DATABASE_TABLE_SOLARSYSTEMS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_PARENT + INT + NEXT
            + COL_PLANETS + INT + NEXT
            + COL_STARS + INT + NEXT
            + COL_VOLUME + NUM + NEXT
            + COL_POS_X + NUM + NN + NEXT
            + COL_POS_Y + NUM + NN + NEXT
            + COL_POS_Z + NUM + NN
            + CLOSE;

    String[] SOLARSYSTEMS_COLUMNS = {
            COL_ID,
            COL_NAME,
            COL_PARENT,
            COL_PLANETS,
            COL_STARS,
            COL_VOLUME,
            COL_POS_X,
            COL_POS_Y,
            COL_POS_Z
    };
    //INCLUDES ALL TABLES WHICH WILL BE CREATED ON START

    String CREATE_ITEMS_TABLE = CREATE_TABLE + DATABASE_TABLE_ITEMS
            + OPEN
            + COL_ID + AUTO_KEY_INT + NEXT
            + COL_NAME + " VARCHAR(50)" + NN + NEXT
            + COL_RACE + INT + NEXT
            + COL_DEFAULT_PRICE + NUM + NEXT
            + COL_MAX_AMOUNT + INT + NEXT
            + COL_CONTAINER_SPACE + NUM + NEXT
            + COL_ITEM_TYPE + INT + NN + NEXT
            + COL_PlANET_TYPE + INT + NN + NEXT
            + COL_IS_ILLEGAL + INT + NN + NEXT
            + COL_DRAWABLE_ID + INT + NN
            + CLOSE;
    String[] TABLES_TO_CREATE =
            {
//                    CREATE_MEMBER_TABLE,
                    CREATE_PLANET_TABLE,
                    CREATE_MOON_TABLE,
                    CREATE_STAR_TABLE,
                    CREATE_GALAXY_TABLE,
                    CREATE_SOLARSYSTEM_TABLE,
                    CREATE_CITIES_TABLE,
                    CREATE_CROX_MARKET_TABLE,
                    CREATE_HIGHS_MARKET_TABLE,
                    CREATE_HUMAN_MARKET_TABLE,
                    CREATE_ITEMS_TABLE
            };


}
