package com.ajitapp.smartwork.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ajitapp.smartwork.Interfaces.SubTaskClickListInterface;
import com.ajitapp.smartwork.Interfaces.TaskClickListInterface;
import com.ajitapp.smartwork.R;
import com.ajitapp.smartwork.models.SubTaskModel;
import com.ajitapp.smartwork.models.TaskModel;
import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.MyViewHolder>{
    Context context;
    Context activity;
    List<SubTaskModel> arrayList;
    SubTaskClickListInterface taskClickListInterface;
    public SubTaskListAdapter(Context context, List<SubTaskModel> arrayList, SubTaskClickListInterface taskClickListInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.taskClickListInterface = taskClickListInterface;
    }


    @NonNull
    @Override
    public SubTaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subtask_list_row, parent, false);
        SubTaskListAdapter.MyViewHolder myViewHolder=new SubTaskListAdapter.MyViewHolder(view);
        activity = view.getContext();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubTaskListAdapter.MyViewHolder holder, int position) {
        final String name=arrayList.get(position).getSubTaskName();

//        holder.taskCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), SubTaskActivity.class);
//                i.putExtra("task_id", task_id);
//                i.putExtra("task_name", name);
//                activity.startActivity(i);
//            }
//        });
        holder.name.setText(name);

//        Picasso.with(context).load(image_path)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;
        CardView taskCardView;


        public MyViewHolder(View viewItem){
            super(viewItem);
            this.name=(TextView) viewItem.findViewById(R.id.subtask_name);


            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskClickListInterface.onSubTaskItemClick(getAdapterPosition());
                }
            });

        }

    }
}
