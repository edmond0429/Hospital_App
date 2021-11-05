package com.example.hospital_app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatbot extends AppCompatActivity {

    private RecyclerView chatsRV;
    private FloatingActionButton sendMsgFab;
    private EditText userMsgEdt;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModel> mChatsModelArrayList;
    private ChatRVAdapter mChatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        sendMsgFab = findViewById(R.id.idfabSend);

        //create arraylist and adapter
        mChatsModelArrayList = new ArrayList<>();
        //initialing adapter class and passing array list to it.
        mChatRVAdapter = new ChatRVAdapter(mChatsModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);          //set layout manager to handle recycler view.
        chatsRV.setAdapter(mChatRVAdapter);         //set adapter to recycler view

        sendMsgFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(chatbot.this, "Please enter message to get respond from chatbot", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());       //method to send msg to chatbot as to get respond
                userMsgEdt.setText("");                             //after send line, empty the edit text for user
            }
        });
    }

    private void getResponse(String message){
        //pass msg to array list with user key
        mChatsModelArrayList.add(new ChatsModel(message,USER_KEY));
        mChatRVAdapter.notifyDataSetChanged();
        //url for connecting the brain of chatbot in brainshop
        String url = "http://api.brainshop.ai/get?bid=160841&key=EJjtMISmlRdvmh8i&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);                          //pass the url to get the API
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if(response.isSuccessful()){
                    MsgModel model = response.body();
                    mChatsModelArrayList.add(new ChatsModel(model.getCnt(),BOT_KEY));       //if get chat from user, bot will answer
                    mChatRVAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                mChatsModelArrayList.add(new ChatsModel("Please revert your question",BOT_KEY));        // handling error response from bot.
                mChatRVAdapter.notifyDataSetChanged();
            }
        });
    }
}