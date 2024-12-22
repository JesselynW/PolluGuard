package com.example.polluguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.R;
import com.example.polluguard.model.Project;

import java.util.List;

public class PreviewProjectAdapter extends RecyclerView.Adapter<PreviewProjectAdapter.PreviewProjectViewHolder> {

    private Context context;
    private List<Project> previews;

    public PreviewProjectAdapter(Context context, List<Project> previews) {
        this.context = context;
        this.previews = previews;
    }

    @NonNull
    @Override
    public PreviewProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previewproject, parent, false);
        return new PreviewProjectAdapter.PreviewProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewProjectViewHolder holder, int position) {
        Project project = previews.get(position);

        holder.projectName.setText(project.getProjectName());
        holder.bgImage.setImageResource(project.getImageProject());
        holder.date.setText(project.getDate().substring(0, 2));
        holder.month.setText(project.getDate().substring(5, 8).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return previews.size();
    }

    public static class PreviewProjectViewHolder extends RecyclerView.ViewHolder {

        ImageView bgImage;
        TextView date;
        TextView month;
        TextView projectName;

        public PreviewProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.projectImage);
            date = itemView.findViewById(R.id.tanggalText);
            month = itemView.findViewById(R.id.bulanText);
            projectName = itemView.findViewById(R.id.projectNameText);
        }
    }
}
