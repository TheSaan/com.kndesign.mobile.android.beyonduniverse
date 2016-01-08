package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;

/**
 * Created by mknoe on 29.04.2015.
 */
public interface ObjectProperties {

    //an random choosen value to multiply the degrees of the star
    float ENERGY_MULTIPLIER = 5.56f;
    void onRotate(float angle, int xAxis, int yAxis, int zAxis);
    void onMove(float x, float y, float z);
    void onZoom(float factor);
    float getX();
    float getY();
    float getZ();
    float getScope();
    float getDegrees();
    float getRadius();
    float getMass();
    float getVolume();
    MathHandler.Vector getPosition();
    String getName();
    long getPopulation();
    int getType();
    SolarSystem[] getSolarsystems();
    Planet[] getPlanets();
    Moon[] getMoons();
    Star[] getStars();
    City[] getCities();
    Vertex getVertex();

    void setX(float x);
    void setY(float y);
    void setZ(float z);
    void setVertexPosition();


}
