package com.gauravs.pdfcreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class viewer extends AppCompatActivity {
    AdView adViewv;
    File path;
    ListView list;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();
    String pathv;
    String spath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        //AD
        //MobileAds.initialize(viewer.this,"ca-app-pub-6766459289558457~6200655771");
        //adViewv=(AdView)findViewById(R.id.adViewv);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adViewv.loadAd(adRequest);
        init(this, "204329486", true);
        list = (ListView) findViewById(R.id.listView1);
        pdf_paths.clear();
        pdf_names.clear();


        //Access External storage
        path = new File(Environment.getExternalStorageDirectory() + "");
        searchFolderRecursive1(path);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, pdf_names);
        Log.e("aaaaaaaaaa", "" + pdf_names);
        if (list.equals(0)){
            Toast.makeText(viewer.this,"Please say something",Toast.LENGTH_SHORT).show();
        }else{

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                final String path = pdf_paths.get(arg2);
                pathv = path;
                spath = path;

                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(viewer.this);
                builder.setTitle("PDF Creator");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setItems(new CharSequence[]
                                {"Open", "Share", "Delete", "Back"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        try {
                                            final Intent intent = new Intent(viewer.this, pdfview.class);
                                            intent.putExtra("path", path);
                                            startActivity(intent);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                    case 1:
                                        String pathn = spath;
                                        File outputFile = new File(pathn);
                                        Uri uri = Uri.fromFile(outputFile);

                                        Intent share = new Intent();
                                        share.setAction(Intent.ACTION_SEND);
                                        share.setType("application/pdf");
                                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(uri)));
                                        startActivity(share);

                                        break;
                                    case 2:
                                        File file = new File(path);
                                        file.delete();
                                        if (file.exists()) {
                                            try {
                                                file.getCanonicalFile().delete();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (file.exists()) {
                                                getApplicationContext().deleteFile(file.getName());
                                            }
                                        }
                                        recreate();
                                        Toast.makeText(viewer.this, "File Deleted", Toast.LENGTH_SHORT).show();


                                        break;
                                    case 3:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        });
                builder.create().show();


            }
        });


    }}

    private static void searchFolderRecursive1(File folder)
    {
        if (folder != null)
        {
            if (folder.listFiles() != null)
            {
                for (File file : folder.listFiles())
                {
                    if (file.isFile())
                    {
                        //.pdf files
                        if(file.getName().contains(".pdf"))
                        {
                            Log.e("ooooooooooooo", "path__="+file.getName());
                            file.getPath();
                            pdf_names.add(file.getName());
                            pdf_paths.add(file.getPath());
                            Log.e("pdf_paths", ""+pdf_names);
                        }
                    }
                    else
                    {
                        searchFolderRecursive1(file);
                    }
                }
            }
        }
    }
}
