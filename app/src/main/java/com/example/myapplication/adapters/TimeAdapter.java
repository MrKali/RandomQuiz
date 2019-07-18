package com.example.myapplication.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.TimeModel;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TimeModel> stringArrayList;
    private int lastPosition = -1;



    public TimeAdapter(Context context, ArrayList<TimeModel> arrayList) {
        this.context = context;
        this.stringArrayList = arrayList;
    }

    @Override
    public TimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.MyViewHolder holder, int position) {
        final TimeModel timeModel = stringArrayList.get(position);

        final String time = "Question " + timeModel.getNumberOfQuestion() + ": " + timeModel.getTimeOfQuestion();
        holder.textViewTime.setText(time);

        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View viewToAnimate, int position)
    { if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public int getItemCount() {
        return stringArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

}
