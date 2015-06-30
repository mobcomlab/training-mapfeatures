package com.mobcomlab.mapfeatures.dataaccess;

import android.content.Context;

import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseManager {

    private final Context context;

    public DatabaseManager(final Context context) {
        this.context = context;
    }

    public List<Layer> getLayers() {
        return Realm.getInstance(context).where(Layer.class).findAll();
    }

    public List<Feature> getFeatures(String layerID) {
        Layer layer = Realm.getInstance(context).where(Layer.class).equalTo("id", layerID).findFirst();
        return layer.getFeatures();
    }

}
