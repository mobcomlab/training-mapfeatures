package com.mobcomlab.mapfeatures.managers;

import android.content.Context;
import android.util.Pair;

import com.mobcomlab.mapfeatures.models.Coordinate;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;
import com.mobcomlab.mapfeatures.models.Property;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class DatabaseManager {

    private final Context context;

    public DatabaseManager(final Context context) {
        this.context = context;
    }

    public List<Layer> getLayers() {
        return Realm.getInstance(context).where(Layer.class).findAll();
    }

    public Layer getLayer(String id) {
        return Realm.getInstance(context).where(Layer.class).equalTo("id", id).findFirst();
    }

    public Layer createOrUpdateLayer(String id, String name, String description) {
        Layer layer = getLayer(id);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        if (layer == null) {
            layer = realm.createObject(Layer.class);
        }

        layer.setId(id);
        layer.setName(name);
        layer.setDescription(description);

        realm.commitTransaction();

        return layer;
    }

    public Feature createOrUpdateFeature(String id, String type, List<Pair<Double,Double>> coordinates, List<Pair<String,String>> properties) {
        Realm realm = Realm.getInstance(context);

        Feature feature = realm.where(Feature.class).equalTo("id", id).findFirst();

        realm.beginTransaction();
        if (feature == null) {
            feature = realm.createObject(Feature.class);
        }

        feature.setId(id);
        feature.setType(type);

        // Update coordinates
        if (coordinates != null) {
            ArrayList<Coordinate> coordinatesNew = new ArrayList<>();
            for (Pair<Double, Double> pair : coordinates) {
                Coordinate coord = realm.createObject(Coordinate.class);
                coord.setLatitude(pair.first.floatValue());
                coord.setLongitude(pair.second.floatValue());
                coordinatesNew.add(coord);
            }
            feature.getCoordinates().clear();
            feature.getCoordinates().addAll(coordinatesNew);
        }

        // Update properties
        if (properties != null) {
            ArrayList<Property> propertiesNew = new ArrayList<>();
            for (Pair<String, String> pair : properties) {
                Property property = realm.createObject(Property.class);
                property.setKey(pair.first);
                property.setValue(pair.second);
                propertiesNew.add(property);
            }
            feature.getProperties().clear();
            feature.getProperties().addAll(propertiesNew);
        }

        realm.commitTransaction();

        return feature;
    }

    public List<Feature> getFeatures(String layerID) {
        return getLayer(layerID).getFeatures();
    }

    public void setFeatures(String layerID, List<Feature> features) {
        Layer layer = getLayer(layerID);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        layer.getFeatures().clear();
        layer.getFeatures().addAll(features);
        realm.commitTransaction();
    }

}
