package com.example.polluguard.adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;

import java.util.List;
import java.util.Random;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final ProjectOnClick projectClick;
    private Context context;
    private List<Project> projects;
    private double userLatitude;
    private double userLongitude;
    Random rand = new Random();

    public ProjectAdapter(Context context, List<Project> projects, ProjectOnClick projectClick, double userLatitude, double userLongitude) {
        this.context = context;
        this.projects = projects;
        this.projectClick = projectClick;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        Log.i("current location in project adapter", userLatitude + " " + userLongitude);
    }

    @NonNull
    @Override
    public ProjectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view, projectClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        DBHelper dbHelper = new DBHelper(context);

        holder.name.setText(project.getProjectName());
        holder.location.setText(project.getLocation());
        holder.bgImage.setImageResource(project.getImageProject());
        holder.logo.setImageResource(project.getOrganizer().getOrganizerLogo());
        holder.date.setText(project.getDate().substring(0, 2));
        holder.month.setText(project.getDate().substring(5, 8).toUpperCase());
        holder.reward.setText(project.getReward() + "");
        holder.price.setText("FREE");
        holder.slot.setText(dbHelper.countSlotbyEventId(project) + " slots left");

        Location userLocation = new Location("");  // Lokasi pengguna
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);

        Location targetLocation = new Location("");
        targetLocation.setLatitude(project.getLatitude());
        targetLocation.setLongitude(project.getLongtitude());
        float distanceInMeters = userLocation.distanceTo(targetLocation);
        float distanceInKilometers = distanceInMeters / 1000;

        holder.distance.setText(String.format("%.2f km", distanceInKilometers));
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

        public ProjectViewHolder(@NonNull View itemView, ProjectOnClick projectClick) {
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
