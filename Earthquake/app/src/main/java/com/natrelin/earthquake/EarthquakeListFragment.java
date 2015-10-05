package com.natrelin.earthquake;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A placeholder fragment containing a simple view.
 */
public class EarthquakeListFragment extends ListFragment {
    ArrayAdapter<Quake> aa;
    ArrayList<Quake> earthquakes = new ArrayList<Quake>();

    private static final String TAG = "EARTHQUAKE";
    private Handler handler = new Handler() {
        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord record) {

        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<Quake>(getActivity(), layoutID, earthquakes);
        setListAdapter(aa);
    }

    public EarthquakeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_earthquake_list_fragment, container, false);
    }

    public void refreshEarthquakes() {

    }
}
