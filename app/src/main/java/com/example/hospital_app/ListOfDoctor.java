package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfDoctor extends AppCompatActivity {

    HelperAdapter2 mHelperAdapater2;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_doctor);

        mRecyclerView = findViewById(R.id.recycleViewListOfDoctor);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        ArrayList<FetchData> fetchData = (ArrayList<FetchData>) bundle.getSerializable("key1");
        String category = bundle.getString("key2");
        ArrayList<FetchData> temp = new ArrayList<>();
        for (FetchData fd: fetchData) {
            if (fd.getCategory().equals(category)){
                temp.add(fd);
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHelperAdapater2 = new HelperAdapter2(temp);
        mRecyclerView.setAdapter(mHelperAdapater2);

    }
}