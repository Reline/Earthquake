package com.natrelin.earthquake;

import android.app.ListFragment;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A placeholder fragment containing a simple view.
 */
public class EarthquakeListFragment extends ListFragment {
    //ArrayAdapter<Quake> aa;
    //.ArrayList<Quake> earthquakes = new ArrayList<>();

    private static final String TAG = "EARTHQUAKE";
    //private Handler handler = new Handler();

    SimpleCursorAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<>(getActivity(), layoutID, earthquakes);
        setListAdapter(aa);*/

        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
                new String[] {EarthquakeProvider.KEY_SUMMARY }, new int[] {android.R.id.text1 }, 0);
        setListAdapter(adapter);

        //getLoaderManager().initLoader(0, null, this);

        refreshEarthquakes();
    }

    public void refreshEarthquakes() {
        //getLoaderManager().restartLoader(0, null, EarthquakeListFragment.this);

        getActivity().stopService(new Intent(getActivity(), EarthquakeUpdateService.class));
        getActivity().startService(new Intent(getActivity(), EarthquakeUpdateService.class));

    }

    /*private void addNewQuake(Quake _quake) {
        ContentResolver cr = getActivity().getContentResolver();

        String w = EarthquakeProvider.KEY_DATE + "=" + _quake.getDate().getTime();

        Cursor query = cr.query(EarthquakeProvider.CONTENT_URI, null, w, null, null);
        if(query.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(EarthquakeProvider.KEY_DATE, _quake.getDate().getTime());
            values.put(EarthquakeProvider.KEY_DETAILS, _quake.getDetails());
            values.put(EarthquakeProvider.KEY_SUMMARY, _quake.toString());
            double lat = _quake.getLocation().getLatitude();
            double lng = _quake.getLocation().getLatitude();
            values.put(EarthquakeProvider.KEY_LOCATION_LAT, lat);
            values.put(EarthquakeProvider.KEY_LOCATION_LNG, lng);
            values.put(EarthquakeProvider.KEY_LINK, _quake.getLink());
            values.put(EarthquakeProvider.KEY_MAGNITUDE, _quake.getMagnitude());
            cr.insert(EarthquakeProvider.CONTENT_URI, values);
        }
        query.close();

        *//*Earthquake earthquakeActivity = (Earthquake) getActivity();
        if(_quake.getMagnitude() > earthquakeActivity.minimumMagnitude) {
            // add the new quake to our list of earthquakes
            earthquakes.add(_quake);
        }

        // notify the array adapter of a change
        aa.notifyDataSetChanged();*//*
    }*/
}
