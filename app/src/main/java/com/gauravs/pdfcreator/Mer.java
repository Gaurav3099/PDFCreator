package com.gauravs.pdfcreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Mer extends AppCompatActivity {
   ListView lvpdfs;
    private ArrayAdapter<String> ListAdapter;
    File pdf, path;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();
    String pathv;
    String spath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer);
            lvpdfs= (ListView)findViewById(R.id.lvpdfs);
        registerForContextMenu(lvpdfs);
        pdf_paths.clear();
        pdf_names.clear();
        File docsFolder1 = new File(Environment.getExternalStorageDirectory()+"/PDFCreator");
        if (!docsFolder1.exists()){
            Toast.makeText(this, "Documents do not Exist", Toast.LENGTH_LONG).show();
        }else{
            //Access External storage
            path = new File(Environment.getExternalStorageDirectory() + "/PDFCreator");
            searchFolderRecursive1(path);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, pdf_names);
            Log.e("aaaaaaaaaa", ""+pdf_names);

            lvpdfs.setAdapter(adapter);
            lvpdfs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    final String path = pdf_paths.get(arg2);
                    pathv=path;
                    spath=path;

                    Intent i = new Intent();
                    i.putExtra("path",path);
                   // i.putExtra(pathv,"pathv");
                    setResult(Activity.RESULT_OK,i);
                    finish();
                    return;
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


}}}
