package com.thesaan.gameengine.android.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.Patterns;
import com.thesaan.gameengine.android.handler.AndroidHandler;
import com.thesaan.gameengine.android.handler.BitmapHandler;
import com.thesaan.gameengine.android.handler.DateHandler;
import com.thesaan.gameengine.android.handler.FilesHandler;

import java.util.HashMap;

public class Database extends SQLiteOpenHelper implements Patterns,DB_Settings {

    SQLiteDatabase db;
    Context context;

    //shows the amount of entries in the backup database
    protected int numberOfBackupEntries = 0;

    TextView bdayEvent;
    /*
        * initalize the Handlers for this activity
        * */
    DateHandler dh;
    FilesHandler fh;
    BitmapHandler bh;
    AndroidHandler ah;
    private HashMap<String, String> mAliasMap;

    /**
     * Default class for extending to create a database object.
     * @param context
     */
    public Database(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.context = context;
        init(context);
        db = this.getWritableDatabase();
        //System.out.println("Database Path:\t" + db.getPath());
    }

    /**
     * Member Database
     * @param context
     * @param db
     * @param databaseName
     */
    public Database(Context context, SQLiteDatabase db, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.context = context;
        init(context);
        this.db = db;
        //System.out.println("Database Backup Path:\t" + db.getPath());
    }

    private void init(Context c) {
        dh = new DateHandler();
        fh = new FilesHandler();
        bh = new BitmapHandler(c);
        ah = new AndroidHandler(c);
    }

    public void onCreate(SQLiteDatabase db) {
        createTables(db,TABLES_TO_CREATE);
    }

    public void createMemberDataTable(String username, int id){

        final String CREATE_MEMBER_DATA_TABLE = CREATE_TABLE + id+"_"
                +OPEN
                + COL_ID + AUTO_KEY_INT
                + COL_USERNAME + " VARCHAR(50)"+NN+NEXT
                + COL_PASSWORD + " VARCHAR(50)"+NN+NEXT
                + COL_EMAIL + " VARCHAR(70)"+NN+NEXT
                + COL_TIME_SINCE_LAST_ONLINE + " VARCHAR(70)"+NN+NEXT
                + COL_TIME_PLAYED + " VARCHAR(70)"+NN+NEXT
                + COL_TIME_PLAYED + " VARCHAR(70)"+NN+NEXT
                + COL_REG_DATE + " VARCHAR(70)"+NN+NEXT
                + COL_REAL_FIRST_NAME + " VARCHAR(70)"+NN+NEXT
                + COL_REAL_LAST_NAME + " VARCHAR(70)"+NN+NEXT
                + COL_RANK + " VARCHAR(50)"+NN+
                CLOSE;
    }

    private SQLiteDatabase getMemberDatabase(){
        SQLiteDatabase backup = SQLiteDatabase.openDatabase(
                Environment.getExternalStorageDirectory() + DATABASE_PATH + DATABASE_NAME_MEMBERS,
                null,
                SQLiteDatabase.OPEN_READWRITE);

        return backup;
    }
    private SQLiteDatabase getMemberGameDataDatabase(){
        SQLiteDatabase backup = SQLiteDatabase.openDatabase(
                Environment.getExternalStorageDirectory() + DATABASE_PATH + DATABASE_NAME_MEMBERS_GAME_DATA,
                null,
                SQLiteDatabase.OPEN_READWRITE);

        return backup;
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
                Log.e("Error message", e + "");
            }
        }
    }

    // TODO Its only upgrade the persons side, but not the password table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Upgrade Database", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PERSONS);
        onCreate(db);
    }

    protected void resetIndexes(SQLiteDatabase db, String table, String[] columns){
        ContentValues cv;

        if(db != null && !db.isReadOnly()){

            Cursor c = db.query(table,columns,null,null,null,null,null);
            c.moveToFirst();
            if(c.getCount() > 0 ){
                for(int i = 1;i<c.getCount();i++){
                    if(c.getType(0) == Cursor.FIELD_TYPE_INTEGER){
                        cv = new ContentValues();

                        System.err.print("Old Id:"+c.getInt(c.getColumnIndex(COL_ID))+
                                "\tNew Id: "+i
                        );
                        cv.put(COL_ID, i);
                        db.update(table, cv, COL_ID+"="+(i+12), null);
                        System.err.print("\tOld Id is now "+c.getInt(c.getColumnIndex(COL_ID)));
                    }
                    c.moveToNext();
                }
            }
        }else{
            System.err.println("ERROR:\tresetIndexes("+db+","+table+")");
            System.err.println("ERROR:\tresetIndexes db.isReadOnly("+db.isReadOnly()+")");
        }
    }
/*
        TODO ich muss schauen ob ich diese methode überhaupt benötige
    private void setHashMapForSearchSuggestions() {

        // This HashMap is used to map table fields to Custom Suggestion fields
        mAliasMap = new HashMap<String, String>();

        // Unique id for the each Suggestions ( Mandatory )
        mAliasMap.put("_ID", COL_ID + " as " + "_id");

        // Text for Suggestions ( Mandatory )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, */
/*COL_AGE+ " " + *//*
COL_FIRSTNAME */
/*+" "+ COL_LASTNAME *//*
 + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);

        // Icon for Suggestions ( Optional )
        // mAliasMap.put( SearchManager.SUGGEST_COLUMN_ICON_1, FIELD_FLAG + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);

        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, COL_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

    }
*/



    /**
     *
     * @param username
     * @param password
     * @return
     */
    public boolean isPasswordCorrect(String username,String password){

        //add selection information to the username for database
        String[] args = {username};
        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_PASSWORD}, COL_USERNAME+"=?", args, null, null, null);
        c.moveToFirst();
        String dbPWD = c.getString(c.getColumnIndex(COL_PASSWORD));

        if(dbPWD.equals(password))
            return true;
        else
            return false;
    }

    public synchronized boolean isUserAssigned(String username){
        String[] args = {username};
        //check uf username is already assigned
        Cursor c;

        if(db.isOpen()) {
            c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_USERNAME}, COL_USERNAME + "=?", args, null, null, null);
            c.moveToFirst();
        }else{
            System.err.println("[isUserAssigned()]Database is closed. Unable to read Usernames");
            return false;
        }

        if(c.getCount() > 0 )
            return true;
        else
            return false;
    }
    /**
     *
     * @return Cursor
     */
    public Cursor readKey(String username){

        //add selection information to the username for database
        String[] args = {username};

        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_PASSWORD}, COL_USERNAME+"=?", args, null, null, null);

        if (c != null)
            c.moveToFirst();
        return c;
    }

    public String readRequiredRank(int rank_id){
        switch (rank_id){
            case RANK_STANDARD_ID: return RANK_STANDARD;
            case RANK_STAFF_ID: return RANK_STAFF;
            case RANK_MASTER_ID: return RANK_MASTER;
            case RANK_ADMIN_ID: return RANK_ADMIN;
            default:return null;
        }
    }

    /**
     *
     * @param rank
     * @return
     */
    public int readRequiredRank(String rank){

        if(RANK_STANDARD.equals(rank)){
            return RANK_STANDARD_ID;
        }
        if(RANK_STAFF.equals(rank))   {
            return RANK_STAFF_ID;
        }
        if(RANK_MASTER.equals(rank))  {
            return RANK_MASTER_ID;
        }
        if(RANK_ADMIN.equals(rank))   {
            return RANK_ADMIN_ID;
        }else {
            return RANK_UNKNOWN_ID;
        }
    }

    public void changeUserPermission(String username, String newPermission){
        String[] args = {username};
        ContentValues cv = new ContentValues();

        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_ID}, COL_USERNAME+"=?", args, null, null, null);
        if (c != null)
            c.moveToFirst();

        if(newPermission == RANK_STAFF ||
           newPermission == RANK_STANDARD ||
           newPermission == RANK_ADMIN ||
           newPermission == RANK_MASTER ) {
            int id = c.getInt(c.getColumnIndex(COL_ID));

            cv.put(COL_RANK,newPermission);



            db.update(DATABASE_TABLE_PASSWORDS, cv, COL_ID + "=" + id, null);

            System.err.println("Berechtigung von "+ username + " auf "+ newPermission+" geändert.");
        }
    }

    public void changeUserName(String oldUsername, String newUsername) {
        String[] args = {oldUsername};
        ContentValues cv = new ContentValues();

        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_ID}, COL_USERNAME + "=?", args, null, null, null);
        if (c != null){
            c.moveToFirst();

            int id = c.getInt(c.getColumnIndex(COL_ID));

            cv.put(COL_USERNAME,newUsername);

            db.update(DATABASE_TABLE_PASSWORDS, cv, COL_ID + "=" + id, null);

            System.err.println("Username von "+ oldUsername + " auf "+ newUsername+" geändert.");
        }
    }

    public void changeUserPassword(String username, String newPassword) {
        String[] args = {username};
        ContentValues cv = new ContentValues();

        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_ID}, COL_USERNAME + "=?", args, null, null, null);
        if (c != null){
            c.moveToFirst();

            int id = c.getInt(c.getColumnIndex(COL_ID));

            cv.put(COL_PASSWORD,newPassword);

            db.update(DATABASE_TABLE_PASSWORDS, cv, COL_ID + "=" + id, null);

            System.err.println("Passwort von "+ username + " auf "+ newPassword+" geändert.");
        }
    }
    /**
     *
     * @return Rank of the User
     */
    public String readRank(String username){

        //add selection information to the username for database
        String[] arg = {username};

        Cursor c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_RANK}, COL_USERNAME+"=?", arg, null, null, null);

        if (c != null)
            c.moveToFirst();
        String rank = c.getString(c.getColumnIndex(COL_RANK));
        return rank;
    }

    /**
     *
     * @return Rank of the User
     */
    public int readRankID(String username){
        return readRequiredRank(username);
    }
    /**
     *
     * @param rank_id
     *  RANK_STANDARD_ID - shouldn't be set
     *  RANK_STAFF_ID
     *  RANK_MASTER_ID - primary for the owner of the company
     *  RANK_ADMIN_ID - Only for the developer
     *  RANK_UNKNOWN_ID
     * @return Cursor
     *  Cursor with the group of this rank.
     *  Returns null if no group member was found for this rank;
     */
    public Cursor readUsersWithRank(int rank_id){
        String rank = "";

        Cursor c;
        String[] arg= {rank};

        String selection = COL_RANK+"=?";

        switch (rank_id){
            case RANK_STANDARD_ID:{
                rank = RANK_STANDARD;
                break;
            }
            case RANK_STAFF_ID:{
                rank = RANK_STAFF;
                break;
            }
            case RANK_MASTER_ID:{
                rank = RANK_MASTER;
                break;
            }
            case RANK_ADMIN_ID:{
                rank = RANK_ADMIN;
                break;
            }
            default: {
                rank = null;
                rank_id = RANK_UNKNOWN_ID;
                break;
            }

        }

        if(rank_id != RANK_UNKNOWN_ID && rank != null) {
            c = db.query(DATABASE_TABLE_PASSWORDS, new String[]{COL_USERNAME}, selection, arg, null, null, null);
            if(c.getCount() > 0) {
                return c;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**Add a new  User to the database.
     * Is checking also if the username is aleady assigned.
     *
     * @param username The username
     * @param password The password
     * @param rank
     *  RANK_STANDARD   -   Standard
     *  RANK_STAFF      -   Personal
     *  RANK_MASTER     -   Chef
     *  RANK_ADMIN      -   Administrator
     */
    protected synchronized void addUser(String username,String password, String rank){
        ContentValues data = new ContentValues();
        getWritableDatabase();
        data.put(COL_USERNAME,username);
        data.put(COL_PASSWORD,password);
        data.put(COL_RANK,rank);



        //if this username is not assigned
        if(!isUserAssigned(username)) {
            db.insert(DATABASE_TABLE_PASSWORDS, null, data);
            System.err.println("Benutzername gespeichert");
        }else{
            //TODO diese Aussage als Textview hinzufügen
            System.err.println("Benutzername ist bereits vergeben");
        }
    }

    /*Get the amount of all Entries
    * */
    public Cursor getAll(String table) {

        Cursor c = db.query(table, null, null, null, null, null, null);

        if (c != null)
            c.moveToFirst();
        return c;
    }


    /*
    * Delete Database Entries
    * */
    public void removeData(int member_id,String table) {
        db
                .delete(table, COL_ID + " = " + member_id, null);
        //TODO define backup again
        //getBackupDatabase(SQLiteDatabase.OPEN_READWRITE)
        //        .delete(table, COL_ID + " = " + member_id, null);
        resetIndexes(db,table,null);
        db.close();
    }




    private String createDatabaseTimestamp(int year,int month,int day,int hour,int minute,int second){

        int[] ints = {year,month,day,hour,minute,second};
        String[] strings = new String[ints.length];

        for(int i = 0;i<strings.length;i++){
            String curr = Integer.toString(ints[i]);
            if(i >0){
                if(ints[i] < 10){
                    strings[i] = "0"+curr;
                }else{
                    strings[i] = curr;
                }
            }else{
                strings[i] = curr;
            }
        }
        //Format: yyyy-mm-dd hh:mm:ss
        //Format: 2015-04-17 13:04:13
        return strings[0]+"-"+strings[1]+"-"+strings[2]+" "+strings[3]+":"+strings[4]+":"+strings[5];
    }

    protected void updateRow(String table,int index, String column, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);

        if (index != 0)
            db.update(table, cv, COL_ID + "=" + index, null);
    }
    /**
     * Make the database entries(persons) older if the have their birthday.
     * If a contact has its birthday. Print it on the homescreen.
     *
     * @param activity
     */
    protected void checkPersonsAgeInDatabase(Activity activity) {

        boolean hasBDay = false;

        Cursor cursor = getAll(DATABASE_TABLE_MEMBERS);

        cursor.moveToFirst();
        /*check every entry of its age. If the person got older
        * set the new age to it
        * */

        for (int i = 0; i < cursor.getCount(); i++) {

            int currentAgeInDatabase = cursor.getInt(cursor.getColumnIndex(COL_AGE));
            String dateOfBirth = cursor.getString(cursor.getColumnIndex(COL_BIRTHDATE));

            /*A new person calculates its age by birthdate. So I just have to create a new
            * person with the cursor: first name, last name, date of birth to get the actual
            * age of the person.
            *
            * Then check the persons age with the age integer in the database and change it if
            * required
            * */

            //if the person has become older, update its age
            if (compareAgeStatus(currentAgeInDatabase, dateOfBirth, cursor)) {
                hasBDay = true;
            }
            //the textview to show the birthday event
            if (hasBDay) {
               /* String notificationText =
                        firstname + " " + lastname + " ist heute " + (currentAgeInDatabase + 1) + " Jahre alt geworden";

                 System.out.println(notificationText);
                */

            } else {
                //System.out.println("No birth day for "+firstname+" "+lastname);
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
    /*
    * If a person has its birthday, update its age
    * */
    protected void updateAgeInEntry(String[] columns, int age, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        for (int i = 0; i < columns.length; i++) {
            cv.put(columns[i], age);
        }

        db.update(DATABASE_TABLE_MEMBERS, cv, COL_ID + "=" + id, null);

    }

    private boolean compareAgeStatus(int databaseAge, String databaseDate, Cursor cursor) {
        String[] columns = {COL_AGE};
        DateHandler dh = new DateHandler();

        String[] date = databaseDate.split("\\.");

        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

        int age = dh.getAgeInYears(day, month, year);
        //System.out.println("Database Date: "+databaseDate+"\tCalc. Age: "+age+"\tDatabase Age: "+databaseAge);
        if (age > databaseAge) {
            updateAgeInEntry(columns, age, cursor.getInt(cursor.getColumnIndex(COL_ID)));
            return true;
        }
        return false;
    }

    /**
     *
     * @param c1
     * @param c2
     * @return true
     *  if the cursor data are equal
     * @return false
     *  if the cursor data are not equal
     *
     */
    public boolean compareCursorData(Cursor c1, Cursor c2){

        if(c1.getCount() == c2.getCount()) {
            System.err.println("Entry count is equal");
            //check if all entries are the same
            for (int i = 0; i < c2.getCount(); i++) {
                //proof every line
                for (int k = 0; k < c1.getColumnCount(); k++) {
                    //check if the amount of columns are the same
                    System.err.println("k");
                    if (c2.getColumnCount() == c1.getColumnCount()) {
                        //check column type
                        switch (c1.getType(k)) {
                            case Cursor.FIELD_TYPE_STRING: {
                                /*System.err.println(
                                        "c1: " + c1.getColumnName(k) + ": " + c1.getString(k) + "\n" +
                                                "c2: " + c2.getColumnName(k) + ": " + c2.getString(k) + "\n"
                                );*/
                                if (c1.getString(k) != c2.getString(k)) {
                                    return false;
                                }
                            }
                            case Cursor.FIELD_TYPE_INTEGER: {
                                /*System.err.println(
                                        "c1: " + c1.getColumnName(k) + ": " + c1.getInt(k) + "\n" +
                                                "c2: " + c2.getColumnName(k) + ": " + c2.getInt(k) + "\n"
                                );*/
                                if (c1.getInt(k) != c2.getInt(k)) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        System.err.println("Column amounts are not the same");
                    }
                }
                c2.moveToNext();
                c1.moveToNext();
            }
        }
            return true;
    }


/*                              BACKUP FUNCTIONS (STILL IN PROGRESS)
    *//**
     *
     * @param backupPurpose
     *  Database.ENTRY_GOT_DELETED - update backup if an entry got deleted
     *  Database.APPLICATION_RESTART_BACKUP - backup database if the application restarts
     *  Database.DATABASE_IS_EMPTY_AND_BACKUP_CONTAINS_DATA - if the actual database is
     *  empty and the backup contains data, copy its content into the application database
     * @return -1
     *  if no action got called or exception thrown
     *//*

    protected int exportDatabase(int backupPurpose) {
        try {
            File sd = new File(Environment.getExternalStorageDirectory() + "/KWIMG/", "BACKUP");
            File data = Environment.getDataDirectory();

            File currentDB = new File(data, DATABASE_NAME);
            File backupDB = new File(sd, DATABASE_BACKUP_NAME);

            //call to set numberOfBackupEntries
            Cursor c2 = getBackupDatabaseData();
            Cursor c1 = readData();

            //check if maybe the app database is empty (after re-install) and if the
            //backup database contains already data to copy
            int callReinstall;
            if(c1.getCount() == 0 && c2.getCount() > 0){
                backupPurpose = DATABASE_IS_EMPTY_AND_BACKUP_CONTAINS_DATA;


            //true if the cursors are equal
            boolean isEqual = false;

            switch (backupPurpose) {
                case Database.APPLICATION_RESTART_BACKUP: {
                    isEqual = compareCursorData(c1, c2);
                    break;
                }
                case Database.ENTRY_GOT_DELETED: {
                    isEqual = false;
                    break;
                }
                default:{
                    if(backupPurpose == DATABASE_IS_EMPTY_AND_BACKUP_CONTAINS_DATA){
                        callReinstall = restoreDatabase();
                        return callReinstall;
                    }
                }
            }

                if (!isEqual) {
                    if (sd.canWrite()) {
                        if (currentDB.exists()) {
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            return DATABASE_RESTORE_PROCESS_COMPLETED;
                        } else {
                            System.out.println("Datenbank existiert nicht");
                            return DATABASE_DONT_EXIST;
                        }

                    } else {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>database not writable");
                        return DATABASE_BACKUP_NOT_WRITABLE;
                    }
                } else {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>true");
                    return DATABASE_NO_UPDATE_REQUIRED;
                }
            }
            //if no action got called and no exception thrown
            return DATABASE_STATUS_OK;
            } catch (Exception e) {
                return DATABASE_BACKUP_PROCESS_FAILED;
            }

    }

    protected SQLiteDatabase getBackupDatabase(int accessType){
        SQLiteDatabase backup = SQLiteDatabase.openDatabase(
                Environment.getExternalStorageDirectory() + "/KWIMG/BACKUP/" + DATABASE_BACKUP_NAME,
                null,
                accessType);

        return backup;
    }

    private Cursor getBackupDatabaseData(){

        SQLiteDatabase backup = getBackupDatabase(SQLiteDatabase.OPEN_READONLY);

        System.out.println("BackupDb Path: " + backup.getPath());

        Cursor c = backup.query(DATABASE_TABLE_PERSONS,COLUMNS,null,null,null,null,null);


        if(c == null){
            System.err.println("Cursor from backup file is null");
            return null;
        }else
        if(c.getCount() == 0){
            *//*
            System.out.println("Column names of backup cursor");
            for(int i = 0; i< c.getColumnCount();i++) {

                System.err.print("[" + i + "]" + c.getColumnName(i));
            }
            for(int i = 0; i< c.getCount();i++){

                System.err.println("["+i+"]"+c.getString(c.getColumnIndex(COL_FIRSTNAME)));
                c.moveToNext();
            }
            *//*

            System.err.println("Cursor from backup file has no entries to recreate");
            return null;
        }else {
            c.moveToFirst();
            //to check when not all entries got copied to the database
            numberOfBackupEntries = c.getCount();
            return c;
        }
    }

    protected void removeFromBackupFile(int id){
        SQLiteDatabase backup = getBackupDatabase(SQLiteDatabase.OPEN_READWRITE);
        Cursor c = backup.query(DATABASE_TABLE_PERSONS, COLUMNS, COL_ID + "="+id, null, null, null, null);

        c.moveToFirst();
        try {
            System.err.println(
                    "Gelöschte Person:\n" + c.getString(2)
            );
        }catch (Exception e){
            System.err.println(e);
        }
        backup.delete(DATABASE_TABLE_PERSONS, COL_ID + " = " + id, null);
        backup.close();
    }

    protected int restoreDatabase() {
        try {
            Cursor c = getBackupDatabaseData();
            int count = c.getCount();

            if(count >0) {
                System.out.println("Backup-Datenbank wird gelesen...\n");

                //now copy the backup data back to the actual database
                for(int i = 0;i<count;i++) {
                    this.addPerson(
                            c.getInt(c.getColumnIndex(COL_AGE)),
                            c.getString(c.getColumnIndex(COL_FIRSTNAME)),
                            c.getString(c.getColumnIndex(COL_LASTNAME)),
                            c.getString(c.getColumnIndex(COL_BIRTHDATE)),
                            c.getString(c.getColumnIndex(COL_PROFILEPICTURE)),
                            c.getString(c.getColumnIndex(COL_DRIVERSLICENCE)),
                            c.getString(c.getColumnIndex(COL_CHECKITCARD)),
                            c.getString(c.getColumnIndex(COL_PASSPORT)),
                            c.getString(c.getColumnIndex(COL_OEBBCARD)),
                            c.getString(c.getColumnIndex(COL_PERSO_FRONT)),
                            c.getString(c.getColumnIndex(COL_PERSO_BACK)),
                            c.getString(c.getColumnIndex(COL_DETAILS)),
                            c.getInt(c.getColumnIndex(COL_BANNED)));
                    c.moveToNext();
                }

                //now box_test if the amount of entries are the same as from the backup
                if(this.countPersons() == count){
                    return DATABASE_RESTORE_PROCESS_COMPLETED;
                }else{
                    return DATABASE_RESTORE_PROCESS_NOT_FULLY_COMPLETED;
                }

            }else{
                System.out.println("Konnte nicht gelesen werden");
            }

            return -1;
        } catch (Exception e) {
            Toast.makeText(context,
                    e+"",
                    Toast.LENGTH_SHORT).show();
            return DATABASE_RESTORE_PROCESS_FAILED;
        }
    }
   */
}
