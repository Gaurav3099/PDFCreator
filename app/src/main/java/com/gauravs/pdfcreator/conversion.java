package com.gauravs.pdfcreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.startapp.android.publish.adsCommon.StartAppSDK.init;

public class conversion extends AppCompatActivity {
    Button pdftotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        pdftotxt=(Button)findViewById(R.id.pdftotxt);
        init(this, "204329486", true);
        pdftotxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(conversion.this,ptt.class);
                startActivity(i);

            }
        });
    }
}
