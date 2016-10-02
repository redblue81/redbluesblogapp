package it.redblue.redbluesblogapp.webservice;

import android.util.Base64;
import android.util.Log;

import it.redblue.redbluesblogapp.model.MailResponse;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by redblue on 01/10/16.
 */

public class MailClient {

    private static final String TAG = MailClient.class.getSimpleName();

    public static final String BASE_URL = "https://api.mailgun.net/v3/mailgun.red-blue.it/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            Log.d(TAG, "Creato client Retrofit per Email");
        }
        return retrofit;
    }

}
