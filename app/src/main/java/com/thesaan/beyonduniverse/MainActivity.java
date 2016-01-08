package com.thesaan.beyonduniverse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaan.beyonduniverse.gamecontent.StarMapBuilder;
import com.thesaan.beyonduniverse.gamecontent.world.World;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.database.AppDatabase;
import com.thesaan.gameengine.android.ui.StarMapSurface;


import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener{


    Button btn3D, btnCreateGalaxies;

    int numberOfGalaxies;

    /**
     * The canvas surface to draw onto.
     */
    public StarMapSurface gameSurface;

    /**
     * To define the waiting distance between redraw steps of the {@link #gameSurface}
     */
    SeekBar setAmountOfGalaxiesBar;

    TextView tv, galaxyInfo,dbInfoText, selectedObjectInfo, starMapModeInfo;

    public static AppDatabase uDb;

    public static Context globalContext;


    /**
     * To get internal storage files
     */
    public static File internalDir;

    /**
     * For Building the universe objects from database
     */
    private StarMapBuilder starMapBuilder;


    World mUniverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_load);
        setContentView(R.layout.activity_main);

        globalContext = getApplicationContext();

        internalDir = getFilesDir();


        mUniverse = new World(this);

        starMapBuilder = new StarMapBuilder(getApplicationContext());

        starMapBuilder.createMapOfGalaxies(1);

        gameSurface = (StarMapSurface)findViewById(R.id.gameSurface);
        if(gameSurface == null)
            System.err.println("GameSurface init failed");


        //all com.thesaan.gameengine.android.views
        initViews();

        if(gameSurface != null) {
            gameSurface.setActivity(this);
            gameSurface.setStarMap(starMapBuilder);
            if(tv != null) {

            }else {
                System.err.println("PropertiesTextView is null");
            }
        }else{
            System.err.println("GameSurface View is null");
        }

        btn3D.callOnClick();
    }

    //===============================================================================
    //= SETTERS & GETTERS
    //===============================================================================
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

    //===============================================================================
    //= BOOLS
    //===============================================================================

    //===============================================================================
    //= HANDLER
    //===============================================================================

    /**
     * Shows the global data of the database like amount of objects, etc.
     */
    public void printUniverseObjectProportions(){
        System.out.println(
                "ALLOWED\nGalaxies:"+ World.numberOfAllowedGalaxies+"\n"+
                        "SolarSystems: "+ World.numberOfAllowedSolarSystems+"\n"+
                        "Planets: "+ World.numberOfAllowedPlanets+"\n"+
                        "Moons: " + World.numberOfAllowedMoons+"\n"+
                        "Stars: " + World.numberOfAllowedStars+"\n"+
                        "Cities: "+ World.numberOfAllowedCities+"\n"+
                        "Rest : "+
                        (
                                UniverseObjectProperties.MAX_UNIVERSE_OBJECTS-
                                        World.numberOfAllowedGalaxies-
                                        World.numberOfAllowedSolarSystems-
                                        World.numberOfAllowedPlanets-
                                        World.numberOfAllowedMoons-
                                        World.numberOfAllowedCities
                        )+"\n"+
                        "There are enough Planets created that each Solarsystem could have "+ World.averageAmountOfPlanetsInSolarSystem+" Planets\n"+
                        "Av. each "+ World.averageAmountOfMoonsAroundPlanet+". Planet has a moon"
        );
    }

    /**
     * Setup all com.thesaan.gameengine.android.views
     */
    private void initViews() {

//        loadButton = (Button)findViewById(R.id.loadButton);
//        loadButton.setOnClickListener(this);

        selectedObjectInfo = (TextView)findViewById(R.id.selectedObjectInfo);
        selectedObjectInfo.setText("Kein Objekt ausgewählt");

        starMapModeInfo = (TextView)findViewById(R.id.starMapModeInfo);
        starMapModeInfo.setText(gameSurface.getStarmapModeDescription());

        dbInfoText = (TextView)findViewById(R.id.dbInfoText);

        setAmountOfGalaxiesBar = (SeekBar)findViewById(R.id.galaxyBar);
        galaxyInfo = (TextView)findViewById(R.id.waitInfo);

        btn3D = (Button)findViewById(R.id.btn3D);
        btn3D.setOnClickListener(this);

        btnCreateGalaxies = (Button)findViewById(R.id.createGalaxies);
        btnCreateGalaxies.setOnClickListener(this);

        setAmountOfGalaxiesBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                galaxyInfo.setText("Create " + progress + " Galaxies when clicking the button below.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setAmountOfGalaxies(progress);
            }
        });

    }

    public void setAmountOfGalaxies(int val) {
        numberOfGalaxies = val;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.loadButton:{
//                mUniverse = new Universe(this,uDb);
                Toast.makeText(MainActivity.this, "Load", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn3D:{
                gameSurface = null;

                Intent i = new Intent(MainActivity.this, OpenGLESActivity.class);
                startActivity(i);

                break;
            }
            case R.id.createGalaxies:{

                StarMapBuilder builder = gameSurface.getStarmapBuilder();

                builder.setGalaxies(null);
                builder.createMapOfGalaxies(numberOfGalaxies);

                gameSurface.setStarMap(builder);

                break;
            }

        }
    }


}
