package com.thesaan.gameengine.android.database;

import android.content.Context;
import android.os.Handler;

import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.thread.TaskManager;

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
public class WebDatabase implements DB_Settings {

    private String username, password;
    private Context context;

    TaskManager taskManager;

    /**
     *Contains the result of the GET_ request
     */

    public String getResult;

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


    //the data for the connection
    private String postData = null;

    //php file link to the file with the insert method
    protected String insertLink;

    /**
     * Constructor without link
     *
     * @param context
     */
    public WebDatabase(Context context) {
        this.context = context;
    }

    /**
     * Constructor with link
     *
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

    public void setInsertLink(String link) {
        this.insertLink = link;
    }


    public void connect() {
        try {
            URLConnection conn = url.openConnection();

            if (conn.getURL() != null && conn.getURL().toString() != "") {
                isConnected = true;
            } else {
                failedToConnect = true;
                isConnected = false;
            }
        } catch (IOException io) {

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

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
