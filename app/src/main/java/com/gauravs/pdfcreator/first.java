package com.gauravs.pdfcreator;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;
//import static com.gaurav.pdfcreator.R.id.pdf1;

public class first extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    private ArrayAdapter<String> ListAdapter;
    AdView adViewf;
    ListView lvpdf;
    File pdf, path;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();
    String pathv;
    String spath;
    FloatingActionButton fablocal;
    private static final int READ_EXRERNAL_STORAGE = 100;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    final private int REQUEST_CODE_ASK_PERMISSIONS=111;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //AD
//        adViewf=(AdView)findViewById(R.id.adViewf);
       // AdRequest adRequest = new AdRequest.Builder().build();
       // adViewf.loadAd(adRequest);
        init(this, "2", true);
        lvpdf=(ListView) findViewById(R.id.lvpdf);
        fablocal=(FloatingActionButton)findViewById(R.id.fablocal);
        registerForContextMenu(lvpdf);
        pdf_paths.clear();
        pdf_names.clear();

        //Toast.makeText(this, "PDF Created using this App", Toast.LENGTH_LONG).show();
        fablocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(first.this,Second.class);
                startActivity(i);
            }
        });


    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_EXRERNAL_STORAGE);
    }else{

            show();
            File docsFolder1 = new File(Environment.getExternalStorageDirectory()+"/PDFCreator");
            if (!docsFolder1.exists()){
                Toast.makeText(this, "Documents do not Exist", Toast.LENGTH_LONG).show();
            }else{        //Access External storage

                path = new File(Environment.getExternalStorageDirectory() + "/PDFCreator");
                searchFolderRecursive1(path);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, pdf_names);
                Log.e("aaaaaaaaaa", ""+pdf_names);
                lvpdf.setAdapter(adapter);

                lvpdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        final String path = pdf_paths.get(arg2);
                        pathv=path;
                        spath=path;

                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(first.this);
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
                                                try{
                                                    final Intent intent= new Intent(first.this, pdfview.class);
                                                    intent.putExtra("path",path);
                                                    startActivity(intent);

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                break;
                                            case 1:
                                                String pathn= spath;
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
                                                if (file.exists()){
                                                    try {
                                                        file.getCanonicalFile().delete();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (file.exists()){
                                                        getApplicationContext().deleteFile(file.getName());
                                                    }
                                                }recreate();
                                                Toast.makeText(first.this,"File Deleted",Toast.LENGTH_SHORT).show();


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

            }}}

    private void show() {
    }

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







