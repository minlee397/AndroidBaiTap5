package com.example.baitap5;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.baitap5.MainActivity.TAG;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    ImageView imageView;
    List<Student> data;
    Student search;

    public RecyclerViewAdapter (Context mContext,List<Student> data)
    {
        this.mContext=mContext;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recyclerview_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        TextView txtFullName = view.findViewById(R.id.txtFirstName);
        TextView txtStudentID = view.findViewById(R.id.txtStudentID);
        TextView txtDay = view.findViewById(R.id.txtUpdate);
        imageView = view.findViewById(R.id.imgPortrait);
        ConstraintLayout constraintContent = view.findViewById(R.id.constraintContent);

        final Student temp_data = data.get(i);
        txtFullName.setText(temp_data.getmFirstName() +" - "+ temp_data.getmLastName());
        txtStudentID.setText(temp_data.getmMSSV());
        String strHour = "", strMinute = "", strSecond = "";
        int hour = temp_data.getmUpdatedTime().getHour();
        int minute = temp_data.getmUpdatedTime().getMinute();
        int second = temp_data.getmUpdatedTime().getSecond();
        if(hour < 10)
            strHour = "0" + hour;
        else
            strHour = ""+ hour;
        if(minute < 10)
            strMinute = "0" + minute;
        else
            strMinute = "" + minute;
        if(second < 10)
            strSecond = "0" + second;
        else
            strSecond = "" + second;

        String time = String.valueOf(strHour + ":" + strMinute + ":" +strSecond);
        txtDay.setText(temp_data.getmUpdatedDay()+" At "+time);

        int resID = view.getResources().getIdentifier(temp_data.getmImages(),"drawable",mContext.getPackageName());
        imageView.setImageResource(resID);

        constraintContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailActivity.class);;
                intent.putExtra("Student_FirstName",temp_data.getmFirstName());
                intent.putExtra("Student_LastName",temp_data.getmLastName());
                intent.putExtra("Student_Images",temp_data.getmImages());
                intent.putExtra("Student_ID",temp_data.getmMSSV());
                intent.putExtra("Student_Day",temp_data.getmUpdatedDay().toString());
                intent.putExtra("Student_Time",temp_data.getmUpdatedTime().toString());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void filterList(List<Student> filteredList)
    {
        if(filteredList.size()==2)
        {
            filteredList.remove(0);
        }
        data = filteredList;
        Log.wtf(TAG, "filterList size:"+filteredList.size());
        notifyDataSetChanged();
    }
}

