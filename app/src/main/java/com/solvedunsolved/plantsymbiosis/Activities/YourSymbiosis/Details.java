package com.solvedunsolved.plantsymbiosis.Activities.YourSymbiosis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.DetailsActivity;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.ListActivity;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.Plants;
import com.solvedunsolved.plantsymbiosis.Data.PlantDB;
import com.solvedunsolved.plantsymbiosis.Data.SymbiosisDB;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.Model.Plantpair;
import com.solvedunsolved.plantsymbiosis.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import maes.tech.intentanim.CustomIntent;

public class Details extends AppCompatActivity {

    private TextView name1;
    private TextView name2;
    private TextView SymCat;
    private TextView Relationship;
    private ImageView image1;
    private ImageView image2;

    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private static final int GALLERY_CODE1 = 1;
    private static final int GALLERY_CODE2 = 2;
    private int SymId;

    private Button Symedit;
    Button Symdelete;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    Bundle bundle;
    SymbiosisDB db=new SymbiosisDB(Details.this);
    Plantpair plantpairclicked;
    private Uri mImageUri1;
    private Uri mImageUri2;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symbiosis_details);
        CustomIntent.customType(this, "left-to-right");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name1 = (TextView) findViewById(R.id.PlantpairnameDet1);
        name2 = (TextView) findViewById(R.id.PlantpairnameDet2);
        SymCat = (TextView) findViewById(R.id.SymCategory);
        Relationship = (TextView) findViewById(R.id.symRelationship);
        Symedit = findViewById(R.id.SymEditDet);
        Symdelete = findViewById(R.id.SymdelDet);
        image1 = findViewById(R.id.textView2);
        image2 = findViewById(R.id.textView3);
        plantpairclicked=new Plantpair();

        bundle = getIntent().getExtras();

        if ( bundle != null ) {
            SymId = bundle.getInt("id");
            plantpairclicked=db.getplantpair(SymId);
            name1.setText(plantpairclicked.getName1());
            name2.setText(plantpairclicked.getName2());
            SymCat.setText("Category: "+plantpairclicked.getCategory());
            Relationship.setText("Description: "+plantpairclicked.getRelationship());
            image1.setImageBitmap(getImage(plantpairclicked.getImg1()));
            image2.setImageBitmap(getImage(plantpairclicked.getImg2()));
        }

        Symedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Plantpair plantpair = new Plantpair();
                plantpair.setId(SymId);
                editItem(plantpair);
            }
        });

        Symdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(SymId);
            }
        });
    }
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
    public void deleteItem(final int id) {

        alertDialogBuilder = new AlertDialog.Builder(Details.this);
        inflater = LayoutInflater.from(Details.this);
        View view = inflater.inflate(R.layout.confirmation_dialog, null);

        Button noButton = (Button) view.findViewById(R.id.noButton);
        Button yesButton = (Button) view.findViewById(R.id.yesButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SymbiosisDB db = new SymbiosisDB(Details.this);
                db.deletePlantpair(id);
                dialog.dismiss();
                startActivity(new Intent(Details.this, symbiosis.class));
            }
        });
    }

    public void editItem(final Plantpair plantpair) {

        alertDialogBuilder = new AlertDialog.Builder(Details.this);
        inflater = LayoutInflater.from(Details.this);
        final View view = inflater.inflate(R.layout.symbiosis_popup, null);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        final EditText plantpairName1 = (EditText) view.findViewById(R.id.Plantpairname1);
        final EditText plantpairName2 = (EditText) view.findViewById(R.id.Plantpairname2);
        final EditText category = (EditText) view.findViewById(R.id.SymCategory);
        final EditText relationship = (EditText) view.findViewById(R.id.symRelationship);
        imageButton1 = view.findViewById(R.id.imageButton3);
        imageButton2 = view.findViewById(R.id.imageButton4);

        Button saveButton = (Button) view.findViewById(R.id.SaveButtonSymbiosis);

        bundle = getIntent().getExtras();

        if ( bundle != null ) {
            plantpairName1.setText(plantpairclicked.getName1());
            plantpairName2.setText(plantpairclicked.getName2());
            category.setText(plantpairclicked.getCategory());
            relationship.setText(plantpairclicked.getRelationship());
            imageButton1.setImageBitmap(getImage(plantpairclicked.getImg1()));
            imageButton2.setImageBitmap(getImage(plantpairclicked.getImg2()));
        }

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update item

                if (!plantpairName1.getText().toString().isEmpty()
                        && !plantpairName2.getText().toString().isEmpty()
                        && !category.getText().toString().isEmpty()
                        && !relationship.getText().toString().isEmpty()) {

                    SymbiosisDB db = new SymbiosisDB(Details.this);

                    Bitmap image1 = ((BitmapDrawable)imageButton1.getDrawable()).getBitmap();
                    Bitmap image2 = ((BitmapDrawable)imageButton2.getDrawable()).getBitmap();

                    plantpair.setName1(plantpairName1.getText().toString());
                    plantpair.setName2(plantpairName2.getText().toString());
                    plantpair.setCategory(category.getText().toString());
                    plantpair.setRelationship(relationship.getText().toString());
                    plantpair.setImg1(getByte(image1));
                    plantpair.setImg2(getByte(image2));

                    db.updatePlantpair(plantpair);

                    Snackbar.make(v, "plan pair information Saved!", Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(Details.this, symbiosis.class));     //start a new activity
                            finish();
                        }
                    }, 1000);
                }
                else {Snackbar.make(view, "Enter names,category and relationship", Snackbar.LENGTH_LONG).show();}
            }
        });
    }

    @Override
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
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(mImageUri2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageButton2.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        }
    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }
}
