package com.example.tokin.mobileterminal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tokin.mobileterminal.R;
import com.example.tokin.mobileterminal.app.AppConfig;
import com.example.tokin.mobileterminal.app.AppController;
import com.example.tokin.mobileterminal.helper.SQLiteHandler;
import com.example.tokin.mobileterminal.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivityCook extends Activity {


    private static final String TAG = MainActivityCook.class.getSimpleName();
    private TextView etName;
    private SQLiteHandler db;
    private SessionManager session;
    private Spinner spinner;
    private TableLayout tableCook;
    private List<Map<String, String>> listCook;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);


        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        fillSpinner();



        session = new SessionManager(getApplicationContext());
    }

    public boolean onCreateOptionsMenu(Menu ActionBarMenu) {
        // TODO Auto-generated method stub

        ActionBarMenu.add("Выйти");

        return super.onCreateOptionsMenu(ActionBarMenu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        logoutUser();
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivityCook.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void fillTable(String inp){

       tableCook = findViewById(R.id.tableCook);
      // tableCook.removeAllViewsInLayout();

      // LayoutInflater inflater = LayoutInflater.from(this);
      // final TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_cook, null);
       db = new SQLiteHandler(getApplicationContext());

       // db.delCook();
        final String Kitchen = inp;
        String tag_string_req = "req_menu";
        final StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_COOK, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "fill table Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray orders = jObj.getJSONArray("ord");
                    for(int i = 0; i<orders.length();i++) {

                        JSONObject order = orders.getJSONObject(i);

                        if (Objects.equals(order.getString("kit"), Kitchen)) {

                          //  tr.setId(View.generateViewId());

                         //   Log.d(TAG, "tr ID Response: " + tr.getId());

                            //
                            String dish = order.getString("dish");
                            //
                          //  TextView dishTV = tr.findViewById(R.id.Dish);
                          //  dishTV.setText(dish);
                            Log.d(TAG, "dish Response: " + dish);

                            //
                            String worker = order.getString("worker");
                            //
                          //  TextView workerTV = tr.findViewById(R.id.Worker);
                          //  workerTV.setText(worker);
                            Log.d(TAG, "worker Response: " + worker);

//                          //
                            int count = Integer.valueOf(order.getString("count"));
                            //
                           // TextView countTV = tr.findViewById(R.id.Count);
                           // countTV.setText(String.valueOf(count));

                          //  Button btnDone = tr.findViewById(R.id.btnDone);
                          //  btnDone.setId(tr.getId());
                           // btnDone.setOnClickListener(ListenerDone);

                            String kit = order.getString("kit");


                            int id_dish = Integer.valueOf(order.getString("id_dish"));
                            int id_ord = Integer.valueOf(order.getString("id_ord"));

                           // db.addCook(dish,worker,count,kit);
                            addTable(dish,worker,String.valueOf(count),id_dish,id_ord);

                           // table.addView(tr);
                        }

                        // Inserting row in users table
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void fillSpinner(){
        spinner = findViewById(R.id.spinner);

        String tag_string_req = "req_kitchen";
        final StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_KITCHEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  db.delCook();
                Log.d(TAG, "Kitchen Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray kit = jObj.getJSONArray("kit");
                    String [] KitchensSpinner = new String[kit.length()];
                    for(int i = 0; i<kit.length();i++) {
                        JSONObject kitchen = kit.getJSONObject(i);

                        // for(int j = 0;j < dish.length();j++) {
                        String KitName = kitchen.getString("kitchen");
                        //spinner[i] = KitName;
                        KitchensSpinner[i]= KitName;

                        // String kit = dish.getString("kitchen");

                        // Inserting row in users table
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivityCook.this, android.R.layout.simple_spinner_item, KitchensSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            clearTable();
                            // Получаем выбранный объект
                            String item = (String)parent.getItemAtPosition(position);
                            fillTable(item);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    };
                    spinner.setOnItemSelectedListener(itemSelectedListener);

                    //  }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Spinner JSON ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addTable (String dish,String worker,String count,int id_dish,int id_ord){

        db = new SQLiteHandler(getApplicationContext());
        tableCook = findViewById(R.id.tableCook);
//        listCook = db.getCookDetails();
        LayoutInflater inflater = LayoutInflater.from(this);
        final TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_cook, null);



        View.OnClickListener ListenerDone = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv.setText(((Button) view).getText());
                String id_dish= String.valueOf(tr.getTag()).split("\\.")[0];
                String id_ord =  String.valueOf(tr.getTag()).split("\\.")[1];
                
                BonAppetit(id_dish,id_ord);
                tr.removeAllViews();

            }
        };

                String tag = String.valueOf(id_dish)+"."+String.valueOf(id_ord)+".";

                tr.setId(View.generateViewId());
                tr.setTag(tag);


                TextView dishTV = tr.findViewById(R.id.DishTV);
                dishTV.setText(dish);


                TextView workerTV = tr.findViewById(R.id.WorkerTV);
                String lastName = worker.split("\\ ")[0];
                workerTV.setText(lastName);

                TextView countTV = tr.findViewById(R.id.CountTV);
                countTV.setText(count);

                Button btnDone = tr.findViewById(R.id.btnDone);
                btnDone.setId(tr.getId());
                btnDone.setOnClickListener(ListenerDone);
                tableCook.addView(tr);

    }

    private void BonAppetit (final String id_dish, final String id_ord){

        String tag_string_req = "req_bonappetit";

        final StringRequest strReqBonAppetit= new StringRequest(Request.Method.POST, AppConfig.URL_BON_APPETIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Order Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "bon appetit jObj: " + jObj.getString("result"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id_dish", id_dish);
                params.put("id_ord", id_ord);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqBonAppetit, tag_string_req);

    }

    private void clearTable(){

        tableCook = findViewById(R.id.tableCook);

        int childCount = tableCook.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            tableCook.removeViews(1, childCount - 1);
        }

    }

}
