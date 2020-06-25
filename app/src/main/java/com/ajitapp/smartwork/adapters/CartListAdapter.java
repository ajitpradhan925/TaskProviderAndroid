package com.ajitapp.smartwork.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajitapp.smartwork.Interfaces.CartClickListListener;
import com.ajitapp.smartwork.Interfaces.ServiceClickListInterface;
import com.ajitapp.smartwork.R;
import com.ajitapp.smartwork.models.CartModel;
import com.ajitapp.smartwork.models.ServiceModel;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private List<CartModel> arrayList;
    private Context context;
    private CartClickListListener cartClickListListener;
    public CartListAdapter(Context applicationContext, List<CartModel> arrayList, CartClickListListener cartClickListListener) {
        this.arrayList = arrayList;
        this.context = applicationContext;
        this.cartClickListListener = cartClickListListener;
    }

    @NonNull
    @Override
    public CartListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_row, parent, false);
        context = parent.getContext(); //to get the activity context use this line.

        final CartListAdapter.MyViewHolder myViewHolder = new CartListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.MyViewHolder holder, int position) {
        final String service_name = arrayList.get(position).getService_name();
        final String service_description = arrayList.get(position).getService_description();
        final String service_price = arrayList.get(position).getService_price();

        holder.subtask_name_tv.setText(service_name);
        holder.subtask_price_tv.setText("Rs: "+service_price);
        holder.subtask_description_tv.setText(service_description);

        holder.display_data.setText(arrayList.get(position).getQuantity());

        holder.total_price = Integer.parseInt(arrayList.get(position).getService_price()) * Integer.parseInt(arrayList.get(position).getQuantity());
        holder.total_price_tv.setText("Total Price: Rs."+ arrayList.get(position).getService_price() +
                " * " + arrayList.get(position).getQuantity() + " = Rs."+holder.total_price );
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subtask_name_tv;
        private TextView subtask_description_tv;
        private TextView subtask_price_tv;
        private Button incrementBtn, decrementBtn;
        private TextView display_data;
        private TextView total_price_tv;

        int total_price;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            subtask_name_tv = itemView.findViewById(R.id.subtask_name);
            subtask_description_tv = itemView.findViewById(R.id.subtask_description);
            subtask_price_tv = itemView.findViewById(R.id.subtask_price);


            incrementBtn = itemView.findViewById(R.id.increment);
            decrementBtn = itemView.findViewById(R.id.decrement);
            display_data = itemView.findViewById(R.id.display_data);

            total_price_tv = itemView.findViewById(R.id.total_price);

            final int position = getAdapterPosition();


            incrementBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartClickListListener.incrementBtnClick(getAdapterPosition());
                }
            });


            decrementBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartClickListListener.decrementBtnClick(getAdapterPosition());
                }
            });

//            incrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    decrementBtn.setVisibility(View.VISIBLE);
//                    String display_data1 = display_data.getText().toString();
//
//                        int d_data =  Integer.parseInt(display_data1);
//                        d_data ++;
//                        if(d_data == 4) {
//                            Toast.makeText(context, "Can not add more than two at a time", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            incrementBtn.setVisibility(View.VISIBLE);
//                            display_data.setText(String.valueOf(d_data));
//                        }
//
//                    }
//            });
//
//            decrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String display_data1 = display_data.getText().toString();
//
//
//                    int d_data = Integer.parseInt(display_data1);
//
//                    if (d_data > 0) {
//                        d_data--;
//                        display_data.setText(String.valueOf(d_data));
//                    }
//                }
//
//
//            });
        }



    }



}
