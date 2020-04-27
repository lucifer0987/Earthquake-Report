/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;

public class EarthquakeActivity extends AppCompatActivity {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=30";

    private earthquakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter = new earthquakeAdapter(this, new ArrayList<earthquake>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                earthquake currentEarthquake = mAdapter.getItem(i);
                Uri url = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(websiteIntent);
            }
        });

        new earthquakeAsyncTask().execute(USGS_REQUEST_URL);
    }

    private class earthquakeAsyncTask extends AsyncTask<String, Void, List<earthquake>> {

        @Override
        protected List<earthquake> doInBackground(String... urls) {
            if(urls.length < 1 || urls[0] == null){
                return null;
            }
            return QueryUtils.fetchEarthquakeData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<earthquake> earthquakes) {
            mAdapter.clear();

            if(earthquakes != null && !earthquakes.isEmpty()){
                mAdapter.addAll(earthquakes);
            }
        }
    }

}
