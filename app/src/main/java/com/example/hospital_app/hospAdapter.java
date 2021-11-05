package com.example.hospital_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class hospAdapter extends RecyclerView.Adapter<hospAdapter.HospViewHolder> {
    Context mContext;
    ArrayList<ReviewModel> listHospModel;
    String hspName;

    public hospAdapter(Context context, ArrayList<ReviewModel> listHospModel) {
        mContext = context;
        this.listHospModel = listHospModel;
    }


    @NonNull
    @Override
    public HospViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hosp, parent, false);
        return new HospViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospViewHolder holder, int position) {
        HospViewHolder hospViewHolder = (HospViewHolder)holder;

        ReviewModel hospitalModel = listHospModel.get(position);
        hospViewHolder.totalStarRating.setRating(Float.parseFloat(String.valueOf(hospitalModel.getTotalStarGiven())));
        hospViewHolder.tvHospName.setText(hospitalModel.getHospitalName());

    }

    @Override
    public int getItemCount() {
        return listHospModel.size();
    }

    public class HospViewHolder extends RecyclerView.ViewHolder {

        TextView tvHospName;
        MaterialRatingBar totalStarRating;
        CardView cvItem;

        public HospViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHospName = itemView.findViewById(R.id.tv_hosp_name);
            totalStarRating = itemView.findViewById(R.id.total_star_rating);
            cvItem = itemView.findViewById(R.id.cv_item);
        }

    }
}

