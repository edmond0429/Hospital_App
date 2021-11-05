package com.example.hospital_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class bookRequestAdapter extends RecyclerView.Adapter<bookRequestAdapter.BookRequestViewHolder> {

    Context mContext;
    ArrayList<Booking> requestList;
    DatabaseReference mDatabaseReference ;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public bookRequestAdapter(Context context, ArrayList<Booking> requestList) {
        mContext = context;
        this.requestList = requestList;
    }


    @NonNull
    @Override
    public BookRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookrequest,parent,false);
        return new BookRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRequestViewHolder holder, int position) {
        BookRequestViewHolder bookingRequestViewHolder = (BookRequestViewHolder)holder;

        Booking bookrequest = requestList.get(position);
        bookingRequestViewHolder.tvPatientName.setText(bookrequest.getPatientName());
        bookingRequestViewHolder.tvDoctor.setText(bookrequest.getDoctorName());
        bookingRequestViewHolder.tvRequestDate.setText(bookrequest.getBookingDate());
        bookingRequestViewHolder.tvRequestTime.setText(bookrequest.getBookingTime());
        bookingRequestViewHolder.tvRequestMethod.setText(bookrequest.getBookingMethod());
        bookingRequestViewHolder.tvHospitalName.setText(bookrequest.getHospitalName());

        bookingRequestViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference("RequestBooking")
                        .child(bookrequest.getPatientName());
                dbRequest.removeValue();

                Booking booking = new Booking(bookrequest.getPatientName(),bookrequest.getBookingDate(),
                        bookrequest.getBookingTime(),bookrequest.getBookingMethod(),bookrequest.getDoctorName(),bookrequest.getHospitalName());
                mDatabase.getReference("Accepted Booking")
                                    .child(bookrequest.getPatientName()).push()
                                    .setValue(booking);
//                MotionToast.Companion.darkColorToast(this,"Added a booking request!",
//                        "Successful request a booking!",
//                        MotionToast.TOAST_SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(mContext,R.font.helvetica_regular));
            }
        });

        bookingRequestViewHolder.btnDeclined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("RequestBooking").child(bookrequest.getPatientName());
                mDatabaseReference.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class BookRequestViewHolder extends RecyclerView.ViewHolder {

        TextView tvPatientName, tvDoctor, tvRequestDate, tvRequestTime, tvRequestMethod, tvHospitalName;
        Button btnAccept, btnDeclined;

        public BookRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvRequestDate = itemView.findViewById(R.id.tvRequestBookingDate);
            tvRequestTime = itemView.findViewById(R.id.tvRequestBookingTime);
            tvRequestMethod = itemView.findViewById(R.id.tvRequestBookingMethod);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDeclined = itemView.findViewById(R.id.btnDeclined);



        }
    }
}
