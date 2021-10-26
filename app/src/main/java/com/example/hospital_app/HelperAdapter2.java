package com.example.hospital_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import www.sanju.motiontoast.MotionToast;

public class HelperAdapter2 extends RecyclerView.Adapter{
    List<FetchData> fetchData;
    DatabaseReference mDatabaseReference ;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
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

        if (mFirebaseAuth.getCurrentUser() != null) {
            viewHolderClass.btnFav.setTag(R.drawable.ic_baseline_shadow_24);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("FavouriteDoctor")
                    .child(mFirebaseAuth.getCurrentUser().getUid());
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String FavKey = ds.getKey();
                        if(FavKey.equals(fetchDataList.getDoctorDescription())){
                            viewHolderClass.btnFav.setImageResource(R.drawable.ic_baseline_red_24);
                            viewHolderClass.btnFav.setTag(R.drawable.ic_baseline_red_24);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            viewHolderClass.btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer res = (Integer) viewHolderClass.btnFav.getTag();
                    if (res == R.drawable.ic_baseline_red_24) {
                        MotionToast.Companion.darkColorToast((Activity) view.getContext(),
                                "Unfavourited!", "You had Unfavourite the doctor",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(),R.font.helvetica_regular));
                        viewHolderClass.btnFav.setImageResource(R.drawable.ic_baseline_shadow_24);
                        viewHolderClass.btnFav.setTag(R.drawable.ic_baseline_shadow_24);
                        DatabaseReference dbFav = mDatabase.getReference("FavouriteDoctor")
                                .child(mFirebaseAuth.getCurrentUser().getUid()).child(fetchDataList.getDoctorDescription());
                        dbFav.removeValue();
                    } else {
                        MotionToast.Companion.darkColorToast((Activity) view.getContext(),"Favourited!",
                                "You had Favourite the doctor",
                                MotionToast.TOAST_INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(),R.font.helvetica_regular));
                        viewHolderClass.btnFav.setImageResource(R.drawable.ic_baseline_red_24);
                        viewHolderClass.btnFav.setTag(R.drawable.ic_baseline_red_24);
                        favDoctor mFavbook = new favDoctor(fetchDataList.getHospitalName(), fetchDataList.getCategory(),
                                fetchDataList.getDoctorName(), fetchDataList.getDoctorDescription(), fetchDataList.getDoctorUrl());

                        mDatabase.getReference("FavouriteDoctor")
                                .child(mFirebaseAuth.getCurrentUser().getUid())
                                .child(fetchDataList.getDoctorDescription())
                                .setValue(mFavbook);
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return fetchData.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView mDoctorDescription, mDoctor;
        ImageView mImageView;
        ImageButton btnFav;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            mDoctor = itemView.findViewById(R.id.doctorName);
            mDoctorDescription = itemView.findViewById(R.id.doctorDescription);
            mImageView = itemView.findViewById(R.id.imgDoctor);
            btnFav = itemView.findViewById(R.id.favBtn);
        }
    }
}
