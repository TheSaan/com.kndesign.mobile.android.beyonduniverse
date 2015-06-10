package com.thesaan.gameengine.android.handler;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Michael Knöfler on 04.03.2015.
 */
public class ServiceHandler {

    Context context;

    public ServiceHandler(Context context){
        this.context = context;

    }
    public static void restartService(Activity activity,Service service){
        activity.stopService(new Intent(activity, service.getClass()));
        activity.startService(new Intent(activity, service.getClass()));
    }
    public void startCreateWorldService(Intent i){
        context.startService(i);
    }
    /*
    *Service template
    *
    public class TemplateService extends Service{

        private final IBinder mBinder = new MyBinder();
        boolean binded;

        public int onStartCommand(Intent intent, int flags, int startId) {

            System.out.println("TemplateService runs");

            //Does not depend on the intent
            //because this service runs anyway
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            binded = true;
            return mBinder;
        }

        protected void restart() {
            stopSelf();
            startService(new Intent(this, TemplateService.class));
        }

        @Override
        public boolean onUnbind(Intent intent) {
            binded = false;
            super.onUnbind(intent);
            return true;
        }

        class MyBinder extends Binder {
            TemplateService getService() {
                return TemplateService.this;
            }
        }
    }
    */

}
