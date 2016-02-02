package com.natrelin.earthquake;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class EarthquakeSearchResults extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "EarthquakeSearchResults";
    private SimpleCursorAdapter adapter;
    private static String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_search_results);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[] { EarthquakeProvider.KEY_SUMMARY },
                new int[] { android.R.id.text1 }, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
        parseIntent(getIntent());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String query = "0";
        if (args != null) {
            query = args.getString(QUERY_EXTRA_KEY);
        }

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String[] projection = { EarthquakeProvider.KEY_ID, EarthquakeProvider.KEY_SUMMARY };
        String where = EarthquakeProvider.KEY_SUMMARY + " LIKE '%" + query + "%' AND " +
                EarthquakeProvider.KEY_MAGNITUDE + " >= " +
                Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));
        String sortOrder = EarthquakeProvider.KEY_SUMMARY + " COLLATE LOCALIZED ASC";

        return new CursorLoader(this, EarthquakeProvider.CONTENT_URI, projection, where, null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, searchQuery);

            Bundle args = new Bundle();
            args.putString(QUERY_EXTRA_KEY, searchQuery);
            getLoaderManager().restartLoader(0, args, this);
        }
    }
}
