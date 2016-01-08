package com.thesaan.gameengine.android.handler;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michael on 12.02.2015.
 */
public class AndroidHandler {

    Context context;


    final String BIG_MARK_LINE =
            "---------------------------------------------------------\n" +
                    "---------------------------------------------------------";

    public AndroidHandler(Context context){
        this.context = context;
    }


    public static void logAllDataFromCursor(Cursor c) {
        //show all entries for check
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {

            //System.out.println("Eintrag(" + i + ")\n");
            //System.out.println("---------------------------------------------------------------");
            for (int j = 0; j < c.getColumnCount(); j++) {

                //check the data type of the current column
                switch (c.getType(j)) {
                    case Cursor.FIELD_TYPE_NULL: {
                        Log.i("(" + j + ")" + c.getColumnName(j), null);
                        break;
                    }
                    case Cursor.FIELD_TYPE_INTEGER: {
                        Log.i("(" + j + ")" + c.getColumnName(j), c.getInt(j) + "");
                        break;
                    }
                    case Cursor.FIELD_TYPE_FLOAT: {
                        Log.i("(" + j + ")" + c.getColumnName(j), c.getFloat(j) + "");
                        break;
                    }
                    case Cursor.FIELD_TYPE_STRING: {
                        Log.i("(" + j + ")" + c.getColumnName(j), c.getString(j) + "");
                        break;
                    }
                    default: {
                        Log.i("(" + j + ")" + c.getColumnName(j), "Other");
                        break;
                    }
                }
            }
            //System.out.println("---------------------------------------------------------------");
            c.moveToNext();
        }
    }

    /**
     * Check if the string has numbers included
     * @param str
     * @return
     * True if numbers are included. False if .
     */
    public static boolean isNumeric(String str){
        try {
            double d = Double.parseDouble(str);
        }catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    /**
     * Takes a filepath+ the file name and splits them.
     * But the file name needs some special prefix where the
     * method is able to split them.
     *
     * The split section will be added afterwards to the file name again.
     *
     * @param path
     * @param splitpoint
     * @param printIt
     * @return
     */
    public static String[] splitIntoFILEPATHAndFILENAME(String path, String splitpoint, boolean printIt) {
        /**
         * Splits the path @see java.io.String into the directory path and its filename. But
         * the split point will be added to the second part, the file name.
         *
         *
         *@param path
         *  The file path
         *@param splitpoint
         *  The String expression where the path will be split
         *@param printIt
         *  The filepath and filename get printed in the console if this parameter is true.
         *
         *  */
        String[] cuts = path.split(splitpoint);

        //add the  prefix to the filename again
        cuts[1] = splitpoint + cuts[1];

        if (printIt) {
            //System.out.println(BIG_MARK_LINE + "\n\t\t\tFilepath & Filename");
            for (int i = 0; i < cuts.length; i++) {
                //System.out.println("cuts[" + i + "]:" + cuts[i]);
            }
        }

        return cuts;
    }

    /**
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * E.g. you want to create a ListView of names
     * and every list element can have only a
     * max length of 20 letters.
     *
     * If a name has more than 20 letters, take the
     * whole length of the full name and cut it off
     * at letter 17 and add three dots '...' to it.
     *
     * Than you have always maximal 20 letters/digits.
     *
     * @param src
     * @param wanted_length
     * @return
     */
    public static String cutStringIfTooLongAndAddDots(String src, int wanted_length){
        String cut = src.subSequence(0, wanted_length-3)+"...";
        return cut;
    }

    /**
     * Takes an array of one ore more patterns and checks them all
     * @param patterns
     * @param stringToCheck
     * @return
     */
    public static boolean checkMultiplePatterns(String[] patterns,String stringToCheck) {

        boolean isMatch = false;
        Pattern p;
        Matcher m;
        for (int i = 0; i < patterns.length; i++) {
            p = Pattern.compile(patterns[i]);
            m = p.matcher(stringToCheck);

            if (m.matches()) {
                return true;
            }else{
                continue;
            }
        }
        //TODO hier kann es passieren dass die methode immer false ausgibt AUFPASSEN!
        return false;
    }

    /**
     * Read the Text from an standard textfile
     * @param path
     * @return
     */
    private static String readTextFile(String path) {
        String details, line;
        File f = new File(path);

        try {

            BufferedReader file = new BufferedReader(new FileReader(f));

            try {
                details = "";
                while ((line = file.readLine()) != null) {
                    details += line + "\n";
                }
                return details;
            } catch (IOException e) {
                //System.out.println("could not read line...");
                return null;
            }

        } catch (FileNotFoundException ex) {
            //System.out.println("details file not found!");
            return null;
        }

    }


}
