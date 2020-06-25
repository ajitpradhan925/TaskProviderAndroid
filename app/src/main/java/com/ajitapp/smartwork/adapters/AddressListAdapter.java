package com.ajitapp.smartwork.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajitapp.smartwork.ChooseAddressActivity;
import com.ajitapp.smartwork.Interfaces.AddressClickListener;
import com.ajitapp.smartwork.Interfaces.CartClickListListener;
import com.ajitapp.smartwork.R;
import com.ajitapp.smartwork.models.AddressModal;
import com.ajitapp.smartwork.models.CartModel;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    private List<AddressModal> arrayList;
    private Context context;
    private AddressClickListener cartClickListListener;
    public AddressListAdapter(Context applicationContext, List<AddressModal> arrayList, ChooseAddressActivity cartClickListListener) {
        this.arrayList = arrayList;
        this.context = applicationContext;
        this.cartClickListListener = cartClickListListener;
    }

    @NonNull
    @Override
    public AddressListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_address_row_item, parent, false);
        context = parent.getContext(); //to get the activity context use this line.

        final AddressListAdapter.MyViewHolder myViewHolder = new AddressListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.MyViewHolder holder, int position) {

        holder.addr1.setText(arrayList.get(position).getAddr1());
        holder.addr2.setText(arrayList.get(position).getAddr2());
        holder.city.setText(arrayList.get(position).getCity());
        holder.state.setText(arrayList.get(position).getCity());
        holder.country.setText(arrayList.get(position).getCountry());
        holder.postalCode.setText(arrayList.get(position).getPostalCode());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView addr1, addr2, city, state, country, postalCode;
        private TextView subtask_description_tv;
        private TextView subtask_price_tv;
        private Button incrementBtn, decrementBtn;
        private TextView display_data;
        private TextView total_price_tv;

        int total_price;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            addr1 = itemView.findViewById(R.id.addr1);
            addr2 = itemView.findViewById(R.id.addr2);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            country = itemView.findViewById(R.id.country);
            postalCode = itemView.findViewById(R.id.postalCode);
//            subtask_description_tv = itemView.findViewById(R.id.subtask_description);
//            subtask_price_tv = itemView.findViewById(R.id.subtask_price);
//

        }


    }
}
