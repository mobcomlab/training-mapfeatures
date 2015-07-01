package com.mobcomlab.mapfeatures;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobcomlab.mapfeatures.managers.DatabaseManager;
import com.mobcomlab.mapfeatures.models.Layer;


public class LayerEditActivity extends ActionBarActivity {

    EditText idText;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_edit);

        idText = (EditText) findViewById(R.id.id_text);
        nameText = (EditText) findViewById(R.id.name_text);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayerEditActivity.this.save();
            }
        });

        String layerID = getIntent().getStringExtra("layerID");
        if (layerID != null) {
            Layer layer = new DatabaseManager(this).getLayer(layerID);
            idText.setText(layerID);
            idText.setEnabled(false);
            nameText.setText(layer.getName());
        }
    }


    protected void save() {

        String id = idText.getText().toString();
        String name = nameText.getText().toString();

        new DatabaseManager(this).createOrUpdateLayer(id, name);

        finish();
    }
}
