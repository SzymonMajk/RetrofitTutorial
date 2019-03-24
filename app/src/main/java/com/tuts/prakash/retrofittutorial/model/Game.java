package com.tuts.prakash.retrofittutorial.model;

import com.google.gson.annotations.SerializedName;

public class Game {

    @SerializedName("id")
    private Integer id;
    @SerializedName("boardGameGeekId")
    private Integer boardGameGeekId;
    @SerializedName("name")
    private String name;
    @SerializedName("thumbnail")
    private String imageThumbnailUrl;
    @SerializedName("image")
    private String imageUrl;

    public Game(Integer id, Integer boardGameGeekId, String name, String imageUrl, String imageThumbnailUrl) {
        this.id = id;
        this.boardGameGeekId = boardGameGeekId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public Game() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBoardGameGeekId() {
        return boardGameGeekId;
    }

    public void setBoardGameGeekId(Integer boardGameGeekId) {
        this.boardGameGeekId = boardGameGeekId;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }
}
