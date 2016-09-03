package it.redblue.redbluesblogapp.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.webkit.WebView;

import java.io.InputStream;

import it.redblue.redbluesblogapp.activity.MainActivity;

/**
 * Created by redblue on 11/08/16.
 */
public class Utilities {

    // Per il momento non viene più utilizzato
    public static void injectCSS(Context context, WebView webView) {
        try {
            InputStream inputStream = context.getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
