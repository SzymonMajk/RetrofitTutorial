package com.tuts.prakash.retrofittutorial.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.tuts.prakash.retrofittutorial.R;
import com.tuts.prakash.retrofittutorial.adapter.CustomAdapter;
import com.tuts.prakash.retrofittutorial.model.Game;
import com.tuts.prakash.retrofittutorial.network.BoardGamesClientInstance;
import com.tuts.prakash.retrofittutorial.network.GetDataService;
import com.tuts.prakash.retrofittutorial.network.RetrofitClientInstance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private List<Game> games;
    ProgressDialog progressDoalog;
    private Context context;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        final GetDataService leanService = BoardGamesClientInstance.getRetrofitInstance().create(GetDataService.class);
        final GetDataService detailService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<List<Game>> detailCall = leanService.getAllGames();
        detailCall.enqueue(new Callback<List<Game>>() {

            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                List<Game> leanGames = response.body();

                for (final Game g : leanGames) {
                    Call<Game> additionalCall = detailService.getGameDetails(g.getBoardGameGeekId());
                    additionalCall.enqueue(new Callback<Game>() {
                        @Override
                        public void onResponse(Call<Game> call, Response<Game> response) {
                            if (response.body() != null) {
                                g.setName(response.body().getName());
                                g.setImageUrl(response.body().getImageUrl());
                                g.setImageThumbnailUrl(response.body().getImageThumbnailUrl());
                            }
                        }

                        @Override
                        public void onFailure(Call<Game> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                progressDoalog.dismiss();
                generateDataList(leanGames);
                games = leanGames;
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.selection) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Game> gameList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this, gameList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            photo.recycle();
            Image inputImage = new Image();
            inputImage.encodeContent(bytes);

            Vision.Builder visionBuilder = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null);

            visionBuilder.setVisionRequestInitializer(
                    new VisionRequestInitializer(getResources().getString(R.string.key)));

            final Vision vision = visionBuilder.build();
            Feature desiredFeature = new Feature();
            desiredFeature.setType("TEXT_DETECTION");

            final AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(inputImage);
            request.setFeatures(Arrays.asList(desiredFeature));


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    boolean found = false;
                    final TextAnnotation text = new TextAnnotation();
                    // More code here
                    BatchAnnotateImagesRequest batchRequest =
                            new BatchAnnotateImagesRequest();

                    batchRequest.setRequests(Arrays.asList(request));

                    List<String> responses = new ArrayList<>();
                    List<String> lines = new ArrayList<>();
                    final StringBuilder lineBuilder = new StringBuilder();
                    try {
                        BatchAnnotateImagesResponse batchResponse =
                                vision.images().annotate(batchRequest).execute();
                         text.setText(batchResponse.getResponses()
                                 .get(0).getFullTextAnnotation().getText());
                         lineBuilder.append(text.getText());
                         lines = Arrays.asList(text.getText().split("[\n ]+"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final String line = lineBuilder.toString();

                    for (Game g : games) {
                        String name = g.getName();

                        for(int i = 0; i < lines.size(); i++) {


                                if (name.toUpperCase().contains(lines.get(i).toUpperCase())) {
                                    found = true;
                                    break;
                                }

                        }

                        if (found) {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra("Game0Our", g.getId());
                            intent.putExtra("Game0Foreign", g.getBoardGameGeekId());
                            intent.putExtra("Game0Url", g.getImageUrl());
                            intent.putExtra("Game0UrlThumb", g.getImageThumbnailUrl());
                            intent.putExtra("Game0Name", g.getName());
                            startActivity(intent);
                            break;
                        }
                    }

                    if (!found) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),
                                        "No games found :/" , Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }
            });
        }
    }
}
