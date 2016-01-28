package com.thesaan.beyonduniverse.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.thesaan.gameengine.android.activities.ProActivity;


/**
 * Activates a Settings Interface to control the app behavior and
 * its data
 */
public class DeveloperActivity extends ProActivity {

    EditText ET_add_galaxy,ET_add_solarsystem;
    TextView TV_galaxies,TV_solarsystems,TV_stars,TV_planets,TV_planets_hab,TV_planets_hab_percent, TV_moons,TV_cities,TV_cities_avg;

    Button B_delete;

    CheckBox CB_confirm_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addViews(){

    }
}
