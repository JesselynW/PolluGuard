package com.example.polluguard.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.adapter.ArticleAdapter;
import com.example.polluguard.databinding.FragmentDiscoverBinding;
import com.example.polluguard.model.Article;
import com.example.polluguard.recyclerView.ProjectOnClick;
import com.example.polluguard.ui.ArticleDetails;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment implements ProjectOnClick {

    private FragmentDiscoverBinding binding;
    private ArticleAdapter articleAdapter;
    private ArrayList<Article> articles;
    private RecyclerView articleRV;
    TextView title, author;
    ImageView image;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DiscoverViewModel discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);

        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        articleRV = root.findViewById(R.id.articleRV);
        articleRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        DBHelper dbHelper = new DBHelper(getContext());
        articles = dbHelper.getAllArticle();

        articleAdapter = new ArticleAdapter(getContext(), articles, this);
        articleRV.setAdapter(articleAdapter);

        title = root.findViewById(R.id.titleText);
        author = root.findViewById(R.id.authorText);
        image = root.findViewById(R.id.articleImage);
        title.setText(articles.get(0).getTitle());
        author.setText(articles.get(0).getAuthor() + " - " + articles.get(0).getDate());
        image.setImageResource(articles.get(0).getImage());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ArticleDetails.class);

        intent.putExtra("title", articles.get(position).getTitle());
        intent.putExtra("author", articles.get(position).getAuthor());
        intent.putExtra("date", articles.get(position).getDate());
        intent.putExtra("content", articles.get(position).getContent());
        intent.putExtra("image", articles.get(position).getImage());

        startActivity(intent);
    }
}