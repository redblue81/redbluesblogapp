package it.redblue.redbluesblogapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
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

    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;

    private WordpressAdapter adapter;
    private List<WordpressPost> postList;
    private ObservableArrayList<WordpressPost> posts;
    private ObservableBoolean loading;
    private long catId;
    private String excerpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        catId = getIntent().getLongExtra("catId", 0);

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
                Intent intent;
                closeNavDrawer();
                switch (item.getTitle().toString().toLowerCase()) {
                    case "home":
                        if (!getComponentName().getClassName().equals(MainActivity.class.getCanonicalName())) {
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;
                    case "categorie":
                        if (!getComponentName().getClassName().equals(CategoriesActivity.class.getCanonicalName())) {
                            intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;
                    case "contatti":
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call;
        List<WordpressPost> pl = new ArrayList<WordpressPost>();
        if (catId == 0)
            call = apiService.getRecentPost();
        else
            call = apiService.getCategoryPosts(catId, 1);
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                int statusCode = response.code();
                postList = response.body().getPosts();
                posts.addAll(postList);
                Log.d(TAG, "Post ricevuti correttamente");
                //recyclerView.setAdapter(new WordpressAdapter(postList, R.layout.post_item, getApplicationContext()));
                adapter = new WordpressAdapter(postList, getApplicationContext());
                adapter.setOnItemClickListener((position, item) -> {
                            Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                            intent.putExtra("postId", item.getId());
                            startActivity(intent);
                            //Snackbar.make(binding.postsRecyclerView, Html.fromHtml(item.getTitle()), Snackbar.LENGTH_SHORT).show()
                        }
                );
                if (postList.size() != 0 && catId != 0) {
                    Iterator<WordpressPost> iter = postList.iterator();
                    while (iter.hasNext()) {
                        WordpressPost wp = iter.next();
                        call = apiService.getExcerptForPost(wp.getId());
                        call.enqueue(new Callback<SiteResponse>() {
                            @Override
                            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                                excerpt = response.body().getExcerpt();
                                postList.get(postList.indexOf(wp)).setExcerpt(excerpt);
                                adapter.notifyItemChanged(postList.indexOf(wp));
                                Log.d(TAG, "Excerpt del post '" + wp.getTitle() + "' aggiunto correttamente");
                            }

                            @Override
                            public void onFailure(Call<SiteResponse> call, Throwable t) {
                                Log.e(TAG, "ERRORE: " + t.getMessage());
                                Log.e(TAG, "Impossibile recuperare l'excerpt del post " + postList.get(postList.indexOf(wp)).getTitle());
                            }
                        });
                    }
                }
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

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<SiteResponse> call;
                List<WordpressPost> pl = new ArrayList<WordpressPost>();
                if (catId == 0)
                    call = apiService.getRecentPost();
                else
                    call = apiService.getCategoryPosts(catId, 1);
                call.enqueue(new Callback<SiteResponse>() {
                    @Override
                    public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                        int statusCode = response.code();
                        postList = response.body().getPosts();
                        posts.addAll(postList);
                        Log.d(TAG, "Post ricevuti correttamente");
                        //recyclerView.setAdapter(new WordpressAdapter(postList, R.layout.post_item, getApplicationContext()));
                        adapter = new WordpressAdapter(postList, getApplicationContext());
                        adapter.setOnItemClickListener((position, item) -> {
                                    Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                                    intent.putExtra("postId", item.getId());
                                    startActivity(intent);
                                    //Snackbar.make(binding.postsRecyclerView, Html.fromHtml(item.getTitle()), Snackbar.LENGTH_SHORT).show()
                                }
                        );
                        if (postList.size() != 0 && catId != 0) {
                            Iterator<WordpressPost> iter = postList.iterator();
                            while (iter.hasNext()) {
                                WordpressPost wp = iter.next();
                                call = apiService.getExcerptForPost(wp.getId());
                                call.enqueue(new Callback<SiteResponse>() {
                                    @Override
                                    public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                                        excerpt = response.body().getExcerpt();
                                        postList.get(postList.indexOf(wp)).setExcerpt(excerpt);
                                        adapter.notifyItemChanged(postList.indexOf(wp));
                                        Log.d(TAG, "Excerpt del post '" + wp.getTitle() + "' aggiunto correttamente");
                                    }

                                    @Override
                                    public void onFailure(Call<SiteResponse> call, Throwable t) {
                                        Log.e(TAG, "ERRORE: " + t.getMessage());
                                        Log.e(TAG, "Impossibile recuperare l'excerpt del post " + postList.get(postList.indexOf(wp)).getTitle());
                                    }
                                });
                            }
                        }
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
                if (binding.swipeRefreshLayout.isRefreshing()) {
                    Log.d(TAG, "Stato refresh: " + binding.swipeRefreshLayout.isRefreshing());
                    binding.swipeRefreshLayout.setRefreshing(false);
                };
            }
        });
    }

    public void loadMorePosts(int page, int count) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call;
        //List<WordpressPost> pl = new ArrayList<>();
        if (catId == 0)
            call = apiService.getPosts(page);
        else
            call = apiService.getCategoryPosts(catId, page);
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                List<WordpressPost> morePosts = response.body().getPosts();
                if (morePosts.size() != 0 && catId != 0) {
                    Iterator<WordpressPost> iter = morePosts.iterator();
                    while (iter.hasNext()) {
                        WordpressPost wp = iter.next();
                        call = apiService.getExcerptForPost(wp.getId());
                        call.enqueue(new Callback<SiteResponse>() {
                            @Override
                            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                                excerpt = response.body().getExcerpt();
                                morePosts.get(morePosts.indexOf(wp)).setExcerpt(excerpt);
                                adapter.notifyItemChanged(postList.indexOf(wp));
                                Log.d(TAG, "Excerpt del post '" + wp.getTitle() + "' aggiunto correttamente");
                            }

                            @Override
                            public void onFailure(Call<SiteResponse> call, Throwable t) {
                                Log.e(TAG, "ERRORE: " + t.getMessage());
                                Log.e(TAG, "Impossibile recuperare l'excerpt del post " + morePosts.get(morePosts.indexOf(wp)).getTitle());
                            }
                        });
                    }
                }
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
