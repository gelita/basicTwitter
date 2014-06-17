package com.fanikiosoftware.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	private final int REQUEST_CODE = 20;
	public static String type;
	public static String color;
	public static String safety;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setUpViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId){
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				//provide the photo to be displayed
				ImageResult imageResult = imageResults.get(position);
				//add data to bundle and start activity to enlarge image
				i.putExtra("result", imageResult);
				startActivity(i);				
			}
		});

		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {	            
				String query = etQuery.getText().toString();			
				//check if search is valid data
				if( query!= null && !query.isEmpty()){
					AsyncHttpClient client = new AsyncHttpClient();		
					client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&imgcolor="
							+ color + "&imgtype=" + type + "&safe=" + safety + "&start=" + page++ + "&v=1.0&q=" + Uri.encode(query),	
							new JsonHttpResponseHandler(){
								@Override
								public void onSuccess(JSONObject response){
									//go through response data and results to get data
									JSONArray imageJsonResults = null;
									try{
										imageJsonResults = response.getJSONObject("responseData")
												.getJSONArray("results");										
										//loads into grid/array and notifies adapter
										imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
										Log.d("DEBUG", imageResults.toString());					
									}catch (JSONException e){
										e.printStackTrace();								
									}
								}

								public void onFailure(Throwable arg0){
									Log.d("DEBUG", "unexpected response. failed response");				
								}

							});
				}
			}
		});
	}

	public void setUpViews(){
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);		
	}

	public void onImageSearch(View v){	
		String query = etQuery.getText().toString();			
		AsyncHttpClient client = new AsyncHttpClient();		
		client.get(
				"https://ajax.googleapis.com/ajax/services/search/images?rsz=8&imgcolor="
						+ color + "&imgtype=" + type + "&safe=" + safety + "&start=" + 0 + "&v=1.0&q=" + Uri.encode(query),	
						new JsonHttpResponseHandler(){
							@Override
							public void onSuccess(JSONObject response){
								//go through response data and results to get data
								JSONArray imageJsonResults = null;
								try{
									imageJsonResults = response.getJSONObject("responseData")
											.getJSONArray("results");
									imageAdapter.clear();
									//loads into grid/array and notifies adapter
									imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
									Log.d("DEBUG", imageResults.toString());
									
								}catch (JSONException e){
									e.printStackTrace();								
								}
							}

							public void onFailure(Throwable arg0){
								Log.d("DEBUG", "unexpected response. failed response");				
							}

						});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);		
		return super.onCreateOptionsMenu(menu);
	}

	public void onGetFilters(MenuItem mi){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		// FirstActivity, launching an activity for a result						
		Intent i = new Intent(SearchActivity.this, FilterDisplayActivity.class);			
		i.putExtra("color", color); // pass arbitrary data to launched activity
		i.putExtra("type", type);
		i.putExtra("safety", safety);
		startActivityForResult(i, REQUEST_CODE);			
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {	     
			color = data.getExtras().getString("color");
			type = data.getExtras().getString("type");
			safety = data.getExtras().getString("safety");
		}
	} 
}