package com.solvedunsolved.plantsymbiosis.Activities.Jsonsymbiosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.solvedunsolved.plantsymbiosis.Model.JsonSymbiosis;
import com.solvedunsolved.plantsymbiosis.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import maes.tech.intentanim.CustomIntent;

public class Json_symbyosis_deatails extends AppCompatActivity {

    private JsonSymbiosis plant;
    private TextView plantname;
    private ImageView plantImage;
    private TextView helps;
    private TextView helpedby;
    private TextView attracts;
    private TextView repels;
    private TextView avoid;
    private TextView comment;
    private static String URL="https://raw.githubusercontent.com/pranay-bh/json/master/symbiosis.json";
    private RequestQueue queue;
    private String plantId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_symbyosis_deatails);
        CustomIntent.customType(this, "left-to-right");

        setupui();

        int i= Integer.parseInt(plantId);
        parseArray(URL,i-1);
        getPlantDetails(plantId);
    }

    private void getPlantDetails(final String i) { }

    public void parseArray(String URL, final int i) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL,new Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONArray response) {
              try {
                        JSONObject obj =response.getJSONObject(i);
                        plantname.setText(obj.getString("name"));
                        helps.setText("helps: " + parseobj(obj,"helps"));
                        helpedby.setText("helped by: " + parseobj(obj,"helpedby"));
                        avoid.setText("avoid : " + parseobj(obj,"avoid"));
                        attracts.setText("attracts: " + parseobj(obj,"attracts"));
                        comment.setText("Comment : " +obj.getString("comment"));
                        repels.setText("Repels: " + parseobj(obj,"repels"));

                        Picasso.get()
                          .load(obj.getString("photoUrl"))
                          .into(plantImage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

               // Log.d("Response :", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(error.getMessage());
            }
        });
        queue.add(arrayRequest);
    }

    public String parseobj(JSONObject obj, String label) {
        String parsedobj = "";
        try {
            JSONArray temp =new JSONArray(obj.getString(label));
            for (int j = 0; j < temp.length(); j++) {
                String value = temp.getString(j);
                parsedobj += value;
                if(j == temp.length()-1)
                    continue;
                parsedobj +=", ";


            }
            Log.d("Response :", parsedobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedobj;
    }

    public void setupui() {

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plantname  = (TextView) findViewById(R.id.PlantIDDets);
        helpedby = (TextView) findViewById(R.id.helpedbyIDDets);
        helps = (TextView) findViewById(R.id.helpsIDDet);
        attracts = (TextView) findViewById(R.id.attractsDet);
        repels = (TextView) findViewById(R.id.repelsDet);
        comment = (TextView) findViewById(R.id.commentsDet);
        avoid = (TextView) findViewById(R.id.avoidDet);
        plantImage= findViewById(R.id.plantImageIDDets);
        queue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        plantId = bundle.getString("id");
        //plantId.setText(bundle.getString("name1"));
    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

}