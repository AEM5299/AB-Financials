package com.example.abfinancials;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http extends AsyncTask<String, String, Integer> {

    private Context mContext;

    public Http(Context context) {
        //Relevant Context should be provided to newly created components (whether application context or activity context)
        //getApplicationContext() - Returns the context for all activities running in application
        mContext = context.getApplicationContext();
    }

    //Execute this before the request is made
    @Override
    protected void onPreExecute() {
        // A toast provides simple feedback about an operation as popup.
        // It takes the application Context, the text message, and the duration for the toast as arguments
        Toast.makeText(mContext, "Going for the network call..", Toast.LENGTH_LONG).show();
    }

    //Perform the request in background
    @Override
    protected Integer doInBackground(String... params) {
        HttpURLConnection connection;
        try {
            //Open a new URL connection
            connection = (HttpURLConnection) new URL(params[0])
                    .openConnection();

            //Defines a HTTP request type
            connection.setRequestMethod("POST");

            //Sets headers: Content-Type, Authorization
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Authorization", "Token fab11c9b6bd4215a989c5bf57eb678");

            //Add POST data in JSON format
//            JSONObject jsonParam = new JSONObject();
//            try {
//                jsonParam.put("campaign_id", "4193");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            //Create a writer object and make the request
            OutputStreamWriter outputStream = new OutputStreamWriter(connection.getOutputStream());
//            outputStream.write(jsonParam.toString());
            outputStream.flush();
            outputStream.close();

            //Get the Response code for the request
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Run this once the background task returns.
    @Override
    protected void onPostExecute(Integer integer) {
        //Print the response code as toast popup
        Toast.makeText(mContext, "Response code: " + integer,
                Toast.LENGTH_LONG).show();

    }
}
