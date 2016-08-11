package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 10/08/16.
 */
public class CustomFields {

    @SerializedName("Immagine in evidenza")
    private String[] imageUrl;
    @SerializedName("views")
    private long[] views;
    @SerializedName("ratings_average")
    private int[] rating;

    public String[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long[] getViews() {
        return views;
    }

    public void setViews(long[] views) {
        this.views = views;
    }

    public int[] getRating() {
        return rating;
    }

    public void setRating(int[] rating) {
        this.rating = rating;
    }
}
