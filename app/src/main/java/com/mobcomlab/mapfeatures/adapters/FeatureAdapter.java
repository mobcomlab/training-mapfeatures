package com.mobcomlab.mapfeatures.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobcomlab.mapfeatures.FeatureActivity;
import com.mobcomlab.mapfeatures.LayerActivity;
import com.mobcomlab.mapfeatures.R;
import com.mobcomlab.mapfeatures.models.Feature;
import com.mobcomlab.mapfeatures.models.Layer;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    public static final class ViewHolder
            extends RecyclerView.ViewHolder {

        public final TextView nameView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name_view);
        }
    }

    private final Context context;
    private final List<Feature> data;

    public FeatureAdapter(final Context context, final List<Feature> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public FeatureAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.row_feature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeatureAdapter.ViewHolder holder, final int position) {
        final Feature feature = data.get(position);

        // Bind data
        holder.nameView.setText(feature.getName());

        // Clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Launch detail activity
                final Intent intent = new Intent(context, FeatureActivity.class);
                intent.putExtra("featureId", feature.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
