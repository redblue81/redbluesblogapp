package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by redblue on 02/10/16.
 */

public class MailResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
