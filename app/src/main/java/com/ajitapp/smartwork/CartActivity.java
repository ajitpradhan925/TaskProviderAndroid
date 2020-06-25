package com.ajitapp.smartwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.CartClickListListener;
import com.ajitapp.smartwork.adapters.CartListAdapter;
import com.ajitapp.smartwork.models.CartModel;
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

public class CartActivity extends AppCompatActivity implements CartClickListListener {
    private String token;

    private RecyclerView recyclerView;
    SharedPreferences loginPreferences;
    private ProgressBar progressBar;
    private CartListAdapter cartListAdapter;
    List<CartModel> arrayList;
    private Button buy_btn;
    String total_price;
    CartModel cartModel;

    private Toolbar toolbar;
    private int mCartItemCount;

    private TextView textCartItemCount;

    private ArrayList<String> service_ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar = findViewById(R.id.progressBar);

        service_ids = new ArrayList<>();


        buy_btn = findViewById(R.id.buy_btn);

        SharedPreferences loginPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if (loginPreferences.contains("token")) {
            token = loginPreferences.getString("token", "");

        }

        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);

        getCart();


        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ChooseAddressActivity.class));
            }
        });

    }

    private void getCart() {
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://taskprovider.herokuapp.com/user/auth/get_cart";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject cartObject = response.getJSONObject("cart");



                            total_price =  cartObject.getString("total_price");
                            buy_btn.setText("BOOK Now: Rs."+total_price);
                            String cart_id =  cartObject.getString("_id");

                            JSONArray jsonArray = cartObject.getJSONArray("task_items");

                            mCartItemCount =jsonArray.length();

                            SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());
                            sharedPreferenceClass.setValue_int("cart_value", mCartItemCount);


                            setupBadge(mCartItemCount);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                cartModel = new CartModel();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                cartModel.setQuantity(jsonObject1.getString("quantity"));

                                JSONObject task_ItemObject = jsonObject1.getJSONObject("task_item");
                                JSONObject subtask_ItemObject = task_ItemObject.getJSONObject("subtask");
                                JSONObject task_cat_ItemObject = subtask_ItemObject.getJSONObject("task");


                                String service_id = task_ItemObject.getString("_id");

                                service_ids.add(service_id);


                                cartModel.setServiceId(service_id);



                                cartModel.setService_description(task_ItemObject.getString("service_description"));
                                cartModel.setService_price(task_ItemObject.getString("service_price"));
                                cartModel.setService_name(task_ItemObject.getString("service_name"));
                                cartModel.setSubtask_name(subtask_ItemObject.getString("subtask_name"));
                                cartModel.setTaskName(task_cat_ItemObject.getString("taskName"));


                                total_price += Integer.parseInt(task_ItemObject.getString("service_price"));

                                arrayList.add(cartModel);
                            }





                            cartListAdapter = new CartListAdapter(getApplicationContext(), arrayList, CartActivity.this);

                            if(cartListAdapter.getItemCount() == 0) {


                                Toast.makeText(getApplicationContext(), "Empty Item", Toast.LENGTH_SHORT).show();
                            }
                            recyclerView.setAdapter(cartListAdapter);

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
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



    private void addCart(String service_id, final String token) {

        progressBar.setVisibility(View.VISIBLE);
        String url = "https://taskprovider.herokuapp.com/user/auth/add_cart?productId="+service_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(CartActivity.this, "Successfully incremented.", Toast.LENGTH_SHORT).show();

                        getCart();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
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


    private void decrementCart(String service_id, final String token) {
        String url = "https://taskprovider.herokuapp.com/user/auth/update_cart?productId="+service_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(CartActivity.this, "Successfully decremented.", Toast.LENGTH_SHORT).show();

                        getCart();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.ic_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);


        return true;
    }



    private void setupBadge(int mCartItemCount) {
        if (textCartItemCount != null) {
            if (this.mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(mCartItemCount));
                }
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.ic_cart) {
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void decrementBtnClick(int position) {
        decrementCart(arrayList.get(position).getServiceId(), token);
    }


    @Override
    public void incrementBtnClick(int position) {
        addCart(arrayList.get(position).getServiceId(), token);
    }
}
