package com.example.hospital_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAcceptedAdapter extends RecyclerView.Adapter<ViewAcceptedAdapter.AcceptedViewHolder> {

    Context mContext;
    ArrayList<Booking> acceptedList;

    public ViewAcceptedAdapter(Context context, ArrayList<Booking> acceptedList) {
        mContext = context;
        this.acceptedList = acceptedList;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.accepted_request,parent,false);
        return new AcceptedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position) {
        AcceptedViewHolder acceptedViewHolder = (AcceptedViewHolder)holder;

        Booking bookrequest = acceptedList.get(position);
        acceptedViewHolder.tvPatientName.setText(bookrequest.getPatientName());
        acceptedViewHolder.tvDoctor.setText(bookrequest.getDoctorName());
        acceptedViewHolder.tvRequestDate.setText(bookrequest.getBookingDate());
        acceptedViewHolder.tvRequestTime.setText(bookrequest.getBookingTime());
        acceptedViewHolder.tvRequestMethod.setText(bookrequest.getBookingMethod());
    }

    @Override
    public int getItemCount() {
        return acceptedList.size();
    }

    public static class AcceptedViewHolder extends RecyclerView.ViewHolder {

        TextView tvPatientName, tvDoctor, tvRequestDate, tvRequestTime, tvRequestMethod;

        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvRequestDate = itemView.findViewById(R.id.tvRequestBookingDate);
            tvRequestTime = itemView.findViewById(R.id.tvRequestBookingTime);
            tvRequestMethod = itemView.findViewById(R.id.tvRequestBookingMethod);
        }
    }
}
