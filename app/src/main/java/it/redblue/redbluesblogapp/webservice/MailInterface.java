package it.redblue.redbluesblogapp.webservice;

import it.redblue.redbluesblogapp.model.MailResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by redblue on 02/10/16.
 */

public interface MailInterface {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("messages")
    Call<MailResponse> authUser(@Header("Authorization") String authorizationHeader,
                                @Field("from") String from,
                                @Field("to") String to,
                                @Field("subject") String subject,
                                @Field("text") String text);

}