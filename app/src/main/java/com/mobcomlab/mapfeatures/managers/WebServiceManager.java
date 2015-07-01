package com.mobcomlab.mapfeatures.managers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WebServiceManager {

    private final static String FEATURES_URL = "http://www2.cgistln.nu.ac.th/geoserver/plkwater/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=%s&maxFeatures=50&outputFormat=json";

    final Context context;
    final WebServiceCallbackListener listener;


    public WebServiceManager(Context context, WebServiceCallbackListener listener) {
        super();
        this.context = context;
        this.listener = listener;
    }

    public void requestLayer(final String layerId) {
        String url = String.format(FEATURES_URL, layerId);
        Log.i("url", url);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        handleLayerResponse(layerId, response);
                        listener.onWebServiceCallback();

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onWebServiceFailed();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void handleLayerResponse(String layerId, JSONObject response) {
        DatabaseManager databaseManager = new DatabaseManager(context);

        ArrayList<Feature> features = new ArrayList<>();

        try {
            JSONArray featuresRaw = response.getJSONArray("features");
            for (int i = 0; i < featuresRaw.length(); i++) {

                JSONObject featureRaw = featuresRaw.getJSONObject(i);
                String id = featureRaw.getString("id");

                JSONObject geometryRaw = featureRaw.getJSONObject("geometry");
                String geometryType = geometryRaw.getString("type");

                JSONArray coordinatesRaw = new JSONArray();
                if (geometryType.equals("MultiLineString")) {
                    coordinatesRaw = geometryRaw.getJSONArray("coordinates").getJSONArray(0);
                } else if (geometryType.equals("MultiPolygon")) {
                    coordinatesRaw = geometryRaw.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
                } else if (geometryType.equals("Point")) {
                    coordinatesRaw = new JSONArray(String.format("[%s]", geometryRaw.getJSONArray("coordinates").toString()));
                }
                ArrayList<Pair<Double,Double>> coordinates = new ArrayList<>();
                for (int j = 0; j < coordinatesRaw.length(); j++) {
                    JSONArray coordRaw = coordinatesRaw.getJSONArray(j);
                    if (coordRaw.length() == 2) {
                        coordinates.add(new Pair<>(coordRaw.getDouble(0), coordRaw.getDouble(1)));
                    }
                }

                Feature feature = databaseManager.createOrUpdateFeature(id, geometryType, coordinates);
                features.add(feature);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        databaseManager.setFeatures(layerId, features);
    }

}
