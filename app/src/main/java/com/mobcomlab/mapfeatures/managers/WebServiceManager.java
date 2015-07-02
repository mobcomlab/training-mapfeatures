package com.mobcomlab.mapfeatures.managers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class WebServiceManager {

    private final static String SERVER = "http://www2.cgistln.nu.ac.th/geoserver/";
    private final static String LAYERS_URL = SERVER+"wfs?request=GetCapabilities&version=1.0.0";
    private final static String FEATURES_URL = SERVER+"wfs?request=GetFeature&version=1.0.0&typeName=%s&maxFeatures=1000&outputFormat=json";

    final Context context;
    final WebServiceCallbackListener listener;


    public WebServiceManager(Context context, WebServiceCallbackListener listener) {
        super();
        this.context = context;
        this.listener = listener;
    }

    public void requestLayers() {
        Log.i("WebServiceManager", LAYERS_URL);
        StringRequest request = new StringRequest(Request.Method.GET, LAYERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleLayersResponse(response);
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

    public void requestFeatures(final String layerId) {
        String url = String.format(FEATURES_URL, layerId);
        Log.i("WebServiceManager", url);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        handleFeaturesResponse(layerId, response);
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

    private void handleLayersResponse(String response) {
        DatabaseManager databaseManager = new DatabaseManager(context);
        String layerId = null;
        String title = null;
        String description = null;

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if (tagName.equals("FeatureType")) {
                        if (layerId != null && title != null) {
                            databaseManager.createOrUpdateLayer(layerId, title, description);
                        }
                        layerId = null;
                        title = null;
                        description = null;
                    }
                    else if (tagName.equals("Name")) {
                        layerId = parser.nextText();
                    }
                    else if (tagName.equals("Title")) {
                        title = parser.nextText();
                    }
                    else if (tagName.equals("Abstract")) {
                        description = parser.nextText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFeaturesResponse(String layerId, JSONObject response) {
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
                        coordinates.add(new Pair<>(coordRaw.getDouble(1), coordRaw.getDouble(0)));
                    }
                }

                JSONObject propertiesRaw = featureRaw.getJSONObject("properties");
                ArrayList<Pair<String,String>> properties = new ArrayList<>();
                Iterator<String> keysIterator = propertiesRaw.keys();
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next();
                    properties.add(new Pair<>(key, propertiesRaw.getString(key)));
                }

                Feature feature = databaseManager.createOrUpdateFeature(id, geometryType, coordinates, properties);
                features.add(feature);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        databaseManager.setFeatures(layerId, features);
    }

}
