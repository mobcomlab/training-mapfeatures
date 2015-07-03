package com.mobcomlab.mapfeatures;

import android.content.Intent;
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
import com.google.android.gms.maps.model.Marker;
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
import java.util.Map;

import io.realm.RealmList;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, WebServiceCallbackListener {

    String layerId;
    Layer layer;
    private GoogleMap map;
    LatLngBounds.Builder bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the layer id
        layerId = getIntent().getStringExtra("layerId");
        layer = new DatabaseManager(this).getLayer(layerId);

        // Setup action bar
        getSupportActionBar().setTitle(layer.getName());

        // Setup map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapActivity.this, PropertiesActivity.class);
                intent.putExtra("featureId", marker.getTitle());
                startActivity(intent);
            }
        });

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
        for (int i = 0; i < layer.getFeatures().size(); i++) {
            Feature feature = layer.getFeatures().get(i);
            drawMapOverlay(feature);
        }

        // Zoom to bounds
        if (layer.getFeatures().size() > 0) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds.build(), 30);
            map.animateCamera(cu);
        }
    }

    private void drawMapOverlay(Feature feature) {
        if (feature.getType().equalsIgnoreCase("Point")) {
            String snippet = String.format("%d properties", feature.getProperties().size());
            drawMarkers(feature.getId(), snippet, feature.getCoordinates());
        }
        else if (feature.getType().equalsIgnoreCase("MultiLineString")) {
            drawPolyline(feature.getCoordinates());
        }
        else if (feature.getType().equalsIgnoreCase("MultiPolygon")) {
            drawPolygon(feature.getCoordinates());
        }
    }

    private void drawMarkers(String title, String snippet, List<Coordinate> coordinates) {
        // Add markers to map
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            MarkerOptions marker = new MarkerOptions();
            marker.title(title);
            marker.snippet(snippet);
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
