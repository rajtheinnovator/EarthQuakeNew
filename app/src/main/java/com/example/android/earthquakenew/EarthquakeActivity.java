package com.example.android.earthquakenew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Earthquake>> {
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    ListView earthquakeListView;

    EarthquakeAdapter earthquakeAdapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";

    //private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);
        //First of all if network is connected or not then only start the laoder
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            /*
            fetch data
            Get a reference to the LoaderManager, in order to interact with loaders.
            */
            /*
        Initialize the loader. Pass in the int ID constant defined above and pass in null for
        the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        because this activity implements the LoaderCallbacks interface).
        */
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // display error
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            // Find a reference to the {@link ListView} in the layout
            earthquakeListView = (ListView) findViewById(R.id.earth_quake_list);
            earthquakeListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText(R.string.no_internet);
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }
    }
    //Show the menu in ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, final ArrayList<Earthquake> earthquakes) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        // earthquakeAdapter.clear();
        // If there is no result, do nothing.
        Log.v(LOG_TAG, "You have got a onLoadFinished");
        if (earthquakes == null) {
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText(R.string.no_earthquakes);
            return;
        }

        // Create a new {@link ArrayAdapter} of earthquakes
        earthquakeAdapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.earth_quake_list);
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

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG, "You have got a onLoaderReset");
        // Loader reset, so we can clear out our existing data.
        earthquakeAdapter.clear();
    }
}