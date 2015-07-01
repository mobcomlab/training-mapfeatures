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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.managers.WebServiceCallbackListener;
import com.mobcomlab.mapfeatures.managers.WebServiceManager;
import com.mobcomlab.mapfeatures.models.Coordinate;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import org.json.JSONArray;

import java.util.List;

import io.realm.RealmList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String layerId;
    private GoogleMap map;

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

        layerId = getIntent().getStringExtra("layerId");

    }

    private void populateFeatures() {
        WebServiceManager layerRequest = new WebServiceManager(this, new WebServiceCallbackListener() {
            @Override
            public void onWebServiceCallback() {
                Layer layer = new DatabaseManager(MapActivity.this).getLayer(layerId);
                List<Feature> features = new DatabaseManager(MapActivity.this).getLayer(layerId).getFeatures();
                for (int i = 0; i < features.size(); i++) {
                    Log.d("onWebServiceCallback", "i = " + i);
                    Feature featureTest = features.get(i);
                    if (featureTest.getCoordinates().size() > 0) {
                        Log.d("onWebServiceCallback", "coordinates size = " + featureTest.getCoordinates().size());
                        Coordinate coordTest = featureTest.getCoordinates().first();
                        Log.d("onWebServiceCallback", "coordinate = " + coordTest.getLatitude() + "," + coordTest.getLongitude());

                        drawMapOverlay(featureTest.getCoordinates());
                    }
                }

            }

            @Override
            public void onWebServiceFailed() {

            }
        });
        layerRequest.requestLayer(layerId);
    }

    private void drawMapOverlay(RealmList<Coordinate> coordinates) {
        Layer layer = new DatabaseManager(MapActivity.this).getLayer(layerId);
        String geometryType = layer.getFeatureType();
        if (geometryType.equals("MultiLineString")) {
            drawPolylines(coordinates);
        } else if (geometryType.equals("MultiPolygon")) {
            drawPolygones(coordinates);
        } else if (geometryType.equals("Point")) {
            drawPoints(coordinates);
        }
    }

    private void drawPoints(RealmList<Coordinate> coordinates) {
        MarkerOptions marker = new MarkerOptions();
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        marker.position(new LatLng(coordinates.get(0).getLatitude(), coordinates.get(0).getLongitude()));
        map.addMarker(marker);
    }

    private void drawPolylines(RealmList<Coordinate> coordinates) {

        PolylineOptions line = new PolylineOptions();
        line.color(Color.RED);
        line.width(4.f);

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coor = coordinates.get(i);
            line.add(new LatLng(coor.getLatitude(), coor.getLongitude()));
        }

        map.addPolyline(line);
    }

    private void drawPolygones(RealmList<Coordinate> coordinates) {
        PolygonOptions shape = new PolygonOptions();
        shape.strokeColor(Color.parseColor("#dddddd"));
        shape.strokeWidth(4.f);
        shape.fillColor(Color.parseColor("#aaaaaa"));

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coor = coordinates.get(i);
            shape.add(new LatLng(coor.getLatitude(), coor.getLongitude()));
        }

        map.addPolygon(shape);
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
        map = googleMap;

        LatLng trainingCenter = new LatLng(16.624413,99.90661);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(trainingCenter, 9));

        // Enable my location
        map.setMyLocationEnabled(true);

        // Enable zoom control
        map.getUiSettings().setZoomControlsEnabled(true);

        // Set map view type
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        Layer layer = new DatabaseManager(MapActivity.this).getLayer(layerId);
        List<Feature> features = layer.getFeatures();
        if (features == null || features.size() == 0) {
            populateFeatures();
        } else {
            for (int i = 0; i < features.size(); i++) {
                Feature featureTest = features.get(i);
                if (featureTest.getCoordinates().size() > 0) {
                    drawMapOverlay(featureTest.getCoordinates());
                }
            }
        }

    }
}
