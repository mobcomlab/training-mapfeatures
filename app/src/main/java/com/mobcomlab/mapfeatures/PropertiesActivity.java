package com.mobcomlab.mapfeatures;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mobcomlab.mapfeatures.adapters.LayerAdapter;
import com.mobcomlab.mapfeatures.adapters.PropertiesAdapter;
import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Property;

import java.util.List;


public class PropertiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);

        // Get the properties
        String featureId = getIntent().getStringExtra("featureId");
        Feature feature = new DatabaseManager(this).getFeature(featureId);
        List<Property> properties = feature.getProperties();

        // Setup recycler
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PropertiesAdapter(this, properties));
    }

}
