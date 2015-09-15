package com.thesaan.gameengine.android.database;

import android.content.Context;

import com.thesaan.gameengine.android.DB_Settings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Michael on 28.07.2015.
 */
public class WebDatabase implements DB_Settings{

    private String username, password;
    private Context context;


    /**
     * Test for connection to database.
     * If the server is not available, use
     * the offline database of the app
     */

    private boolean isOnline = false;
    private boolean failedToConnect = false;
    private boolean isConnectionAvailable = false;
    private boolean isConnected = false;

    protected boolean isDataEntered = false;

    private String link;

    private URL url;

    private String client;

    public final static String HTTP = "http://";
    public final static String HTTPS = "https://";
    public final static String FTP = "ftp://";

    //the data for the connection
    private String postData = null;

    //php file link to the file with the insert method
    protected String insertLink;

    /**
     * Constructor without link
     * @param context
     */
    public WebDatabase(Context context) {
        this.context = context;
    }

    /**
     * Constructor with link
     * @param context
     * @param link
     */
    public WebDatabase(Context context, String link) {
        this.context = context;
        this.link = link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setInsertLink(String link){
        this.insertLink = link;
    }

    public String get() {

        try {
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));

            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            System.out.print(in.toString());

            return in.toString();

        } catch (MalformedURLException mex) {
            mex.printStackTrace();
            return null;
        } catch (org.apache.http.client.ClientProtocolException cpex) {
            cpex.printStackTrace();
            return null;
        } catch (URISyntaxException uriex) {
            uriex.printStackTrace();
            return null;
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }
    }

    public void setPostValues(String link, String[] valueNames, String[] values){
        //the check is in post()
        this.link = link;
        if(valueNames.length == values.length) {
            try {
                url = new URL(link);

                for (int i = 0; i < valueNames.length; i++) {
                    postData += URLEncoder.encode(valueNames[i], "UTF-8") + "=" + URLEncoder.encode(values[i], "UTF-8");

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
        }else{
            System.err.println("setPostValues() -> valueNames and values don't have the same length!");
        }
    }

    public void connect(){
        try {
            URLConnection conn = url.openConnection();

            if(conn.getURL() != null && conn.getURL().toString() != ""){
                isConnected = true;
            }else{
                failedToConnect = true;
                isConnected = false;
            }
        }catch(IOException io){

            isConnected = false;
            failedToConnect = true;
            System.err.println(io);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isConnectionAvailable() {
        return isConnectionAvailable;
    }

    public boolean isFailedToConnect() {
        return failedToConnect;
    }

    public boolean isOnline() {
        return isOnline;
    }


    /**
     * Works the same as the $_POST[] method in php.
     * Before calling this method, make sure you called {@linksetPostValues(String link, String[] valueNames, String[] values)}
     * @return
     * String of reader.readLine()
     */
    public String post() {
        if(postData != null && postData != "") {
            try {
                URLConnection conn = url.openConnection();

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(postData);

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                System.out.print(reader.toString());

                return reader.readLine();


            } catch (MalformedURLException mex) {
                mex.printStackTrace();
                return null;
            } catch (org.apache.http.client.ClientProtocolException cpex) {
                cpex.printStackTrace();
                return null;
            } catch (IOException io) {
                io.printStackTrace();
                return null;
            }
        }else{
            System.err.println("Data for post() is empty or null!\n [POSTDATA: \n"+postData+"]");
            return null;
        }
    }

    public void insert(List<NameValuePair> pairs, String table){
        InputStream is = null;
        try{
            //default http client
            HttpClient httpClient = new DefaultHttpClient();
            if(insertLink == "" || insertLink == null)
                insertLink = HTTP + DB_Settings.HOST +"/beyond-universe/rec_in.php";

            //http post method
            HttpPost post = new HttpPost(insertLink);

            //store the size of the pairs for looping in php
            pairs.add(new BasicNameValuePair("amountOfPairs",Integer.toString(pairs.size())));
            pairs.add(new BasicNameValuePair("table",table));

            //passing the values
            post.setEntity(new UrlEncodedFormEntity(pairs));

            //getting the response
            HttpResponse response = httpClient.execute(post);

            //setting up the entity

            HttpEntity entity = response.getEntity();

            //Setting up the content
            is = entity.getContent();

            System.out.println("Inserted Content: \n"+is.toString());
            //Display toast
            isDataEntered = true;


        }catch (ClientProtocolException e1){
            System.err.println("Connection failed:\n");
            e1.printStackTrace();
        }catch (IOException e2){
            e2.printStackTrace();
        }
    }

    public void login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
