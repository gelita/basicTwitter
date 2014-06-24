package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {
	EditText etBody;
	String name, screenName;
	String url;
	TextView tvName, tvScreenName, tvUrl; 
	ImageView ivProfileImage;
	String status;
	TwitterClient c;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_compose_tweet);
		getExtras();
		setUpViews();
		c = TwitterApplication.getRestClient();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);			
		return true;
	}
	public void onCancelTweet(MenuItem item){
		finish();		
	}

	public void onPostTweet(MenuItem item){		
		final String newStatus = etBody.getText().toString();
		c.postTweet(newStatus,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject jsonObj){
				Intent data = new Intent();
				data.putExtra("status", newStatus);				
				setResult(RESULT_OK, data);	
				finish();
			}


			@Override
			public void onFailure(Throwable e, String s){
				super.onFailure(e,s);						
			}
		});

	}

	private void getExtras(){
		name = getIntent().getStringExtra("name");
		screenName = getIntent().getStringExtra("screenName");	
		url = getIntent().getStringExtra("userProfileImageUrl");		
	}

	private void setUpViews(){
		tvName = (TextView) findViewById(R.id.tvName);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		tvName.setText(name);
		tvScreenName.setText("@" +screenName);
		etBody = (EditText) findViewById(R.id.etBody);
		ImageView profileImage = (ImageView) findViewById(R.id.ivProfileImage);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, profileImage);
	}
}
