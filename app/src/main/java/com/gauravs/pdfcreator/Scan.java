package com.gauravs.pdfcreator;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class Scan extends AppCompatActivity {

    AdView adg;
    ImageButton ibCam, ibg,itp;
    EditText etfname;
    ImageView ivPhoto;
    public File photo;
    Bitmap ph;
    String fnamem;
    String name;
    File pdffile;
    private String fname;
    String pfn;
    private final int PICKFILE_RESULT_CODE=10;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private int CAMERA_PERMISSION_CODE = 24;
    private String btTag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //btnScan = (Button) findViewById(R.id.btnScan);
        itp = (ImageButton) findViewById(R.id.itp);
        ibg = (ImageButton) findViewById(R.id.ibg);
        ibCam = (ImageButton) findViewById(R.id.ibCam);
        init(this, "204329486", true);
       // MobileAds.initialize(this,"ca-app-pub-6766459289558457~6200655771");
       // adg=(AdView)findViewById(R.id.adViews);
       // AdRequest adRequest = new AdRequest.Builder().build();

       // adg.loadAd(adRequest);
        StartAppAd.showAd(this);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        itp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isStorageReadable()) {
                    //If permission is already having then showing the toast
                    showFileChooser();
                    //Toast.makeText(Scan.this, "You already have the permission to Access Storage", Toast.LENGTH_LONG).show();
                    //Existing the method with return
                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
                //showFileChooser();
                //  extractImages(Environment.getExternalStorageDirectory()+"/"+"cam.pdf");
            }
        });


        ibg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g = new Intent(Scan.this,gallery.class);
                startActivity(g);

            }
        });


        ibCam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (canOpenCamera()) {
                    //If permission is already having then showing the toast
                    Toast.makeText(Scan.this, "You already have the permission To Open Camera", Toast.LENGTH_LONG).show();

                    Cam();
                    //Existing the method with return
                    return;
                }

                //If the app has not the permission then asking for the permission
                requestCameraPermission();

            }

            private String getPictureName() {
                return  "pp" + ".jpg";
            }
        });

    }
    private boolean isStorageReadable() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Permission Required to Access Storage", Toast.LENGTH_SHORT).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(getApplicationContext(), "Permission Required to Open Camera", Toast.LENGTH_SHORT).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }


    private boolean canOpenCamera() {
            //Getting the permission status
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            //If permission is granted returning true
            if (result == PackageManager.PERMISSION_GRANTED)
                return true;

            //If permission is not granted returning false
            return false;
        }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                showFileChooser();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the Storage permission",Toast.LENGTH_LONG).show();
            }
        } else if(requestCode == CAMERA_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Cam();
                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can open camera",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the Camera permission",Toast.LENGTH_LONG).show();
            }
        }
    }
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
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //}

    private void extractImages(String filepath){

        PRStream prStream;

        PdfObject pdfObject;

        PdfImageObject pdfImageObject;

        FileOutputStream fos;

        try {

            // Create pdf reader

            PdfReader reader = new PdfReader(filepath);

            // Get number of objects in pdf document

            int numOfObject=reader.getXrefSize();

            for(int i=0;i<numOfObject;i++){

                // Get PdfObject

                pdfObject=reader.getPdfObject(i);

                if(pdfObject!=null && pdfObject.isStream()){

                    prStream= (PRStream)pdfObject; //cast object to stream

                    PdfObject type =prStream.get(PdfName.SUBTYPE); //get the object type

                    // Check if the object is the image type object

                    if (type != null && type.toString().equals(PdfName.IMAGE.toString())){

                        // Get the image from the stream
                        String path = "PDFCreator/Images";
                        File  docsFolder = new File(Environment.getExternalStorageDirectory() ,path);
                        if (!docsFolder.exists()){
                            docsFolder.mkdir();
                        }

                        String p = path;
                        // String outputPath = Environment.getExternalStoragePublicDirectory(p)+ File.separator + fnamem +timestamp + ".pdf";

                        pdfImageObject= new PdfImageObject(prStream);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String timestamp = sdf.format(new Date());

                        fos=new FileOutputStream(Environment.getExternalStoragePublicDirectory(p)+"/image"+i+timestamp+".jpg");

                        // Read bytes of image in to an array

                        byte[] imgdata=pdfImageObject.getImageAsBytes();

                        // Write the bytes array to file

                        fos.write(imgdata, 0, imgdata.length);
                        //Toast.makeText(Scan.this,"Text Converted To PDF, Saved in \n File path" +path,Toast.LENGTH_LONG).show();
                        fos.flush();

                        fos.close();
                        Toast.makeText(Scan.this,"PDF Converted To Image, Saved in \n File path " + path,Toast.LENGTH_LONG).show();
                    }
                }
            }



        }catch (IOException ioe){

            ioe.printStackTrace();

        }

    }

    private void Cam() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Scan.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(" File Name");
        alertDialog.setTitle("PDF Creator");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Dialog Message
        alertDialog.setMessage("Enter File Name");

        final EditText inputp = new EditText(Scan.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputp.setLayoutParams(lp);
        alertDialog.setView(inputp);

        alertDialog.setPositiveButton("Open Camera",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                // Write your code here to execute after dialog
                fnamem = inputp.getText().toString();
                if (fnamem.isEmpty()){
                    return;
                }else {
                    Camera();

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

        File  docsFolder = new File(Environment.getExternalStorageDirectory() ,"/PDFCreator");
        if (!docsFolder.exists()){
            docsFolder.mkdir();
        }

    }


    private void Camera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String pinn=getPictureName();
        File imgF = new File(pic,pinn);
        Uri picuri = Uri.fromFile(imgF);
        i.putExtra(MediaStore.EXTRA_OUTPUT,picuri);
        startActivityForResult(i, 123);


    }
    private String getPictureName() {
        return  "pp" + ".jpg";
    }


    private boolean convertToPdf(String jpgFilePath, String outputPdfPath) {
        try
        {
            // Check if Jpg file exists or not
            File inputFile = new File(jpgFilePath);
            if (!inputFile.exists()) throw new Exception("File '" + jpgFilePath + "' doesn't exist.");
            // Create output file if needed
            File outputFile = new File(outputPdfPath);
           // Document document = new Document();
            File docsFolder = new File(Environment.getExternalStorageDirectory()+"/PDFCreator");
            if (!docsFolder.exists()){
                docsFolder.mkdir();
            }


            if (!outputFile.exists()){ //outputFile.createNewFile();
                Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            Image image = Image.getInstance(jpgFilePath);
                // Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(outputFile));
                document.open();
               // Image image = Image.getInstance(jpgFilePath);
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
               image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP );
                document.add(image);
                document.close();

            Camera();
            return true;
        }else{
                //PdfReader te = new PdfReader(String.valueOf(outputFile));
                //int count=te.getNumberOfPages()+1;
                //te.close();
                name = "temp";
                pdffile=new File(docsFolder.getAbsolutePath(),name + ".pdf");
              //  OutputStream output = new FileOutputStream(pdffile);
               // Document doc=new Document();
               // PdfWriter.getInstance(doc,output);
               // doc.open();
               // doc.newPage();
               // Image image = Image.getInstance(jpgFilePath);
                // Document document = new Document();
                // PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
                // doc.open();
                // Image image = Image.getInstance(jpgFilePath);
                // float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                //         - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                // image.scalePercent(scaler);
                // image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                // doc.add(".");
              //  String txt ="kj";
              //  doc.add(new Paragraph(txt));
              //  doc.close();
                PdfReader reader = new PdfReader(String.valueOf(outputFile));
                int count=reader.getNumberOfPages()+1;
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdffile));
                stamper.insertPage(reader.getNumberOfPages()+1, reader.getPageSize(1));

                PdfContentByte content = stamper.getOverContent(count);

                Image image = Image.getInstance(jpgFilePath);
                // scale the image to 50px height
                image.scaleAbsoluteHeight(800);
                image.scaleAbsoluteWidth((image.getWidth()*900)/image.getHeight() );
                image.setAbsolutePosition(62,0);

                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                content.addImage(image);
                image.setInitialRotation(0);
                //image.getInitialRotation();
                stamper.close();

               pdffile.renameTo(outputFile);

                Camera();
                return true;

            }
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
        if (requestCode == 123 && resultCode == RESULT_OK)
        {
            CreatePDF();
           // Camera();
        }
        if (data != null) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {
                    pfn = data.getData().getPath();
                    extractImages(pfn);
                }
            }
        }
    }

    private void CreatePDF() {

        Thread t = new Thread() {

            public void run()
            {
                // Input file
                String inputPath =Environment.getExternalStorageDirectory() + File.separator + "/Documents/pp.jpg";

                // Output file
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = sdf.format(new Date());
                // fname=etfname.getText().toString();
                String path = "PDFCreator/"+fnamem;
                File  docsFolder = new File(Environment.getExternalStorageDirectory() ,path);
                if (!docsFolder.exists()){
                    docsFolder.mkdir();
                }

                String p = path;
                String outputPath = Environment.getExternalStoragePublicDirectory(p)+ File.separator + fnamem  + ".pdf";

                // Run conversion
                final boolean result = Scan.this.convertToPdf(inputPath, outputPath);

                // Notify the UI
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        if (result) Toast.makeText(Scan.this, "The JPG was successfully converted to PDF.", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(Scan.this, "An error occured while converting the JPG to PDF.", Toast.LENGTH_SHORT).show();
                    }   });
            }
        };

        t.start();

    }

}





