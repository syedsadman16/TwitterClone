package com.codepath.apps.restclienttemplate.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public String image_url;
    public String screen_name;

    public static User jsonData(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.image_url = jsonObject.getString("profile_image_url_https");
        user.screen_name = jsonObject.getString("screen_name");
        return user;
    }
}
