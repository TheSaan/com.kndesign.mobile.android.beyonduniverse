package com.thesaan.beyonduniverse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaan.beyonduniverse.gamecontent.UniverseMap;
import com.thesaan.beyonduniverse.gamecontent.world.Universe;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.ui.StarMapSurface;

import com.thesaan.gameengine.android.database.AppDatabase;

import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener{


    Button loadButton, starMapButton, scaleUpButton, scaleDownButton;
    Button backToUniverseLayerButton;

    SeekBar waitBar;

    public StarMapSurface gameSurface;

    TextView tv, waitInfo,dbInfoText, selectedObjectInfo, starMapModeInfo;


    public static Context globalContext;

    public static AppDatabase uDb;

    public static File internalDir;

    private UniverseMap map;

    Universe mUniverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_load);
        setContentView(R.layout.activity_main);

        globalContext = getApplicationContext();

        internalDir = getFilesDir();

        uDb = new AppDatabase(MainActivity.this);

        mUniverse = new Universe(this,uDb);

        map = new UniverseMap(uDb);





        gameSurface = (StarMapSurface)findViewById(R.id.gameSurface);
        if(gameSurface == null)
            System.err.println("GameSurface init failed");


        //all views
        initViews();

        if(gameSurface != null) {
            gameSurface.setActivity(this);
            gameSurface.setStarMap(map);
            if(tv != null) {

            }else {
                System.err.println("PropertiesTextView is null");
            }
        }else{
            System.err.println("GameSurface View is null");
        }
    }

    /*----------------------------------------SETTERS-----------------------------------*/
    public void setOptionsText(String text){
        if(tv != null){
            tv.setText(text);
        }
    }
    public void setLoadingText(String text){
        if(tv != null){
            tv.setText(text);
        }
    }
    public void setSelectedObjectInfo(String name){
        selectedObjectInfo.setText("\tSelected object: "+name);
    }
    public void setStarMapModeInfo(String mode){
        starMapModeInfo.setText(mode);
    }
    /*----------------------------------------GETTERS-----------------------------------*/
    /*----------------------------------------BOOLERS-----------------------------------*/
    /*----------------------------------------HANDLERS-----------------------------------*/
    public void printUniverseObjectProportions(){
        System.out.println(
                "ALLOWED\nGalaxies:"+Universe.numberOfAllowedGalaxies+"\n"+
                        "SolarSystems: "+Universe.numberOfAllowedSolarSystems+"\n"+
                        "Planets: "+Universe.numberOfAllowedPlanets+"\n"+
                        "Moons: " +Universe.numberOfAllowedMoons+"\n"+
                        "Stars: " +Universe.numberOfAllowedStars+"\n"+
                        "Cities: "+Universe.numberOfAllowedCities+"\n"+
                        "Rest : "+
                        (
                                UniverseObjectProperties.MAX_UNIVERSE_OBJECTS-
                                        Universe.numberOfAllowedGalaxies-
                                        Universe.numberOfAllowedSolarSystems-
                                        Universe.numberOfAllowedPlanets-
                                        Universe.numberOfAllowedMoons-
                                        Universe.numberOfAllowedCities
                        )+"\n"+
                        "There are enough Planets created that each Solarsystem could have "+ Universe.averageAmountOfPlanetsInSolarSystem+" Planets\n"+
                        "Av. each "+Universe.averageAmountOfMoonsAroundPlanet+". Planet has a moon"
        );
    }
    private void initViews() {

//        loadButton = (Button)findViewById(R.id.loadButton);
//        loadButton.setOnClickListener(this);

        backToUniverseLayerButton = (Button)findViewById(R.id.backToUniverse);
        backToUniverseLayerButton.setText("Universe Mode");
        selectedObjectInfo = (TextView)findViewById(R.id.selectedObjectInfo);
        selectedObjectInfo.setText("Kein Objekt ausgewählt");

        starMapModeInfo = (TextView)findViewById(R.id.starMapModeInfo);
        starMapModeInfo.setText(gameSurface.getStarmapModeDescription());

        starMapButton = (Button)findViewById(R.id.StarmapButton);
        starMapButton.setOnClickListener(this);

        dbInfoText = (TextView)findViewById(R.id.dbInfoText);
        dbInfoText.setText(
                "\tGalaxien: "+uDb.getGalaxies().getCount()+"\n"+
                "\tSternensysteme: "+uDb.getSolarSystems().getCount()+"\n"+
                "\tSterne: "+uDb.getStars().getCount()+"\n"+
                "\tPlaneten: "+uDb.getPlanets().getCount()+"\n"+
                "\tMonde: "+uDb.getMoons().getCount()+"\n"+
                "\tStädte: "+uDb.getCities().getCount()+"\n"

        );

        scaleDownButton = (Button) findViewById(R.id.scaleDownButton);
        scaleUpButton = (Button) findViewById(R.id.scaleUpButton);

        scaleDownButton.setOnClickListener(this);
        scaleUpButton.setOnClickListener(this);


        waitBar = (SeekBar)findViewById(R.id.waitBar);
        waitInfo = (TextView)findViewById(R.id.waitInfo);

        gameSurface.setWaitValue(50);
        waitBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress =  0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                waitInfo.setText("Wait "+progress+" milliseconds between drawing steps.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                gameSurface.setWaitValue(progress);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.scaleUpButton:{
                try {
                    gameSurface.scaleUp(0.5f);
                }catch (InterruptedException e){

                }
                break;
            }
            case R.id.scaleDownButton:{
                try {
                    if(gameSurface.getmScaleFactor()-0.5f >= 0)
                        gameSurface.scaleDown(0.5f);
                }catch (InterruptedException e){

            }
                break;
            }
            case R.id.loadButton:{
//                mUniverse = new Universe(this,uDb);
                Toast.makeText(MainActivity.this, "Load", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.StarmapButton:{
//                gameSurface.showStarMap(map.getMapBuilder());

                Toast.makeText(MainActivity.this, "Open Starmap with " + uDb.getNumberOfGalaxies() + " Galaxies...", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.backToUniverse:{
                gameSurface.goToUniverseMode();
            }
        }
    }


}
