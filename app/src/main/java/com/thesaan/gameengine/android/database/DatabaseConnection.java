package com.thesaan.gameengine.android.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.thesaan.gameengine.android.Errors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Common Connection Class to connect to an server database.
 *
 * First add the data with {@link #setPostValues(String, String[], String[], String[], String)}, then
 * call if (encodingFormat != null)
 format = encodingFormat;
 else
 format = UTF8;
 * Created by Michael Knöfler on 20.09.2015.
 */
public class DatabaseConnection extends AsyncTask<String, Void, Integer> implements DatabaseConnectionSupport {

    Context context;

    String folder = null;
    String urlPrefix = null;
    String host = null;
    String file = null;
    String dbName = null;

    //the appovement if the user is logged in
    private boolean logged_in = false;

    //
    private boolean isConnected = false;

    public static final int INT_RETURN_NULL = -1;
    URL url;

    public final static String UTF8 = "UTF-8";
    public final static String METHOD_POST = "POST";
    public final static String METHOD_GET = "GET";

    /**
     * The complete encoded data for OutputStreamWriter
     */
    String postData;

    /**
     * If the encoding values do have different encoding formats, use this
     * array to add the different formats.
     * Please note that for this functionality the order of the values has
     * to be correct so that no wrong format gets assigned to a value!
     */
    String[] encodingFormats;

    /**
     * Selector for process
     */
    private int identifier;

    DatabaseConnection(Context context, String host, int method_identifier) {
        this.context = context;
        this.host = host;

        identifier = method_identifier;

        Toast.makeText(context, host, Toast.LENGTH_SHORT).show();

        init(host, "beyond_universe_android_game");
        login("mknoefler", "latinrce44");
//        login("root", "VUbwAMK9");
    }

    protected void init(String host,String database_name){
        this.host = host;
        dbName = database_name;

        addPostValue("database_name", database_name, null);

    }

    protected void login(String username, String password){
        addPostValue("username",username,null);
        addPostValue("key", password,null);
    }
    @Override
    protected Integer doInBackground(String... params) {
//        System.out.println("Identifier: " + identifier);
        switch (identifier) {
            case POST:
                System.out.println("POST");
                return post();
            case INSERT:

                return insert();
            case UPDATE:

                return update();
            case DELETE:

                return delete();
            case CONNECTION_TEST:
                return post();
            default:
                return INT_RETURN_NULL;
        }
    }

    /**
     * Creates the @see #postData string as encoded format
     * @param host
     * @param valueNames
     * @param values
     * @param encodingFormats
     * @param encodingFormat
     */
    public void setPostValues(String host, String[] valueNames, String[] values, @Nullable String[] encodingFormats, @Nullable String encodingFormat) {

        String format;
        //the check is in post()
        this.host = host;
        if (valueNames.length == values.length) {
            try {
                url = new URL(host);

                for (int i = 0; i < valueNames.length; i++) {
//                    System.out.println("valueNames["+i+"] "+valueNames[i]+" = "+values[i]);
                    //if different formats are available use the array
                    if (encodingFormats != null) {
                        if (encodingFormats != null)
                            format = encodingFormats[i];
                        else
                            format = UTF8;

                        if(!postData.equals("") && i == 0)
                            postData += " & ";

                        postData += " & ";
                        postData += URLEncoder.encode(valueNames[i], format) + "=" + URLEncoder.encode(values[i], format);
                        //otherwise use the single format arg
                    } else {
                        if (encodingFormat != null)
                            format = encodingFormat;
                        else
                            format = UTF8;


                        if(!postData.equals("") && i == 0)
                            postData += " & ";

                        postData += URLEncoder.encode(valueNames[i], format) + "=" + URLEncoder.encode(values[i], format);


                    }
                    // if another value is available add '&'
                    if ((i + 1) != valueNames.length) {
                        postData += " & ";
                    }
                }
            } catch (MalformedURLException mex) {
                mex.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
        } else {
            System.err.println("setPostValues() -> valueNames and values don't have the same length!");
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        System.out.println("Connection opened...");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer result) {

        System.out.println("onPostExecute():" + result);



        if(result == POST_SUCCESS) {
            System.out.println("Success. Clear Data." + result);
            isConnected = true;
            postData = "";
        }else{
            System.err.println("FAIL-> Resultcode: " + result);
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public boolean isConnected(){
       return isConnected;
    }

    @Override
    public int post() {
        if (postData != null || postData != "") {
            try {
                String[] pdata = postData.split(" & ");
                for(String pd:pdata)
                    System.out.println(pd);

                url = new URL(host);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(METHOD_POST);

                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();

                BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(os,UTF8));

                buff.write(getPostData());

                buff.flush();
                buff.close();
                os.close();

                InputStream is = conn.getInputStream();
                is.close();

                return POST_SUCCESS;

            } catch (MalformedURLException mex) {
                mex.printStackTrace();
                return INT_RETURN_NULL;
            } catch (org.apache.http.client.ClientProtocolException cpex) {
                cpex.printStackTrace();
                return INT_RETURN_NULL;
            } catch (IOException io) {
                io.printStackTrace();
                return INT_RETURN_NULL;
            }
        } else {
            System.err.println("Data for post() is empty or null!\n [POSTDATA: \n" + postData + "]");
            return INT_RETURN_NULL;
        }
    }

    @Override
    public int insert() {
        return INT_RETURN_NULL;
    }

    @Override
    public int update() {
        return INT_RETURN_NULL;
    }

    @Override
    public int delete() {
        return INT_RETURN_NULL;
    }

    private String getPostData(){
        if(postData != null)
            return postData;
        else {
            Toast.makeText(context, Errors.ERROR_POST_DATA_NOT_CREATED, Toast.LENGTH_LONG).show();
            return null;
        }
    }

    protected void activateConnectionTest(){
        identifier = CONNECTION_TEST;
    }

    protected void addPostValue(String valueName, String value, @Nullable String encodingFormat) {
        try {
            String format;

            if (encodingFormat != null)
                format = encodingFormat;
            else
                format = UTF8;

            if(postData == null)
                postData = "";

            if(postData.equals(""))
                postData = URLEncoder.encode(valueName, format) + "=" + URLEncoder.encode(value, format);
            else {
                postData += " & ";
                postData += URLEncoder.encode(valueName, format) + "=" + URLEncoder.encode(value, format);
            }
        }catch (UnsupportedEncodingException unex){
            unex.printStackTrace();
        }

    }

    protected String getHost(){
        return host;
    }
}
