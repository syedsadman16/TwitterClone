package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Tweet> tweets;

    public RecyclerAdapter(Context ctx, ArrayList<Tweet> t){
        this.tweets = t;
        this.mContext = ctx;
    }


    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    //holds references to elements in recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView full_name;
        public TextView screen_name;
        public TextView tweet_body;
        public TextView creation_time;
        public ImageView profile_img;


        public ViewHolder(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            screen_name = (TextView) itemView.findViewById(R.id.screen_name);
            tweet_body = (TextView) itemView.findViewById(R.id.tweet_body);
            creation_time = (TextView) itemView.findViewById(R.id.creation_time);
            profile_img = (ImageView) itemView.findViewById(R.id.profile_img);
        }


        public void populateView(final Tweet tweet) {
            full_name.setText(tweet.user.name);
            screen_name.setText("@"+tweet.user.screen_name);
            tweet_body.setText(tweet.tweet_body);
            creation_time.setText(getRelativeTimeAgo(tweet.created_at) + "s");
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

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
