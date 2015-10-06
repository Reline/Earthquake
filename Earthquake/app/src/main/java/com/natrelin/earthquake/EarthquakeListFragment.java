package com.natrelin.earthquake;

import android.app.ListFragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    ArrayAdapter<Quake> aa;
    ArrayList<Quake> earthquakes = new ArrayList<Quake>();

    private static final String TAG = "EARTHQUAKE";
    private Handler handler = new Handler();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<Quake>(getActivity(), layoutID, earthquakes);
        setListAdapter(aa);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshEarthquakes();
            }
        });
        t.start();
    }

    public void refreshEarthquakes() {
        Log.d(TAG, "Refreshing quakes");
        // Get the XML
        try {
            String quakeFeed = getString(R.string.quake_feed);
            URL url = new URL(quakeFeed);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            int responseCode = httpConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                // parse the earthquake feed
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();

                // clear the old earthquakes
                earthquakes.clear();

                // get a list of each earthquake entry
                NodeList nl = docEle.getElementsByTagName("event");

                if (nl != null && nl.getLength() > 0) { // nl.getLength() always equals 0
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element entry = (Element) nl.item(i);

                        // origin holds link, date, and location
                        Element origin = (Element) entry.getElementsByTagName("origin").item(0);

                        // date
                        Element time = (Element) origin.getElementsByTagName("time").item(0);
                        Element timeVal = (Element) time.getElementsByTagName("value").item(0);
                        String dt = timeVal.getFirstChild().getNodeValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); // ex. "2015-10-06T09:50:54.270Z"
                        Date qdate = new GregorianCalendar(0,0,0).getTime();
                        try {
                            qdate = sdf.parse(dt);
                        } catch (ParseException e) {
                            Log.d(TAG, "Date parsing exception.", e);
                        }

                        // details
                        Element description = (Element) entry.getElementsByTagName("description").item(0);
                        Element text = (Element)description.getElementsByTagName("text").item(0);
                        String details = text.getFirstChild().getNodeValue();

                        // location
                        Element longitude = (Element) origin.getElementsByTagName("longitude").item(0);
                        Element lonValue = (Element) longitude.getElementsByTagName("value").item(0);
                        String lon = lonValue.getFirstChild().getNodeValue();

                        Element latitude = (Element) origin.getElementsByTagName("latitude").item(0);
                        Element latValue = (Element) latitude.getElementsByTagName("value").item(0);
                        String lat = latValue.getFirstChild().getNodeValue();

                        Location l = new Location("dummyGPS");
                        l.setLatitude(Double.parseDouble(lat));
                        l.setLongitude(Double.parseDouble(lon));

                        // magnitude
                        Element magnitude = (Element) entry.getElementsByTagName("magnitude").item(0);
                        Element mag = (Element) magnitude.getElementsByTagName("mag").item(0);
                        Element magValue = (Element) mag.getElementsByTagName("value").item(0);
                        double mgtd = Double.parseDouble(magValue.getFirstChild().getNodeValue());

                        // link
                        String hostname = "http://earthquake.usgs.gov";
                        String linkString = hostname + origin.getAttribute("publicID").substring(8);

                        final Quake quake = new Quake(qdate, details, l, mgtd, linkString);

                        // process a newly found earthquake
                        handler.post(new Runnable() {
                            public void run() {
                                addNewQuake(quake);
                            }
                        });
                    }
                }
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed URL Exception");
        } catch (IOException e) {
            Log.d(TAG, "IO Exception");
        } catch (ParserConfigurationException e) {
            Log.d(TAG, "Parser Configuration Exception");
        } catch (SAXException e) {
            Log.d(TAG, "SAX Exception");
        }
    }

    private void addNewQuake(Quake _quake) {
        // add the new quake to our list of earthquakes
        earthquakes.add(_quake);

        // notify the array adapter of a change
        aa.notifyDataSetChanged();
    }
}
