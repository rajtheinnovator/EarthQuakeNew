package com.example.android.earthquakenew;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        EarthquakeAsyncTask downloadFilesTask = new EarthquakeAsyncTask();
        downloadFilesTask.execute(USGS_REQUEST_URL);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        protected ArrayList<Earthquake> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            // Create a fake list of earthquakes.
            final ArrayList<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);

            // Update the information displayed to the user.
            return earthquakes;
        }

        protected void onPostExecute(final ArrayList<Earthquake> earthquakes) {
            // If there is no result, do nothing.
            if (earthquakes == null) {
                return;
            }

            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.earth_quake_list);

            // Create a new {@link ArrayAdapter} of earthquakes
            EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(earthquakeAdapter);
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Earthquake earthquake = earthquakes.get(position);
                    Intent goToUrl = new Intent(Intent.ACTION_VIEW);
                    goToUrl.setData(Uri.parse(earthquake.getUrl()));
                    startActivity(goToUrl);
                }
            });
        }
    }
}