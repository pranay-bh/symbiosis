package com.solvedunsolved.plantsymbiosis.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.Util.Constants;
import java.util.ArrayList;
import java.util.List;

public class PlantDB extends SQLiteOpenHelper {

    private Context ctx;

    public PlantDB(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx =context;
    }

    @Override //oncreate
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANT_TABLE = "CREATE TABLE " +  Constants.TABLE_NAME
                + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_PLANT_NAME + " TEXT,"
                + Constants.KEY_CATEGORY + " TEXT,"
                + Constants.KEY_CONDITIONS + " TEXT,"
                + Constants.KEY_DESCRIPTION + " TEXT,"
                + Constants.KEY_IMAGE + " BLOB);";

        db.execSQL(CREATE_PLANT_TABLE);
    }

    @Override //onpgrade
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    // add plant
    public void addPlant(Plant plant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_PLANT_NAME, plant.getName());
        values.put(Constants.KEY_CATEGORY, plant.getCategory());
        values.put(Constants.KEY_CONDITIONS, plant.getConditions());
        values.put(Constants.KEY_DESCRIPTION, plant.getDescription());
        values.put(Constants.KEY_IMAGE, plant.getImg());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);
    }

    // Function to get a Plant
    public Plant getplant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                        Constants.KEY_PLANT_NAME, Constants.KEY_CATEGORY, Constants.KEY_CONDITIONS, Constants.KEY_DESCRIPTION,Constants.KEY_IMAGE},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Plant plant = new Plant();
        plant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        plant.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_PLANT_NAME)));
        plant.setCategory(cursor.getString(cursor.getColumnIndex(Constants.KEY_CATEGORY)));
        plant.setConditions(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONDITIONS)));
        plant.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
        plant.setImg(cursor.getBlob(cursor.getColumnIndex(Constants.KEY_IMAGE)));

        return plant;
    }

    // Function to get all plants
    public List<Plant> getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Plant> plantList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID,
                Constants.KEY_PLANT_NAME,
                Constants.KEY_CATEGORY,
                Constants.KEY_CONDITIONS,
                Constants.KEY_DESCRIPTION,
                Constants.KEY_IMAGE}, null, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                Plant plant = new Plant();
                plant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                plant.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_PLANT_NAME)));
                plant.setCategory(cursor.getString(cursor.getColumnIndex(Constants.KEY_CATEGORY)));
                plant.setConditions(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONDITIONS)));
                plant.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                plant.setImg(cursor.getBlob(cursor.getColumnIndex(Constants.KEY_IMAGE)));
                plantList.add(plant);
            }while (cursor.moveToNext());
        }
        return plantList;
    }

    //Updated Plant
    public int updatePlant(Plant plant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_PLANT_NAME, plant.getName());
        values.put(Constants.KEY_CATEGORY, plant.getCategory());
        values.put(Constants.KEY_CONDITIONS, plant.getConditions());
        values.put(Constants.KEY_DESCRIPTION, plant.getDescription());
        values.put(Constants.KEY_IMAGE, plant.getImg());
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] { String.valueOf(plant.getId())} );
    }

    //Delete Plant
    public void deletePlant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
    }

    //get count
    public int getPlantCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
