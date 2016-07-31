package it.redblue.redbluesblogapp.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.databinding.ActivityPostDetailBinding;
import it.redblue.redbluesblogapp.databinding.PostDetailBinding;
import it.redblue.redbluesblogapp.model.SiteResponse;
import it.redblue.redbluesblogapp.model.WordpressPost;
import it.redblue.redbluesblogapp.webservice.ApiClient;
import it.redblue.redbluesblogapp.webservice.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = PostDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getPost(getIntent().getLongExtra("postId", 0));
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                WordpressPost post = response.body().getPost();
                Log.d(TAG, "Post " + post.getId() + " - " + post.getTitle() + " ricevuto correttamente");
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {

            }
        });
    }
}
