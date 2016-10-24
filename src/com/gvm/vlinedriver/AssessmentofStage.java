package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class AssessmentofStage extends Activity {
	
	private AssessmentofStage_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> assessment;
	private String stageid;
	public boolean[] chbinprodess;
	public boolean[] chbcomplete;
	public boolean[] chbnotcomplete;
	
	private GridView gridview;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmentofstage);
		
		TextView txtnotcompelete=(TextView) findViewById(R.id.txtnotcomplete);
		
		stageid = getIntent().getStringExtra("stageid");

		paperList();
		
		madapter=new AssessmentofStage_adapter(this, id, assessment,chbinprodess,chbcomplete,chbnotcomplete,stageid);
		gridview=(GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(madapter);
		
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		TestAdapter mdbhelper=new TestAdapter(this);
		mdbhelper.open();
		Cursor cassessor=mdbhelper.assessoruserid();
		if(cassessor.getCount()>0)
		{
			menu.getItem(1).getSubMenu().getItem(3).setVisible(true);
		}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		new MyMenuevent(item.toString(),this);
		return false;
	}
	
	public void paperList()
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		id=new ArrayList<String>();
		assessment=new ArrayList<String>();
		
		Cursor c=mdbHelper.getallsessment(stageid);
		int arraysize=c.getCount();	
		chbinprodess=new boolean[arraysize];
		chbcomplete=new boolean[arraysize];
		chbnotcomplete=new boolean[arraysize];
		int rownumber=0;
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				id.add(c.getString(c.getColumnIndex("Id")));
				assessment.add(c.getString(c.getColumnIndex("name")));
				if(c.getString(c.getColumnIndex("result"))!=null && c.getString(c.getColumnIndex("result")).equalsIgnoreCase("true") && c.getString(c.getColumnIndex("assessorsig"))!=null && c.getString(c.getColumnIndex("assessorsig")).equalsIgnoreCase("true") && c.getString(c.getColumnIndex("traineesig"))!=null && c.getString(c.getColumnIndex("traineesig")).equalsIgnoreCase("true"))
				{	
					chbcomplete[rownumber]=true;
				}
				else
				{
					chbinprodess[rownumber]=true;
				}
				if(c.getString(c.getColumnIndex("result"))!=null && c.getString(c.getColumnIndex("result")).equalsIgnoreCase("false"))
				{	
					chbnotcomplete[rownumber]=true;
				}
				rownumber=rownumber+1;
			}
			while(c.moveToNext());
		}
	}
	

}
