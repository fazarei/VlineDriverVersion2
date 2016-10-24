package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ChecklistTaskTab extends TabActivity {
	private String checknum;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklisttasktab);
		
		checknum=getIntent().getStringExtra("checknum");
		//checknum="1.14.86.09";
		TabHost tabHost=getTabHost();
				
		TabSpec act_checklistinstruction=tabHost.newTabSpec("INSTRUCTION");
		act_checklistinstruction.setIndicator("INSTRUCTION");		
		Intent checklistinstruction1=new Intent(this,ChecklistInstructed.class);
		checklistinstruction1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		checklistinstruction1.putExtra("checknum", checknum);
		act_checklistinstruction.setContent(checklistinstruction1);
		
		TabSpec act_checklistpractice=tabHost.newTabSpec("PRACTICE");
		act_checklistpractice.setIndicator("PRACTICE");		
		Intent checklistpractice=new Intent(this,ChecklistPractice.class);
		checklistpractice.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		checklistpractice.putExtra("checknum", checknum);
		act_checklistpractice.setContent(checklistpractice);
		
		TabSpec act_checklistTask=tabHost.newTabSpec("TASK");
		act_checklistTask.setIndicator("TASK");		
		Intent checklisttask=new Intent(this,ChecklistTask.class);
		//We want to load activity after each visit.So, first we need to close activity after each visit 
		checklisttask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		checklisttask.putExtra("checknum", checknum);
		act_checklistTask.setContent(checklisttask);
		
		tabHost.addTab(act_checklistinstruction);
		tabHost.addTab(act_checklistpractice);
		tabHost.addTab(act_checklistTask);
		
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
