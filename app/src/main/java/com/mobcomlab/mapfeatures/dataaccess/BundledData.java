package com.mobcomlab.mapfeatures.dataaccess;

import android.content.Context;

import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import io.realm.Realm;

public class BundledData {

    public static void populateDatabase(Context context) {

        Realm realm = Realm.getInstance(context);

        if (realm.where(Layer.class).count() == 0) {

            realm.beginTransaction();

            Feature f1 = realm.createObject(Feature.class);
            f1.setId("forest pue.1");
            f1.setName("ป่าน้ำยาวและป่าน้ำสวด");

            Feature f2 = realm.createObject(Feature.class);
            f1.setId("forest pue.2");
            f1.setName("ป่าดอยภูคาและป่าผาแดง");

            Feature f3 = realm.createObject(Feature.class);
            f1.setId("forest pue.3");
            f1.setName("นอกเขตป่า");

            Layer layer1 = realm.createObject(Layer.class);
            layer1.setId("1");
            layer1.setName("Forests");
            layer1.getFeatures().add(f1);
            layer1.getFeatures().add(f2);
            layer1.getFeatures().add(f3);

            Layer layer2 = realm.createObject(Layer.class);
            layer2.setId("2");
            layer2.setName("Lakes");

            Layer layer3 = realm.createObject(Layer.class);
            layer3.setId("3");
            layer3.setName("Mountains");

            realm.commitTransaction();
        }

    }


}
