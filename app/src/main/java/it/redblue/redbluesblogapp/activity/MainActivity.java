package it.redblue.redbluesblogapp.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.adapter.WordpressAdapter;
import it.redblue.redbluesblogapp.databinding.ActivityMainBinding;
import it.redblue.redbluesblogapp.listeners.EndlessScrollingListener;
import it.redblue.redbluesblogapp.model.SiteResponse;
import it.redblue.redbluesblogapp.model.WordpressPost;
import it.redblue.redbluesblogapp.webservice.ApiClient;
import it.redblue.redbluesblogapp.webservice.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();;
    private DrawerLayout drawerLayout;

    private WordpressAdapter adapter;
    private List<WordpressPost> postList;
    private ObservableArrayList<WordpressPost> posts;
    private ObservableBoolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        posts = new ObservableArrayList<>();
        binding.setPosts(posts);

        loading = new ObservableBoolean();
        binding.setLoading(loading);
        binding.progressBarPosts.bringToFront();

        setSupportActionBar(binding.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.menuNavigazione.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getRecentPost();
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                int statusCode = response.code();
                postList = response.body().getPosts();
                posts.addAll(postList);
                Log.d(TAG, "Post ricevuti correttamente");
                //recyclerView.setAdapter(new WordpressAdapter(postList, R.layout.post_item, getApplicationContext()));
                adapter = new WordpressAdapter(postList, getApplicationContext());
                binding.postsRecyclerView.setAdapter(adapter);
                binding.postsRecyclerView.addOnScrollListener(new EndlessScrollingListener((LinearLayoutManager) binding.postsRecyclerView.getLayoutManager()) {
                    @Override
                    public void onLoadMore(int page, int count) {
                        loading.set(true);
                        loadMorePosts(page, postList.size());
                    }
                });
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Errore nel caricamento dei dati dal WEB.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Errore primo caricamento post - " + t.toString());
            }
        });

    }

    public void loadMorePosts(int page, int count) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getPosts(page);
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                List<WordpressPost> morePosts = response.body().getPosts();
                postList.addAll(morePosts);
                loading.set(false);
                Log.d(TAG, "Ricevuti altri 10 post - Totale: " + postList.size());
                adapter.notifyItemRangeInserted(adapter.getItemCount(), postList.size() - 1);
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Errore nel caricamento dei dati dal WEB.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Errore caricamento in seguito a scrolling - " + t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            /*case R.id.action_settings:
                return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
