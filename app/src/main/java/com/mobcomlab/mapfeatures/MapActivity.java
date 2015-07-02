package com.mobcomlab.mapfeatures;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.managers.WebServiceCallbackListener;
import com.mobcomlab.mapfeatures.managers.WebServiceManager;
import com.mobcomlab.mapfeatures.models.Coordinate;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

import io.realm.RealmList;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, WebServiceCallbackListener {

    String layerId;
    private GoogleMap map;
    LatLngBounds.Builder bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the layer id
        layerId = getIntent().getStringExtra("layerId");

        // Setup map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);

        refreshMap();

        // Call web service to get features
        new WebServiceManager(this, this).requestFeatures(layerId);
    }

    @Override
    public void onWebServiceCallback() {
        refreshMap();
    }

    private void refreshMap() {
        map.clear();
        bounds = new LatLngBounds.Builder();

        // Add all the features
        Layer layer = new DatabaseManager(this).getLayer(layerId);
        for (int i = 0; i < layer.getFeatures().size(); i++) {
            Feature feature = layer.getFeatures().get(i);
            drawMapOverlay(feature.getType(), feature.getCoordinates());
        }

        // Zoom to bounds
        if (layer.getFeatures().size() > 0) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds.build(), 30);
            map.animateCamera(cu);
        }
    }

    private void drawMapOverlay(String featureType, List<Coordinate> coordinates) {
        if (featureType.equalsIgnoreCase("Point")) {
            drawMarkers(coordinates);
        }
        else if (featureType.equalsIgnoreCase("MultiLineString")) {
            drawPolyline(coordinates);
        }
        else if (featureType.equalsIgnoreCase("MultiPolygon")) {
            drawPolygon(coordinates);
        }
    }

    private void drawMarkers(List<Coordinate> coordinates) {
        // Add markers to map
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(coord.getLatitude(), coord.getLongitude()));
            bounds.include(marker.getPosition());
            map.addMarker(marker);
        }
    }


    private void drawPolyline(List<Coordinate> coordinates) {
        // Add lines to map
        PolylineOptions line = new PolylineOptions();
        line.color(Color.RED);
        line.width(5.0f);

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            LatLng latLng = new LatLng(coord.getLatitude(), coord.getLongitude());
            bounds.include(latLng);
            line.add(latLng);
        }
        map.addPolyline(line);
    }

    private void drawPolygon(List<Coordinate> coordinates) {
        // Add polygon to map
        PolygonOptions polygon = new PolygonOptions();
        polygon.strokeColor(Color.RED);
        polygon.strokeWidth(5.0f);
        polygon.fillColor(Color.argb(100,255,0,0));

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            LatLng latLng = new LatLng(coord.getLatitude(), coord.getLongitude());
            bounds.include(latLng);
            polygon.add(latLng);
        }
        map.addPolygon(polygon);
    }


    @Override
    public void onWebServiceFailed() {

    }
}
