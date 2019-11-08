package com.codepath.apps.restclienttemplate.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String tweet_body;
    public String created_at;
    public User user;

    public static Tweet jsonData(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.tweet_body = jsonObject.getString("text");
        tweet.created_at = jsonObject.getString("created_at");
        tweet.user = User.jsonData(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public static List<Tweet> jsonDataArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweetList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            tweetList.add(jsonData(jsonArray.getJSONObject(i)));
        }
        return tweetList;
    }


}
