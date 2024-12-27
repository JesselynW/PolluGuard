package com.example.polluguard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;

import java.util.List;
import java.util.Random;

public class UserProjectAdapter extends RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder> {
    private final ProjectOnClick projectClick;
    private Context context;
    private List<Project> userProjects;
    private int userId;

    private DBHelper dbHelper;

    Random rand = new Random();

    public UserProjectAdapter(Context context, int userId, List<Project> userProjects, ProjectOnClick projectClick) {
        this.context = context;
        this.userId = userId;
        this.userProjects = userProjects;
        this.projectClick = projectClick;
        this.dbHelper = new DBHelper(context.getApplicationContext());
    }

    @NonNull
    @Override
    public UserProjectAdapter.UserProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_project, parent, false);
        return new UserProjectViewHolder(view, projectClick);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProjectAdapter.UserProjectViewHolder holder, int position) {
        Project userProject = userProjects.get(position);
        int projectId = userProject.getProjectId();
        int rewardPoint = userProject.getReward();

        holder.name.setText(userProject.getProjectName());
        holder.bgImage.setImageResource(userProject.getImageProject());
        holder.logo.setImageResource(userProject.getOrganizer().getOrganizerLogo());
        holder.date.setText(userProject.getDate().substring(0, 2));
        holder.month.setText(userProject.getDate().substring(5, 8).toUpperCase());
        holder.reward.setText(rewardPoint + "");

        boolean rewardStatus = dbHelper.getUserProjectRewardStatus(userId, projectId);
        Log.i("USER PROJECT ADAPTER", "REWARD STATUS = " + rewardStatus);

        // jika sudah diclaim
        if(!rewardStatus){
            holder.rewardButton.setEnabled(false);
            holder.rewardButton.setBackgroundResource(R.drawable.gray_rounded_corners);
            holder.reward.setTextColor(holder.itemView.getContext().getColor(R.color.white));
            holder.rewardLogo.setImageResource(R.drawable.handshake);
        }
        else {
            holder.rewardButton.setOnClickListener(v -> {
                dbHelper.updateUserProjectStatus(userId, projectId);
                dbHelper.updateUserPoint(userId, rewardPoint);
                notifyItemChanged(position);
            });

        }
    }

    @Override
    public int getItemCount() {
        return userProjects.size();
    }

    public static class UserProjectViewHolder extends RecyclerView.ViewHolder {

        ImageView bgImage;
        ImageView logo;
        TextView name;
        TextView location;
        TextView date;
        TextView month;
        TextView reward;
        LinearLayout rewardButton;
        ImageView rewardLogo;

        public UserProjectViewHolder(@NonNull View itemView, ProjectOnClick projectClick) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.projectImage_userProject);
            logo = itemView.findViewById(R.id.organizerLogo_userProject);
            name = itemView.findViewById(R.id.nameText_userProject);
            location = itemView.findViewById(R.id.locationText_userProject);
            date = itemView.findViewById(R.id.dateText_userProject);
            month = itemView.findViewById(R.id.monthText_userProject);
            reward = itemView.findViewById(R.id.rewardText_userProject);
            rewardButton = itemView.findViewById(R.id.rewardButton);
            rewardLogo = itemView.findViewById(R.id.rewardLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public void updateData(List<Project> newProjects) {
        this.userProjects.clear();
        this.userProjects.addAll(newProjects);
        notifyDataSetChanged();
    }


}
