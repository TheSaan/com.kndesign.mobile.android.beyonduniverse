package com.thesaan.gameengine.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Michael on 18.09.2015.
 */
public class ProActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void longToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void shortToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
