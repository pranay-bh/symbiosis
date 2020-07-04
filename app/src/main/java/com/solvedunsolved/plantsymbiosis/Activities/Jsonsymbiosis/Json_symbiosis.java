
package com.solvedunsolved.plantsymbiosis.Activities.Jsonsymbiosis;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.solvedunsolved.plantsymbiosis.Model.JsonSymbiosis;
import com.solvedunsolved.plantsymbiosis.R;
import com.solvedunsolved.plantsymbiosis.ui.JsonSymbiosisAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import maes.tech.intentanim.CustomIntent;

public class Json_symbiosis extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JsonSymbiosisAdapter JSonSymbiosisAdapter;
    private List<JsonSymbiosis> plantList;
    private List<JsonSymbiosis> plantListcomplete;
    JSONObject obj;
    private final static String URL = "https://raw.githubusercontent.com/pranay-bh/json/master/symbiosis.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_symbiosis);
        CustomIntent.customType(this, "left-to-right");

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        builtrecyclerview();
        parseArraycomplete(URL);
        parseArray(URL);
    }

    @Override
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
                filter(newText);
                return false;
                // perform search when change is finished
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String newText) {
        ArrayList<JsonSymbiosis> filteredlist = new ArrayList<>();

        for (JsonSymbiosis plant : plantListcomplete ){
            if(plant.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredlist.add(plant);
            }
        }
        for (JsonSymbiosis plant : plantListcomplete ) {
            if (plant.getHelpedby().toLowerCase().contains(newText.toLowerCase())) {
                filteredlist.add(plant);
            }
            else if (plant.getHelps().toLowerCase().contains(newText.toLowerCase())) {
                filteredlist.add(plant);
            }
        }
        JSonSymbiosisAdapter.filterlist(filteredlist);
    }

    public void builtrecyclerview() {
        recyclerView = findViewById(R.id.RecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        plantList = new ArrayList<>();
        plantListcomplete = new ArrayList<>();
        JSonSymbiosisAdapter = new JsonSymbiosisAdapter(this,plantList);
        recyclerView.setAdapter(JSonSymbiosisAdapter);
        JSonSymbiosisAdapter.notifyDataSetChanged();
    }

    public void parseArray(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++)
                    {
                     try {
                        JsonSymbiosis plant = new JsonSymbiosis();
                        obj =response.getJSONObject(i);
                        plant.setPlantid(obj.getString("id"));
                        plant.setName(obj.getString("name"));
                        plant.setHelps("Helps : " + parseobj(obj,"helps"));
                        plant.setHelpedby("Helped by : "  +  parseobj(obj,"helpedby"));
                        plant.setPhotoURL(obj.getString("photoUrl"));

                        plantList.add(plant);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }  JSonSymbiosisAdapter.notifyDataSetChanged();
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

    public void parseArraycomplete(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++)
                {
                    try {
                        JsonSymbiosis plant = new JsonSymbiosis();
                        JSONObject obj2 =response.getJSONObject(i);
                        plant.setPlantid(obj2.getString("id"));
                        plant.setName(obj2.getString("name"));
                        plant.setHelps("Helps : " + parseobjcomplete(obj2,"helps"));
                        plant.setHelpedby("Helped by : "  +  parseobjcomplete(obj2,"helpedby"));
                        plant.setPhotoURL(obj2.getString("photoUrl"));
                        plantListcomplete.add(plant);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
            for (int j = 0;j<4; j++) {                    //j < temp.length() for complete display
                String value = temp.getString(j);
                parsedobj += value;
                if(j == 3)
                    continue;
                parsedobj +=", ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedobj;
    }

    public String parseobjcomplete(JSONObject obj2, String label) {
        String parsedobj = "";
        try {
            JSONArray temp =new JSONArray(obj2.getString(label));
            for (int j = 0;j < temp.length(); j++) {
                String value = temp.getString(j);
                parsedobj += value +", ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedobj;
    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

}