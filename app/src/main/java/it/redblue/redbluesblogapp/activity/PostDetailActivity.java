package it.redblue.redbluesblogapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

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
    private ObservableField<String> content;
    private ObservableBoolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        content = new ObservableField<>();
        error = new ObservableBoolean();
        binding.setError(error);

        setSupportActionBar(binding.toolbar);
        binding.setPost(new WordpressPost(0, "", null, "", "", "", "", "", null));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getPost(getIntent().getLongExtra("postId", 0));
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                WordpressPost post = response.body().getPost();
                error.set(false);
                Log.d(TAG, "Post " + post.getId() + " - " + post.getTitle() + " ricevuto correttamente");
                binding.setPost(null);
                binding.setPost(post);
                binding.toolbarText.setText(Html.fromHtml(post.getTitle()));
                binding.subtitle.setText(post.getAuthor().getName() + " - " + post.getData());
                if (post.getCustomFields().getImageUrl() != null && !"".equals(post.getCustomFields().getImageUrl()[0])) {
                    Picasso.with(getApplicationContext()).load(post.getCustomFields().getImageUrl()[0]).into(binding.postImage);
                }
                binding.webView.getSettings().setJavaScriptEnabled(true);
                /*
                binding.webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                        WebResourceResponse response = super.shouldInterceptRequest(view, request);
                        try {
                            response = new WebResourceResponse("text/css", "UTF-8", getApplicationContext().getAssets().open("style.css"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return response;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {

                        // Inject CSS when page is done loading
                        Utilities.injectCSS(getApplicationContext(), binding.webView);
                        super.onPageFinished(view, url);
                    }

                });
                */
                //binding.webView.loadData(post.getContent().substring(0, post.getContent().indexOf("<div class=\"addtoany_share_save_container") - 1), "text/html; charset=UTF-8", null);
                if (post.getContent().length() > 0) {
                    content.set(post.getContent());
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html><head><link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></head><body>");
                    sb.append(post.getContent().substring(0, post.getContent().indexOf("<div class=\"addtoany_share_save_container") - 1));
                    sb.append("</body></html>");
                    //view.loadData(string.substring(0, string.indexOf("<div class=\"addtoany_share_save_container") - 1), "text/html; charset=UTF-8", null);
                    binding.webView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
                }

                binding.shareButton.setOnClickListener((view) -> {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(post.getTitle()) + "\n\n" + post.getUrl() + " - RedBlue's Blog");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(post.getUrl()));
                    sharingIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sharingIntent, "Condividi il post:"));
                });
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                error.set(true);
                binding.errorTextView.setText("Errore nel caricamento dei dati");
                Log.e(TAG, "ERRORE NEL CARICAMENTO DEL POST - " + t.getMessage());
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<SiteResponse> call = apiService.getPost(getIntent().getLongExtra("postId", 0));
                call.enqueue(new Callback<SiteResponse>() {
                    @Override
                    public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                        WordpressPost post = response.body().getPost();
                        error.set(false);
                        Log.d(TAG, "Post " + post.getId() + " - " + post.getTitle() + " ricevuto correttamente");
                        binding.setPost(null);
                        binding.setPost(post);
                        binding.toolbarText.setText(Html.fromHtml(post.getTitle()));
                        binding.subtitle.setText(post.getAuthor().getName() + " - " + post.getData());
                        if (post.getCustomFields().getImageUrl() != null && !"".equals(post.getCustomFields().getImageUrl()[0])) {
                            Picasso.with(getApplicationContext()).load(post.getCustomFields().getImageUrl()[0]).into(binding.postImage);
                        }
                        binding.webView.getSettings().setJavaScriptEnabled(true);

                        if (post.getContent().length() > 0) {
                            content.set(post.getContent());
                            StringBuilder sb = new StringBuilder();
                            sb.append("<html><head><link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></head><body>");
                            sb.append(post.getContent().substring(0, post.getContent().indexOf("<div class=\"addtoany_share_save_container") - 1));
                            sb.append("</body></html>");
                            //view.loadData(string.substring(0, string.indexOf("<div class=\"addtoany_share_save_container") - 1), "text/html; charset=UTF-8", null);
                            binding.webView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
                        }

                        binding.shareButton.setOnClickListener((view) -> {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(post.getTitle()) + "\n\n" + post.getUrl() + " - RedBlue's Blog");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(post.getUrl()));
                            sharingIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sharingIntent, "Condividi il post:"));
                        });
                    }

                    @Override
                    public void onFailure(Call<SiteResponse> call, Throwable t) {
                        error.set(true);
                        binding.errorTextView.setText("Errore nel caricamento dei dati");
                        Log.e(TAG, "ERRORE NEL CARICAMENTO DEL POST - " + t.getMessage());
                    }
                });
                if (binding.swipeRefreshLayout.isRefreshing()) {
                    Log.d(TAG, "Stato refresh: " + binding.swipeRefreshLayout.isRefreshing());
                    binding.swipeRefreshLayout.setRefreshing(false);
                };
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
