package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import java.util.ArrayList;

public class RecyclerAdapter extends
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Tweet> tweets;

    public RecyclerAdapter(Context ctx, ArrayList<Tweet> t){
        this.tweets = t;
        this.mContext = ctx;
    }


    //holds references to elements in recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView full_name;
        public TextView screen_name;
        public TextView tweet_body;
        public ImageView profile_img;


        public ViewHolder(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            screen_name = (TextView) itemView.findViewById(R.id.screen_name);
            tweet_body = (TextView) itemView.findViewById(R.id.tweet_body);
            profile_img = (ImageView) itemView.findViewById(R.id.profile_img);
        }


        public void populateView(final Tweet tweet) {
            full_name.setText(tweet.user.name);
            screen_name.setText(tweet.user.screen_name);
            tweet_body.setText(tweet.tweet_body);
            Glide.with(mContext).load(tweet.user.image_url).placeholder(R.drawable.placeholder)
                    .error(R.drawable.imagenotfound).apply(new RequestOptions()).into(profile_img);
        }

    }

    // Inflating a layout file and returning it to view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate from XML file
        View movieView = LayoutInflater.from(mContext).inflate(R.layout.stream_recycler_layout, parent, false);
        return new ViewHolder(movieView);
    }


    // Get data from position and put into view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.populateView(tweet);
    }


    @Override
    public int getItemCount() {
        return tweets.size();
    }

}
