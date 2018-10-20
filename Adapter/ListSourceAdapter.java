package com.example.nayanjyoti.androidnewsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayanjyoti.androidnewsapp.Common.Common;
import com.example.nayanjyoti.androidnewsapp.Interface.IconBetterIdeaService;
import com.example.nayanjyoti.androidnewsapp.Interface.ItemClickListener;
import com.example.nayanjyoti.androidnewsapp.ListNews;
import com.example.nayanjyoti.androidnewsapp.Model.IconBetterIdea;
import com.example.nayanjyoti.androidnewsapp.Model.WebSite;
import com.example.nayanjyoti.androidnewsapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceAdapter.ViewHolder> {

    private Context context;
    private WebSite webSite;

    private IconBetterIdeaService mService;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ItemClickListener itemClickListener;
        TextView source_title;
        CircleImageView source_image;

        public ViewHolder(View itemView) {
            super(itemView);

            source_title = itemView.findViewById(R.id.source_name);
            source_image = itemView.findViewById(R.id.source_image);
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

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

        mService = Common.getIconService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        StringBuilder iconBetterApi = new StringBuilder("https://i.olsh.me/allicons.json?url=");
        iconBetterApi.append(webSite.getSources().get(position).getUrl());

        Log.d("source_data",iconBetterApi.toString());

        mService.getIconUrl(iconBetterApi.toString()).enqueue(new Callback<IconBetterIdea>() {
            @Override
            public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                if(response.body().getIcons().size() > 0){
                    Picasso.get()
                            .load(response.body().getIcons().get(0).getUrl())
                            .into(holder.source_image);
                }

            }

            @Override
            public void onFailure(Call<IconBetterIdea> call, Throwable t) {
                //Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        holder.source_title.setText(webSite.getSources().get(position).getName());
        Log.d("source_data",webSite.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent i = new Intent(context, ListNews.class);
                i.putExtra("source",webSite.getSources().get(position).getId());
                //i.putExtra("sortBy",webSite.getSources().get(position).getSortBysAvailable().get(0));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.d("source_data", Integer.toString(webSite.getSources().size()));
        return webSite.getSources().size();
    }
}
