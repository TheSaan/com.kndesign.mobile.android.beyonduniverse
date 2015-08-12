package com.thesaan.gameengine.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.thesaan.gameengine.android.DB_Settings;

/**
 * Created by Michael on 28.07.2015.
 */
public class ServerDatabase extends WebDatabase {

    Context context;
    public ServerDatabase(Context context) {
        super(context);
        this.context = context;

    }

}
