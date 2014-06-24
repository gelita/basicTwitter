package com.codepath.apps.basictwitter.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.text.format.DateUtils;


public class Tweet {
	private String body;
	private long uid;
	private String createdAt;
	private User user;	
	@SuppressWarnings("unused")
	private String timeCreated;

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}
	@Override
	public String toString(){
		return getBody()+ " - " + getUser().getScreenName();
	}

	public String getCreatedAt() throws java.text.ParseException {
		createdAt = getRelativeTimeAgo(createdAt);
		return createdAt;
	}

	public User getUser(){
		return user;		
	}

	public Tweet(User user, String body, String timeCreated) {
		super();
		this.user = user;
		this.body = body;
		this.createdAt = timeCreated;
	}
	public Tweet(){
		super();			
	}

	//method for passing in the ENTIRE JSONArray and return it in array of tweets		
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i=0; i<jsonArray.length(); i++){
			JSONObject tweetJson = null;
			try{
				tweetJson = jsonArray.getJSONObject(i);								
			}catch(Exception e){
				e.printStackTrace();
				continue;			
			}

			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet !=null){
				tweets.add(tweet);
			}
		}
		return tweets;
	}	

	public static Tweet fromJSON(JSONObject jsonObject){
		//make a new tweet (singular)
		Tweet tweet = new Tweet();
		//extract values fr the json to populate the member variables
		try{
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			//user is it's own object completely coming fr json
			//this is a User object embedded in a tweet object
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));			
		}catch (JSONException e){
			//problem w/the tweet then return as NULL
			e.printStackTrace();
			return null;			
		}
		return tweet;
	}	
	public String getRelativeTimeAgo(String rawJsonDate) throws java.text.ParseException {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return relativeDate;
	}
}