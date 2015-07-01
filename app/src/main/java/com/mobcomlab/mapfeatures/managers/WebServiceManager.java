package com.mobcomlab.mapfeatures.managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceManager {

    private final static String FEATURES_URL = "http://www2.cgistln.nu.ac.th/geoserver/plkwater/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=%s&maxFeatures=50&outputFormat=json";

    final Context context;
    final WebServiceCallbackListener listener;


    public WebServiceManager(Context context, WebServiceCallbackListener listener) {
        super();
        this.context = context;
        this.listener = listener;
    }

    public void requestLayer(String layerId) {
        String url = String.format(FEATURES_URL, layerId);
        Log.i("url", url);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("WebServiceManager", "Response: " + response.toString());

                        handleLayerResponse(response);

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

    private void handleLayerResponse(JSONObject response) {
        DatabaseManager databaseManager = new DatabaseManager(context);

        try {
            JSONArray features = response.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {

                JSONObject feature = features.getJSONObject(i);
                String id = feature.getString("id");

                JSONObject geometry = feature.getJSONObject("geometry");
                String geometryType = geometry.getString("type");

                JSONArray coordinates = new JSONArray();
                if (geometryType.equals("MultiLineString")) {
                    coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);
                } else if (geometryType.equals("MultiPolygon")) {
                    coordinates = geometry.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
                } else if (geometryType.equals("Point")) {
                    coordinates = geometry.getJSONArray("coordinates");
                }
                Log.i("coordinates", coordinates.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestSoilSamples() {

        String url = String.format(FEATURES_URL, "mmgdb:soil_sample54");

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("WebServiceManager", "Response: " + response.toString());

                        handleSoilSamplesResponse(response);

                        listener.onWebServiceCallback();

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void handleSoilSamplesResponse(JSONObject response) {

        DatabaseManager databaseManager = new DatabaseManager(context);

        try {
            JSONArray features = response.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {

                JSONObject feature = features.getJSONObject(i);
                String id = feature.getString("id");

                JSONArray coords = feature.getJSONObject("geometry").getJSONArray("coordinates");

                double xCoord = coords.getDouble(0);
                double yCoord = coords.getDouble(1);



                /*JSONObject properties = feature.getJSONObject("properties");

                String dateStr = properties.getString("date");
                int year = (2500 + Integer.parseInt(dateStr.substring(6))) - 543;
                String gregDateStr = dateStr.substring(0,6) + year;
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(gregDateStr);
                Log.i("Date", date.toString());
                float ph = (float)properties.getDouble("ph");
                int cd = properties.getInt("cd");

                databaseManager.addSoilSample(id, xCoord, yCoord, date, ph, cd);*/

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
