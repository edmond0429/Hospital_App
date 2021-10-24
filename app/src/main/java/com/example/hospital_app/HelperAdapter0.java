package com.example.hospital_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HelperAdapter0 extends RecyclerView.Adapter{
    ArrayList<FetchHospitalProfile> fetchData;
    ArrayList<String> hospitalProfile;

    public HelperAdapter0 (ArrayList<FetchHospitalProfile> fetchData) {
        this.fetchData = fetchData;
        hospitalProfile = new ArrayList<>();
        for (FetchHospitalProfile temp: fetchData) {
            if (!hospitalProfile.contains(temp.gethospitalName())) {
                hospitalProfile.add(temp.gethospitalName());
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_layout,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        final String hospital = hospitalProfile.get(position);
        viewHolderClass.mHospitalProfile.setText(hospital);
        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),DoctorCategory.class);
                i.putExtra("key1", fetchData.get(position)); //fetch hospital profile (class name)
                i.putExtra("key2", hospital);   //String
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hospitalProfile.size();

    }

    public class ViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView mHospitalProfile;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            mHospitalProfile = itemView.findViewById(R.id.hospitalProfile);
        }
    }
}
