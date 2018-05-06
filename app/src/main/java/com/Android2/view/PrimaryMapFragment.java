package com.Android2.view;

// From https://github.com/googlemaps/android-samples/tree/master/ApiDemos

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Android2.controller.IHasActionBarItems;
import com.Android2.R;
import com.Android2.controller.PrimaryMapController;
import com.Android2.model.MapArea;
import com.Android2.model.CustomLocationManager;
import com.Android2.model.CustomLocationRequest;
import com.Android2.model.POIMapNode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

//public class PrimaryMapFragment extends FragmentActivity implements OnMapReadyCallback {
//public class PrimaryMapFragment extends AppCompatActivity implements OnMapReadyCallback, PrimaryMapController.OnSelectionChangeListener {
public class PrimaryMapFragment extends Fragment implements OnMapReadyCallback, PrimaryMapController.OnSelectionChangeListener, IHasActionBarItems {

    public static final int REQUEST_CODE_SCAN = 1;

    private static final String BUNDLE_REQUESTING_LOCATION_UPDATES_KEY = "requestingLocationUpdates";
    private static final String BUNDLE_MODEL_KEY = "model";

    private PrimaryMapController mapController;
    private MapArea model = new MapArea();

    private SoundPool soundPool;
    private int soundID = -1;
    private boolean loaded = false;
    private float volume = .025f;
    private boolean ready = false;

    private boolean mRequestingLocationUpdates = false;
    private CustomLocationManager locationManager;

    private FragmentActivity hostActivity;
    private OnActionBarItemEnabledChangedListener enabledChangedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Setup toolbar as Action Bar

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/

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
        soundID = soundPool.load(hostActivity, R.raw.blip, 1);

        // Geolocation

        locationManager = new CustomLocationManager(hostActivity.getApplicationContext(),
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

        updateValuesFromBundle(savedInstanceState);

        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup google maps view
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

        /*SupportMapFragment mapFragment = (SupportMapFragment) hostActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }*/
        if (childFragment instanceof SupportMapFragment) {
            ((SupportMapFragment)childFragment).getMapAsync(this);
        }
    }

    private void refreshMap() {
        if (mapController != null) {
            mapController.refresh();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapController = new PrimaryMapController(model);
        mapController.setup(googleMap);
        mapController.setOnSelectionChanged(this);
        mapController.refresh();

        ready = true;
        playSound();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(BUNDLE_REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        outState.putParcelable(BUNDLE_MODEL_KEY, model);
        super.onSaveInstanceState(outState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(BUNDLE_REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(BUNDLE_REQUESTING_LOCATION_UPDATES_KEY);
            } else {
                mRequestingLocationUpdates = true;
            }

            if (savedInstanceState.keySet().contains(BUNDLE_MODEL_KEY)) {
                model = savedInstanceState.getParcelable(BUNDLE_MODEL_KEY);
            } else {
                model = new MapArea();
            }
        }
    }

    @Override
    public void onSelectionChanged() {
        if (enabledChangedListener != null) {
            enabledChangedListener.onActionBarItemEnabledChanged();
        }
    }

    @Override
    public void onDetach() {
        if (enabledChangedListener != null) {
            enabledChangedListener.onHasActionBarItemsDetach();
            enabledChangedListener = null;
        }

        super.onDetach();
    }

    void beginScanQRCode() {
        Intent intent = new Intent(hostActivity, QRScannerActivity.class);
        //String message = editText.getText().toString();
        //intent.putExtra(QR_EXTRA_MESSAGE_KEY, message);
        startActivityForResult(intent, REQUEST_CODE_SCAN);

        /*
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE" for bar codes
            intent.

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
        */

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == RESULT_OK && data != null) {
                String contents = data.getStringExtra(QRScannerActivity.QR_EXTRA_MESSAGE_KEY);
                if (mapController != null && mapController.selectedNode != null && mapController.selectedNode.getNode() instanceof POIMapNode) {
                    ((POIMapNode)mapController.selectedNode.getNode()).qrCode = contents;
                    refreshMap();
                }
            } else {
                if (resultCode == RESULT_CANCELED) {
                    //handle cancel
                } else {
                    //handle other weird cases
                }
            }
        }
    }

    private void startLocationUpdates() {
        Context context = hostActivity.getApplicationContext();
        locationManager.connect(context);

        CustomLocationRequest customLocationRequest = new CustomLocationRequest();
        customLocationRequest.setPollInterval(2000);
        customLocationRequest.setLocationProvider(LocationManager.GPS_PROVIDER);
        customLocationRequest.setNumberOfUpdates(0);
        customLocationRequest.setMinDistanceForPoll(0);

        locationManager.requestLocation(context, customLocationRequest);

    }

    private void stopLocationUpdates() {
        //if (locationManager != null) {
            locationManager.disconnect();
        //}
    }

    private void playSound() {
        if (loaded && ready) {

            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            ready = false; //< kludge to avoid playing twice
        }
    }

    @Override
    public void setOnActionBarItemEnabledEnabledChanged(OnActionBarItemEnabledChangedListener listener) {
        enabledChangedListener = listener;
    }

    @Override
    public boolean isActionBarItemEnabled(int itemId) {
        switch (itemId) {
            case R.id.action_delete:
                return mapController != null && mapController.canRemoveSelectedNode();
            case R.id.action_scan_code:
                return mapController != null && mapController.selectedNode != null; //< todo: scannable thingy
            default:
                return false;
        }
    }

    @Override
    public int[] getOwnedActionBarItemIds() {
        return new int[] {
            R.id.action_delete,
                R.id.action_scan_code
        };
    }

    @Override
    public boolean onActionBarItemSelected(int itemId) {
        switch (itemId) {
            case R.id.action_scan_code:
                beginScanQRCode();
                return true;

            case R.id.action_delete:
                if (mapController != null) {
                    mapController.removeSelectedNode();
                }
                return true;
        }
        return false;
    }
}