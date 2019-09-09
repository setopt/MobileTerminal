package com.example.tokin.mobileterminal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by tokin on 27.05.2018.
 */

public class ReadyDishActivity extends Activity {

    private SessionManager session;
    private static final String TAG = ReadyDishActivity.class.getSimpleName();
    private SQLiteHandler db;
    private TableLayout ReadyDishTable;
    private Context context = ReadyDishActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_dish);

        session = new SessionManager(getApplicationContext());
        getReadyDish();


        if (!session.isLoggedIn()) {
            logoutUser();
        }


    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();


        Intent intent = new Intent(ReadyDishActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getReadyDish(){

        String tag_string_req = "req_readydish";

        final StringRequest strReqReadyDish = new StringRequest(Request.Method.POST, AppConfig.URL_READY_DISH, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "ReadyDish Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray dishes = jObj.getJSONArray("dishes");
                    for(int i = 0; i<dishes.length();i++) {

                        JSONObject dish = dishes.getJSONObject(i);

                        String dishName = dish.getString("dish");

                        String id_dish = dish.getString("id_dish");

                        String IdOrd = dish.getString("ord");

                        String TableNumb = dish.getString("table_numb");

                        String count = dish.getString("count");

                        FillReadyDishTable(dishName,IdOrd,id_dish,TableNumb,count);

                    }

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
                db = new SQLiteHandler(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                final String name = user.get("name");

                Log.d(TAG, "ReadyDish name: " + name);

                params.put("name", name);

                Log.d(TAG, "ReadyDish name: " + params);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqReadyDish, tag_string_req);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void FillReadyDishTable (String dish, String Ord, String id_dish, String TableNumber,String count){

        ReadyDishTable = findViewById(R.id.tableReadyDish);
        LayoutInflater inflater = LayoutInflater.from(this);

        final TableRow tr = (TableRow) inflater.inflate(R.layout.row_ready_dish, null);

        View.OnClickListener ListenerDone = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv.setText(((Button) view).getText());

                String id_dish= String.valueOf(tr.getTag()).split("\\.")[0];
                String id_ord =  String.valueOf(tr.getTag()).split("\\.")[1];

                DishDelivered(id_dish,id_ord);

                tr.removeAllViews();
            }
        };

        String tag = id_dish+"."+Ord+".";

        tr.setId(View.generateViewId());
        tr.setTag(tag);

        TextView tvDish = tr.findViewById(R.id.tvDish);
        tvDish.setText(dish+" X"+count);

        TextView tvOrd = tr.findViewById(R.id.tvOrder);
        tvOrd.setText(Ord);

        TextView tvTableNumb = tr.findViewById(R.id.tvTableNumber);
        tvTableNumb.setText(TableNumber);

        Button btnCol = tr.findViewById(R.id.btnCol);
        btnCol.setId(tr.getId());
        btnCol.setOnClickListener(ListenerDone);

        ReadyDishTable.addView(tr);

    }

    public boolean onCreateOptionsMenu(Menu ActionBarMenu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ready_dish, ActionBarMenu);
        return true;
        // return super.onCreateOptionsMenu(ActionBarMenu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.toOrder:
                Intent intent = new Intent(ReadyDishActivity.this, MainActivityWaiter.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exit:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


        // return super.onOptionsItemSelected(item);
    }

    private void DishDelivered(final String id_dish, final String id_ord){

        String tag_string_req = "strReqCloseOrder";
        final StringRequest strReqCloseOrder= new StringRequest(Request.Method.POST, AppConfig.URL_DELIVERED_DISH, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "dish delivered Response: " + response);
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
                params.put("id_ord", id_ord);
                params.put("id_dish", id_dish);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReqCloseOrder, tag_string_req);
    }

    public void onClickRefresh(View view) {

        clearTable();
        getReadyDish();

    }

    private void clearTable(){

        ReadyDishTable = findViewById(R.id.tableReadyDish);

        int childCount = ReadyDishTable.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            ReadyDishTable.removeViews(1, childCount - 1);
        }

    }
}
