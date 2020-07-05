package com.solvedunsolved.plantsymbiosis.Activities.YourSymbiosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.solvedunsolved.plantsymbiosis.Data.SymbiosisDB;
import com.solvedunsolved.plantsymbiosis.Model.Plantpair;
import com.solvedunsolved.plantsymbiosis.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import maes.tech.intentanim.CustomIntent;

public class symbiosis extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button SavebuttonSymbiosis;
    EditText name1; //plantname
    EditText name2;
    EditText SymCategory;
    EditText Relationship;
    SymbiosisDB db;
    private Uri mImageUri1;
    private Uri mImageUri2;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private static final int GALLERY_CODE1 = 1;
    private static final int GALLERY_CODE2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbiosis);
            Toolbar toolbar = findViewById(R.id.toolbar2);
            CustomIntent.customType(this, "left-to-right");

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            db= new SymbiosisDB(this);
            byPassActivity();
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createPopupDialog();
                }
            });
    }

       public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");//fadein-to-fadeout
    }

       private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.symbiosis_popup, null);

        name1 = (EditText) view.findViewById(R.id.Plantpairname1);
        name2 = view.findViewById(R.id.Plantpairname2);
        SymCategory = (EditText) view.findViewById(R.id.SymCategory);
        Relationship = view.findViewById(R.id.symRelationship);
        SavebuttonSymbiosis =view.findViewById(R.id.SaveButtonSymbiosis);
        imageButton1=view.findViewById(R.id.imageButton3);
        imageButton2=view.findViewById(R.id.imageButton4);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

           imageButton1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                   galleryIntent.setType("image/*");
                   startActivityForResult(galleryIntent, GALLERY_CODE1);}});

           imageButton2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                   galleryIntent.setType("image/*");
                   startActivityForResult(galleryIntent, GALLERY_CODE2);}});

        SavebuttonSymbiosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name1.getText().toString().isEmpty()
                        && !SymCategory.getText().toString().isEmpty()
                        && !name2.getText().toString().isEmpty()
                        && !Relationship.getText().toString().isEmpty()) {
                    savePlantpairToDB(v);
                }
                else {Snackbar.make(v, "Enter names,category and Relationship", Snackbar.LENGTH_LONG).show();}
            }
        });
    }

       private void savePlantpairToDB(View v) {
        Bitmap image1 = ((BitmapDrawable)imageButton1.getDrawable()).getBitmap();
        Bitmap image2 = ((BitmapDrawable)imageButton2.getDrawable()).getBitmap();

        Plantpair plantpair = new Plantpair();
        String newPlant1 = name1.getText().toString();
        String newPlant2 = name2.getText().toString();
        String newcategory = SymCategory.getText().toString();
        String newRelationship = Relationship.getText().toString();

        plantpair.setName1(newPlant1);
        plantpair.setName2(newPlant2);
        plantpair.setCategory(newcategory);
        plantpair.setRelationship(newRelationship);
        plantpair.setImg1(getByte(image1));
        plantpair.setImg2(getByte(image2));

        db.addPlantpair(plantpair);

        Snackbar.make(v, "Plant pair Saved!", Snackbar.LENGTH_LONG).show();
        Log.d("added ID:", String.valueOf(db.getPlantpairCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(symbiosis.this, list.class));
                finish();
            }
        }, 1200);

    }

       public void byPassActivity(){
            if (db.getPlantpairCount() > 0) {
                startActivity(new Intent(symbiosis.this, list.class));
                finish(); }
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE1 && resultCode == RESULT_OK) {
            mImageUri1 = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(mImageUri1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageButton1.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        }

        if (requestCode == GALLERY_CODE2 && resultCode == RESULT_OK) {
            mImageUri2 = data.getData();
            InputStream imageStream1 = null;
            try {
                imageStream1 = getContentResolver().openInputStream(mImageUri2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageButton2.setImageBitmap(BitmapFactory.decodeStream(imageStream1));
        }
    }
    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

}