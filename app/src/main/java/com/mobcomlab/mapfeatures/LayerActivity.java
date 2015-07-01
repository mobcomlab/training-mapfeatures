package com.mobcomlab.mapfeatures;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mobcomlab.mapfeatures.adapters.FeatureAdapter;
import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.models.Layer;

public class LayerActivity extends AppCompatActivity {

    private Layer layer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer);

        final String layerId = getIntent().getStringExtra("layerId");
        layer = new DatabaseManager(this)
                .getLayer(layerId);

        // Setup recycler view
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FeatureAdapter(this, layer.getFeatures()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportActionBar().setTitle(layer.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit_layer) {
            Intent intent = new Intent(this, LayerEditActivity.class);
            intent.putExtra("layerID", layer.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
