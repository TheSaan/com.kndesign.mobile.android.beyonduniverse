package com.thesaan.gameengine.android.database;

/**
 * Contains building block methods to change the code in {@link DatabaseConnection#doInBackground(String...)} for the
 * certain purpose.
 *
 * Simply add the code to the, for example {@link DatabaseConnectionSupport#post()}, to run the $POST_ method.
 * Same works for buildPost(),buildUpdate(),buildInsert(), buildDelete()
 */
public interface DatabaseConnectionSupport {


    int GET = 123;
    int POST = 124;
    int INSERT = 125;
    int UPDATE = 126;
    int DELETE = 127;

    int CONNECTION_TEST = 99;
    int POST_SUCCESS = 100;
    /**
     *
     */
    int post();
    int insert();
    int update();
    int delete();
}
