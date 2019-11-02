package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

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
                    tweetsList.addAll(Tweet.jsonDataArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }
}
