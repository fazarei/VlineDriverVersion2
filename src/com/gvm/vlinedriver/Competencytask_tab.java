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

public class Competencytask_tab extends TabActivity {
	private String competencyid;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessment10_tab);
		
		competencyid=getIntent().getStringExtra("competencyid");
		TabHost tabHost=getTabHost();
		TabSpec act_competencytask=tabHost.newTabSpec("Task");
		act_competencytask.setIndicator("Task");
		Intent competencytask=new Intent(this,Competencytask.class);
		competencytask.putExtra("competencyid", competencyid);
		act_competencytask.setContent(competencytask);
		
		TabSpec act_competencyinstructed=tabHost.newTabSpec("Assessment Panel");
		act_competencyinstructed.setIndicator("Assessment Panel");
		Intent competencyinstructed=new Intent(this,CompetencyInstructed.class);
		competencyinstructed.putExtra("competencyid", competencyid);
		act_competencyinstructed.setContent(competencyinstructed);
		
		TabSpec act_competencycomment=tabHost.newTabSpec("General Comment");
		act_competencycomment.setIndicator("General Comment");
		Intent competencycomment=new Intent(this,Competencycomment.class);
		competencycomment.putExtra("competencyid", competencyid);
		act_competencycomment.setContent(competencycomment);
		
		tabHost.addTab(act_competencytask);
		tabHost.addTab(act_competencyinstructed);
		tabHost.addTab(act_competencycomment);
		
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
