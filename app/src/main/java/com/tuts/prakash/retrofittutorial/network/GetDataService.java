package com.tuts.prakash.retrofittutorial.network;

import com.tuts.prakash.retrofittutorial.model.Game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by praka on 12/24/2017.
 */

public interface GetDataService {

    /*@GET("/collection/PegaKrakow?grouped=true")
    Call<List<Game>> getAllGames();*/

    @GET("/api/Games/")
    Call<List<Game>> getAllGames();

    @GET("/thing/{id}")
    Call<Game> getGameDetails(@Path("id") int gameId);
}
