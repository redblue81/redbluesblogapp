package it.redblue.redbluesblogapp.model;

import android.databinding.ObservableField;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 02/07/16.
 */
public class WordpressPost {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private Author author;
    @SerializedName("slug")
    private String slug;
    @SerializedName("url")
    private String url;
    @SerializedName("excerpt")
    private String excerpt;
    @SerializedName("date")
    private String data;
    @SerializedName("content")
    private String content;
    @SerializedName("custom_fields")
    private CustomFields customFields;

    public WordpressPost(long id, String title, Author author, String slug, String url, String excerpt, String data, String content, CustomFields customFields) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.slug = slug;
        this.url = url;
        this.excerpt = excerpt;
        this.data = data;
        this.content = content;
        this.customFields = customFields;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }
}
