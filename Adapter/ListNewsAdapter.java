package com.example.nayanjyoti.androidnewsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nayanjyoti.androidnewsapp.Common.ISO8601Parse;
import com.example.nayanjyoti.androidnewsapp.DetailsArticle;
import com.example.nayanjyoti.androidnewsapp.Interface.ItemClickListener;
import com.example.nayanjyoti.androidnewsapp.Model.Article;
import com.example.nayanjyoti.androidnewsapp.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ItemClickListener itemClickListener;
        TextView articleTitle;
        RelativeTimeTextView articleTime;
        CircleImageView articleImage;

        public ViewHolder(View itemView) {
            super(itemView);

            articleTitle = itemView.findViewById(R.id.article_title);
            articleTime = itemView.findViewById(R.id.article_time);
            articleImage = itemView.findViewById(R.id.article_image);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }


    @NonNull
    @Override
    public ListNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsAdapter.ViewHolder holder, int position) {

        Picasso.get()
                .load(articleList.get(position).getUrlToImage())
                .into(holder.articleImage);

        if(articleList.get(position).getTitle().length() > 65){
            holder.articleTitle.setText(articleList.get(position).getTitle().substring(0,65)+" ...");
        }else{
            holder.articleTitle.setText(articleList.get(position).getTitle());
        }

        Date date = null;

        try{
            date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        holder.articleTime.setReferenceTime(date.getTime());

//      set event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, DetailsArticle.class);
                detail.putExtra("webURL",articleList.get(position).getUrl());
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

}
