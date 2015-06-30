package com.mobcomlab.mapfeatures.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Layer extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private RealmList<Feature> features;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(RealmList<Feature> features) {
        this.features = features;
    }
}
