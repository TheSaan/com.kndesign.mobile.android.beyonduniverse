package com.thesaan.beyonduniverse.gamecontent.world;

import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.opengl.shapes.Shape;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;

/**
 * Created by Michael on 07.01.2016.
 */
public class Map {

    float[] objectCoordinates;
    public Vertex coordList;

    //to save the translation after draw
    MathHandler.Vector[] positionList;


    //Screen dimension
    int screenWidth, screenHeight;

    public Map() {

    }

    public Map(UniverseObject[] objects){

    }

    public void makeVertex() {
        if (objectCoordinates != null) {
            coordList = new Vertex(objectCoordinates);
            coordList.setDimension(screenWidth, screenHeight);
            coordList.convertToScreenPercentage();
//            coordList.init();

        } else {
            System.err.println("Coordinates not set yet!");
        }
    }

    public void makeObjects(){
        float[] coords = coordList.getPointCoords();
        int length = coords.length;
        positionList = new MathHandler.Vector[length/3];

        int c = 0;

        for(int i = 0; i < length; i++){
            if(i == 0){
                positionList[0] = new MathHandler.Vector(coords[0],coords[1],coords[2]);
            }else{
                positionList[i] = new MathHandler.Vector(coords[c],coords[c+1],coords[c+2]);
            }
            c+=3;
        }
    }

    public void rotateObjects(float angle, int x, int y, int z, MathHandler.Matrix projCamView, MathHandler.Vector origin){
        makeObjects();

        for(MathHandler.Vector v : positionList){
            v.rotate3D(angle,x,y,z,projCamView,origin);
        }
    }
    public Vertex getCoordList() {
//        coordList.init();
        return coordList;
    }

    public void setObjectCoordinates(float[] objectCoordinates) {
        this.objectCoordinates = objectCoordinates;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

    }

    public float[] getObjectCoordinates() {
        return objectCoordinates;
    }

    public Vertex[] getObjectVertexes() {

        float[][] f = new float[objectCoordinates.length / Shape.COORDS_PER_VERTEX][Shape.COORDS_PER_VERTEX];

        Vertex[] vertexes = new Vertex[f.length];


        for (int i = 0; i < vertexes.length; i++) {
            Vertex temp = new Vertex(f[i][0], f[i][1], f[i][2]);
            vertexes[i] = temp;
        }

        return vertexes;
    }

    public void loadCoordinatesFromObjects(UniverseObject[] objects){

    }
}
