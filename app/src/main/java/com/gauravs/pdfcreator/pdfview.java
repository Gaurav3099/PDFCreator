package com.gauravs.pdfcreator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.File;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class pdfview extends AppCompatActivity {
    public PDFView pdfview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        pdfview = (PDFView)findViewById(R.id.pdfview);
        init(this, "2", true);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        File pd = new File(path);
        Uri uri = Uri.fromFile(pd);
        pdfview.fromUri(Uri.parse(String.valueOf(uri))).defaultPage(0).spacing(10).load();


    }
    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }

}
