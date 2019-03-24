package com.tuts.prakash.retrofittutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tuts.prakash.retrofittutorial.R;
import com.tuts.prakash.retrofittutorial.activity.DetailActivity;
import com.tuts.prakash.retrofittutorial.model.Game;

import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by praka on 12/24/2017.
 * Edited by Diksu on 23/03/2019.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Game> dataList;
    private Context context;

    public CustomAdapter(Context context,List<Game> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.txtTitle.setText(dataList.get(position).getName());

        Log.e("card", "ok");
/*
        File image = new File(context.getFilesDir().getPath() + "/id.jpg");

        if (image.exists()) {
            holder.coverImage.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
        } else {*/
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(dataList.get(position).getImageThumbnailUrl())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Game g = dataList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("Game0Our", g.getId());
                    intent.putExtra("Game0Foreign", g.getBoardGameGeekId());
                    intent.putExtra("Game0Url", g.getImageUrl());
                    intent.putExtra("Game0UrlThumb", g.getImageThumbnailUrl());
                    intent.putExtra("Game0Name", g.getName());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
