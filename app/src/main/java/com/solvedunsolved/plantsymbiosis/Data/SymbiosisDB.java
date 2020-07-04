package com.solvedunsolved.plantsymbiosis.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.solvedunsolved.plantsymbiosis.Model.Plantpair;
import com.solvedunsolved.plantsymbiosis.Util.SymbiosisConstants;
import java.util.ArrayList;
import java.util.List;

public class SymbiosisDB extends SQLiteOpenHelper {

    private Context ctx;

    public SymbiosisDB(@Nullable Context context) {
        super(context, SymbiosisConstants.DB_NAME, null, SymbiosisConstants.DB_VERSION);
        this.ctx =context;
    }

    @Override //oncreate
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANT_TABLE = "CREATE TABLE " +  SymbiosisConstants.TABLE_NAME
                + "(" + SymbiosisConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + SymbiosisConstants.KEY_PLANT_NAME1 + " TEXT,"
                + SymbiosisConstants.KEY_PLANT_NAME2 + " TEXT,"
                + SymbiosisConstants.KEY_CATEGORY + " TEXT,"
                + SymbiosisConstants.KEY_RELATION + " TEXT,"
                + SymbiosisConstants.KEY_IMAGE1 + " BLOB,"
                + SymbiosisConstants.KEY_IMAGE2 + " BLOB);";

        db.execSQL(CREATE_PLANT_TABLE);
    }

    @Override //onpgrade
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SymbiosisConstants.TABLE_NAME);
        onCreate(db);
    }

    // add plant
    public void addPlantpair(Plantpair plantpair) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SymbiosisConstants.KEY_PLANT_NAME1, plantpair.getName1());
        values.put(SymbiosisConstants.KEY_PLANT_NAME2, plantpair.getName2());
        values.put(SymbiosisConstants.KEY_CATEGORY, plantpair.getCategory());
        values.put(SymbiosisConstants.KEY_RELATION, plantpair.getRelationship());
        values.put(SymbiosisConstants.KEY_IMAGE1, plantpair.getImg1());
        values.put(SymbiosisConstants.KEY_IMAGE2, plantpair.getImg2());

        //Insert the row
        db.insert(SymbiosisConstants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");
    }

    // Function to get a Plant
    public Plantpair getplantpair(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(SymbiosisConstants.TABLE_NAME, new String[] {SymbiosisConstants.KEY_ID,
                        SymbiosisConstants.KEY_PLANT_NAME1, SymbiosisConstants.KEY_PLANT_NAME2, SymbiosisConstants.KEY_CATEGORY, SymbiosisConstants.KEY_RELATION,SymbiosisConstants.KEY_IMAGE1,SymbiosisConstants.KEY_IMAGE2},
                SymbiosisConstants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Plantpair plantpair = new Plantpair();
        plantpair.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_ID))));
        plantpair.setName1(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_PLANT_NAME1)));
        plantpair.setName2(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_PLANT_NAME2)));
        plantpair.setCategory(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_CATEGORY)));
        plantpair.setRelationship(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_RELATION)));
        plantpair.setImg1(cursor.getBlob(cursor.getColumnIndex(SymbiosisConstants.KEY_IMAGE1)));
        plantpair.setImg2(cursor.getBlob(cursor.getColumnIndex(SymbiosisConstants.KEY_IMAGE2)));


        return plantpair;
    }

    // Function to get all plants pair
    public List<Plantpair> getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Plantpair> plantpairList = new ArrayList<>();

        Cursor cursor = db.query(SymbiosisConstants.TABLE_NAME, new String[] {
                SymbiosisConstants.KEY_ID,
                SymbiosisConstants.KEY_PLANT_NAME1,
                SymbiosisConstants.KEY_PLANT_NAME2,
                SymbiosisConstants.KEY_CATEGORY,
                SymbiosisConstants.KEY_RELATION,
                SymbiosisConstants.KEY_IMAGE1,
                SymbiosisConstants.KEY_IMAGE2}, null, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                Plantpair plantpair = new Plantpair();
                plantpair.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_ID))));
                plantpair.setName1(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_PLANT_NAME1)));
                plantpair.setName2(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_PLANT_NAME2)));
                plantpair.setCategory(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_CATEGORY)));
                plantpair.setRelationship(cursor.getString(cursor.getColumnIndex(SymbiosisConstants.KEY_RELATION)));
                plantpair.setImg1(cursor.getBlob(cursor.getColumnIndex(SymbiosisConstants.KEY_IMAGE1)));
                plantpair.setImg2(cursor.getBlob(cursor.getColumnIndex(SymbiosisConstants.KEY_IMAGE2)));
                plantpairList.add(plantpair);
            }while (cursor.moveToNext());
        }
        return plantpairList;
    }

    //Updated Plant pair
    public int updatePlantpair(Plantpair plantpair) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SymbiosisConstants.KEY_PLANT_NAME1, plantpair.getName1());
        values.put(SymbiosisConstants.KEY_PLANT_NAME2, plantpair.getName2());
        values.put(SymbiosisConstants.KEY_CATEGORY, plantpair.getCategory());
        values.put(SymbiosisConstants.KEY_RELATION, plantpair.getRelationship());
        values.put(SymbiosisConstants.KEY_IMAGE1, plantpair.getImg1());
        values.put(SymbiosisConstants.KEY_IMAGE2, plantpair.getImg2());
        return db.update(SymbiosisConstants.TABLE_NAME, values, SymbiosisConstants.KEY_ID + "=?", new String[] { String.valueOf(plantpair.getId())} );
    }

    //Delete Plantpair
    public void deletePlantpair(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SymbiosisConstants.TABLE_NAME, SymbiosisConstants.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
    }

    //get count
    public int getPlantpairCount() {
        String countQuery = "SELECT * FROM " + SymbiosisConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
