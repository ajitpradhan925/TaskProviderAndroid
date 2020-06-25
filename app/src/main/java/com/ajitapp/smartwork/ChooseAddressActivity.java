package com.ajitapp.smartwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.AddressClickListener;
import com.ajitapp.smartwork.adapters.AddressListAdapter;
import com.ajitapp.smartwork.adapters.CartListAdapter;
import com.ajitapp.smartwork.models.AddressModal;
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

public class ChooseAddressActivity extends AppCompatActivity implements AddressClickListener {
    private SharedPreferenceClass sharedPreferenceClass;
    private RecyclerView recyclerView;
    private List<AddressModal> arrayList;
    private AddressListAdapter addressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.address_list);

        sharedPreferenceClass = new SharedPreferenceClass(this);
        String token = sharedPreferenceClass.getValue_string("token");

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        getAuthUser(token);
    }


    private void getAuthUser(final String token) {
        String url = "https://taskprovider.herokuapp.com/user/auth";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("user");
                    JSONArray addressArray = jsonObject.getJSONArray("address");

                    for( int i = 0; i < addressArray.length(); i ++) {
                        JSONObject jsonObject1 = addressArray.getJSONObject(i);

                        AddressModal addressModal = new AddressModal();

                        addressModal.setAddr1(jsonObject1.getString("addr1"));
                        addressModal.setAddr2(jsonObject1.getString("addr2"));
                        addressModal.setCity(jsonObject1.getString("city"));
                        addressModal.setCountry(jsonObject1.getString("country"));
                        addressModal.setState(jsonObject1.getString("state"));
                        addressModal.setPostalCode(jsonObject1.getString("postalCode"));


                        arrayList.add(addressModal);
                    }

                    addressListAdapter = new AddressListAdapter(getApplicationContext(), arrayList, ChooseAddressActivity.this);

                    if(addressListAdapter.getItemCount() == 0) {


                        Toast.makeText(getApplicationContext(), "Empty Item", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.setAdapter(addressListAdapter);

                    Toast.makeText(ChooseAddressActivity.this, addressArray.toString(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("x-auth-token", token);

                return params;
            }


        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void clickAddressRadioButton(int position) {

    }
}
