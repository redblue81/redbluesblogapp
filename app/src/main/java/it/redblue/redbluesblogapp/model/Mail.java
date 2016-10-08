package it.redblue.redbluesblogapp.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by redblue on 24/09/16.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Mail {

    @Expose private String name;
    @Expose private String email;
    @Expose private String subject;
    @Expose private String content;

    @ParcelConstructor
    public Mail(String name, String email, String subject, String content) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
