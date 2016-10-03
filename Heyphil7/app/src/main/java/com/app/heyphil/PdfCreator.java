package com.app.heyphil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ABDalisay on 8/16/2016.
 */
public class PdfCreator extends Activity {
    WebView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfview);
        pdfView = (WebView)findViewById(R.id.pdfView);
        webView();
    }

    //Metodo llamar el webview
    private void webView(){
        //Habilitar JavaScript (Videos youtube)
        String myPdfUrl = "https://apps.philcare.com.ph/ITGPortalTest/GenerateLOA.aspx?CaseNo="+ Data.caseno;
        String url1 = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        pdfView.getSettings().setJavaScriptEnabled(true);

        //Handling Page Navigation
        pdfView.setWebViewClient(new MyWebViewClient());

        //Load a URL on WebView
        pdfView.loadUrl(url1);
    }

    // Metodo Navigating web page history
    @Override public void onBackPressed() {
        if(pdfView.canGoBack()) {
            pdfView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Subclase WebViewClient() para Handling Page Navigation
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("docs.google.com")) { //Force to open the url in WEBVIEW
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

}