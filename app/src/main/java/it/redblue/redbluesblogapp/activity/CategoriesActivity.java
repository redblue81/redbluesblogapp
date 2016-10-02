package it.redblue.redbluesblogapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.adapter.CategoriesAdapter;
import it.redblue.redbluesblogapp.databinding.ActivityCategoriesBinding;
import it.redblue.redbluesblogapp.model.SiteResponse;
import it.redblue.redbluesblogapp.model.WPCategory;
import it.redblue.redbluesblogapp.webservice.ApiClient;
import it.redblue.redbluesblogapp.webservice.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    private static final String TAG = CategoriesActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;

    private ObservableBoolean loading;
    private ObservableArrayList<WPCategory> categories;
    private CategoriesAdapter adapter;
    private List<WPCategory> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCategoriesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_categories);

        categories = new ObservableArrayList<>();
        binding.setCategories(categories);
        loading = new ObservableBoolean();
        binding.setLoading(loading);
        binding.progressBarCat.bringToFront();

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_cat);

        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                        if (!getComponentName().getClassName().equals(ContactsActivity.class.getCanonicalName())) {
                            intent = new Intent(getApplicationContext(), ContactsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getCategories();
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                int statusCode = response.code();
                categoryList = response.body().getCategories();
                categories.addAll(categoryList);
                Log.d(TAG, "Categorie ricevute correttamente");
                adapter = new CategoriesAdapter(categoryList, getApplicationContext());
                adapter.setOnItemClickListener((position, item) -> {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("catId", item.getId());
                            startActivity(intent);
                            //nackbar.make(binding.categoriesRecyclerView, Html.fromHtml(item.getTitle()), Snackbar.LENGTH_SHORT).show();
                        }
                );
                binding.categoriesRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                Log.e(TAG, "ERRORE NELLA RICEZIONE DELLE CATEGORIE");
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<SiteResponse> call = apiService.getCategories();
                call.enqueue(new Callback<SiteResponse>() {
                    @Override
                    public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                        int statusCode = response.code();
                        categoryList = response.body().getCategories();
                        categories.addAll(categoryList);
                        Log.d(TAG, "Categorie ricevute correttamente");
                        adapter = new CategoriesAdapter(categoryList, getApplicationContext());
                        adapter.setOnItemClickListener((position, item) -> {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("catId", item.getId());
                                    startActivity(intent);
                                }
                        );
                        binding.categoriesRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<SiteResponse> call, Throwable t) {
                        Log.e(TAG, "ERRORE NELLA RICEZIONE DELLE CATEGORIE");
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
