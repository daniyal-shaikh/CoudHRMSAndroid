package com.cloudhr.attendancepoc.webview;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // open in same app
       /* if (url.endsWith(".pdf")) {
            String googleDocs = "https://docs.google.com/viewer?url=";
            webView.loadUrl(googleDocs + url);
        }*/

        //open in pdfviewer
        if (url.endsWith(".pdf")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //user does not have a pdf viewer installed
                String googleDocs = "https://docs.google.com/viewer?url=";
                webView.loadUrl(googleDocs + url);
            }
        }
        return false;
    }
}