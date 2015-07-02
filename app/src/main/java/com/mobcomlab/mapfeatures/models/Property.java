package com.mobcomlab.mapfeatures.models;

import io.realm.RealmObject;

public class Property extends RealmObject {

    private String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String value;

}
