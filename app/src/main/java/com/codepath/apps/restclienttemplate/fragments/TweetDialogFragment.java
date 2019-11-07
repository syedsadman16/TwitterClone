package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.StreamActivity;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Headers;

public class TweetDialogFragment extends DialogFragment {

    public static String TAG = "TweetDialogFragment";
    EditText tweet_edit_field;
    RestClient client;
    Button send_tweet;
    ImageView imageView;
    Tweet tweet = new Tweet();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.create_tweet_fragment, container, false);
        send_tweet = (Button) rootView.findViewById(R.id.create_tweet);
        tweet_edit_field = (EditText) rootView.findViewById(R.id.tweet_edit_field);
        imageView = rootView.findViewById(R.id.imageView);
        client = TwitterApplication.getRestClient(getContext());
        

        send_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String tweet_body = tweet_edit_field.getText().toString();

                if(tweet_body.isEmpty()){
                    tweet_edit_field.setError("Cannot be empty");
                }
                else {

                    client.postTweet(tweet_body, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Toast.makeText(getContext(), "Tweet posted", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, throwable + "");
                        }
                    });
                }
            }
        });

        return rootView;
    }

    public TweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static TweetDialogFragment newInstance(String title) {
        TweetDialogFragment frag = new TweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height = (int) (500 * (getResources().getDisplayMetrics().density));
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }



}