package com.codepath.apps.restclienttemplate.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.Networking.RestClient;
import com.codepath.apps.restclienttemplate.Networking.TwitterApplication;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Adapters.RecyclerAdapter;
import com.codepath.apps.restclienttemplate.fragments.TweetDialogFragment;
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
    public static String TAG = "StreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Twitter");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#110011")));
        actionBar.setLogo(R.drawable.twitter);
        actionBar.setDisplayUseLogoEnabled(true);


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
                        } catch (JSONException e) { e.printStackTrace(); }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, throwable + "");
                    }
                });
            }
        });


        twitterFeedRV = findViewById(R.id.twitterFeedRV);
        tweetsList = new ArrayList<>();
        adapter = new RecyclerAdapter(this, (ArrayList<Tweet>) tweetsList);
        twitterFeedRV.setLayoutManager(new LinearLayoutManager(this));
        twitterFeedRV.setAdapter(adapter);
        twitterFeedRV.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        //Return the tweet list as json
        restClient = TwitterApplication.getRestClient(this);
        restClient.getTweetList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                Log.i(TAG,jsonArray+"");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                //launch an intent
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TweetDialogFragment editNameDialogFragment = TweetDialogFragment.newInstance("Tweet something");
        editNameDialogFragment.show(fm, "tweet_dialog_fragment");
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    
}

