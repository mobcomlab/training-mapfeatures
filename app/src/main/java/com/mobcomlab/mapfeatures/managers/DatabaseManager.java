package com.mobcomlab.mapfeatures.managers;

import android.content.Context;

import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

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

    public List<Feature> getFeatures(String layerID) {
        return getLayer(layerID).getFeatures();
    }

}