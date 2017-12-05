package ca.qc.lbpsb.fusion.fcmnotification;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private  Button btnParent, btnEmployee;
    private Button register;
    private EditText editTextEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Fusion Notification");

        editTextEmail =  findViewById(R.id.editTextEmail);
        register =  findViewById(R.id.register);

        btnParent = (Button)findViewById(R.id.btnParent);
        btnEmployee = (Button)findViewById(R.id.btnEmployee);
        //----------------------------------------------

        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleBgButton(view);
            }
        });
        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleBgButton(view);
            }
        });


    }

    private void toogleBgButton(View v) {



        switch (v.getId()){

            case R.id.btnEmployee:
                Toast.makeText(this,"Employee",Toast.LENGTH_LONG).show();
                btnParent.setBackgroundResource(R.drawable.round_button_trans);
                btnParent.setTextColor(Color.WHITE);
                btnEmployee.setBackgroundResource(R.drawable.round_button);
                btnEmployee.setTextColor(Color.rgb(62,81,102));
                setTitle("Employee Fusion Notification");
                break;

            case R.id.btnParent:
                Toast.makeText(this,"Parent",Toast.LENGTH_LONG).show();
                btnEmployee.setBackgroundResource(R.drawable.round_button_trans);
                btnEmployee.setTextColor(Color.WHITE);
                btnParent.setBackgroundResource(R.drawable.round_button);
                btnParent.setTextColor(Color.rgb(62,81,102));
                setTitle("Parent Fusion Notification");
                break;
        }

    }

    public void sendToken(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPreference.getInstance(this).getDeviceToken();
        final String email = editTextEmail.getText().toString();

        //=========
         Log.d("TAG_MAIN", "Refreshed token: " + token);

        if (email.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }


        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("TAG_RESPONSE", "response string: " + response);

                        try {

                           JSONObject obj = new JSONObject(response);

                           Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("TAG_ERROR_MESSAGE", "message string: " + error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };

        Log.e("FIREBASE_ID", "Refreshed token: " + token);

        FcmVolley.getInstance(this).addToRequestQueue(stringRequest);
       // Toast.makeText(MainActivity.this, "email registered", Toast.LENGTH_LONG).show();
    }
}