package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private static final int REQUEST_CODE = 20;
	protected TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets; //list view variable
	public User authUser;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		//gives the instance of the twitterclient that 
		//was created at the time of login
		if(isNetworkAvailable() == false){
			Toast.makeText(this,"Please check internet connection.",Toast.LENGTH_SHORT).show();
		} else {
			client = TwitterApplication.getRestClient();		
			populateTimeline("0", false);
			lvTweets = (ListView) findViewById(R.id.lvTweets);
			tweets = new ArrayList<Tweet>();	//a single tv for each item
			aTweets = new TweetArrayAdapter(this,tweets);
			lvTweets.setAdapter(aTweets);
			client.getAuthUser(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject jsonUser){
					authUser = User.fromJSON(jsonUser);
				}				
			});		
			lvTweets.setOnScrollListener(new EndlessScrollListener(){
				@Override
				public void onLoadMore(int page, int totalItemsCount) {				
					//must pass page!
					loadMore(page);
				}
			});	
		}
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo !=null && activeNetworkInfo.isConnectedOrConnecting();	
	}

	public void loadMore(int page){
		Tweet tweetLast = tweets.get(tweets.size()-1);
		String tweetLastId = String.valueOf(tweetLast.getUid());
		populateTimeline(tweetLastId, false);		
	}

	public void populateTimeline(String maxId, boolean clearTweets){		
		final boolean clearAll = clearTweets;
		client.getHomeTimeline(new JsonHttpResponseHandler(){		
			//this handler takes the json and puts it into a very manageable form
			@Override
			public void onSuccess(JSONArray json){
				super.onSuccess(json);
				if(clearAll == true){
					tweets.clear();
				}else{
					aTweets.addAll(Tweet.fromJSONArray(json));
				}
			}
			@Override
			public void onFailure(Throwable e, String s){
				super.onFailure(e, s);
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}, maxId, 20);
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);			
		return true;
	}

	public void onCompose(MenuItem mi){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		// FirstActivity, launching an activity for a result

		/****to do ****send authUser to populate name/screenName and profile image on 
		composeTweetActivity layout */ 
		Intent i = new Intent(getBaseContext(), ComposeTweetActivity.class);
		i.putExtra("name", authUser.getName());
		i.putExtra("screenName", authUser.getScreenName());
		i.putExtra("url", authUser.getProfileImageUrl());
		startActivityForResult(i, REQUEST_CODE);			
	}

	@Override
	protected void onActivityResult(int resultCode, int requestCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String newStatus = data.getExtras().getString("status");
			Toast.makeText(getBaseContext(),newStatus,Toast.LENGTH_SHORT).show();
			populateTimeline(newStatus, false);		
			
		}
	}
}