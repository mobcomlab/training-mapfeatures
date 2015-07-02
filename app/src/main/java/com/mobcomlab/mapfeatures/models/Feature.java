package com.mobcomlab.mapfeatures.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Feature extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String type;
    private RealmList<Coordinate> coordinates;
    private RealmList<Property> properties;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(RealmList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public RealmList<Property> getProperties() {
        return properties;
    }

    public void setProperties(RealmList<Property> properties) {
        this.properties = properties;
    }
}
