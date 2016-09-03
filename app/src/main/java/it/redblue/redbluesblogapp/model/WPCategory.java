package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 23/08/16.
 */
public class WPCategory {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("slug")
    private String slug;
    @SerializedName("parent")
    private long parent;
    @SerializedName("post_count")
    private String postCount;

    public WPCategory(long id, String title, String slug, long parent, String postCount) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.parent = parent;
        this.postCount = postCount;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }
}
