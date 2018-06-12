package com.polito.gruppo4.pixweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

import static android.support.v4.content.ContextCompat.startActivity;


public class GameActivity extends AppCompatActivity implements OnClickListener {

    WebView myBrowser;
    MyWebViewClient myWebViewC;
    private ImageButton back;

    protected void onCreate(Bundle savedInstanceState) {


        myWebViewC= new MyWebViewClient(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        myBrowser = (WebView) findViewById(R.id.sito);
       // myBrowser.setWebViewClient(myWebViewC);
        myBrowser.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Uri uri= Uri.parse(url);
                Uri base = Uri.parse("https://play.google.com/store");

                if (uri.equals(base)) {
                    Intent i = new Intent(Intent.ACTION_WEB_SEARCH, base);
                    startActivity(i);
                    return true; // Handle By application itself
                } else {
                    view.loadUrl(String.valueOf(uri));
                    return true;
                }
            }
        });
        myBrowser.loadUrl("file:///android_asset/index.html");

        back = (ImageButton) findViewById(R.id.tornaHome);
        back.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view.getId()==R.id.tornaHome){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
