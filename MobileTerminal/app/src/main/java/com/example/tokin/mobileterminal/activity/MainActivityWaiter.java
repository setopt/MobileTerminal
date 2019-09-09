package com.example.tokin.mobileterminal.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
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
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Created by tokin on 15.05.2018.
 */

public class MainActivityWaiter extends Activity {

    private static final String TAG = MainActivityWaiter.class.getSimpleName();
    private SQLiteHandler db;
    private FlexboxLayout flexCat;
    private List<Map<String, String>> listMenu;
    private GridView listCat;
    private SessionManager session;
    private Button back;
    private TableLayout tableOrder;
    private EditText etTableOrder;
    AlertDialog.Builder ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);


        flexCat = findViewById(R.id.flexCat);
        listCat = findViewById(R.id.listCat);
        back = findViewById(R.id.back);



       // listCat.setVisibility(View.INVISIBLE);
       // back.setVisibility(View.INVISIBLE);

        db = new SQLiteHandler(getApplicationContext());
        getMenu();

        listMenu = db.getMenuDetails();

      //  Map<String,String> menu = listMenu.get(2);
      //  Map<String,String> menu2 = listMenu.get(0);
        getCategories(listMenu);

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

    }
    public void getMenu(){

        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_menu";


        final StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_MENU, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                db.delMenu();
                Log.d(TAG, "Menu Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray dishes = jObj.getJSONArray("dishes");
                    for(int i = 0; i<dishes.length();i++) {
                        JSONObject dish = dishes.getJSONObject(i);

                       // for(int j = 0;j < dish.length();j++) {
                            String name = dish.getString("name");

                            Double value = dish.getDouble("value");

                            String cat = dish.getString("category");

                            String kit = dish.getString("kitchen");

                            Log.d(TAG, "Добавлены: " + name);
                            // Inserting row in users table
                            db.addMenu(name, value, cat, kit);
                        }
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
        });

        // Adding request to request queue
          AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getCategories(List<Map<String, String>> inp){
        Set<String> foundStrings = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        Set<String> data = new HashSet<>();

        listCat.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);

        for(int i = 0; i< inp.size();i++){
            data.add(inp.get(i).get("cat"));
        }
        for (String str : data){
            if (foundStrings.contains(str))
            {
                duplicates.add(str);
            }
            else if (Collections.frequency(data,str) == 1) {
                duplicates.add(str);
            }
            else
            {
                foundStrings.add(str);
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //tv.setText(((Button) view).getText());
                fillCategories(String.valueOf(((Button) view).getText()));
            }
        };

        for (String str : duplicates){
            Button btn = new Button(this);
            btn.setText(str);
            btn.setOnClickListener(listener);
            flexCat.addView(btn);
        }
    }

    public boolean onCreateOptionsMenu(Menu ActionBarMenu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_waiter, ActionBarMenu);
        return true;
       // return super.onCreateOptionsMenu(ActionBarMenu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.ready_dish:
                Intent intent = new Intent(MainActivityWaiter.this, ReadyDishActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.close_order:
                Intent intentCloseOrder = new Intent(MainActivityWaiter.this, CloseOrderActivity.class);
                startActivity(intentCloseOrder);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void fillCategories(String inp){
        db = new SQLiteHandler(getApplicationContext());
        ArrayAdapter<String> adapter;
        Set<String> item = new HashSet<>();


        listMenu = db.getMenuDetails();

        String [] data = new String[listMenu.size()];

        for(int i = 0; i< listMenu.size();i++) {
            if(Objects.equals(listMenu.get(i).get("cat"), inp)){

                  item.add(listMenu.get(i).get("name") + "  " + listMenu.get(i).get("value")+"Р" );
                  //item.add(ob[1]+" "+ob[0]);
              //  item.add(listMenu.get(i).get("value")+"Р" );


            }
        }

        data = item.toArray(new String[0]);
        flexCat = findViewById(R.id.flexCat);
        flexCat.removeAllViews();
        listCat = findViewById(R.id.listCat);
        listCat.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);


        adapter = new ArrayAdapter<>(this, R.layout.item, R.id.tvText, data);
        listCat.setAdapter(adapter);
        adjustGridView();

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String dish = parent.getItemAtPosition(position).toString().split("\\  ")[0];
               // Toast.makeText(getApplicationContext(),"Вы выбрали "
               //                 + parent.getItemAtPosition(position).toString(),
               //         Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(),"Вы выбрали "
                                        + dish,
                                Toast.LENGTH_SHORT).show();
                addDish(dish);

            }
        };
        listCat.setOnItemClickListener(itemListener);



    }
    private void adjustGridView() {
        listCat.setNumColumns(1);

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivityWaiter.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickBack(View view) {
      //  listCat.setVisibility(View.INVISIBLE);
      //  back.setVisibility(View.INVISIBLE);
        listMenu = db.getMenuDetails();
        getCategories(listMenu);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addDish(String inp){
        tableOrder = findViewById(R.id.tableOrder);
        listMenu = db.getMenuDetails();


        LayoutInflater inflater = LayoutInflater.from(this);
        final TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);

        View.OnClickListener delet = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv.setText(((Button) view).getText());
                tr.removeAllViews();
            }
        };

        for(int i = 0; i< listMenu.size();i++) {
            if (Objects.equals(listMenu.get(i).get("name"), inp)) {
                tr.setId(View.generateViewId());

                TextView col1 = tr.findViewById(R.id.col1);
                col1.setText(listMenu.get(i).get("name"));



                TextView col2 = tr.findViewById(R.id.col2);
                col2.setText(listMenu.get(i).get("value"));

                EditText numb = tr.findViewById(R.id.numb);
                numb.setText("1");

                Button btnCol = tr.findViewById(R.id.btnCol);
                btnCol.setId(tr.getId());
                btnCol.setOnClickListener(delet);
                tableOrder.addView(tr);
            }
        }

    }


    public void checkoutOnClick(View view) {
        ad = new AlertDialog.Builder(MainActivityWaiter.this);
        ad.setTitle("Оформить заказ?");



        double sum=0;

        tableOrder = findViewById(R.id.tableOrder);
        for(int i =1;i<tableOrder.getChildCount();i++) {
            TableRow row = (TableRow)tableOrder.getChildAt(i);

            TextView value = row.findViewById(R.id.col2);
            double val = Double.parseDouble((String) value.getText());

            EditText numb = row.findViewById(R.id.numb);
            int count = Integer.parseInt(String.valueOf(numb.getText()));

            sum = sum + (val*count);
        }

            ad.setMessage("Оформить заказ на сумму: "+ sum+"?");
            ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    SendOrder();

                    //clearTable();
                   // EditText tbNumb = findViewById(R.id.editText);
                    //tbNumb.setText("");

                }
            });
            ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    //Toast.makeText(MainActivityWaiter.this, "Возможно вы правы", Toast.LENGTH_LONG)
                    //        .show();

                }
            });
            ad.setCancelable(true);
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {

                  //  Toast.makeText(MainActivityWaiter.this, "Вы ничего не выбрали",
                   //         Toast.LENGTH_LONG).show();

                }
            });
            ad.show();


        }



        private void SendOrder (){
            String tag_string_req = "req_order";

            HashMap<String, String> user = db.getUserDetails();

            final String name = user.get("name");

            etTableOrder = findViewById(R.id.editText);

            final String tableNumb = etTableOrder.getText().toString();


            final int[] id_order = new int[1];

            final StringRequest strReqOrd = new StringRequest(Request.Method.POST, AppConfig.URL_ORDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Order Response: " + response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        Log.d(TAG, "Order jObj: " + jObj);

                            // Now store the user in SQLite
                            id_order[0] = Integer.parseInt(jObj.getString("id_order"));

                            String id_ord = String.valueOf(id_order[0]);
                            tableOrder = findViewById(R.id.tableOrder);
                            for(int i =1;i<tableOrder.getChildCount();i++) {
                                TableRow row = (TableRow)tableOrder.getChildAt(i);


                                TextView dishTV = row.findViewById(R.id.col1);
                                String dish = dishTV.getText().toString();


                                EditText numb = row.findViewById(R.id.numb);
                                int countInt = Integer.parseInt(String.valueOf(numb.getText()));
                                String count = String.valueOf(countInt);


                                OrderDish(dish,count,id_ord);
                            }

                        clearTable();


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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tableNumb", tableNumb);
                    params.put("worker", name);

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(strReqOrd, tag_string_req);



          /*  String id_ord = String.valueOf(id_order[0]);
            tableOrder = findViewById(R.id.tableOrder);
            for(int i =1;i<tableOrder.getChildCount();i++) {
                TableRow row = (TableRow)tableOrder.getChildAt(i);

                TextView dishTV = row.findViewById(R.id.col1);
                String dish = dishTV.getText().toString();

                EditText numb = row.findViewById(R.id.numb);
                int countInt = Integer.parseInt(String.valueOf(numb.getText()));
                String count = String.valueOf(countInt);

                OrderDish(dish,count,id_ord);
            }*/



        }



        private void OrderDish (String inpDish, String inpCount, String intId_order){
            String tag_string_req = "req_orderdish";
            final String dish = inpDish;
            final String count = inpCount;
            final String id_order = intId_order;

            final StringRequest strReqOrdDish = new StringRequest(Request.Method.POST, AppConfig.URL_ORDERDISH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Order Response: " + response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        Log.d(TAG, "OrderDish jObj: " + jObj.getString("result"));
                        Log.d(TAG, "OrderDish jObj dish : " + jObj.getString("dish"));
                        Log.d(TAG, "OrderDish jObj count : " + jObj.getString("count"));
                        Log.d(TAG, "OrderDish jObj id_order : " + jObj.getString("id_order"));

                        //tableOrder.removeAllViews();

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
                    params.put("dish", dish);
                    params.put("count", count);
                    params.put("id_order", id_order);

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(strReqOrdDish, tag_string_req);


        }

        private void clearTable(){

            tableOrder = findViewById(R.id.tableOrder);

            int childCount = tableOrder.getChildCount();

            // Remove all rows except the first one
            if (childCount > 1) {
                tableOrder.removeViews(1, childCount - 1);
            }

        }

    }

