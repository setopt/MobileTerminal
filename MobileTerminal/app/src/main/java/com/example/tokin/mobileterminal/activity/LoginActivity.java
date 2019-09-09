package com.example.tokin.mobileterminal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputLogin;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    final String PosCook = "1";
    final String PosWaiter = "2";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLogin = findViewById(R.id.login);
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
      //  btnLinkToRegister =  findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {

            HashMap<String, String> user = db.getUserDetails();

            final String id_position = user.get("id_position");

           // Intent intent = new Intent(LoginActivity.this, MainActivityWaiter.class);
           // startActivity(intent);
           // finish();

            if(Objects.equals(id_position, PosCook)){
                Intent intentCook = new Intent(LoginActivity.this,
                        MainActivityCook.class);
                startActivity(intentCook);
                finish();
            }
            else if(Objects.equals(id_position, PosWaiter)) {
                Intent intentWaiter = new Intent(LoginActivity.this,
                        MainActivityWaiter.class);
                startActivity(intentWaiter);
                finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String login = inputLogin.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!login.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(login, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


    }

    private void checkLogin(final String login, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
     //   final String PosCook = "1";
     //   final String PosWaiter = "2";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("Name");
                        String login = user.getString("Login");
                        String position = user.getString("Position");

                        // Inserting row in users table
                        db.addUser(name, login, uid, position);

                        if(Objects.equals(position, PosCook)){
                            Intent intentCook = new Intent(LoginActivity.this,
                                    MainActivityCook.class);
                            startActivity(intentCook);
                            finish();
                        }
                        if(Objects.equals(position, PosWaiter)) {
                            Intent intentWaiter = new Intent(LoginActivity.this,
                                    MainActivityWaiter.class);
                            startActivity(intentWaiter);
                            finish();
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
