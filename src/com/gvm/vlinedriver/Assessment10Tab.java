package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Assessment10Tab extends TabActivity {
	private String assessmentid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessment10_tab);
		
		assessmentid=getIntent().getStringExtra("assessmentid");
		TabHost tabHost=getTabHost();
		TabSpec act_competency=tabHost.newTabSpec("Overall Competency");
		act_competency.setIndicator("Overall Competency");
		Intent competency=new Intent(this,Competency.class);
		competency.putExtra("assessmentid", assessmentid);
		act_competency.setContent(competency);
		
		TabSpec act_assessment10comment=tabHost.newTabSpec("General Comment");
		act_assessment10comment.setIndicator("General Comment");
		Intent comment=new Intent(this,Assessment10comment.class);
		comment.putExtra("assessmentid", assessmentid);
		act_assessment10comment.setContent(comment);
		
		tabHost.addTab(act_competency);
		tabHost.addTab(act_assessment10comment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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

}
