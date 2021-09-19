package com.gauravs.pdfcreator;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.File;
import java.io.FileWriter;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class ptt extends AppCompatActivity {
    AdView adViewptt;
    EditText Text;
    Button chf,save;
    private final int PICKFILE_RESULT_CODE=10;
    private String btTag="";
    String path;
    String fpath= String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
    String filename="";
    File pdffile;
    File exp;
    File mpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptt);
       // MobileAds.initialize(this,"ca-app-pub-6");

        init(this, "2", true);
        //adViewptt=(AdView)findViewById(R.id.adViewptt);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adViewptt.loadAd(adRequest);
        Text=(EditText)findViewById(R.id.Text);
        chf=(Button) findViewById(R.id.chf);
        save=(Button) findViewById(R.id.save);
        showFileChooser();
        if (!ESA() || ESR()){
           save.setEnabled(true);
        }else {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
        exp=new File(cw.getExternalFilesDir(fpath),filename);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Text.getText().toString().isEmpty()){
                    Toast.makeText(ptt.this,"Please Choose a PDF File",Toast.LENGTH_LONG).show();
                }
                else{
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ptt.this);

                //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle(" File Name");
                alertDialog.setTitle("PDF Creator");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                // Setting Dialog Message
                alertDialog.setMessage("Enter File Name");

                final EditText inputp = new EditText(ptt.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputp.setLayoutParams(lp);
                alertDialog.setView(inputp);
                alertDialog.setPositiveButton("Save",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        filename = inputp.getText().toString()+".txt";
                        String fsave = Text.getText().toString();
                        try {
                            File fileout=new File(fpath,filename);
                            FileWriter fw =new FileWriter(fileout);

                            fw.write(fsave);
                            Toast.makeText(ptt.this,"Text Converted To PDF, Saved in \n File path" +fpath,Toast.LENGTH_LONG).show();
                            fw.close();
                           // fw.flush();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }


            }}
        );

       chf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showFileChooser();
           }
       });

    }

    private boolean ESR() {
        String eS=Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(eS)){
            return true;
        }return false;
    }

    private boolean ESA() {
        String eS=Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(eS)){
            return true;
        }return false;
    }


    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();}
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
                     path = data.getData().getPath();
                    //final Intent intent= new Intent(Second.this, pdfview.class);
                    //intent.putExtra("path",path);
                    //startActivity(intent);
                    try {
                        String parsedText="";
                        PdfReader reader = new PdfReader(path);
                        int n = reader.getNumberOfPages();
                        for (int i = 0; i <n ; i++) {
                            parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
                            Text.setText(parsedText);
                        }
                        System.out.println(parsedText);
                        reader.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

}

