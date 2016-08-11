package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 11/08/16.
 */
public class Author {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
