package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 29.04.2015.
 */
public interface ObjectProperties {

    //an random choosen value to multiply the degrees of the star
    public final static float ENERGY_MULTIPLIER = 5.56f;
    public void onRotate(float angle, int mode, int direction, int axis);
    public void onMove(float x, float y, float z);
    public void onScale(float factor);
    public float getX();
    public float getY();
    public float getZ();
    public float getScope();
    public float getDegrees();
    public float getRadius();
    public float getMass();
    public float getVolume();
    public MathHandler.Vector getPosition();
    public String getName();
    public long getPopulation();
    public int getType();
    public SolarSystem[] getSolarsystems();
    public Planet[] getPlanets();
    public Moon[] getMoons();
    public Star[] getStars();
    public City[] getCities();

    public void setX(float x);
    public void setY(float y);
    public void setZ(float z);


}
