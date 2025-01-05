package com.example.polluguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.R;
import com.example.polluguard.model.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void setData(ArrayList<Comment> comments){
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        Date currentDate = new Date();
        Date pastDate = comment.getDate();
        String date;

        holder.comment.setText(comment.getComment());
        holder.user.setText(comment.getUsername());

        long diffInMillis = currentDate.getTime() - pastDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if(diffInSeconds < 60) {
            date = diffInSeconds + " seconds ago";
        } else if (diffInMinutes < 60) {
            date = diffInMinutes + " minutes ago";
        } else if (diffInHours < 24) {
            date = diffInHours + " hours ago";
        } else if (diffInDays < 30) {
            date = diffInDays + " days ago";
        } else if (diffInDays < 365) {
            long diffInMonths = diffInDays / 30;
            date = diffInMonths + " months ago";
        } else {
            long diffInYears = diffInDays / 365;
            date = diffInYears + " years ago";
        }

        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView comment, user, date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.commentText);
            user = itemView.findViewById(R.id.userText);
            date = itemView.findViewById(R.id.dateText);
        }
    }
}
