package com.example.hospital_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    Context mContext;
    ArrayList<MedicAlarm> alarmlist;

    public AlarmAdapter(Context context, ArrayList<MedicAlarm> alarmlist) {
        mContext = context;
        this.alarmlist = alarmlist;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alarm_item,parent,false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmViewHolder alarmViewHolder = (AlarmViewHolder)holder;

        MedicAlarm medicAlarm = alarmlist.get(position);
        alarmViewHolder.tvMedicTitle.setText(medicAlarm.getMedicTitle());
        alarmViewHolder.tvMedicTime.setText(medicAlarm.getMedicTime());
        alarmViewHolder.tvRepetition.setText(medicAlarm.getMedicRepetition());

    }

    @Override
    public int getItemCount() {
        return alarmlist.size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        TextView tvMedicTitle, tvMedicTime, tvRepetition;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMedicTitle = itemView.findViewById(R.id.tvMedicTitle);
            tvMedicTime = itemView.findViewById(R.id.tvMedicTime);
            tvRepetition = itemView.findViewById(R.id.tvRepetition);
        }

    }


}
