package ca.qc.lbpsb.fusion.fcmnotification;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.qc.lbpsb.fusion.fcmnotification.Adapter.NotificationsAdapter;
import ca.qc.lbpsb.fusion.fcmnotification.Model.Notifications;
import okhttp3.OkHttpClient;

public class NotificationCenterActivity extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_MESSAGES = Constants.URL_GET_MESSAGES_TO_DEVICE;

    //a list to store all the items of a message
  //  List<Notifications> itemList;
    //the recyclerview
   // RecyclerView recyclerView;
   // NotificationsAdapter adapter;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private NotificationsAdapter adapter;
    private List<Notifications> itemList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        setTitle("Notification Center");

//        //creating adapter object and setting it to recyclerview
//        adapter = new NotificationsAdapter(NotificationCenterActivity.this, itemList);
//
//        //getting the recyclerview from xml
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        //recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////        //initializing the productlist
//        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        itemList  = new ArrayList<>();

        final String token = SharedPreference.getInstance(this).getDeviceToken();

        //===============================================
       // load_data_from_server(token);

        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new NotificationsAdapter(this,itemList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == itemList.size()-1){
                   // load_data_from_server(itemList.get(itemList.size()-1).getId());
                }

            }
        });

        //this method will fetch and parse json
        //to display it in recyclerview

       // loadMessages();

        // Load webview activity
        loadWebView();


    }

    //------------------------------------------------------

    private void loadWebView() {
        Intent intent = new Intent(this, WebviewActivity.class);
        startActivity(intent);
    }


    //------------------------------------------------------

    private void load_data_from_server(final String token) {

        AsyncTask<String,Void,Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {

                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Constants.URL_GET_MESSAGES_TO_DEVICE + strings[0] )
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    Log.d("TAG_RESPONSE", "response : " + response);


                    JSONArray array = new JSONArray(response.body().string());

                    Log.d("TAG_RESPONSE_ARRAY", "response array : " + array);

                    for (int i=0; i<array.length(); i++){

                        //getting product object from json array
                        JSONObject notification = array.getJSONObject(i);

                        //adding the product to product list
                        itemList.add(new Notifications(
                                notification.getString("title"),
                                notification.getString("id_channel"),
                                 notification.getString("message")
                               // notification.getString("title")
                        ));

//                        JSONObject object = array.getJSONObject(i);
//
//                        MyData data = new MyData(object.getInt("id"),object.getString("description"),
//                                object.getString("image"));
//
//                        data_list.add(data);
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("TAG_VOLLEY", "error  string: " + e);
                } catch (JSONException e) {
                    Log.d("TAG_VOLLEY", "error  string: " + e);
                    System.out.println("End of content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(token);
    }




    //------------------------------------------------------
    private void loadMessages() {


        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("TAG2_RESPONSE", "response : " + response);

                        try {

                            // Configure gson
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            JSONArray array1 = new JSONArray(gson.toJson(response));

                            //converting the string to json array object
                           JSONArray array = new JSONArray(gson.toJson(response));

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                               JSONObject notification = array.getJSONObject(i);

                                //adding the product to product list
                                itemList.add(new Notifications(
                                        notification.getString("title"),
                                        notification.getString("message"),
                                        notification.getString("image")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                           adapter = new NotificationsAdapter(NotificationCenterActivity.this, itemList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG_VOLLEY", "error  string: " + error);
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}