package it.redblue.redbluesblogapp.adapter;

import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.util.Utilities;

/**
 * Created by redblue on 11/08/16.
 */
public class BindingAdapters {

    @BindingAdapter("bind:htmlBinder")
    public static void toHtml(TextView view, String string) {
        view.setText(Html.fromHtml(string));
    }

    @BindingAdapter("bind:dataBinder")
    public static void toItalianDate(TextView view, String string) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date postDate = null;
        String postDateString = null;
        try {
            postDate = df.parse(string);
            postDateString = dateFormat.format(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.setText(postDateString != null ? postDateString : "");
    }

    @BindingAdapter("bind:src")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !"".equals(url))
            Picasso.with(view.getContext()).load(url).placeholder(R.drawable.logo).into(view);
        else
            view.setImageResource(R.drawable.logo);
    }

    @BindingAdapter("bind:subtitle")
    public static void subTitleText(TextView view, String string) {
        String author = string.substring(0, string.indexOf('-') - 1);
        String data = string.substring(string.indexOf('-') + 2);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date postDate = null;
        String postDateString = null;
        if (data != null && !"".equals(data)) {
            try {
                postDate = df.parse(data);
                postDateString = dateFormat.format(postDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        view.setText(Html.fromHtml(author) + " - " + postDateString);
    }

    @BindingAdapter("bind:loadData")
    public static void loadData(WebView view, String string) {
        view.getSettings().setJavaScriptEnabled(true);
        // Preferisco l'applicazione diretta del CSS, per non avere l'effetto del caricamento
        // visibile durante l'uso dell'app
/*        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                // Applica il css personalizzato dopo il caricamento della pagina
                Utilities.injectCSS(view.getContext(), view);
                super.onPageFinished(view, url);
            }
        });
*/
        if (string.length() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><head><link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></head><body>");
            sb.append(string.substring(0, string.indexOf("<div class=\"addtoany_share_save_container") - 1));
            sb.append("</body></html>");
            //view.loadData(string.substring(0, string.indexOf("<div class=\"addtoany_share_save_container") - 1), "text/html; charset=UTF-8", null);
            view.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
        }
    }

}
