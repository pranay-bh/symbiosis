package com.solvedunsolved.plantsymbiosis.Activities.yourplants;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.solvedunsolved.plantsymbiosis.Data.PlantDB;
import com.solvedunsolved.plantsymbiosis.Model.JsonSymbiosis;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.R;
import com.solvedunsolved.plantsymbiosis.ui.RecyclerViewAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;


public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Plant> plantList;
    private List<Plant> listItems;
    private PlantDB db;
    private Uri mImageUri;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    ImageButton imageButton;
    EditText Plant_name;
    EditText category;
    EditText Conditions;
    EditText Description;
    private Button saveButton;
    private static final int GALLERY_CODE = 1;

    public ListActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_list);
        CustomIntent.customType(this, "left-to-right");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopDialog();
            }
        });

        db = new PlantDB(this);
        plantList = new ArrayList<>();
        listItems = new ArrayList<>();
        plantList = db.getAllPlants();

        createRVA();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem searchitem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform search when text changes (on press of search button)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Plant> filteredlist = new ArrayList<>();
                for (Plant plant : plantList ){
                    if(plant.getName().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(plant);
                    } }
                recyclerViewAdapter.filterlist(filteredlist);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void createRVA(){
        recyclerView = findViewById(R.id.RecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (Plant c : plantList) {
            Plant plant = new Plant();
            plant.setName(c.getName());
            plant.setCategory(c.getCategory());
//            plant.setConditions(c.getConditions());
//            plant.setDescription(c.getDescription());
            plant.setId(c.getId());
            plant.setImg(c.getImg());
            listItems.add(plant);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");//fadein-to-fadeout
    }

    public void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.plants_popup, null);
        Plant_name = (EditText) view.findViewById(R.id.plantname);
        category = (EditText) view.findViewById(R.id.category);
        Conditions = (EditText) view.findViewById(R.id.conditions);
        Description = (EditText) view.findViewById(R.id.descriptionDet);
        saveButton = (Button) view.findViewById(R.id.save_button);
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (!Plant_name.getText().toString().isEmpty()
                        && !category.getText().toString().isEmpty()
                        && !Conditions.getText().toString().isEmpty()
                        && !Description.getText().toString().isEmpty()) {
                    savePlantToDB(v);
                }
                else {Snackbar.make(v, "Enter name,category,environmental conditions and description", Snackbar.LENGTH_LONG).show();}
            }
        });
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

    public void savePlantToDB(View v) {

        Plant plant = new Plant();


        plant.setName(Plant_name.getText().toString());
        plant.setCategory(category.getText().toString());
        plant.setConditions(Conditions.getText().toString());
        plant.setDescription(Description.getText().toString());

        Bitmap image = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        plant.setImg(getByte(image));

        //Save to DB
        db.addPlant(plant);

        Snackbar.make(v, "Information Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));     //start a new activity
                finish();
            }
        }, 1000); //  1 second.

    }

    public byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}