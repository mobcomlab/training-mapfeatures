package com.mobcomlab.mapfeatures.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Layer extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String featureType;
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

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public RealmList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(RealmList<Feature> features) {
        this.features = features;
    }
}
