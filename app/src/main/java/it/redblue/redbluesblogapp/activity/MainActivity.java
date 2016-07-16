package it.redblue.redbluesblogapp.activity;

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
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        recyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NavigationView navigationView = (NavigationView) findViewById(R.id.menuNavigazione);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
