package it.redblue.redbluesblogapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.adapter.WordpressAdapter;
import it.redblue.redbluesblogapp.model.SiteResponse;
import it.redblue.redbluesblogapp.model.WordpressPost;
import it.redblue.redbluesblogapp.webservice.ApiClient;
import it.redblue.redbluesblogapp.webservice.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getRecentPost();
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                int statusCode = response.code();
                List<WordpressPost> postList = response.body().getPosts();
                Log.d(TAG, "Post ricevuti correttamente");
                //recyclerView.setAdapter(new WordpressAdapter(postList, R.layout.post_item, getApplicationContext()));
                recyclerView.setAdapter(new WordpressAdapter(postList, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
