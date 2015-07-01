package com.mobcomlab.mapfeatures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mobcomlab.mapfeatures.managers.WebServiceCallbackListener;
import com.mobcomlab.mapfeatures.managers.WebServiceManager;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Setup Google Maps
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() == null) {
            finish();
        }

        String layerId = getIntent().getStringExtra("layerId");

        WebServiceManager layerRequest = new WebServiceManager(this, new WebServiceCallbackListener() {
            @Override
            public void onWebServiceCallback() {

            }

            @Override
            public void onWebServiceFailed() {

            }
        });
        layerRequest.requestLayer(layerId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set center
        LatLng trainingCenter = new LatLng(17.036753, 100.587073);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trainingCenter, 15));

        // Enable my location
        googleMap.setMyLocationEnabled(true);

        // Enable zoom control
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Set map view type
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
}
