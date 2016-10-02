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
/*
    public static ClientResponse SendSimpleMessage(String from) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-f42f2f79ca03cb9aac98521977f1b6a0"));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/mailgun.red-blue.it" + "/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", "Email da un Utente di RedBlue's Blog App <" + from +">");
        //formData.add("to", "bar@example.com");
        formData.add("to", "redblue@red-blue.it");
        formData.add("subject", "Messaggio da RedBlue's Blog App");
        formData.add("text", "Testing some Mailgun awesomness!");
        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
    }
*/