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

public class ChecklistTab extends TabActivity {
	private String subjectid;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklisttab);
		
		subjectid=getIntent().getStringExtra("subjectid");
		//subjectid="2";
		TabHost tabHost=getTabHost();
		
		TabSpec act_checklist=tabHost.newTabSpec("Checklist");
		act_checklist.setIndicator("Checklist");		
		Intent subjectchecklist=new Intent(this,SubjectChecklist.class);
		subjectchecklist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		subjectchecklist.putExtra("subjectid", subjectid);
		act_checklist.setContent(subjectchecklist);
		
		TabSpec act_checklistcomment=tabHost.newTabSpec("COMMENTS AND RECOMMENDATIONS");
		act_checklistcomment.setIndicator("COMMENTS AND RECOMMENDATIONS");		
		Intent checklistcomment=new Intent(this,ChecklistComment.class);
		checklistcomment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		checklistcomment.putExtra("subjectid", subjectid);
		act_checklistcomment.setContent(checklistcomment);
		
		tabHost.addTab(act_checklist);
		tabHost.addTab(act_checklistcomment);
		
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
