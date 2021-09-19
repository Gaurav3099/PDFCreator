package com.gauravs.pdfcreator;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.layout.simple_list_item_1;
import static com.gauravs.pdfcreator.R.id.lvpdf;
import static com.startapp.android.publish.adsCommon.StartAppSDK.init;
//import static com.gaurav.pdfcreator.R.id.pdf1;


public class Second extends AppCompatActivity {
    AdView adViewsec;
   public ArrayAdapter<String> ListAdapter;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();
    ListView lvmypdf,lv2;
    String path2;
    File pmypdf;
    String path;
    Spinner spnFolder;
    FloatingActionButton fabfile;
    String pathv;
    String spath;
    private final int PICKFILE_RESULT_CODE=10;
    private static final int READ_EXRERNAL_STORAGE = 100;
    private String btTag="";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

       // MobileAds.initialize(this,"ca-app-pub-67");
       // adViewsec=(AdView)findViewById(R.id.adViewsec);
       // AdRequest adRequest = new AdRequest.Builder().build();
       // adViewsec.loadAd(adRequest);
        init(this, "2", true);

        lvmypdf=( ListView)findViewById(R.id.lvmypdf);
       // lv2=(ListView)findViewById(R.id.lv2);
        fabfile=(FloatingActionButton)findViewById(R.id.fabfile);
        spnFolder=(Spinner)findViewById(R.id.spnFolder);
        pdf_paths.clear();
        pdf_names.clear();
        final String folder[] = {"My PDF","Documents", "Downloads", "WhatsApp","All"};
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, simple_list_item_1, folder);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                               READ_EXRERNAL_STORAGE);
        }
            else{
            final File docsFolderm = new File(String.valueOf(Environment.getExternalStorageDirectory()+"/PDFCreator"));
            final File docsFolder1 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
        File docsFolder2 = new File(Environment.getExternalStorageDirectory()+"/Download");
        final File docsFolder3 = new File(Environment.getExternalStorageDirectory()+ "/WhatsApp/Media/WhatsApp Documents");
        File docsFolder4 = (Environment.getExternalStorageDirectory());
            if (!docsFolderm.exists()){
               // docsFolderm.mkdir();
                Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
            }
        if (!docsFolder1.exists()){
           // docsFolder1.mkdir();
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }
        if (!docsFolder2.exists()){
            docsFolder2.mkdir();
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }
        if (!docsFolder3.exists()){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }

       else if (!docsFolder1.exists() && !docsFolder2.exists() &&!docsFolder3.exists() &&!docsFolder4.exists()){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }

             fabfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        spnFolder.setAdapter(a);

    spnFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           switch (i){
               case 0:
                   //File docsFolderm =  new File(String.valueOf(Environment.getExternalStorageDirectory()+"/PDFCreator"));
                    Mypdf();
                  // WhatsApp();
                   break;

               case 1:
                   Documents();
                   break;
               case 2:
                   Downloads();
                   break;

               case 3:
                   File docsFolder3 = new File(Environment.getExternalStorageDirectory()+ "/WhatsApp/Media/WhatsApp Documents");
                   if (!docsFolder3.exists()){
                       File docsFolder1 = new File(Environment.getExternalStorageDirectory()+"/WhatsApp");
                       docsFolder1.mkdir();
                         return;
                   }
                   WhatsApp();
                   break;
               case 4:
                   Intent v = new Intent(Second.this,viewer.class);
                   startActivity(v);
                   break;
                default:
                   Local();
           }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Local();
        }
    });
  }}

    private void showFileChooser(){
        Log.e("AA","bttag="+btTag);
        String folderPath = Environment. getExternalStorageDirectory()+"/";
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_GET_CONTENT);
        Uri myUri = Uri.parse(folderPath);
        intent.setDataAndType( myUri ,"file/*");
        Intent intentChooser = Intent.createChooser(intent, "Select a file");
        startActivityForResult(intentChooser, PICKFILE_RESULT_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    final Intent intent= new Intent(Second.this, pdfview.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }
            }
        }
    }


    private void Mypdf() {
        File docsFolder1 = new File(Environment.getExternalStorageDirectory()+"/PDFCreator");
        if (!docsFolder1.exists()){
            Toast.makeText(this, "Documents do not Exist", Toast.LENGTH_LONG).show();
        }else{        //Access External storage
            pdf_paths.clear();
            pdf_names.clear();
            pmypdf = new File(Environment.getExternalStorageDirectory() + "/PDFCreator");
            searchFolderRecursive1(pmypdf);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, pdf_names);
            Log.e("aaaaaaaaaa", ""+pdf_names);
            lvmypdf.setAdapter(adapter);

            lvmypdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    final String path = pdf_paths.get(arg2);
                    pathv=path;
                    spath=path;

                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Second.this);
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
                                                final Intent intent= new Intent(Second.this, pdfview.class);
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
                                            Toast.makeText(Second.this,"File Deleted",Toast.LENGTH_SHORT).show();


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

        }

        }

    private void Local() {  //ALL files
        File images = Environment.getExternalStorageDirectory();
        final File[] imagelist = images.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.endsWith(".pdf")));
            }
        });
        String[] pdflist = new String[imagelist.length];
        for (int i = 0; i < imagelist.length; i++) {
            pdflist[i] = imagelist[i].getName();
        }if (pdflist.length==0){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }else {
        lvmypdf.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,pdflist));
           // pathv=path;
            //spath=path;
        lvmypdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int id, long l) {
                path2 = imagelist[(int) id].getAbsolutePath();
              popact();
                 }
                 });
    }}
  private void WhatsApp() {
        File images4 = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents");

        final File[] imagelist4 = images4.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.endsWith(".pdf")));
            }
        });

        String[] pdflist4 = new String[imagelist4.length];
        for (int i = 0; i < imagelist4.length; i++) {
            pdflist4[i] = imagelist4[i].getName();
        }if (pdflist4.length==0){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }else {
        lvmypdf.setAdapter(new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pdflist4));
        lvmypdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int id, long l) {
                 path2 = imagelist4[(int) id].getAbsolutePath();
                //String path2 = imagelist4[(int) id].getAbsolutePath();
                popact();
             }
        });
    }}



    private void Downloads() {
        File images3 = new File(Environment.getExternalStorageDirectory() + "/Download");
        final File[] imagelist3 = images3.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.endsWith(".pdf")));
            }
        });

        String[] pdflist3 = new String[imagelist3.length];
        for (int i = 0; i < imagelist3.length; i++) {
            pdflist3[i] = imagelist3[i].getName();
        }if (pdflist3.length==0){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }else{
        lvmypdf.setAdapter(new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pdflist3));
            pathv=path;
            spath=path;
        lvmypdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int id, long l) {
                path2 = imagelist3[(int) id].getAbsolutePath();
                popact();
                  }

        });
    }}

    private void Documents() {
        File images2 = new File(Environment.getExternalStorageDirectory() + "/Documents");
        final File[] imagelist2 = images2.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.endsWith(".pdf")));
            }
        });

        String[] pdflist2 = new String[imagelist2.length];
        for (int i = 0; i < imagelist2.length; i++) {
            pdflist2[i] = imagelist2[i].getName();
        }if (pdflist2.length==0){
            Toast.makeText(this, "No PDF To Show", Toast.LENGTH_SHORT).show();
        }{
        lvmypdf.setAdapter(new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pdflist2));
        lvmypdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int id, long l) {
                path2 = imagelist2[(int) id].getAbsolutePath();
            popact();

            }

        });

    }



}
    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
    private void popact() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Second.this);
        builder.setTitle("PDF Creator");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(new CharSequence[]
                        {"Open", "Share", "Delete", "Back"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        //String path2 = imagelist4[(int) id].getAbsolutePath();
                        switch (which) {
                            case 0:
                                // path2 = imagelist4[(int) id].getAbsolutePath();
                                openPdfIntent(path2);

                                break;
                            case 1:
                                String pathn= path2;
                                File outputFile = new File(pathn);
                                Uri uri = Uri.fromFile(outputFile);

                                Intent share = new Intent();
                                share.setAction(Intent.ACTION_SEND);
                                share.setType("application/pdf");
                                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(uri)));
                                startActivity(share);

                                break;
                            case 2:
                                File file = new File(path2);
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
                                Toast.makeText(Second.this,"File Deleted",Toast.LENGTH_SHORT).show();


                                break;
                            case 3:
                                dialog.cancel();
                                break;
                        }
                    }
                });
        builder.create().show();
    } private void openPdfIntent(String path) {

        try{
            final Intent intent= new Intent(Second.this, pdfview.class);
            intent.putExtra("path",path);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
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



