package com.mobcomlab.mapfeatures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mobcomlab.mapfeatures.dataaccess.BundledData;
import com.mobcomlab.mapfeatures.dataaccess.DatabaseManager;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private List<Layer> layers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BundledData.populateDatabase(this);

        layers = new DatabaseManager(this).getLayers();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
