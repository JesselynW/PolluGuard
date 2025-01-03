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
import com.example.polluguard.model.Article;
import com.example.polluguard.recyclerView.ProjectOnClick;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private final ProjectOnClick onclick;
    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles, ProjectOnClick onclick) {
        this.context = context;
        this.articles = articles;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view, onclick);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.image.setImageResource(article.getImage());
        holder.judul.setText(article.getTitle());
        holder.authorDate.setText(article.getAuthor() + " - " + article.getDate());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView judul;
        TextView authorDate;

        public ArticleViewHolder(@NonNull View itemView, ProjectOnClick articleClick) {
            super(itemView);
            image = itemView.findViewById(R.id.articleImageRV);
            judul = itemView.findViewById(R.id.titleTextRV);
            authorDate = itemView.findViewById(R.id.authorTextRV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(articleClick != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION)
                            articleClick.onItemClick(pos);
                    }
                }
            });
        }
    }
}
