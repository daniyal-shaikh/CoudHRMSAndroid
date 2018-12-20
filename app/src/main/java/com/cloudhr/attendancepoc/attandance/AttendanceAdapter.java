package com.cloudhr.attendancepoc.attandance;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudhr.attendancepoc.R;
import com.cloudhr.attendancepoc.core.model.SwipeDetailEntity;


import java.util.List;

/**
 * Created by IN-RB on 22-05-2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendeanceItem> {


    Activity mContext;
    List<SwipeDetailEntity> attendDetailEntityList;



    public AttendanceAdapter(Activity context, List<SwipeDetailEntity> attendDetailEntityList) {

        this.mContext = context;
        this.attendDetailEntityList = attendDetailEntityList;
    }

    public class AttendeanceItem extends RecyclerView.ViewHolder{

        public TextView txtDate, txtloc,txtType;
        public AttendeanceItem(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtloc = (TextView)itemView.findViewById(R.id.txtLoc);
            txtType = (TextView)itemView.findViewById(R.id.txtType);
        }
    }
    @Override
    public AttendanceAdapter.AttendeanceItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item, parent, false);

        return new AttendanceAdapter.AttendeanceItem(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.AttendeanceItem holder, int position) {

        final SwipeDetailEntity swipeDetailsEntity = attendDetailEntityList.get(position);
        holder.txtDate.setText(swipeDetailsEntity.getCreateddate());

        holder.txtloc.setText(swipeDetailsEntity.getAddress());//swipeDetailsEntity.getLocation()
        holder.txtType.setText(swipeDetailsEntity.getType());

    }

    @Override
    public int getItemCount() {
        return attendDetailEntityList.size();
    }
}
