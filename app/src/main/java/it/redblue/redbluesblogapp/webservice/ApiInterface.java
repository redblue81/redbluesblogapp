package it.redblue.redbluesblogapp.webservice;

import it.redblue.redbluesblogapp.model.SiteResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by redblue on 02/07/16.
 */
public interface ApiInterface {

    int POSTS_NUMBER = 10;

    @GET("get_recent_posts/?count=" + POSTS_NUMBER + "&exclude=content,title_plain,tags,custom_fields,categories,comments&author_meta=email")
    Call<SiteResponse> getRecentPost();

}
