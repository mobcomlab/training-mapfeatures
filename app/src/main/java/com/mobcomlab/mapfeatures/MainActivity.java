package com.mobcomlab.mapfeatures;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mobcomlab.mapfeatures.adapters.LayerAdapter;
import com.mobcomlab.mapfeatures.managers.BundledData;
import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.managers.WebServiceCallbackListener;
import com.mobcomlab.mapfeatures.managers.WebServiceManager;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Layer> layers;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BundledData.clearDatabase(this);
        BundledData.populateDatabase(this);

        layers = new DatabaseManager(this).getLayers();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LayerAdapter(this, layers));
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_layer) {
            Intent intent = new Intent(this, LayerEditActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_simple_map_overlay) {
            startActivity(new Intent(this, SimpleMapOverlayActivity.class));
        }
        else if (id == R.id.action_pull_data) {
            new WebServiceManager(this, new WebServiceCallbackListener() {
                @Override
                public void onWebServiceCallback() {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onWebServiceFailed() {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }).requestLayers();
        }

        return super.onOptionsItemSelected(item);
    }
}
