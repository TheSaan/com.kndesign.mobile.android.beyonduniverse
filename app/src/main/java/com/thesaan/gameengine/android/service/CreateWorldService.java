package com.thesaan.gameengine.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.thesaan.beyonduniverse.gamecontent.world.Game;

/**
 * Created by mknoe on 30.04.2015.
 */
public class CreateWorldService extends Service {

    private final IBinder mBinder = new MyBinder();
    boolean binded;

    Context context;

    Game mUniverse;
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("CreateWorldService runs");
        //mUniverse = new Universe(getContext());
        //Does not depend on the intent
        //because this service runs anyway
        return Service.START_STICKY;
    }

    public Game getBuiltUniverse(){
        return mUniverse;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public IBinder onBind(Intent intent) {
        binded = true;
        return mBinder;
    }

    protected void restart() {
        stopSelf();
        startService(new Intent(this, CreateWorldService.class));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binded = false;
        super.onUnbind(intent);
        return true;
    }

    public class MyBinder extends Binder {
        public CreateWorldService getService() {
            return CreateWorldService.this;
        }
    }
}