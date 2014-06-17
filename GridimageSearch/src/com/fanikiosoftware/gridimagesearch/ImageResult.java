package com.fanikiosoftware.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImageResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*describe which fields will be here and 
	  how to parse the JSON and turn into this object
	 */
	private String fullUrl; //url
	private String thumbUrl; //tburl
	
	public ImageResult(JSONObject json){
		try{
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");			
		}
		catch (JSONException e){
			this.fullUrl = null;
			this.thumbUrl = null;			
		}
	}

	public String getFullURl(){
		return fullUrl;		
	}

	public String getThumbUrl(){
		return thumbUrl;		
	}

	public String toString(){
		return this.thumbUrl;		
	}

	//process array results and get what is needed
	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray array){
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int x=0; x < array.length(); x++){
			try{
				results.add(new ImageResult(array.getJSONObject(x)));				
			} catch (JSONException e) {
				e.printStackTrace();					
			}
		}			
		return results;
	}
}