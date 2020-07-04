package com.solvedunsolved.plantsymbiosis.Activities.yourplants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.solvedunsolved.plantsymbiosis.Data.PlantDB;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

public class Plants extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button save_button;
    private EditText Plant_name;
    private EditText Category;
    private EditText Description;
    private EditText Conditions;

    PlantDB db;
    private Uri mImageUri;
    private ImageButton imageButton;
    private static final int GALLERY_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        CustomIntent.customType(this, "left-to-right");
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db= new PlantDB(this);
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
        View view = getLayoutInflater().inflate(R.layout.plants_popup, null);

        Plant_name = (EditText) view.findViewById(R.id.plantname);
        Category = (EditText) view.findViewById(R.id.category);
        Conditions= (EditText) view.findViewById(R.id.conditions);
        Description = view.findViewById(R.id.descriptionDet);
        save_button =view.findViewById(R.id.save_button);
        imageButton = view.findViewById(R.id.imageButton);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);}});

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Plant_name.getText().toString().isEmpty() &&
                    !Category.getText().toString().isEmpty() &&
                    !Conditions.getText().toString().isEmpty() &&
                    !Description.getText().toString().isEmpty()) {
                    savePlantToDB(v);
                }
                else {Snackbar.make(v, "Enter name,category,conditions and description", Snackbar.LENGTH_LONG).show();}
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(mImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageButton.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        }
    }

    private void savePlantToDB(View v) {

        Plant plant = new Plant();

        Bitmap image = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();

        plant.setName(Plant_name.getText().toString());
        plant.setCategory(Category.getText().toString());
        plant.setDescription(Description.getText().toString());
        plant.setConditions(Conditions.getText().toString());
        plant.setImg(getByte(image));
        db.addPlant(plant);

        Snackbar.make(v, "Plant Saved!", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(Plants.this, ListActivity.class));
                finish();
            }
        }, 1200);
    }

    public byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void byPassActivity() {
       // Checks if database is empty; if not, then we just
       // go to ListActivity and show all added items
        if (db.getPlantCount() > 0) {
            startActivity(new Intent(Plants.this, ListActivity.class));
            finish();
        }
    }

    }