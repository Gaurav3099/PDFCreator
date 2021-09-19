package com.gauravs.pdfcreator;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import static android.R.layout.simple_list_item_1;

public class Merge extends FragmentActivity {
    private TextView txt1,txt2;
    ListView lvadd;
    private Button bt1,bt2;
    private Handler handler;
    private final int PICKFILE_RESULT_CODE=10;
    static ArrayList<String> path=new ArrayList<String>();
    private String btTag="";
    String[] srcs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
        txt1=(TextView)findViewById(R.id.txtfirstpdf);
        txt2=(TextView)findViewById(R.id.txtsecondpdf);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        lvadd = (ListView)findViewById(R.id.lvadd);

        String[] folder = new String[0];
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, simple_list_item_1, folder);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btTag= view.getTag().toString();
                showFileChooser();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btTag= v.getTag().toString();
                showFileChooser();

            }
        });}


    public void mergePdfFiles(View view){
        try {
             srcs = new String[]{txt1.getText().toString(), txt2.getText().toString()};
            mergePdf(srcs);
        }catch (Exception e){e.printStackTrace();}
    }

    public void mergePdf(String[] srcs){
        try{
            // Output file
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            // fname=etfname.getText().toString();
            String path = "PDFCreator/";
            File  docsFolder = new File(Environment.getExternalStorageDirectory() ,path);
            if (!docsFolder.exists()){
                docsFolder.mkdir();
            }

            String p = path;
            String outputPath = Environment.getExternalStoragePublicDirectory(p)+ File.separator  +timestamp + ".pdf";

            // Create document object
            // Document document = new Document();
            Document document = new Document();
            // Create pdf copy object to copy current document to the output mergedresult file
            // PdfCopy copy = new PdfCopy(document, new FileOutputStream(Environment.getExternalStorageDirectory()+"/mergedresult.pdf"));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream( Environment.getExternalStoragePublicDirectory(p)+ File.separator  +timestamp + ".pdf"));
            // Open the document
            document.open();
            PdfReader pr;
            int n;
            for (int i = 0; i < srcs.length; i++) {
                // Create pdf reader object to read each input pdf file
                pr = new PdfReader(srcs[i]);
                // Get the number of pages of the pdf file
                n = pr.getNumberOfPages();
                for (int page = 1; page <=n;page++) {
                    // Import all pages from the file to PdfCopy
                    copy.addPage( copy.getImportedPage(pr,page));
                }
            }
            document.close(); // close the document

        }catch(Exception e){e.printStackTrace();}
    }


    @Override
    // Save tag of the clicked button
    // It is used to identify the button has been pressed
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savText", btTag);


    }

    @Override
    // Restore the tag
    protected void onRestoreInstanceState( Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState);
        btTag=savedInstanceState.getString( "savText");

    }

    private void showFileChooser(){
        Log.e("AA","bttag="+btTag);
        String p = "PDFCreator/";
        File folderPath = new File(Environment.getExternalStorageDirectory() ,p);
        // String folderPath = String.valueOf(Environment.getExternalStoragePublicDirectory(p));
        Intent intent = new Intent(Merge.this,Mer.class);
        startActivityForResult(intent,5);
        //  intent.setAction( Intent.ACTION_GET_CONTENT);
        //  Uri myUri = Uri.parse(String.valueOf(folderPath));
        //  intent.setDataAndType( myUri, "file/*");  //"file/*"
        //  Intent intentChooser = Intent.createChooser(intent, "Select a file");
        //  startActivityForResult(intentChooser, PICKFILE_RESULT_CODE);
    }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //   super.onActivityResult(requestCode, resultCode, data);
    //   if (resultCode == 5){


    //   }
    // }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == 5) {
                if (resultCode == RESULT_OK) {
                    //String FilePath = data.getData().getPath();
                    String p = data.getStringExtra("path");
                    String FilePath = p;



                    String pn = p;

                    // final String path[] = new String[0]; //{"", "", ""}; //{""};
                    path.add(p);
                    ArrayAdapter<String> a = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, path);
                    lvadd.setAdapter(a);

                    if(bt1.getTag().toString().equals(btTag)){
                        txt1.setText(FilePath);
                    }
                    else
                        txt2.setText(FilePath);

                }
            }
        }
    }

}
