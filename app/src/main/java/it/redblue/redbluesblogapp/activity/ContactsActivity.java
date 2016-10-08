package it.redblue.redbluesblogapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.databinding.ActivityContactsBinding;
import it.redblue.redbluesblogapp.model.Mail;
import it.redblue.redbluesblogapp.model.MailResponse;
import it.redblue.redbluesblogapp.webservice.MailClient;
import it.redblue.redbluesblogapp.webservice.MailInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    public static final String MAIL = "MAIL";
    private DrawerLayout drawerLayout;
    private ObservableBoolean error;
    private Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContactsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        if (savedInstanceState == null) {
            mail = new Mail("", "", "", "");
        } else {
            mail = Parcels.unwrap(savedInstanceState.getParcelable(MAIL));
        }
        binding.setMail(mail);
        error = new ObservableBoolean();
        binding.setError(error);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Contacts");
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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


        binding.sendMail.setOnClickListener((v) -> {
            if (!binding.getMail().getName().isEmpty() && !binding.getMail().getEmail().isEmpty() && !binding.getMail().getSubject().isEmpty() && !binding.getMail().getContent().isEmpty()) {
                boolean error = false;
                if (!isValidEmail(binding.getMail().getEmail())) {
                    binding.email.setError("Email non valida");
                    error = true;
                }
                if (!error) {
                    MailInterface mailService = MailClient.getClient().create(MailInterface.class);
                    String from = "Email da RedBlue's Blog App: " + binding.getMail().getName() + "<" + binding.getMail().getEmail() + ">";
                    String clientIdAndSecret = "api" + ":" + "key-f42f2f79ca03cb9aac98521977f1b6a0";
                    String authorizationHeader = "Basic " + Base64.encodeToString(clientIdAndSecret.getBytes(), Base64.NO_WRAP);
                    Call<MailResponse> call = mailService.authUser(authorizationHeader, from, "redblue@red-blue.it", binding.getMail().getSubject(), binding.getMail().getContent());
                    call.enqueue(new Callback<MailResponse>() {
                        @Override
                        public void onResponse(Call<MailResponse> call, Response<MailResponse> response) {
                            Log.d(TAG, "Email inviata correttamente");
                            String responseMsg = response.body().getMessage();
                            binding.getMail().setName("");
                            binding.getMail().setEmail("");
                            binding.getMail().setSubject("");
                            binding.getMail().setContent("");
                            binding.fullName.setText("");
                            binding.email.setText("");
                            binding.subject.setText("");
                            binding.message.setText("");
                            binding.fullName.requestFocus();
                            Snackbar.make(binding.coordinator, "Email inviata correttamente. Grazie.", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<MailResponse> call, Throwable t) {
                            Log.e(TAG, "Errore nell'invio della mail: " + t.getMessage());
                            Snackbar.make(binding.coordinator, t.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Snackbar.make(binding.coordinator, "E' necessario compilare tuttti i campi.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    // Validazione email
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MAIL, Parcels.wrap(mail));
    }

}
