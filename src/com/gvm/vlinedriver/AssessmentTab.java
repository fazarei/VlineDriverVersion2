package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class AssessmentTab extends TabActivity{
	private String assessmentid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmenttab);
		
		assessmentid=getIntent().getStringExtra("assessmentid");

		//Time lost Tab visibility
		String timelostvisibility="false";
		TestAdapter MDBHelper=new TestAdapter(this);
		MDBHelper.open();
		Cursor c=MDBHelper.assessmenttimelostvisible(assessmentid.toString());
		if(c.moveToFirst())
		{
			do
			{
				if( c.getString(c.getColumnIndex("timevisible"))!=null && c.getString(c.getColumnIndex("timevisible")).equalsIgnoreCase("TRUE"))
					timelostvisibility=c.getString(c.getColumnIndex("timevisible"));
			}
			while(c.moveToNext());
		}
		
		@SuppressWarnings("deprecation")
		TabHost tabHost=getTabHost();
		
		TabSpec act_assessment=tabHost.newTabSpec("Assessment");
		act_assessment.setIndicator("Assessment");
		Intent assessmentsubject=new Intent(this,AssessmentSubject.class);
		assessmentsubject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		assessmentsubject.putExtra("assessmentid", assessmentid);
		act_assessment.setContent(assessmentsubject);		
		
		TabSpec act_assessmentdetail=tabHost.newTabSpec("Assessment Detail");
		act_assessmentdetail.setIndicator("Assessment Detail");
		Intent assessmentdetail=new Intent(this,AssessmentDetail.class);
		assessmentdetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		assessmentdetail.putExtra("assessmentid", assessmentid);
		act_assessmentdetail.setContent(assessmentdetail);
		
		TabSpec act_assessmenttime=tabHost.newTabSpec("Time Lost");
		act_assessmenttime.setIndicator("Time Lost");
		Intent assessmenttime=new Intent(this,AssessmentTimeLost.class);
		assessmenttime.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		assessmenttime.putExtra("assessmentid", assessmentid);
		act_assessmenttime.setContent(assessmenttime);
		
		TabSpec act_assessmentcomment=tabHost.newTabSpec("GENERAL COMMENT");
		act_assessmentcomment.setIndicator("GENERAL COMMENT");
		Intent assessmentcomment=new Intent(this,AssessmentComment.class);
		assessmentcomment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		assessmentcomment.putExtra("assessmentid", assessmentid);
		act_assessmentcomment.setContent(assessmentcomment);
		
		tabHost.addTab(act_assessment);
		tabHost.addTab(act_assessmentdetail);
		
		//Some of the assessments don't have time lost part
		if(timelostvisibility.equalsIgnoreCase("TRUE"))
			{	tabHost.addTab(act_assessmenttime);	}
		
		tabHost.addTab(act_assessmentcomment);
		
		//Assign color to Tab
		/*for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	    {
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000")); //unselected
	    }
		
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.sel_button); // selected
		TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setTextSize(16);*/
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
	public void onBackPressed() {
		
	}

}
