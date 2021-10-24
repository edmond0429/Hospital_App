package com.example.hospital_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HelperAdapter extends RecyclerView.Adapter{

    ArrayList<FetchData> fetchData;
    ArrayList<String> doctorCategory;

    public HelperAdapter(ArrayList<FetchData> fetchData) {
        this.fetchData = fetchData;
        doctorCategory = new ArrayList<>();
        for (FetchData temp: fetchData) {
            if (!doctorCategory.contains(temp.getCategory())) {
                doctorCategory.add(temp.getCategory());
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_category,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        final String category = doctorCategory.get(position);
        viewHolderClass.mCategory.setText(category);
        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),ListOfDoctor.class);
                i.putExtra("key1", fetchData); //arraylist
                i.putExtra("key2", category);   //String
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorCategory.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView mCategory;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            mCategory = itemView.findViewById(R.id.category);
        }
    }
}
