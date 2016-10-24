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
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class SubjectChecklist extends Activity {
	
	private SubjectChecklist_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> checknum;
	private ArrayList<String> name;
	private ArrayList<String> critical;
	private String subjectid;
	boolean itemChecked[] = new boolean[100];
	public boolean[] chbsdemonstrated;
	public boolean[] chbsnyc;
	public boolean[] chbscomp;
	public boolean[] chbrpl;
	public boolean[] chbperform;
	public String[] keepetexam;
	private String trainee="";
	private String assessor="";
	private String trainer="";
	
	private GridView gridview;
	private String editableachive="true";
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_subjectchecklist);
		
		
		Button bsave=(Button) findViewById(R.id.button1);
		final TestAdapter MDBHelper = new TestAdapter(SubjectChecklist.this);
		MDBHelper.open();
		
		subjectid=getIntent().getStringExtra("subjectid");
	
		//get trainee username
				Cursor ctraineeusername=MDBHelper.traineexist();
				
				if(ctraineeusername.getCount()>0)
				{
					ctraineeusername.moveToFirst();
					trainee=ctraineeusername.getString(ctraineeusername.getColumnIndex("username"));
				}
				//Get assessor username
				Cursor cassessorusername=MDBHelper.assessoruserid();
				if(cassessorusername.getCount()>0)
				{
					cassessorusername.moveToFirst();
					assessor=cassessorusername.getString(cassessorusername.getColumnIndex("username"));
				}
				//Get trainer username
				Cursor ctrainerusername=MDBHelper.traineruserid();
				if(ctrainerusername.getCount()>0)
				{
					ctrainerusername.moveToFirst();
					trainer=ctrainerusername.getString(ctrainerusername.getColumnIndex("username"));
				}
				//Get role of user
				Cursor crole=MDBHelper.userexist();
				if(crole.getCount()>0)
				{
					crole.moveToFirst();
					if(!crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
					{
						bsave.setVisibility(View.GONE);
						editableachive="false";
					}
				}
		//Fill data in top of the page
		TextView tvsubjectname=(TextView) findViewById(R.id.textView1);
		Cursor csubject=MDBHelper.getsubject(subjectid);
		if(csubject.getCount()>0)
		{
			csubject.moveToFirst();
			tvsubjectname.setText(csubject.getString(csubject.getColumnIndex("name")));
		}
		
		//Fill gridview
			paperList();
			madapter=new SubjectChecklist_adapter(this, id, checknum, name, critical, chbscomp,chbsdemonstrated,chbsnyc,keepetexam,editableachive,trainee,assessor,trainer,chbrpl,chbperform);
			//madapter=new SubjectChecklist_adapter(this);
			gridview=(GridView) findViewById(R.id.gridView1);
			gridview.setAdapter(madapter);
			
			//Save data
			/*bsave.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					int rowcount=gridview.getChildCount();
					for(int i=0; i<rowcount; i++)
					{
						TextView tvchecknum=(TextView) gridview.getChildAt(i).findViewById(R.id.textView1);
						String checknumchild=tvchecknum.getText().toString();
						
					}
				}
			});*/
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_subjectchecklist, menu);
		return true;
	}

	public void paperList()
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		id=new ArrayList<String>();
		checknum=new ArrayList<String>();
		name=new ArrayList<String>();
		critical=new ArrayList<String>();
		
		Cursor c=mdbHelper.getallsubjectchecklist(subjectid);
		int arraysize=c.getCount();		
		chbsdemonstrated=new boolean[arraysize];
		chbsnyc=new boolean[arraysize];
		chbscomp=new boolean[arraysize];
		chbrpl=new boolean[arraysize];
		chbperform=new boolean[arraysize];
		keepetexam=new String[arraysize];
		int rownumber=0;
		String rpl="";
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				id.add(c.getString(c.getColumnIndex("subjectchecklistid")));
				checknum.add(c.getString(c.getColumnIndex("checknum")));
				name.add(c.getString(c.getColumnIndex("name")));
				
				//This part should come from subjectchecklist table because same checklist has different answer in different assessment  
				if(c.getString(c.getColumnIndex("sbjectcritical"))!=null)
				{
					critical.add(c.getString(c.getColumnIndex("sbjectcritical")));
				}
				else
				{
					critical.add("");
				}
				
				if(c.getString(c.getColumnIndex("subjectexplained"))!=null)
				{
					keepetexam[rownumber]=c.getString(c.getColumnIndex("subjectexplained"));
				}
				else
				{
					keepetexam[rownumber]="";
				}
				
				
				if(c.getString(c.getColumnIndex("subjectdemonstration"))!=null && c.getString(c.getColumnIndex("subjectdemonstration")).equalsIgnoreCase("true"))
					{	chbsdemonstrated[rownumber]=true;	}
				
				if(c.getString(c.getColumnIndex("subjectnyc"))!=null && c.getString(c.getColumnIndex("subjectnyc")).equalsIgnoreCase("true"))
					{	chbsnyc[rownumber]=true;	}
				//
				
				//If RPL is checked, All item in checklist row is locked. If checklist is completed, RPL is locked
				rpl=c.getString(c.getColumnIndex("rpl"));
				if(rpl!=null)
				{
					if(rpl.equalsIgnoreCase("true"))
					{	chbrpl[rownumber]=true;	}
				}
				
					if(c.getString(c.getColumnIndex("tasktrainersig"))!=null && c.getString(c.getColumnIndex("tasktrainersig")).equalsIgnoreCase("true") && c.getString(c.getColumnIndex("tasktraineesig"))!=null && c.getString(c.getColumnIndex("tasktraineesig")).equalsIgnoreCase("true"))
					{
						chbscomp[rownumber]=true;
					}
					
					if(c.getString(c.getColumnIndex("ungperform"))==null || c.getString(c.getColumnIndex("ungperform")).equalsIgnoreCase("Yes"))
					{
						chbperform[rownumber]=true;
					}
					
					rownumber=rownumber+1;
			}
			while(c.moveToNext());
		}

	}

}
