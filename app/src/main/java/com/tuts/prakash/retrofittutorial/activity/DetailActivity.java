package com.tuts.prakash.retrofittutorial.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tuts.prakash.retrofittutorial.R;
import com.tuts.prakash.retrofittutorial.adapter.CustomAdapter;
import com.tuts.prakash.retrofittutorial.model.Game;

public class DetailActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private ImageView detailedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;

        Intent intent = getIntent();

        //final GetDataService detailService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        final Game game = new Game();
        game.setId(intent.getIntExtra("Game0Our", 1));
        game.setBoardGameGeekId(intent.getIntExtra("Game0Foreign", 1));
        game.setImageUrl(intent.getStringExtra("Game0Url"));
        game.setImageThumbnailUrl(intent.getStringExtra("Game0UrlThumb"));
        game.setName(intent.getStringExtra("Game0Name"));
        /*Call<Game> additionalCall = detailService.getGameDetails(game.getBoardGameGeekId());



        additionalCall.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.body() != null) {
                    game.setName(response.body().getName());
                    game.setImageUrl(response.body().getImageUrl());
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });*/

        final String url = game.getImageUrl();
        TextView title = findViewById(R.id.detailedTitle);
        title.setText(game.getName());
        detailedImage = findViewById(R.id.detailedImage);


        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(url)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(detailedImage);
    }
}
