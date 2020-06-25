package com.ajitapp.smartwork.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajitapp.smartwork.Interfaces.ServiceClickListInterface;
import com.ajitapp.smartwork.R;
import com.ajitapp.smartwork.ServiceListActivity;
import com.ajitapp.smartwork.models.ServiceModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    private List<ServiceModel> arrayList;
    private Context context;
    private ServiceClickListInterface serviceClickListInterface;

    SharedPreferences sharedPreferences;
    String token;
    public ServiceListAdapter(Context applicationContext, List<ServiceModel> arrayList, ServiceClickListInterface serviceClickListInterface) {
        this.arrayList = arrayList;
        this.context = applicationContext;
        this.serviceClickListInterface = serviceClickListInterface;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_list_row, parent, false);
        context = parent.getContext(); //to get the activity context use this line.

        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String service_name = arrayList.get(position).getService_name();
        final String service_description = arrayList.get(position).getService_description();
        final String service_price = arrayList.get(position).getService_price();

        holder.subtask_name_tv.setText(service_name);
        holder.subtask_price_tv.setText("Rs: "+service_price);
        holder.subtask_description_tv.setText(service_description);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subtask_name_tv;
        private TextView subtask_description_tv;
        private TextView subtask_price_tv;
        private TextView add_to_cart_tv;
        private TextView display_data;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
            if (sharedPreferences.contains("token")) {
                token = sharedPreferences.getString("token", null);
            }
            subtask_name_tv = itemView.findViewById(R.id.subtask_name);
            subtask_description_tv = itemView.findViewById(R.id.subtask_description);
            subtask_price_tv = itemView.findViewById(R.id.subtask_price);



            add_to_cart_tv = itemView.findViewById(R.id.add_to_cart);
            display_data = itemView.findViewById(R.id.display_data);

//            incrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    serviceClickListInterface.incrementBtnClick(arrayList.get(getAdapterPosition()).getService_id(), display_data);
//                }
//            });

            add_to_cart_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            serviceClickListInterface.incrementBtnClick(arrayList.get(getAdapterPosition()).getService_id());

                }
            });

//            decrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String display_data1 = display_data.getText().toString();
//                    int d_data =  Integer.parseInt(display_data1);
//
//                    if(d_data > 0) {
//                        d_data --;
//                        display_data.setText(String.valueOf(d_data));
//                    }
//
//                }
//            });
        }

    }

//    private void addToCart(int data, int position) {
//
//
//        Toast.makeText(context, String.valueOf(arrayList.get(position).getService_name()), Toast.LENGTH_SHORT).show();
//
//
//    }


}
