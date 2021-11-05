package com.example.hospital_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HelperAdapter2 extends RecyclerView.Adapter{
    List<FetchData> fetchData;
    private Context mContext;

    public HelperAdapter2(List<FetchData> fetchData) {
        this.fetchData = fetchData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorlist_layout,parent,false);
        HelperAdapter2.ViewHolderClass viewHolderClass = new HelperAdapter2.ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HelperAdapter2.ViewHolderClass viewHolderClass = (HelperAdapter2.ViewHolderClass) holder;
        final FetchData fetchDataList = fetchData.get(position);
        viewHolderClass.mDoctor.setText(fetchDataList.getDoctorName());
        viewHolderClass.mDoctorDescription.setText(fetchDataList.getDoctorDescription());
        viewHolderClass.mDoctorDescription.setTextColor(Color.parseColor("#3b0101"));
        Picasso.get().load(fetchDataList.getDoctorUrl()).into(viewHolderClass.mImageView);

        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DoctorDetails.class);
                i.putExtra("key", fetchDataList);
                view.getContext().startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return fetchData.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView mDoctorDescription, mDoctor;
        ImageView mImageView;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            mDoctor = itemView.findViewById(R.id.doctorName);
            mDoctorDescription = itemView.findViewById(R.id.doctorDescription);
            mImageView = itemView.findViewById(R.id.imgDoctor);
        }
    }
}
