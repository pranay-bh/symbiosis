package com.solvedunsolved.plantsymbiosis.Activities.YourSymbiosis;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.solvedunsolved.plantsymbiosis.Data.SymbiosisDB;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.Model.Plantpair;
import com.solvedunsolved.plantsymbiosis.R;
import com.solvedunsolved.plantsymbiosis.ui.RVAdapterSymbiosis;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class list extends AppCompatActivity {
    private RecyclerView recyclerView;
    RVAdapterSymbiosis rvadapter;
    private List<Plantpair> plantpairList;
    private List<Plantpair> listItems;
    private SymbiosisDB db;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    EditText name1;
    EditText name2;
    EditText Symcategory;
    EditText relationship;
    ImageButton imageButton1;
    ImageButton imageButton2;
    private Button SaveButtonSymbiosis;
    private static final int GALLERY_CODE1 = 1;
    private static final int GALLERY_CODE2 = 2;
    private Uri mImageUri1;
    private Uri mImageUri2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symbiosis_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CustomIntent.customType(this, "left-to-right");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.Symfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopDialog();
            }
        });

        db = new SymbiosisDB(list.this);
        recyclerView = findViewById(R.id.RecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        plantpairList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        plantpairList = db.getAllPlants();

        rvadapter = new RVAdapterSymbiosis(this, plantpairList);
        recyclerView.setAdapter(rvadapter);
        rvadapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem searchitem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Plantpair> filteredlist = new ArrayList<>();
                for (Plantpair plant : plantpairList ){
                    if(plant.getName1().toLowerCase().contains(newText.toLowerCase()) ||
                       plant.getName2().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(plant);
                    } }
                rvadapter.filterlist(filteredlist);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");//fadein-to-fadeout
    }
    public void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.symbiosis_popup, null);
        name1 = (EditText) view.findViewById(R.id.Plantpairname1);
        name2 = (EditText) view.findViewById(R.id.Plantpairname2);
        Symcategory = (EditText) view.findViewById(R.id.SymCategory);
        relationship = (EditText) view.findViewById(R.id.symRelationship);
        SaveButtonSymbiosis = (Button) view.findViewById(R.id.SaveButtonSymbiosis);
        imageButton1 = view.findViewById(R.id.imageButton3);
        imageButton2 = view.findViewById(R.id.imageButton4);

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

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        SaveButtonSymbiosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name1.getText().toString().isEmpty()
                        && !name2.getText().toString().isEmpty()
                        && !Symcategory.getText().toString().isEmpty()
                        && !relationship.getText().toString().isEmpty()) {
                    savePlantpairToDB(v);
                }
                else {Snackbar.make(v, "Enter names,category and Relationship", Snackbar.LENGTH_LONG).show();}
            }


        public void savePlantpairToDB (View v){

            Bitmap image1 = ((BitmapDrawable)imageButton1.getDrawable()).getBitmap();
            Bitmap image2 = ((BitmapDrawable)imageButton2.getDrawable()).getBitmap();

            Plantpair plantpair = new Plantpair();

            String newPlantpair1 = name1.getText().toString();
            String newPlantpair2 = name2.getText().toString();
            String newPlantpairCategory = Symcategory.getText().toString();
            String newPlantrelationship = relationship.getText().toString();

            plantpair.setName1(newPlantpair1);
            plantpair.setName2(newPlantpair2);
            plantpair.setCategory(newPlantpairCategory);
            plantpair.setRelationship(newPlantrelationship);
            plantpair.setImg1(getByte(image1));
            plantpair.setImg2(getByte(image2));
            //Save to DB
            db.addPlantpair(plantpair);

            Snackbar.make(v, "information Saved!", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    startActivity(new Intent(list.this, list.class));
                    finish();
                }
            }, 1200);

        }    });
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
    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}