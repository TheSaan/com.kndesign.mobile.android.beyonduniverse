package com.thesaan.gameengine.android.handler;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Michael Knöfler on 02.03.2015.
 */
public class BitmapHandler {
    Context context;
    ContentResolver mContentResolver;

    public static final String FORMAT_GIF = ".gif";

    public BitmapHandler(Context context) {

        this.context = context;
    }

    public Bitmap resizeBitmap(Bitmap bm, double scaleFactor) {
        if (bm.getHeight() > 0 && bm.getWidth() > 0) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * scaleFactor), (int) (bm.getHeight() * scaleFactor), true);
            return resizedBitmap;
        } else {
            return null;
        }
    }

    /**
     * @param bitmap
     * @return
     */
    public Bitmap makeBitmapMutable(Bitmap bitmap) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap.Config type = bitmap.getConfig();


            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, bitmap.getRowBytes() * height);
            bitmap.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            bitmap.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            Bitmap bm = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            bm.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();
            return bm;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param path
     * @return
     */
    public Bitmap getBitmap(String path) {
        AndroidHandler ah = new AndroidHandler(context);


        try {
            String filepath = ah.splitIntoFILEPATHAndFILENAME(path, "IMG_", false)[0];
            String filename = ah.splitIntoFILEPATHAndFILENAME(path, "IMG_", false)[1];

            File file = new File(filepath, filename);

            if (file.exists()) {

                file.setReadable(true);
                file.setExecutable(true);
                //System.out.println("File exists");

                FileInputStream fis = new FileInputStream(file);


                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                if (bitmap != null) {
                    //System.out.println("Bitmap returned.");
                    fis.close();
                    return bitmap;

                } else {
                    fis.close();
                    //System.out.println("Bitmap not created.");
                    return null;
                }
            } else {
                //System.out.println("File don't exist");
                return null;
            }
        } catch (Exception ex) {
            //System.out.println("IOE " + ex);
            return null;
        }
    }

    /**
     * @param path
     * @return
     */
    public Bitmap getBitmapWithOptions(String path) {

        Bitmap b;
        BitmapFactory.Options options = new BitmapFactory.Options();

        //set configuration
        //Each pixel is stored on 4 bytes.

        options.inMutable = true;

        b = BitmapFactory.decodeFile(path, options);
        return b;

    }

    /**
     * @param bmp
     * @return
     */
    public ImageView getImage(Bitmap bmp) {

        try {
            ImageView image = new ImageView(context);
            image.setImageBitmap(bmp);
            return image;
        } catch (NullPointerException npe) {
            System.err.println("NPE in getImage(" + bmp + "," + context + ")\n\n" + npe);
            return null;
        }
    }

    /**
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * @param src
     * @param width
     * @param height
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap src, int width, int height) {

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    /**
     * @param file
     * @param bm
     */
    public void saveBitmapToFile(File file, Bitmap bm) {
        System.out.println("Width: " + bm.getWidth() + " Height: " + bm.getHeight());
        try {
            FileOutputStream out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("File Compress Error", "ERROR:" + e.toString());
        }
    }

    /**
     * Plays a GIF animation inside a custom view
     * <p/>
     * maybe some settings could be inserted as arguments like
     * gif size...
     *
     * @param filename File has to be contained in assets folder
     */
    public View playGif(String filename) {

        InputStream stream = null;
        try {
            stream = context.getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GIFView view = new GIFView(stream);

        return view;
    }

    /**
     *
     */
    public class GIFView extends View {

        InputStream mStream;

        Movie mMovie;

        private long movieStart;

        public GIFView(InputStream stream) {
            super(context);

            mStream = stream;

            mMovie = Movie.decodeStream(mStream);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.TRANSPARENT);
            super.onDraw(canvas);

            final long now = SystemClock.uptimeMillis();

            if (movieStart == 0) {
                movieStart = now;
            }
            final int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 10, 10);
            this.invalidate();
        }
    }

    /**
     * @param resource_ids
     */
    public static void displayDiashow(int[] resource_ids, ImageView view, Context context) {

        TestHandler test = new TestHandler();

        int length = resource_ids.length;

        Bitmap[] bitmaps = new Bitmap[length];

        for (int i = 0; i < length; i++) {
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), resource_ids[i]);
            bitmaps[i] = b;
        }

        //show all images for a 2 seconds


        view.setImageResource(resource_ids[0]);

        //now after every second second change the image
        for (int i = 1; i < length; i++) {
            // wait two seconds
            test.startTimer();
            if (test.stopTime() >= 2000) {

                //next image after 2 seconds
                view.setImageBitmap(bitmaps[i]);
//                view.setImageResource(resource_ids[i]);
            } else {
                i--;
            }
        }
    }
}
