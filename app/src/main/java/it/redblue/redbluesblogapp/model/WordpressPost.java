package it.redblue.redbluesblogapp.model;

import android.databinding.ObservableField;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 02/07/16.
 */
public class WordpressPost {

    @SerializedName("title")
    private String title;
    @SerializedName("slug")
    private String slug;
    @SerializedName("url")
    private String url;
    @SerializedName("excerpt")
    private String excerpt;
    @SerializedName("date")
    private String data;

    public WordpressPost(String title, String slug, String url, String excerpt, String data) {
        this.title = title;
        this.slug = slug;
        this.url = url;
        this.excerpt = excerpt;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void onClick(View view) {
        Snackbar.make(view,getTitle(), Snackbar.LENGTH_LONG).show();
    }

}
