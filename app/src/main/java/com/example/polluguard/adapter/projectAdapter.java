package com.example.polluguard.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper.ProjectDBHelper;
import com.example.polluguard.R;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.projectOnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class projectAdapter extends RecyclerView.Adapter<projectAdapter.ProjectViewHolder> {

    private final projectOnClick projectClick;
    private Context context;
    private List<Project> projects;
    Random rand = new Random();

    public projectAdapter(Context context, List<Project> projects,projectOnClick projectClick) {
        this.context = context;
        this.projects = projects;
        this.projectClick = projectClick;
    }

    @NonNull
    @Override
    public projectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view, projectClick);
    }

    @Override
    public void onBindViewHolder(@NonNull projectAdapter.ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        int dist = rand.nextInt(50)+1;

        holder.name.setText(project.getProjectName());
        holder.location.setText(project.getLocation());
        holder.bgImage.setImageResource(project.getImageProject());
        holder.logo.setImageResource(project.getOrganizer().getOrganizerLogo());
        holder.date.setText(project.getDate().substring(0, 2));
        holder.month.setText(project.getDate().substring(5, 8).toUpperCase());
        holder.reward.setText(project.getReward() + "");
        holder.price.setText(project.getPrice());
        holder.slot.setText(project.getSlot() + " slots left");
        holder.distance.setText(dist + " km");
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

        ImageView bgImage;
        ImageView logo;
        TextView name;
        TextView location;
        TextView date;
        TextView month;
        TextView reward;
        TextView price;
        TextView distance;
        TextView slot;

        public ProjectViewHolder(@NonNull View itemView, projectOnClick projectClick) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.projectImage_Project);
            logo = itemView.findViewById(R.id.organizerLogo);
            name = itemView.findViewById(R.id.nameText_Project);
            location = itemView.findViewById(R.id.locationText_Project);
            date = itemView.findViewById(R.id.dateText);
            month = itemView.findViewById(R.id.monthText);
            reward = itemView.findViewById(R.id.rewardText);
            price = itemView.findViewById(R.id.priceText);
            distance = itemView.findViewById(R.id.distanceText);
            slot = itemView.findViewById(R.id.slotText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (projectClick != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            projectClick.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
