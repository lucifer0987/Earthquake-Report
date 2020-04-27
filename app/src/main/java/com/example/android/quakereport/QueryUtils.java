package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private QueryUtils() {
    }

    private static URL createUrl(String url){
        URL urlc = null;
        try {
            urlc = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlc;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return null;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try{
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.setConnectTimeout(50000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInput(inputStream);
            }else{
                Log.e("this", String.valueOf(httpURLConnection.getResponseCode()));
            }

        }catch (IOException e){
            Log.e("this", String.valueOf(e));
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromInput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            String line = buffer.readLine();
            while(line != null){
                output.append(line);
                line = buffer.readLine();
            }
        }
        return output.toString();
    }


    private static List<earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<earthquake> earthquakes = new ArrayList<>();
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray earthquakeArrays = root.getJSONArray("features");

            String place;
            Long time;
            Double mag;
            String url;
            for(int i = 0; i < earthquakeArrays.length(); i++){
                JSONObject earthquake = earthquakeArrays.getJSONObject(i);
                JSONObject properties = earthquake.getJSONObject("properties");

                place = properties.getString("place");
                mag = properties.getDouble("mag");
                time = properties.getLong("time");
                url = properties.getString("url");

                earthquake currentObject = new earthquake(mag, place, time, url);
                earthquakes.add(currentObject);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("this", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<earthquake> fetchEarthquakeData(String url){
        URL urlc = createUrl(url);
        String response = null;
        try {
            response = makeHttpRequest(urlc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<earthquake> earthquakes = extractEarthquakes(response);
        return earthquakes;
    }

}
