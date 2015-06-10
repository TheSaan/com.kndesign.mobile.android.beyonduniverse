package com.thesaan.gameengine.android.drawables.forms;

import android.graphics.Canvas;

/**
 * Created by mknoe on 28.04.2015.
 */
public interface Form {
    public final static int WIREFRAME = 1000;
    public final static int SURFACED = 1001;
    public final static int RENDERED = 1002;

    public void makeWireframe(Canvas canvas, int look);
    public void makeSurface(Canvas canvas, int look);
    public void makeRendered(Canvas canvas, int look);

    public void onRotate(Canvas canvas, float angle, int mode, int direction, int axis);
}
