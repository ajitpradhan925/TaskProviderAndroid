package com.ajitapp.smartwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.SubTaskClickListInterface;
import com.ajitapp.smartwork.adapters.SubTaskListAdapter;
import com.ajitapp.smartwork.models.SubTaskModel;
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
import java.util.List;

public class SubTaskListActivity extends AppCompatActivity implements SubTaskClickListInterface {
    Intent intent;
    String task_id;
    ProgressDialog progressBar;
    RecyclerView subTask_list_view;


    List<SubTaskModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask_list);
        intent = getIntent();
        task_id = intent.getStringExtra("task_id");



        arrayList = new ArrayList<>();
        subTask_list_view = findViewById(R.id.subtask_list);

        getTaskLists();

    }

    private void getTaskLists() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading...");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();


        subTask_list_view.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        final String url = "https://taskprovider.herokuapp.com/admin/subtask/subtask_by_id/?id="+task_id;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject taskData = jsonObject1.getJSONObject("task");
                            SubTaskModel taskModel = new SubTaskModel(
                                    jsonObject1.getString("subtask_name"),
                                    jsonObject1.getString("_id"),
                                    jsonObject1.getString("slogan"),
                                    taskData.getString("_id"),
                                    taskData.getString("taskName")

                            );

                            arrayList.add(taskModel);
                        }

                        SubTaskListAdapter taskListAdapter = new SubTaskListAdapter(getApplicationContext(), arrayList,
                                SubTaskListActivity.this);

                        if(taskListAdapter.getItemCount() == 0) {
                            Toast.makeText(SubTaskListActivity.this, "Empty Item", Toast.LENGTH_SHORT).show();
                        }
                        subTask_list_view.setAdapter(taskListAdapter);
                    }
                    progressBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubTaskListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

//        {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", task_id);
//                return super.getParams();
//            }
//        };

        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onSubTaskItemClick(int position) {
        Intent intent = new Intent(SubTaskListActivity.this, ServiceListActivity.class);
        intent.putExtra("subtask_id", arrayList.get(position).getSubTaskId());
        startActivity(intent);
    }
}
