package com.mobcomlab.mapfeatures;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SimpleMapOverlayActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_map_overlay);

        // Setup Google Maps
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_map_overlay, menu);
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
        map = googleMap;
        // Set center
        LatLng trainingCenter = new LatLng(16.61082729468525, 99.96323487022345);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(trainingCenter, 15));

        // Enable my location
        map.setMyLocationEnabled(true);

        // Enable zoom control
        map.getUiSettings().setZoomControlsEnabled(true);

        // Set map view type
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        addPolyline();

        addPolygone();
    }

    private void addPolyline() {
        PolylineOptions line1 = new PolylineOptions();
        line1.color(Color.RED);
        line1.width(2.f);
        line1.add(new LatLng(16.614405, 99.960081));
        line1.add(new LatLng(16.610539, 99.966797));
        line1.add(new LatLng(16.607476, 99.967398));
        line1.add(new LatLng(16.606941, 99.961776));
        map.addPolyline(line1);
    }

    private void addPolygone() {
        PolygonOptions shape1 = new PolygonOptions();
        shape1.strokeColor(Color.BLUE);
        shape1.strokeWidth(2.f);
        shape1.fillColor(Color.argb(100, 0, 0, 255));
        shape1.add(new LatLng(16.610231, 99.954909));
        shape1.add(new LatLng(16.608216, 99.958278));
        shape1.add(new LatLng(16.607270, 99.954416));
//        shape1.add(new LatLng(16.610231, 99.954909));
        map.addPolygon(shape1);

    }

}
