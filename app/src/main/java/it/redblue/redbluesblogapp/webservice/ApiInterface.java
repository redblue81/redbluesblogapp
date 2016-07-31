package it.redblue.redbluesblogapp.webservice;

import it.redblue.redbluesblogapp.model.SiteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by redblue on 02/07/16.
 */
public interface ApiInterface {

    int POSTS_NUMBER = 10;
    String PARAMETERS = "&exclude=content,title_plain,tags,custom_fields,categories,comments&author_meta=email";

    @GET("get_recent_posts/?count=" + POSTS_NUMBER + PARAMETERS)
    Call<SiteResponse> getRecentPost();

    @GET("get_posts/?count=" + POSTS_NUMBER + PARAMETERS)
    Call<SiteResponse> getPosts(@Query("page") int page);

    @GET("get_post")
    Call<SiteResponse> getPost(@Query("id") long id);

}
