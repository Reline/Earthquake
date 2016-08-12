package com.natrelin.earthquake;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
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
}
