package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by redblue on 02/07/16.
 */
public class SiteResponse {

    @SerializedName("count")
    private int count;
    @SerializedName("count_total")
    private int count_total;
    @SerializedName("pages")
    private int pages;
    @SerializedName("posts")
    private List<WordpressPost> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<WordpressPost> getPosts() {
        return posts;
    }

    public void setPosts(List<WordpressPost> posts) {
        this.posts = posts;
    }

}
