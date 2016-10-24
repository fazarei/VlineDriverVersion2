package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import com.gvm.vlinedriver.Timeout;

public class ChecklistofStage extends Activity {
	
	private ChecklistofStage_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> checknum;
	private ArrayList<String> checklist;
	private String stageid;
	public boolean[] chbinprodess;
	public boolean[] chbcomplete;
	public boolean[] chbrpl;
	
	private GridView gridview;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklistofstage);
		
		stageid=getIntent().getStringExtra("stageid");
		paperList();
		
		madapter=new ChecklistofStage_adapter(this, id, checknum, checklist,chbinprodess,chbcomplete,chbrpl);
		gridview=(GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(madapter);
		
	}

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
	
	public void paperList()
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		id=new ArrayList<String>();
		checknum=new ArrayList<String>();
		checklist=new ArrayList<String>();
		//Select all checklist of stage
		Cursor c=mdbHelper.getallchecklistindex(stageid);
		
		int arraysize=c.getCount();	
		chbinprodess=new boolean[arraysize];
		chbcomplete=new boolean[arraysize];
		chbrpl=new boolean[arraysize];
		String rpl="";
		int rownumber=0;
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				id.add(c.getString(c.getColumnIndex("Id")));
				checknum.add(c.getString(c.getColumnIndex("checknum")));
				checklist.add(c.getString(c.getColumnIndex("name")));
				rpl=c.getString(c.getColumnIndex("rpl"));
				if(rpl!=null)
				{
					if(rpl.equalsIgnoreCase("true"))
					{
						chbrpl[rownumber]=true;
					}
				}
				//I don't need to check here checklist is complete from unguide because I checked it when trainer want to sign complete on the checklist task. So, here I just check the trainer is signed or no 
					if(c.getString(c.getColumnIndex("tasktrainersig"))!=null && c.getString(c.getColumnIndex("tasktraineesig"))!=null && c.getString(c.getColumnIndex("tasktrainersig")).equalsIgnoreCase("true") && c.getString(c.getColumnIndex("tasktraineesig")).equalsIgnoreCase("true"))
					{
						chbcomplete[rownumber]=true;
					}
					else
					{
						Cursor checklistinprocess=mdbHelper.getchecklistinprocess(c.getString(c.getColumnIndex("checknum")));
						if(checklistinprocess.getCount()>0)
						{
							chbinprodess[rownumber]=true;
						}
						else
						{
							chbinprodess[rownumber]=false;
						}
					}
			rownumber=rownumber+1;
			}
			while(c.moveToNext());
		}
	}
}
