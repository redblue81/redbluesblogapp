package it.redblue.redbluesblogapp.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.databinding.ActivityContactsBinding;
import it.redblue.redbluesblogapp.model.Page;
import it.redblue.redbluesblogapp.model.SiteResponse;
import it.redblue.redbluesblogapp.webservice.ApiClient;
import it.redblue.redbluesblogapp.webservice.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private ObservableBoolean error;
    private Page page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContactsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        page = new Page("", "");
        binding.setPage(page);
        error = new ObservableBoolean();
        binding.setError(error);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Contacts");
        }

        //binding.webView.loadUrl("http://www.red-blue.it/contacts");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SiteResponse> call = apiService.getPage("contacts");
        call.enqueue(new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                error.set(false);
                Page page = response.body().getPage();
                Log.d(TAG, "Pagina " + page.getTitle() + " ricevuta correttamente");
                binding.webView.getSettings().setJavaScriptEnabled(true);
                if (page.getContent().length() > 0) {
                    binding.webView.loadData(page.getContent(), "text/html", "UTF-8");
                } else {
                    error.set(true);
                    binding.errorTextView.setText("Errore nel caricamento dei dati");
                }
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
                error.set(true);
                binding.errorTextView.setText("Errore nel caricamento dei dati");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
