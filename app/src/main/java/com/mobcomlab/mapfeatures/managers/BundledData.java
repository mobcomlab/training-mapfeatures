package com.mobcomlab.mapfeatures.managers;

import android.content.Context;

import com.mobcomlab.mapfeatures.models.Coordinate;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import io.realm.Realm;

public class BundledData {

    public static void clearDatabase(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.clear(Coordinate.class);
        realm.clear(Feature.class);
        realm.clear(Layer.class);
        realm.commitTransaction();
    }

    public static void populateDatabase(Context context) {

        Realm realm = Realm.getInstance(context);

        if (realm.where(Layer.class).count() == 0) {

            realm.beginTransaction();

            Feature f1 = realm.createObject(Feature.class);
            f1.setId("forest pue.1");
            f1.setName("ป่าน้ำยาวและป่าน้ำสวด");

            Feature f2 = realm.createObject(Feature.class);
            f2.setId("forest pue.2");
            f2.setName("ป่าดอยภูคาและป่าผาแดง");

            Feature f3 = realm.createObject(Feature.class);
            f3.setId("forest pue.3");
            f3.setName("นอกเขตป่า");

            Layer layer1 = realm.createObject(Layer.class);
            layer1.setId("gistck:forest pue");
            layer1.setName("Forests");
            layer1.getFeatures().add(f1);
            layer1.getFeatures().add(f2);
            layer1.getFeatures().add(f3);

            Layer layer2 = realm.createObject(Layer.class);
            layer2.setId("gistck:river pue");
            layer2.setName("Rivers");

            Layer layer3 = realm.createObject(Layer.class);
            layer3.setId("gistck:landmap pue");
            layer3.setName("Landmap");

            realm.commitTransaction();
        }

    }


}
