package it.redblue.redbluesblogapp.adapter;

import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.redblue.redbluesblogapp.R;

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
        String data = string.substring(string.indexOf('-') + 1);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date postDate = null;
        String postDateString = null;
        try {
            postDate = df.parse(data);
            postDateString = dateFormat.format(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        view.setText(Html.fromHtml(author) + " - " + postDateString);
    }

    @BindingAdapter("bind:loadData")
    public static void loadData(WebView view, String string) {
        view.getSettings().setJavaScriptEnabled(true);
        view.loadData(string, "text/html; charset=UTF-8", null);
    }

}
