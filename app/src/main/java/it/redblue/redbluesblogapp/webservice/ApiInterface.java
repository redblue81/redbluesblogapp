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
    //String PARAMETERS = "&exclude=content,title_plain,tags,custom_fields,categories,comments&author_meta=email";
    String PARAMETERS = "&exclude=content,title_plain,tags,categories,comments&author_meta=email";

    @GET("get_recent_posts/?count=" + POSTS_NUMBER + PARAMETERS)
    Call<SiteResponse> getRecentPost();

    @GET("get_posts/?count=" + POSTS_NUMBER + PARAMETERS)
    Call<SiteResponse> getPosts(@Query("page") int page);

    @GET("get_post")
    Call<SiteResponse> getPost(@Query("id") long id);

    @GET("get_category_index")
    Call<SiteResponse> getCategories();

    @GET("get_category_posts/?count=" + POSTS_NUMBER + PARAMETERS)
    Call<SiteResponse> getCategoryPosts(@Query("id") long id, @Query("page") int page);

    @GET("redblue/get_post_excerpt")
    Call<SiteResponse> getExcerptForPost(@Query("post_id") long id);

    @GET("get_page")
    Call<SiteResponse> getPage(@Query("slug") String slug);

    @GET("get_search_results")
    Call<SiteResponse> getSearchResult(@Query("search") String search, @Query("page") int page);

}
