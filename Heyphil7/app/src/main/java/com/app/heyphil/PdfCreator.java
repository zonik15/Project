package com.app.heyphil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;

/**
 * Created by ABDalisay on 8/16/2016.
 */
public class PdfCreator extends Activity {
    WebView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfview);
        pdfView=(WebView)findViewById(R.id.pdfView);
        pdfView.getSettings().setJavaScriptEnabled(true);
        String myPdfUrl = "https://apps.philcare.com.ph/ITGPortalTest/GenerateLOA.aspx?CaseNo="+ Data.caseno;
        String url1 = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        pdfView.loadUrl(url1);
        pdfView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}