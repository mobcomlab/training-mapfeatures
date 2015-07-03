package com.mobcomlab.mapfeatures.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobcomlab.mapfeatures.FeatureActivity;
import com.mobcomlab.mapfeatures.R;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Property;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.ViewHolder> {

    public static final class ViewHolder
            extends RecyclerView.ViewHolder {

        public final TextView nameView;
        public final TextView valueView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name_view);
            valueView = (TextView) itemView.findViewById(R.id.value_view);
        }
    }

    private final Context context;
    private final List<Property> data;

    public PropertiesAdapter(final Context context, final List<Property> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.row_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Property property = data.get(position);

        // Bind data
        holder.nameView.setText(property.getKey());
        holder.valueView.setText(property.getValue());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
