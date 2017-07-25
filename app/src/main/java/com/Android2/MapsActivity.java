package com.Android2;

// From https://github.com/googlemaps/android-samples/tree/master/ApiDemos

import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "requestingLocationUpdates";

    private MapController mapController;
    private Model model = new Model();

    private SoundPool soundPool;
    private int soundID = -1;
    private boolean loaded = false;
    private float volume = 1.f;
    private boolean ready = false;

    private boolean mRequestingLocationUpdates = false;
    private CustomLocationManager locationManager;
    private Marker myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup google maps view

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Setup toolbar as Action Bar

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Load sounds
        // TODO: Don't do this every time the activity is initialized, like when
        // rotating the view.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams (5)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            /*.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                            .setUsage(AudioAttributes.USAGE_GAME)*/
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build())
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
                playSound();
            }
        });
        soundID = soundPool.load(this, R.raw.blip, 1);

        // Geolocation

        locationManager = new CustomLocationManager(getApplicationContext(),
                new CustomLocationManager.OnLocationChangeListener() {
                    @Override
                    public void onLocationChange(Location location) {
                        model.myLocation.location = new LatLng(location.getLatitude(), location.getLongitude());
                        refreshMap();
                    }
                },
                new CustomLocationManager.OnConnectionChangeCallback() {
                    @Override
                    public void onConnected() {

                    }

                    @Override
                    public void onDisconnected() {

                    }
                },
                new CustomLocationManager.OnConnectionFailedCallback() {
                    @Override
                    public void onConnectionFailed() {

                    }
                });

        CustomLocationRequest customLocationRequest = new CustomLocationRequest();
        customLocationRequest.setPollInterval(2000);
        customLocationRequest.setLocationProvider(LocationManager.GPS_PROVIDER);
        customLocationRequest.setNumberOfUpdates(0);
        customLocationRequest.setMinDistanceForPoll(0);
        locationManager.requestLocation(this, customLocationRequest);

        updateValuesFromBundle(savedInstanceState);
    }

    private void refreshMap() {
        if (mapController != null) {
            mapController.refresh();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapController = new MapController(model);
        mapController.setup(googleMap);
        mapController.refresh();

        ready = true;
        playSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void startLocationUpdates() {
        locationManager.connect();
    }

    private void stopLocationUpdates() {
        locationManager.disconnect();
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        // Update the value of mRequestingLocationUpdates from the Bundle.
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
        } else {
            mRequestingLocationUpdates = true;
        }
        // ...

        // Update UI to match restored state
        //updateUI();
    }

    private void playSound() {
        if (loaded && ready) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            ready = false; //< kludge to avoid playing twice
        }
    }
}