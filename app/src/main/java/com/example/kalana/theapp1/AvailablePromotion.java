package com.example.kalana.theapp1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AvailablePromotion extends AppCompatActivity {

    private RecyclerView recyclerViewAva;
    private RecyclerView.Adapter adapter;
    String MY_PREFS_NAME = "login_pref";
    String company = "";

    private static List<ListItemAvailable> ListItemAvailable;

    private static final String url_data="http://10.0.2.2:8080/api/promotion/availableDispromo/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        company = preferences.getString("company", null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_promotion);


        recyclerViewAva = (RecyclerView) findViewById(R.id.recyclerViewAva);
        recyclerViewAva.setHasFixedSize(true);
        recyclerViewAva.setLayoutManager(new LinearLayoutManager(this));

        ListItemAvailable = new ArrayList<>();

        loadRecycleViewData();

    }

    private void loadRecycleViewData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data + company, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i=0;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);

                        ListItemAvailable u = new ListItemAvailable(o.getString("item"), o.getString("percentage"),o.getString("details"),o.getString("start_date"),o.getString("end_date"));
                        ListItemAvailable.add(u);
                    }
                    adapter=new MyAdapterAvailable(ListItemAvailable,getApplicationContext());
                    recyclerViewAva.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
