package com.natrelin.earthquake;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mukondono on 10/19/15.
 */
public class EarthquakeUpdateService extends IntentService {

    public static String TAG = "EARTHQUAKE UPDATE SERVICE";
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    public EarthquakeUpdateService() {
        super("EarthquakeUpdateService");
    }

    public EarthquakeUpdateService(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int updateFreq = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ,
                "60"));
        boolean updateAutoChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

        if(updateAutoChecked) {
            int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long timeToRefresh = SystemClock.elapsedRealtime() + updateFreq*60*1000;
            alarmManager.setInexactRepeating(alarmType, timeToRefresh, updateFreq*60*1000,
                    alarmIntent);
        } else {
            alarmManager.cancel(alarmIntent);
        }

        refreshEarthquakes();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        String ALARM_ACTION = EarthquakeAlarmReceiver.ACTION_REFRESH_EARTHQUAKE_ALARM;
        Intent intentToFire = new Intent(ALARM_ACTION);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
    }

    private void refreshEarthquakes() {
        URL url;
        try {
            String quakeFeed = getString(R.string.quake_feed);
            url = new URL(quakeFeed);

            URLConnection connection;
            connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();

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

                        Quake quake = new Quake(qdate, details, l, mgtd, linkString);

                        addNewQuake(quake);
                    }
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void addNewQuake(Quake _quake) {
        ContentResolver cr = getContentResolver();

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

        /*Earthquake earthquakeActivity = (Earthquake) getActivity();
        if(_quake.getMagnitude() > earthquakeActivity.minimumMagnitude) {
            // add the new quake to our list of earthquakes
            earthquakes.add(_quake);
        }

        // notify the array adapter of a change
        aa.notifyDataSetChanged();*/
    }
}
