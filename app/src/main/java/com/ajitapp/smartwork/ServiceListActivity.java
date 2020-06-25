package com.ajitapp.smartwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.ServiceClickListInterface;
import com.ajitapp.smartwork.adapters.ServiceListAdapter;
import com.ajitapp.smartwork.models.ServiceModel;
import com.ajitapp.smartwork.utils.SharedPreferenceClass;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceListActivity extends AppCompatActivity implements ServiceClickListInterface {
    Intent intent;
    String sub_task_id;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ServiceModel> arrayList;
    private ServiceListAdapter serviceListAdapter;
    private Toolbar toolbar;
    private String token;
    SharedPreferences loginPreferences;

    private TextView textCartItemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);


        loginPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if (loginPreferences.contains("token")) {
            token = loginPreferences.getString("token",null);
        }


        intent = getIntent();

        sub_task_id = intent.getStringExtra("subtask_id");

        toolbar = findViewById(R.id.toolbar_service_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.service_list_recycler);
        arrayList = new ArrayList<>();
        getServices();

    }

    private void getServices() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://taskprovider.herokuapp.com/admin/services/task_items?subtaskId="+sub_task_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                ServiceModel serviceModel = new ServiceModel();

                                serviceModel.setService_id(jsonObject1.getString("_id"));
                                serviceModel.setService_name(jsonObject1.getString("service_name"));
                                serviceModel.setService_description(jsonObject1.getString("service_description"));
                                serviceModel.setService_price(jsonObject1.getString("service_price"));
                                serviceModel.setService_enabled(jsonObject1.getBoolean("service_enabled"));
                                serviceModel.setService_enabled(jsonObject1.getBoolean("service_enabled"));

                                arrayList.add(serviceModel);
                            }


                             serviceListAdapter = new ServiceListAdapter(getApplicationContext(), arrayList, ServiceListActivity.this);

                            if(serviceListAdapter.getItemCount() == 0) {
                                Toast.makeText(getApplicationContext(), "Empty Item", Toast.LENGTH_SHORT).show();
                            }
                            recyclerView.setAdapter(serviceListAdapter);

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ServiceListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });


        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }



    @Override
    public void decrementBtnClick(int position) {

    }

    @Override
    public void incrementBtnClick(String service_id) {
        addToCart(service_id);
    }

    private void addToCart(String service_id) {

        String url = "https://taskprovider.herokuapp.com/user/auth/add_cart?productId="+service_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(ServiceListActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ServiceListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("x-auth-token", token);
                return map;
            }
        };


        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }



    private void showLoginDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Login to add to cart")
                .setPositiveButton("Login", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ServiceListActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ServiceListActivity.this, LoginActivity.class));

                        Toast.makeText(ServiceListActivity.this, "Positive", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        //Dismiss once everything is OK.

                    }
                });
            }
        });
        dialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.product_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.ic_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);


        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());

        int cart_count = sharedPreferenceClass.getValue_int("cart_value");
        setupBadge(cart_count);


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        return true;
    }
    private void setupBadge(int mCartCount) {
        if (textCartItemCount != null) {
            if (mCartCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(mCartCount));
                }
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.ic_cart) {
            startActivity(new Intent(getApplicationContext(), CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
