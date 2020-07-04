package com.solvedunsolved.plantsymbiosis.Activities.yourplants;

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
import com.solvedunsolved.plantsymbiosis.Data.PlantDB;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.R;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import maes.tech.intentanim.CustomIntent;

public class DetailsActivity extends AppCompatActivity {

    private TextView plantName;
    private TextView Category;
    private TextView Conditions;
    private TextView Description;
    private ImageView image;
    private int PlantId;
    private ImageButton imageButton;
    private Button edit;
    private Button delete;
    private AlertDialog.Builder alertDialogBuilder;
    private static final int GALLERY_CODE = 1;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Bundle bundle;
    PlantDB db = new PlantDB(DetailsActivity.this);
    Plant plantclicked;
    private Uri mImageUri;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plants_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plantName = (TextView) findViewById(R.id.plantNameDet);
        Category = (TextView) findViewById(R.id.category);
        Conditions = (TextView) findViewById(R.id.conditionsDet);
        Description = (TextView) findViewById(R.id.DescriptionDet);
        edit = findViewById(R.id.editButtonDet);
        delete = findViewById(R.id.deleteButtonDet);
        image = findViewById(R.id.plantImageIDDets);

        plantclicked=new Plant();

        bundle = getIntent().getExtras();

        if ( bundle != null ) {
            PlantId=bundle.getInt("id");

            plantclicked=db.getplant(PlantId);
            plantName.setText(plantclicked.getName());
            Category.setText("Category: "+plantclicked.getCategory());
            Conditions.setText("Environmental Conditions: "+plantclicked.getConditions());
            Description.setText("Description: "+plantclicked.getDescription());
            image.setImageBitmap(getImage(plantclicked.getImg()));
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Plant plant = new Plant();
                plant.setId(PlantId);
                editItem(plant);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(PlantId);
            }
        });

    }
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");//fadein-to-fadeout
    }
    public void deleteItem(final int id) {

        alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);
        inflater = LayoutInflater.from(DetailsActivity.this);
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
                PlantDB db = new PlantDB(DetailsActivity.this);
                db.deletePlant(id);
                dialog.dismiss();
                startActivity(new Intent(DetailsActivity.this, Plants.class));
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void editItem(final Plant plant) {

        alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);
        inflater = LayoutInflater.from(DetailsActivity.this);
        final View view = inflater.inflate(R.layout.plants_popup, null);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        final EditText plantName = (EditText) view.findViewById(R.id.plantname);
        final EditText category = (EditText) view.findViewById(R.id.category);
        final EditText description = (EditText) view.findViewById(R.id.descriptionDet);
        final EditText conditions = (EditText) view.findViewById(R.id.conditions);
        imageButton = view.findViewById(R.id.imageButton);
        TextView title = (TextView) view.findViewById(R.id.tile);
        title.setText(" Edit Plant information ");
        Button saveButton = (Button) view.findViewById(R.id.save_button);

        plantName.setText(plantclicked.getName());
        category.setText(plantclicked.getCategory());
        conditions.setText(plantclicked.getConditions());
        description.setText(plantclicked.getDescription());
        imageButton.setImageBitmap(getImage(plantclicked.getImg()));


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);}});


            saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plantName.getText().toString().isEmpty()
                        && !category.getText().toString().isEmpty()
                        && !conditions.getText().toString().isEmpty()
                        && !description.getText().toString().isEmpty()) {


                PlantDB db = new PlantDB(DetailsActivity.this);
                Bitmap image = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                plant.setName(plantName.getText().toString());
                plant.setCategory(category.getText().toString());
                plant.setConditions(conditions.getText().toString());
                plant.setDescription(description.getText().toString());
                plant.setImg(getByte(image));
             //   plant.setImg(imageButton.getDrawingCache());

                db.updatePlant(plant);
                Snackbar.make(v, "Plant details Saved!", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        startActivity(new Intent(DetailsActivity.this, ListActivity.class));     //start a new activity
                        finish();
                    }
                }, 1000);

                }
                else {Snackbar.make(view, "Enter Name, Category, Environmental Conditions and Description", Snackbar.LENGTH_LONG).show();}
            }}); }


    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
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
    }

