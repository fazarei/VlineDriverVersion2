package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.GridView;

public class SubjectofAssessment extends Activity {
	private SubjectofAssessment_adapter mAdapter;
	private ArrayList<String> id;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_subjectofassessment);
    	
		paperList();
				
		mAdapter = new SubjectofAssessment_adapter(this,id);
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_subjectofassessment, menu);
		return true;
	}
	
	public void paperList()
	{
		id = new ArrayList<String>();
		
		//SQLiteDatabase db = openOrCreateDatabase("MyDB",MODE_PRIVATE,null);
		/*db.execSQL("CREATE TABLE IF NOT EXISTS MyTable (LastName VARCHAR,FirstName VARCHAR,Age INT(3));");
		db.execSQL("INSERT INTO MyTable VALUES('Zarei','Elham',26);");
		Cursor c = db.rawQuery("SELECT * FROM MyTable", null);
		c.moveToFirst();
		Log.d("LEE", c.getString(c.getColumnIndex("FirstName")));*/
		
		/*Cursor c = db.rawQuery("SELECT * FROM subject where field2='9'", null);
		
		if (c.moveToFirst()) {
            do {
            
            	id.add(c.getString(c.getColumnIndex("field1")));
            	
            } while (c.moveToNext());
        }
		db.close();*/
		id.add("id 1");
		id.add("id 2");
		id.add("id 3");
		id.add("id 4");
		id.add("id 5");
	}
	
	

}
