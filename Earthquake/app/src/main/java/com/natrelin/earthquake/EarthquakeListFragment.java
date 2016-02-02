package com.natrelin.earthquake;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class EarthquakeListFragment extends ListFragment {

    SimpleCursorAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
                new String[] {EarthquakeProvider.KEY_SUMMARY }, new int[] {android.R.id.text1 }, 0);
        setListAdapter(adapter);

        refreshEarthquakes();
    }

    public void refreshEarthquakes() {
        getActivity().stopService(new Intent(getActivity(), EarthquakeUpdateService.class));
        getActivity().startService(new Intent(getActivity(), EarthquakeUpdateService.class));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: 2/2/2016 change results to current input, this might not be the right function

    }
}
