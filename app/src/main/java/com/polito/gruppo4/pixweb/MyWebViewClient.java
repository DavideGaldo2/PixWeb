package com.polito.gruppo4.pixweb;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.content.Intent;
import android.os.Message;
import android.os.Handler;
import android.content.Context;
import android.webkit.WebViewClient;

import static android.support.v4.content.ContextCompat.startActivity;



public class MyWebViewClient extends WebViewClient {

    private Context cont;

    public MyWebViewClient(Context c){
        super();
        cont=c;
    }



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        Uri uri= request.getUrl();
        Uri base = Uri.parse("market://search?q=");

        if (uri.equals(base)) {
            Intent i = new Intent(Intent.ACTION_VIEW, base);
            cont.startActivity(i);
            return true; // Handle By application itself
        } else {
            view.loadUrl(String.valueOf(uri));
            return true;
        }
    }





}
