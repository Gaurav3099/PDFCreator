package com.gauravs.pdfcreator;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS;
import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    AdView mAV;
    EditText etInput;
    Button btnSubmit;
    File pdffile;
    FloatingActionButton fabc;
    ImageButton ibmic;
    String fnamem;
    private String btTag="";
    String path;
    private DrawerLayout navd;
    private ActionBarDrawerToggle ntoggle;
    final private int REQUEST_CODE_ASK_PERMISSIONS=111;
    private final int REQ_CODE_VOICE_INPUT=100;
    private final int PICKFILE_RESULT_CODE=10;
    final private int REQUEST_CODE_ASK_PERMISSIONSs=11;
    final private int REQUEST_CODE_ASK_PERMISSIONSp=12;
    final private int REQUEST_CODE_ASK_PERMISSIONSptx=13;
    final private int REQUEST_CODE_ASK_PERMISSIONSt=14;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     //   if (item.getItemId() == R.id.pdf3) {
     //       Intent i = new Intent(MainActivity.this, viewer.class);
     //       startActivity(i);
     //   }
     //   if (item.getItemId() == R.id.gallery) {
     //       Intent i = new Intent(MainActivity.this, gallery.class);
      //      startActivity(i);
     //   }

        if (item.getItemId() == R.id.txtp) {
            //Intent i = new Intent(MainActivity.this,first.class);
            //startActivity(i);
            try {
                tx();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }
           // txp();
        }
        if (item.getItemId() == R.id.mypdf) {
            try {
                pd();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }
          //  Intent i = new Intent(MainActivity.this, Second.class);
          //  startActivity(i);
        }


        if (item.getItemId() == R.id.ptxt) {
            try {
                ptx();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }

        }

        if (ntoggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void txp() {
        showFileChooser();

    }

    private void showFileChooser(){
        Log.e("AA","bttag="+btTag);
        String folderPath = Environment. getExternalStorageDirectory()+"/";
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_GET_CONTENT);
        Uri myUri = Uri.parse(folderPath);
        intent.setDataAndType( myUri ,"file/*");
        Intent intentChooser = Intent.createChooser(intent, "Select a Text file");
        startActivityForResult(intentChooser, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            ///Add
       // MobileAds.initialize(this,"ca-app-pub-6766459289558457~6200655771");
       // mAV=(AdView)findViewById(R.id.adView);
        init(this, "204329486", true);

        etInput=(EditText)findViewById(R.id.etInput);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        ibmic=(ImageButton)findViewById(R.id.ibmic);
        fabc=(FloatingActionButton)findViewById(R.id.fabc);
        navd=(DrawerLayout)findViewById(R.id.navd);
        ntoggle=new ActionBarDrawerToggle(this,navd,R.string.open,R.string.close);
        navd.addDrawerListener(ntoggle);
        ntoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView= (NavigationView)findViewById(R.id.navn);
        navigationView.setNavigationItemSelectedListener(this);


        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ibmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVoiceInput();
            }
        });
        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pr();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (DocumentException e){
                    e.printStackTrace();
                }
               // Intent c = new Intent(MainActivity.this,Scan.class);
                //startActivity(c);
            }
        });
    }


    public void onClick(View view){
        if (etInput.getText().toString().isEmpty()){
            etInput.setError("Please Enter Text");
            etInput.requestFocus();
            return;
        }
        // Creating alert Dialog with one Button
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        // Setting Dialog Title
        alertDialog.setTitle(" File Name");
        alertDialog.setTitle("PDF Creator");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Dialog Message
        alertDialog.setMessage("Enter File Name");

        final EditText inputp = new EditText(MainActivity.this);
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

                try {
                    createPDF();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (DocumentException e){
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

    private void tx()throws FileNotFoundException,DocumentException {
        int wp= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (wp != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessage("You need to Allow access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONSt);
                            }

                        }
                    });return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }return;
        }
        else {
            txp();
        }
    }

    private void ptx()throws FileNotFoundException,DocumentException {
        int wp= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (wp != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessage("You need to Allow access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONSptx);
                            }

                        }
                    });return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }return;
        }
        else {
            Intent c = new Intent(MainActivity.this,ptt.class);
            startActivity(c);

        }
    }

    private void pd()throws FileNotFoundException,DocumentException {
        int wp= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (wp != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessage("You need to Allow access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONSp);
                            }

                        }
                    });return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }return;
        }
        else {
            Intent c = new Intent(MainActivity.this,Second.class);
            startActivity(c);

        }
    }

    private void pr()throws FileNotFoundException,DocumentException {
        int wp= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (wp != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessage("You need to Allow access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONSs);
                            }

                        }
                    });return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }return;
        }
        else {
            Intent c = new Intent(MainActivity.this,Scan.class);
            startActivity(c);

        }
    }
    //________________

    private void createPDF()throws FileNotFoundException,DocumentException {
        int wp= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (wp != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessage("You need to Allow access to Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }

                        }
                    });return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }return;
        }
        else {
            File docsFolder = new File(Environment.getExternalStorageDirectory()+"/PDFCreator");
            if (!docsFolder.exists()){
                docsFolder.mkdir();
            }
            String name = fnamem;
            pdffile=new File(docsFolder.getAbsolutePath(),name + ".pdf");
            OutputStream output = new FileOutputStream(pdffile);
            Document document=new Document();
            PdfWriter.getInstance(document,output);
            document.open();
            document.add(new Paragraph(etInput.getText().toString()));
            document.close();
            Toast.makeText(this,"Text Converted To PDF",Toast.LENGTH_SHORT).show();
            //showPDF();
            String path= String.valueOf(pdffile);
            final Intent intent= new Intent(MainActivity.this, pdfview.class);
            intent.putExtra("path",path);
            startActivity(intent);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try{
                        createPDF();
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e){
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
              case  REQUEST_CODE_ASK_PERMISSIONSs:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try{
                        pr();
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e){
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                  break;
            case  REQUEST_CODE_ASK_PERMISSIONSp:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try{
                        pd();
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e){
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case  REQUEST_CODE_ASK_PERMISSIONSptx:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try{
                        ptx();
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e){
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case  REQUEST_CODE_ASK_PERMISSIONSt:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try{
                        tx();
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e){
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showMessage(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK",okListener)
                .setNegativeButton("Cancel",null).create().show();
    }

    //____



    //____
    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("PDF Creator");
        builder.setMessage("Do you yant to close this Application?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNeutralButton("Rate us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent r = new Intent(Intent.ACTION_VIEW);
                r.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gauravs.pdfcreator"));
                startActivity(r);
            }
        });
        builder.create().show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id == R.id.txttpdf){
            try {
                tx();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }
            //Intent n = new Intent(MainActivity.this,Second.class);
            //startActivity(n);
        }else if (id == R.id.pttxt){
            try {
                ptx();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }

        }
           // Intent m = new Intent(MainActivity.this, first.class);
           // startActivity(m);

        else if (id == R.id.qr){
            Intent m = new Intent(MainActivity.this, qr.class);
            startActivity(m);
        }
        else if (item.getItemId() == R.id.mpdf) {
            try {
                pd();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (DocumentException e){
                e.printStackTrace();
            }
            //  Intent i = new Intent(MainActivity.this, Second.class);
            //  startActivity(i);
        }
        else if (id == R.id.fb){
            Intent s = new Intent(Intent.ACTION_SENDTO);
            s.setData(Uri.parse("mailto:"+ "gaurav.sarkar830@gmail.com"));
           // s.putExtra(Intent.EXTRA_TEXT,"" + res);
            startActivity(s);
        }

        else if (id == R.id.rate){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gauravs.pdfcreator"));
            startActivity(i);
        }
        else if (id == R.id.share){

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hey! I found this Amazing PDF Creator App, I recommend you to use this App"+ "\n" +
                    " Easy PDF Creator"+ "\n"
                    +" https://play.google.com/store/apps/details?id=com.gauravs.pdfcreator");
            startActivity(share);
        }
        else if (id == R.id.StT){
            getVoiceInput();
        }

       navd.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getVoiceInput() {
        Intent v = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        v.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        v.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        v.putExtra( RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,2000000000);
        v.putExtra(EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,2000000000);
        v.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please Speak Something !");
        try{
            startActivityForResult(v,REQ_CODE_VOICE_INPUT);
        }catch (ActivityNotFoundException a){
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_VOICE_INPUT:{
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etInput.setText(result.get(0));
                }break;
            }
        }

        if (data != null) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {
                    path = data.getData().getPath();
                    //final Intent intent= new Intent(Second.this, pdfview.class);
                    //intent.putExtra("path",path);
                    //startActivity(intent);
                    String line = null;

                    try {
                        FileInputStream fileInputStream = new FileInputStream (new File(path));
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();

                        while ( (line = bufferedReader.readLine()) != null )
                        {
                            stringBuilder.append(line + System.getProperty("line.separator"));
                        }
                        fileInputStream.close();
                        line = stringBuilder.toString();
                        etInput.setText(line);
                        bufferedReader.close();
                    }
                    catch(FileNotFoundException ex) {
                        //Log.d(TAG, ex.getMessage());
                        ex.printStackTrace();
                    }
                    catch(IOException ex) {
                        //Log.d(TAG, ex.getMessage());
                        ex.printStackTrace();
                    }
                   // return line;
                }
                }
            }
        }

    }



