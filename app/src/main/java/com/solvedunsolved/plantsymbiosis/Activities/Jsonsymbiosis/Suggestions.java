package com.solvedunsolved.plantsymbiosis.Activities.Jsonsymbiosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.solvedunsolved.plantsymbiosis.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import maes.tech.intentanim.CustomIntent;

public class Suggestions extends AppCompatActivity {

    private TextView headname;
    private RadioGroup RG;
    private RadioButton TB1;
    private RadioButton TB2;
    private CheckBox checkBox;
    private TextView plant2;
    private TextView plant3;
    private TextView plant4;
    private ImageView[] imageViews = new ImageView[64];
    private static String URL="https://raw.githubusercontent.com/pranay-bh/json/master/symbiosis.json";
    String uri;
    String uri2;
    String uri3;
    String uri4;
    String imgUrl;
//    int imageResource ;
    int imageResource2;
    int imageResource3;
    int imageResource4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        CustomIntent.customType(this, "left-to-right");
        setupui();

        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.TB1 && checkBox.isChecked()) {
                    plant2.setVisibility(View.VISIBLE);
                    plant3.setVisibility(View.INVISIBLE);
                    plant4.setVisibility(View.VISIBLE);
                    display1();
                }
                else if(checkedId == R.id.TB1 && !checkBox.isChecked()) {
                    plant2.setVisibility(View.VISIBLE);
                    plant3.setVisibility(View.INVISIBLE);
                    display2();
                }
                else if(checkedId == R.id.TB2 && checkBox.isChecked()) {
                    plant2.setVisibility(View.VISIBLE);
                    plant3.setVisibility(View.VISIBLE);
                    plant4.setVisibility(View.VISIBLE);
                    display3();
                }
                else {
                    plant2.setVisibility(View.VISIBLE);
                    plant3.setVisibility(View.VISIBLE);
                    plant4.setVisibility(View.INVISIBLE);
                    display4();

                }
            }
        });
    }

    void display1() {
        for(int i=0; i<64; i+=3) putimage1(i);
        for(int i=1; i<64; i+=3) putimage(i,imageResource2);
        for(int i=2; i<64; i+=3) putimage(i,imageResource4);
    }


    void display2() {
        for(int i=0; i<64; i+=2) { putimage1(i); }
        for(int i=1; i<64; i+=2) {   putimage(i,imageResource2); }
    }

    void display3() {

        for(int i=0;i<8;i++) {
            putimage1(i);
            putimage1(i+56);
        }
        for(int i=8;i<54;i+=8) {
         putimage1(i);
         putimage1(i+7);
         }

        for(int i=9;i<15;i++) {
            putimage(i,imageResource4);
            putimage(i+40,imageResource4);
        }
        for(int i=17;i<54;i+=8) {
            putimage(i,imageResource4);
            putimage(i+5,imageResource4);
        }

        for(int i=18;i<22;i++) {
            putimage(i,imageResource2);
            putimage(i+24,imageResource2);
        }
        for(int i=26;i<35;i+=8) {
            putimage(i,imageResource2);
            putimage(i+3,imageResource2);
        }

        putimage(27,imageResource3);
        putimage(28,imageResource3);
        putimage(35,imageResource3);
        putimage(36,imageResource3);
    }

    void display4() {

        for(int i=0; i<64; i+=3) {putimage1(i); }
        for(int i=1; i<64; i+=3) { putimage(i,imageResource2); }
        for(int i=2; i<64; i+=3) { putimage(i,imageResource3); }
    }

    void putimage(int index,int imageResource) {
        imageViews[index].setTranslationY(-1000f);
        imageViews[index].setImageResource(imageResource);
        imageViews[index].animate().translationYBy(1000f).setDuration(300);
    }

    private void putimage1(int i) {
        imageViews[i].setTranslationY(-1000f);
        Picasso.get().load(imgUrl).into(imageViews[i]);
        imageViews[i].animate().translationYBy(1000f).setDuration(300);
    }

    @SuppressLint("SetTextI18n")
    public void setupui() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headname = findViewById(R.id.Heading);
        TB1 = findViewById(R.id.TB1);
        TB2 = findViewById(R.id.TB2);
        RG = findViewById(R.id.radiogroup);
        checkBox = findViewById(R.id.checkbox);
        plant2 = findViewById(R.id.plant2);
        plant3 = findViewById(R.id.plant3);
        plant4 = findViewById(R.id.plant4);

        for(int i=0;i<64;i++){
            String imageid = "imageView" + (i+1) ;
            int resID = getResources().getIdentifier(imageid,"id",getPackageName());
            imageViews[i] = ((ImageView) findViewById(resID));
        }
        uri = "@drawable/image";
        uri2 = "@drawable/two";
        uri3 = "@drawable/three";
        uri4 = "@drawable/four";

        imageResource2 = getResources().getIdentifier(uri2, null, Suggestions.this.getPackageName());
        imageResource3 = getResources().getIdentifier(uri3, null,  Suggestions.this.getPackageName());
        imageResource4 = getResources().getIdentifier(uri4, null,   Suggestions.this.getPackageName());

        Bundle bundle = getIntent().getExtras();
        String plantname=bundle.getString("name");
        String Plant2 = bundle.getString("plant2");
        String Plant3 = bundle.getString("plant3");
        String attracts = bundle.getString("attracts");
        String Plant4 = bundle.getString("plant4");
        imgUrl = bundle.getString("img");

        headname.setText(plantname);
        plant2.setText("Plant 2: " + Plant2);
        plant3.setText("Plant 3: " + Plant3);
        if(attracts!="none")
            plant4.setText("Plant 4 (To Avoid Insects): "+Plant4);
        else
            plant4.setText("Does not attracts insects");

    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

}