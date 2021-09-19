package com.gauravs.pdfcreator;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;
//import static com.gaurav.pdfcreator.R.id.etfname;

public class gallery extends AppCompatActivity {
    AdView adViewg;
    Button btnShare, btnLoad, btnMake;
    ImageView ivImg;
    EditText   etifile;
    String url;
    private String fname;
    private String ppath;
    String fnamem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

       // MobileAds.initialize(this,"ca-app-pub-6766459289558457~6200655771");
       // adViewg=(AdView)findViewById(R.id.adViewg);
       // AdRequest adRequest = new AdRequest.Builder().build();
       // adViewg.loadAd(adRequest);
        init(this, "204329486", true);
        btnLoad=(Button)findViewById(R.id.btnLoad);
        btnMake=(Button)findViewById(R.id.btnMake);
        ivImg=(ImageView)findViewById(R.id.ivImg);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        File  docsFolder = new File(Environment.getExternalStorageDirectory() ,"/PDFCreator");
        if (!docsFolder.exists()){
            docsFolder.mkdir();
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,123);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File  docsFolder = new File(Environment.getExternalStorageDirectory() ,"/PDFCreator");
                if (!docsFolder.exists()){
                    docsFolder.mkdir();
                }
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(i,123);
            }
        });

        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(gallery.this);
                //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                // Setting Dialog Title
                alertDialog.setTitle(" File Name");
                // Setting Dialog Message
                alertDialog.setMessage("Enter File Name");

                final EditText inputp = new EditText(gallery.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputp.setLayoutParams(lp);
                alertDialog.setView(inputp);
                //alertDialog.setView(input);
                // Setting Icon to Dialog
                // fname = inputp.getText().toString();
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Save",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        fnamem = inputp.getText().toString();
                        if (fnamem.isEmpty()){
                            return;
                        }else {
                            CreatePDF();
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
        });


    }

    private void CreatePDF() {
        Thread t = new Thread() {

            public void run()
            {
                // Input file
                String inputPath = ppath;
                // Output file


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = sdf.format(new Date());
               // fname=etifile.getText().toString();
                String path = "PDFCreator/"+fnamem;
                File docsFolder = new File(Environment.getExternalStorageDirectory(),path);
                if (!docsFolder.exists()){
                    docsFolder.mkdir();
                }
                String p = path;
                String outputPath = Environment.getExternalStoragePublicDirectory(p)+ File.separator + fnamem+timestamp + ".pdf";

                // Run conversion
                final boolean result = gallery.this.convertToPdf(inputPath, outputPath);

                // Notify the UI
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        if (result) Toast.makeText(gallery.this, "The JPG was successfully converted to PDF.", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(gallery.this, "An error occured while converting the JPG to PDF.", Toast.LENGTH_SHORT).show();

                    }   });
            }
        };

        t.start();
    }

    private boolean convertToPdf(String jpgFilePath, String outputPdfPath) {
        try
        {
            // Check if Jpg file exists or not

            File inputFile = new File(jpgFilePath);
            if (!inputFile.exists()) throw new Exception("File '" + jpgFilePath + "' doesn't exist.");

            // Create output file if needed
            File outputFile = new File(outputPdfPath);
            if (!outputFile.exists()) outputFile.createNewFile();


            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            Image image = Image.getInstance(jpgFilePath);
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(image);
            document.close();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && null != data)
        {
            ContentResolver c = getContentResolver();
            Cursor cursor = c.query(data.getData(), null ,null,null,null);
            cursor.moveToFirst();
            ppath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            Bitmap bm = BitmapFactory.decodeFile(ppath);
            ivImg.setImageBitmap(bm);
            url = MediaStore.Images.Media.insertImage(c, bm, "image", "description");

        }
    }

    }

