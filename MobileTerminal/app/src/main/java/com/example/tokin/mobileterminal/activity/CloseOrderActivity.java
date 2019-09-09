package com.example.tokin.mobileterminal.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

public class CloseOrderActivity extends Activity {


    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = CloseOrderActivity.class.getSimpleName();
    private TableLayout table;
    AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_order);

        session = new SessionManager(getApplicationContext());
        getOrders();

    }

    public boolean onCreateOptionsMenu(Menu ActionBarMenu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_close_order, ActionBarMenu);
        return true;
        // return super.onCreateOptionsMenu(ActionBarMenu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.toOrder:
                Intent intentMain = new Intent(CloseOrderActivity.this, MainActivityWaiter.class);
                startActivity(intentMain);
                finish();
                return true;
            case R.id.ready_dish:
                Intent intentReadyDish = new Intent(CloseOrderActivity.this, ReadyDishActivity.class);
                startActivity(intentReadyDish);
                finish();
                return true;
            case R.id.exit:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        private void logoutUser() {

            session.setLogin(false);

            db.deleteUsers();

            Intent intent = new Intent(CloseOrderActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        private void getOrders() {
            String tag_string_req = "strReqGetOrders";

            final StringRequest strReqGetOrders= new StringRequest(Request.Method.POST, AppConfig.URL_GET_ORDERS, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "close order Response: " + response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray orders = jObj.getJSONArray("ord");
                        for(int i = 0; i<orders.length();i++) {

                            JSONObject order = orders.getJSONObject(i);

                            String id = order.getString("id");

                            String sum = order.getString("sum");

                            String TableNumb = order.getString("table_numb");

                           // FillReadyDishTable(dishName,IdOrd,TableNumb);
                            fillTable(id,TableNumb,sum);

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

            AppController.getInstance().addToRequestQueue(strReqGetOrders, tag_string_req);


        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        private void fillTable(String id, String TableNumber, String sum){

        table = findViewById(R.id.table);

            LayoutInflater inflater = LayoutInflater.from(this);

            final TableRow tr = (TableRow) inflater.inflate(R.layout.row_close_order, null);

            View.OnClickListener ListenerClose = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CloseOrder(String.valueOf(view.getId()));
                    tr.removeAllViews();

                }
            };

            View.OnClickListener ListenerGetInfo = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInfo(String.valueOf(view.getId()));

                }
            };

            int ID = Integer.valueOf(id);
            tr.setId(ID);

            TextView tvOrd = tr.findViewById(R.id.tvOrder);
            tvOrd.setText(id);

            TextView tvTableNumb = tr.findViewById(R.id.tvTableNumber);
            tvTableNumb.setText(TableNumber);

            TextView tvSum = tr.findViewById(R.id.tvSum);
            tvSum.setText(sum);

            Button btnInfo = tr.findViewById(R.id.btnInfo);
            btnInfo.setId(ID);
            btnInfo.setOnClickListener(ListenerGetInfo);

            Button btnCol = tr.findViewById(R.id.btnCol);
            btnCol.setId(ID);
            btnCol.setOnClickListener(ListenerClose);

            table.addView(tr);

        }


        private  void CloseOrder(final String id){


            String tag_string_req = "strReqCloseOrder";
            final StringRequest strReqCloseOrder= new StringRequest(Request.Method.POST, AppConfig.URL_CLOSE_ORDER, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "close order Response: " + response);
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
                    params.put("id_ord", id);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(strReqCloseOrder, tag_string_req);



        }
        public void getInfo (final String idOrd){

            ad = new AlertDialog.Builder(CloseOrderActivity.this);

            ad.setTitle("Информация о заказе");

            String tag_string_req = "strReqGetInfo";
            final StringRequest strReqGetInfo= new StringRequest(Request.Method.POST, AppConfig.URL_GET_ORD_INFO, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray orders = jObj.getJSONArray("ord");
                        StringBuilder adStr = new StringBuilder();
                        for(int i = 0; i<orders.length();i++) {
                            JSONObject ord = orders.getJSONObject(i);

                            // for(int j = 0;j < dish.length();j++) {
                            String name = ord.getString("name");

                            Double value = ord.getDouble("value");

                            String amount = ord.getString("amount");

                            String str = "Блюдо: "+name + "  " + "Кол-во: "+amount + " " + "Цена: "+value+"\n";
                            adStr.append(str);
                        }
                        ad.setMessage(adStr);
                        ad.show();
                        //  }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "JSON ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                    params.put("id_ord", idOrd);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(strReqGetInfo, tag_string_req);

        }

        public void buildAD (String name,String amount,String value){

        }
}
