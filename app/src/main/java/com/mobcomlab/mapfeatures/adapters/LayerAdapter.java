package com.mobcomlab.mapfeatures.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobcomlab.mapfeatures.MapActivity;
import com.mobcomlab.mapfeatures.R;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.ViewHolder> {

    public static final class ViewHolder
            extends RecyclerView.ViewHolder {

        public final TextView nameView;
        public final TextView featuresView;
        public final TextView featureTypeView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name_view);
            featuresView = (TextView) itemView.findViewById(R.id.features_view);
            featureTypeView = (TextView) itemView.findViewById(R.id.geometry_type_view);
        }
    }

    private final Context context;
    private final List<Layer> data;

    public LayerAdapter(final Context context, final List<Layer> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public LayerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.row_layer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LayerAdapter.ViewHolder holder, final int position) {
        final Layer layer = data.get(position);

        // Bind data
        holder.nameView.setText(layer.getId());
        holder.featuresView.setText(layer.getFeatures().size() > 0 ? String.valueOf(layer.getFeatures().size()) : "-");
        holder.featureTypeView.setText(layer.getName());



        // Clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Launch detail activity
                final Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("layerId", layer.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
