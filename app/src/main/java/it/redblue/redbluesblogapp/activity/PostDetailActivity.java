package it.redblue.redbluesblogapp.activity;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.databinding.ActivityPostDetailBinding;
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

        setSupportActionBar(binding.toolbar);
        binding.setPost(new WordpressPost(0, "", null, "", "", "", "", "", null));
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }*/

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getPost(getIntent().getLongExtra("postId", 0));
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                WordpressPost post = response.body().getPost();
                Log.d(TAG, "Post " + post.getId() + " - " + post.getTitle() + " ricevuto correttamente");
                binding.setPost(null);
                binding.setPost(post);
                binding.toolbarText.setText(Html.fromHtml(post.getTitle()));
                binding.subtitle.setText(post.getAuthor().getName() + " - " + post.getData());
                if (post.getCustomFields().getImageUrl() != null && !"".equals(post.getCustomFields().getImageUrl()[0])) {
                    Picasso.with(getApplicationContext()).load(post.getCustomFields().getImageUrl()[0]).into(binding.postImage);
                }
                binding.webView.getSettings().setJavaScriptEnabled(true);
                binding.webView.loadData(post.getContent(), "text/html; charset=UTF-8", null);
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                Log.e(TAG, "ERRORE NEL CARICAMENTO DEL POST - " + t.getMessage());
            }
        });
    }

}
