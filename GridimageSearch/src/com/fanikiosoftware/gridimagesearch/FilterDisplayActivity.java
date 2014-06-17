package com.fanikiosoftware.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FilterDisplayActivity extends Activity{
	private Spinner colorFilter, typeFilter, safetyFilter;	
	private Button btnSave;
	String color, type, safety;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_display);		
		color = getIntent().getStringExtra("color");
		type = getIntent().getStringExtra("type");
		safety = getIntent().getStringExtra("safety");
		btnSave = (Button) findViewById(R.id.btnSave);		
		colorFilter = (Spinner) findViewById(R.id.spColorFilter);
		ArrayAdapter<CharSequence> colorAdapter =
				ArrayAdapter.createFromResource(this,R.array.color_array, 
						android.R.layout.simple_spinner_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		colorFilter.setAdapter(colorAdapter);
		typeFilter = (Spinner) findViewById(R.id.spTypeFilter);		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
				R.array.type_array, android.R.layout.simple_spinner_item);		
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		typeFilter.setAdapter(typeAdapter);		
		safetyFilter = (Spinner) findViewById(R.id.spSafetyFilter);
		ArrayAdapter<CharSequence> safetyAdapter =
				ArrayAdapter.createFromResource(this,R.array.safety_array, 
						android.R.layout.simple_spinner_item);

		safetyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		safetyFilter.setAdapter(safetyAdapter);		
		colorFilter.setSelection(getIndex(colorFilter,color));
		typeFilter.setSelection(getIndex(typeFilter,type));
		safetyFilter.setSelection(getIndex(safetyFilter,safety));
		getSaveButton();
	}
	 private int getIndex(Spinner spinner, String myString){		 
		  int index = 0;		 
		  for (int i=0;i<spinner.getCount();i++){
		   if (spinner.getItemAtPosition(i).equals(myString)){
		    index = i;
		   }
		  }
		  return index;
	}	

	public void getSaveButton(){
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				sendFilters();				
			}			
		});		
	}

	public void sendFilters(){
		color = colorFilter.getSelectedItem().toString(); 		
		type = typeFilter.getSelectedItem().toString();
		safety = safetyFilter.getSelectedItem().toString();
		Intent data = new Intent(FilterDisplayActivity.this,SearchActivity.class);
		checkValues();
		data.putExtra("type", type);
		data.putExtra("color", color);
		data.putExtra("safety", safety);
		setResult(RESULT_OK, data);		
		finish();		
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {       
		//do nothing
	}

	public void onNothingSelected(AdapterView<?> parent) {
		//do nothing		
	}    

	public void checkValues(){		
		if (color.length() ==0){
			color = "null";			
		}else {
			color = colorFilter.getSelectedItem().toString(); 	
		}
		if (type.length() ==0){
			type = "null";			
		}else {
			type = typeFilter.getSelectedItem().toString(); 	
		}
		if (safety.length() ==0){
			safety = "null";			
		}else {
			safety = safetyFilter.getSelectedItem().toString(); 	
		}
		return;
	}	
}