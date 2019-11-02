package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class StreamActivity extends AppCompatActivity {

    RestClient restClient;
    RecyclerAdapter adapter;
    List<Tweet> tweetsList;
    RecyclerView twitterFeedRV;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Twitter");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00bfff")));
        actionBar.setIcon(R.drawable.ic_launcher);

        //pull to refresh
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restClient.getTweetList(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONArray jsonArray = json.jsonArray;
                        try {
                            adapter.clear();
                            adapter.addAll(Tweet.jsonDataArray(jsonArray));
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("StreamActivity", throwable + "");
                    }
                });
            }
        });


        twitterFeedRV = findViewById(R.id.twitterFeedRV);
        tweetsList = new ArrayList<>();
        adapter = new RecyclerAdapter(this, (ArrayList<Tweet>) tweetsList);
        twitterFeedRV.setLayoutManager(new LinearLayoutManager(this));
        twitterFeedRV.setAdapter(adapter);

        restClient = TwitterApplication.getRestClient(this);
        restClient.getTweetList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.jsonDataArray(jsonArray));
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("StreamActivity", throwable + "");
            }
        });

    }
}
