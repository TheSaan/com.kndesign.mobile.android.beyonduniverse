package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.handler.BitmapHandler;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Star extends UniverseObject {


    private float mEnergy;

    private float lumosity;

    private float innerHabitableBorderRadius;
    private float outerHabitableBorderRadius;

    String gifPrefix = "starObject";
    String gifStarBrightBlue = "BrightBlue";
    String gifStarOrange = "Orange";
    String gifStarRed = "Red";
    String gifStarYellow = "Yellow";

    private String gifFileName;
    public Star(String name, MathHandler.Vector position, float mass, float degrees, int type){
        super(name,position,mass,degrees,type);

        mEnergy = degrees*ENERGY_MULTIPLIER;

        setLuminosity(mass);
        setInnerHabitableBorder();
        setOuterHabitableBorder();

        generateStarColor();
    }

    public String getGifFileName(){
        return gifFileName;
    }

    private void generateStarColor(){
        switch (RandomHandler.createIntegerFromRange(1,4,new Random())){
            case 1:{
                gifFileName = gifPrefix + gifStarYellow + BitmapHandler.FORMAT_GIF;
            }
            case 2:{
                gifFileName = gifPrefix + gifStarOrange + BitmapHandler.FORMAT_GIF;
            }
            case 3:{
                gifFileName = gifPrefix + gifStarRed + BitmapHandler.FORMAT_GIF;
            }
            case 4:{
                gifFileName = gifPrefix + gifStarBrightBlue + BitmapHandler.FORMAT_GIF;
            }
            default:{
                gifFileName = gifPrefix + gifStarYellow + BitmapHandler.FORMAT_GIF;
            }
        }
    }

    /**
     * Defines the luminosity (DE: Leuchtkraft)
     * @param mass
     */
    public void setLuminosity(float mass){
        lumosity = 0.0f;

        if(mass < UniverseObjectProperties.SUN_MASS){
            lumosity = 1.75f*(mass-0.1f)+3.325f;
        }else{
            lumosity = 0.5f*(2.0f-mass)+4.4f;
        }
    }

    /**
     * Sets the distance in AE
     * TODO maybe calculation in pixels
     */
    public void setInnerHabitableBorder(){
        innerHabitableBorderRadius = (float)(0.93f*(Math.sqrt((double)lumosity)+0.5f)*(149.6f));
    }

    /**
     * Sets the distance in AE
     * TODO maybe calculation in pixels
     */
    public void setOuterHabitableBorder(){
        outerHabitableBorderRadius = (float)(1.1f*(Math.sqrt((double)lumosity)+0.5f)*(149.6f));
    }
    public float getSolarPower(){
        return mEnergy;
    }
}
