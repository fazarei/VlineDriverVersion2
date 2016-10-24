package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ChecklistTask extends Activity {
	
	private ChecklistTask_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> checknumdb;
	private ArrayList<String> groupname;
	private ArrayList<String> name;
	private ArrayList<String> taskgroupvisible;
	private String checknum;
	public boolean[] chbstateinstructed;
	public boolean[] chbstategna;
	public boolean[] chbstategd;
	public boolean[] chbstatege;
	public boolean[] chbstateunna;
	public boolean[] chbstateund;
	public boolean[] chbstateune;
	private String editableachive="true";
	private String trainee="";
	private String assessor="";
	private String trainer="";
	private String trainerguide="";
	private String trainerunguide="";
	private String trainerinstruction="";
	private String taskguide="true";
	private String role="";
	
	private String inssig="false";
	private String guidesig="false";
	private String unguidesig="false";
	
	String assessoraccesstosig="no";
	String traineraccesstosig="no";
	
	private GridView gridview;
	final Calendar myCalendar=Calendar.getInstance();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklisttask);
		
		final TestAdapter mDbHelper = new TestAdapter(this); 
		mDbHelper.open();
		
		final TextView tvdchecklistnama=(TextView) findViewById(R.id.textView17);
		final EditText edtraineedate=(EditText) findViewById(R.id.editText1);
		final EditText edtrainerdate=(EditText) findViewById(R.id.editText2);
		final CheckBox chbtraineesig=(CheckBox) findViewById(R.id.checkBox1);
		final CheckBox chbtrainersig=(CheckBox) findViewById(R.id.checkBox2);
		final TextView tvungtrainer=(TextView) findViewById(R.id.textView13);
		Button bsave=(Button) findViewById(R.id.button1);

		final ImageView ivtraineedate=(ImageView) findViewById(R.id.imageView1);
		final ImageView ivtrainerdate=(ImageView) findViewById(R.id.imageView2);
		
		final CheckBox chballinstructed=(CheckBox) findViewById(R.id.chballinstructed);
		final CheckBox chballgna=(CheckBox) findViewById(R.id.chballgna);
		final CheckBox chballgd=(CheckBox) findViewById(R.id.chballgd);
		final CheckBox chballge=(CheckBox) findViewById(R.id.chballge);
		final CheckBox chballungna=(CheckBox) findViewById(R.id.chballungna);
		final CheckBox chballungd=(CheckBox) findViewById(R.id.chballungd);
		final CheckBox chballunge=(CheckBox) findViewById(R.id.chballunge);
		
		checknum=getIntent().getStringExtra("checknum");

		//get trainee username
		Cursor ctraineeusername=mDbHelper.traineexist();
		
		if(ctraineeusername.getCount()>0)
		{
			ctraineeusername.moveToFirst();
			trainee=ctraineeusername.getString(ctraineeusername.getColumnIndex("username"));
		}
		//Get assessor username
		Cursor cassessorusername=mDbHelper.assessoruserid();
		if(cassessorusername.getCount()>0)
		{
			cassessorusername.moveToFirst();
			assessor=cassessorusername.getString(cassessorusername.getColumnIndex("username"));
		}
		//Get trainer username who is login 
		Cursor ctrainerusername=mDbHelper.traineruserid();
		if(ctrainerusername.getCount()>0)
		{
			ctrainerusername.moveToFirst();
			trainer=ctrainerusername.getString(ctrainerusername.getColumnIndex("username"));
		}
		
		//Load data for name of the checklist
		Cursor cgetcheckname=mDbHelper.checklistinfo(checknum);
		if(cgetcheckname.getCount()>0)
		{
			cgetcheckname.moveToFirst();
			tvdchecklistnama.setText(checknum+" _ "+cgetcheckname.getString(cgetcheckname.getColumnIndex("name")));
		}
		
		//Load date for top of the page
				Cursor cloaddata =mDbHelper.getchecklisttrainer(checknum);				
				
				if(cloaddata.getCount()>0)
					{
						cloaddata.moveToFirst();
						trainerguide=cloaddata.getString(cloaddata.getColumnIndex("trainerguide"));
						trainerunguide=cloaddata.getString(cloaddata.getColumnIndex("trainerunguide"));
						trainerinstruction=cloaddata.getString(cloaddata.getColumnIndex("trainerinstruction"));	
						if(cloaddata.getString(cloaddata.getColumnIndex("unguidetrainerfullname"))!=null)
						{
						tvungtrainer.setText("Trainer Name: "+cloaddata.getString(cloaddata.getColumnIndex("unguidetrainerfullname")));
						}
						edtraineedate.setText(cloaddata.getString(cloaddata.getColumnIndex("tasktraineedate")));
						//We fill these data and  unguide trainer has access to them
						edtrainerdate.setText(cloaddata.getString(cloaddata.getColumnIndex("tasktrainerdate")));
						
						if(cloaddata.getString(cloaddata.getColumnIndex("tasktraineesig"))!=null && cloaddata.getString(cloaddata.getColumnIndex("tasktraineesig")).equalsIgnoreCase("true"))
						{
							chbtraineesig.setChecked(true);
						}
						if(cloaddata.getString(cloaddata.getColumnIndex("tasktrainersig"))!=null && cloaddata.getString(cloaddata.getColumnIndex("tasktrainersig")).equalsIgnoreCase("true"))
						{
							chbtrainersig.setChecked(true);
						}
						
						if(cloaddata.getString(cloaddata.getColumnIndex("instrainersig"))!=null)
						{
							inssig=cloaddata.getString(cloaddata.getColumnIndex("instrainersig"));
						}
						
						if(cloaddata.getString(cloaddata.getColumnIndex("gtrainersig"))!=null)
						{
							guidesig=cloaddata.getString(cloaddata.getColumnIndex("gtrainersig"));
						}
						
						if(cloaddata.getString(cloaddata.getColumnIndex("ungtrainersig"))!=null)
						{
							unguidesig=cloaddata.getString(cloaddata.getColumnIndex("ungtrainersig"));
						}
					}
		//Get role of user and permission
		Cursor crole=mDbHelper.userexist();
		if(crole.getCount()>0)
		{
			crole.moveToFirst();
			role=crole.getString(crole.getColumnIndex("role"));
			
			Cursor cchecklistsig=mDbHelper.checklistinfo(checknum);
			cchecklistsig.moveToFirst();
			
			//fist to select number of checklist task. 
			//second to select number of checklist task completed if their number are equal means checklist is completed and of course number of checklist task should not be zero
			Cursor calltask=mDbHelper.getallchecklisttask(checknum);
			Cursor ccomptask=mDbHelper.getchecklisttaskcmp(checknum);
			
			Cursor ccomptaskinstructed=mDbHelper.getchecklisttaskinstcmp(checknum);
			Cursor ccomptaskguide=mDbHelper.getchecklisttaskcmpguide(checknum);
			
			int alltask=calltask.getCount();
			int comptask=ccomptask.getCount();	
			int comptaskinstructed=ccomptaskinstructed.getCount();	
			int comptaskguide=ccomptaskguide.getCount();	
			
			//
			//Assessor Role
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				assessoraccesstosig="yes";
				bsave.setVisibility(View.VISIBLE);
				
				if(alltask==comptask)
				{
					chbtrainersig.setEnabled(true);
					ivtrainerdate.setVisibility(View.VISIBLE);
				}
				//If instruction and unguied part was signed then trainer or assessor can signed				
					if(!(cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig")).equalsIgnoreCase("true") && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig")).equalsIgnoreCase("true")))
					{
						chbtrainersig.setEnabled(false);
					}
				
				//Make active select all instructed
						if(cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig")).equalsIgnoreCase("true") )
						{
							chballinstructed.setEnabled(true);
						}
						//If all instructed have selected and guide part signed
						if(cchecklistsig.getString(cchecklistsig.getColumnIndex("gtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("gtrainersig")).equalsIgnoreCase("true") )
						{
							if(alltask==comptaskinstructed)
							{
								chballgna.setEnabled(true);
								chballgd.setEnabled(true);
								chballge.setEnabled(true);
							}
						}
						//If all guide part have selected and unguided part signed
						if(cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig")).equalsIgnoreCase("true") )
						{
							if(alltask==comptaskguide)
							{
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
						}			
			}
			
			//Trainer Role
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainer"))
			{	
				if((trainerunguide==null) ||(trainerunguide.isEmpty()) || (crole.getString(crole.getColumnIndex("username")).equalsIgnoreCase(trainerunguide)))
				{
					traineraccesstosig="yes";
					if(alltask==comptask)
					{
						chbtrainersig.setEnabled(true);
						ivtrainerdate.setVisibility(View.VISIBLE);	
					}
					
					if(!(cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig")).equalsIgnoreCase("true") && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig")).equalsIgnoreCase("true")))
					{
						chbtrainersig.setEnabled(false);
						ivtrainerdate.setVisibility(View.INVISIBLE);	
					}
				}
				
				//Make active select all instructed
					if(cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("instrainersig")).equalsIgnoreCase("true") && trainerinstruction.equalsIgnoreCase(trainer))
					{
						chballinstructed.setEnabled(true);
					}
					//If all instructed have selected and guide part signed
					if(cchecklistsig.getString(cchecklistsig.getColumnIndex("gtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("gtrainersig")).equalsIgnoreCase("true") && trainerguide.equalsIgnoreCase(trainer))
					{
						if(alltask==comptaskinstructed)
						{
							chballgna.setEnabled(true);
							chballgd.setEnabled(true);
							chballge.setEnabled(true);
						}
					}
					
					//If all guide part have selected and unguided part signed
					if(cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig"))!=null && cchecklistsig.getString(cchecklistsig.getColumnIndex("ungtrainersig")).equalsIgnoreCase("true") && trainerunguide.equalsIgnoreCase(trainer))
					{
						if(alltask==comptaskguide)
						{
							chballungna.setEnabled(true);
							chballungd.setEnabled(true);
							chballunge.setEnabled(true);
						}
					}
				//
				bsave.setVisibility(View.VISIBLE);						
			}
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
			{
				if(chbtrainersig.isChecked())
				{
					chbtraineesig.setEnabled(true);
					ivtraineedate.setVisibility(View.VISIBLE);
					bsave.setVisibility(View.VISIBLE);
				}
			}
			
		}
		//Fill gridview
		gridviewbind();
		
		//Save data in top of the page
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String traineesig="false";
				String trainersig="false";
				if(chbtraineesig.isChecked())
					{	traineesig="true";	}
				if(chbtrainersig.isChecked())
					{	
						trainersig="true";	
					}
				//If nobody worked on the unguide before we save current trainer as a unguide trainer
				if(trainerunguide==null)
				{
					mDbHelper.checklistapprove(ChecklistTask.this,traineesig, edtraineedate.getText().toString(), trainersig, edtrainerdate.getText().toString(), checknum,trainee,trainer,assessor);
				}
				else
				{
					mDbHelper.checklistapprove(ChecklistTask.this,traineesig, edtraineedate.getText().toString(), trainersig, edtrainerdate.getText().toString(), checknum,trainee,trainerunguide,assessor);
				}
				
				Cursor calltask2=mDbHelper.getallchecklisttask(checknum);
				Cursor ccomptask2=mDbHelper.getchecklisttaskcmp(checknum);
				int alltask2=calltask2.getCount();
				int comptask2=ccomptask2.getCount();			

					if(alltask2==comptask2)
					{
						if(assessoraccesstosig.equalsIgnoreCase("yes") || traineraccesstosig.equalsIgnoreCase("yes"))
						{
							chbtrainersig.setEnabled(true);
							ivtrainerdate.setVisibility(View.VISIBLE);
						}
					}
					else
					{
						chbtrainersig.setEnabled(false);
						ivtrainerdate.setVisibility(View.INVISIBLE);
					}
			}
		});
		
		//Select All Instructed
		chballinstructed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(chballinstructed.isChecked())
				{
					mDbHelper.checklisttaskallselect("instructed","true", checknum, trainee, assessor);
					gridviewbind();
					
					//Enable guide part
					
					if(role.equalsIgnoreCase("trainer") && trainerguide.equalsIgnoreCase(trainer) && guidesig.equalsIgnoreCase("true"))
					{
						chballgna.setEnabled(true);
						chballgd.setEnabled(true);
						chballge.setEnabled(true);
					}
					if(role.equalsIgnoreCase("assessor") && guidesig.equalsIgnoreCase("true"))
					{
						chballgna.setEnabled(true);
						chballgd.setEnabled(true);
						chballge.setEnabled(true);
					}
				}
				else
				{
					mDbHelper.checklisttaskallselect("instructed","false", checknum, trainee, assessor);
					gridviewbind();
					
					//Disable guide part
					chballgna.setEnabled(false);
					chballgd.setEnabled(false);
					chballge.setEnabled(false);
					chballungna.setEnabled(false);
					chballungd.setEnabled(false);
					chballunge.setEnabled(false);
				}
			}
		});
		//
		
		//Select All guide N/A
				chballgna.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballgna.isChecked())
						{
							mDbHelper.checklisttaskallselect("gna","true", checknum, trainee, assessor);
							//unselect D and E
							mDbHelper.checklisttaskallunselect("gd","ge", checknum, trainee, assessor);
							
							chballgd.setChecked(false);
							chballge.setChecked(false);
							
							gridviewbind();
							
							//Enable unguide part
							if(role.equalsIgnoreCase("trainer") &&  trainerunguide.equalsIgnoreCase(trainer) && unguidesig.equalsIgnoreCase("true"))
							{	
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
							
							if(role.equalsIgnoreCase("assessor") && unguidesig.equalsIgnoreCase("true"))
							{	
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
							
						}
						else
						{
							mDbHelper.checklisttaskallselect("gna","false", checknum, trainee, assessor);
							gridviewbind();
							
							//Disable unguide part
							chballungna.setEnabled(false);
							chballungd.setEnabled(false);
							chballunge.setEnabled(false);
						}
					}
				});
		//
		//Select All guide D
				chballgd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballgd.isChecked())
						{
							mDbHelper.checklisttaskallselect("gd","true", checknum, trainee, assessor);
							//unselect N/A and E
							mDbHelper.checklisttaskallunselect("gna","ge", checknum, trainee, assessor);
							
							chballgna.setChecked(false);
							chballge.setChecked(false);
							
							gridviewbind();
							
							//Enable unguide part
							if(role.equalsIgnoreCase("trainer") && trainerunguide.equalsIgnoreCase(trainer) && unguidesig.equalsIgnoreCase("true"))
							{
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
							
							if(role.equalsIgnoreCase("assessor") && unguidesig.equalsIgnoreCase("true"))
							{
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
						}
						else
						{
							mDbHelper.checklisttaskallselect("gd","false", checknum, trainee, assessor);
							gridviewbind();
							
							//Disable unguide part
							chballungna.setEnabled(false);
							chballungd.setEnabled(false);
							chballunge.setEnabled(false);
						}
					}
				});
		//
		//Select All guide E
				chballge.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballge.isChecked())
						{
							mDbHelper.checklisttaskallselect("ge","true", checknum, trainee, assessor);
							//unselect D and N/A
							mDbHelper.checklisttaskallunselect("gna","gd", checknum, trainee, assessor);
							
							chballgna.setChecked(false);
							chballgd.setChecked(false);
							
							gridviewbind();
							
							//Enable unguide part
							if(role.equalsIgnoreCase("trainer") && trainerunguide.equalsIgnoreCase(trainer) && unguidesig.equalsIgnoreCase("true"))
							{
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
							
							if(role.equalsIgnoreCase("assessor") && unguidesig.equalsIgnoreCase("true"))
							{
								chballungna.setEnabled(true);
								chballungd.setEnabled(true);
								chballunge.setEnabled(true);
							}
						}
						else
						{
							mDbHelper.checklisttaskallselect("ge","false", checknum, trainee, assessor);
							gridviewbind();
							
							//Disable unguide part
							chballungna.setEnabled(false);
							chballungd.setEnabled(false);
							chballunge.setEnabled(false);
						}
					}
				});
		//
		//Select All unguide N/A
				chballungna.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballungna.isChecked())
						{
							mDbHelper.checklisttaskallselect("unna","true", checknum, trainee, assessor);
							//unselect D and E
							mDbHelper.checklisttaskallunselect("und","une", checknum, trainee, assessor);
							
							chballungd.setChecked(false);
							chballunge.setChecked(false);
							
							gridviewbind();
						}
						else
						{
							mDbHelper.checklisttaskallselect("unna","false", checknum, trainee, assessor);
							gridviewbind();
						}
					}
				});
		//
		//Select All unguide D
				chballungd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballungd.isChecked())
						{
							mDbHelper.checklisttaskallselect("und","true", checknum, trainee, assessor);
							//unselect N/A and E
							mDbHelper.checklisttaskallunselect("unna","une", checknum, trainee, assessor);
							
							chballungna.setChecked(false);
							chballunge.setChecked(false);
							
							gridviewbind();
						}
						else
						{
							mDbHelper.checklisttaskallselect("und","false", checknum, trainee, assessor);
							gridviewbind();
						}
					}
				});
		//
		//Select All unguide E
				chballunge.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(chballunge.isChecked())
						{
							mDbHelper.checklisttaskallselect("une","true", checknum, trainee, assessor);
							//unselect N/A and D
							mDbHelper.checklisttaskallunselect("unna","und", checknum, trainee, assessor);
							
							chballungna.setChecked(false);
							chballungd.setChecked(false);
							
							gridviewbind();
						}
						else
						{
							mDbHelper.checklisttaskallselect("une","false", checknum, trainee, assessor);
							gridviewbind();
						}
					}
				});
		//
		final DatePickerDialog.OnDateSetListener traineedate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText1);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivtraineedate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(ChecklistTask.this, traineedate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		final DatePickerDialog.OnDateSetListener trainerdate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText2);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivtrainerdate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(ChecklistTask.this, trainerdate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}
	
	private void updateLabel(EditText et) {

	    String myFormat = "dd/MM/yyyy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	    
	    et.setText(sdf.format(myCalendar.getTime()));
	    }
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_checklisttask, menu);
		
		return true;
	}
	
	public void gridviewbind()
	{
		paperList();
		madapter=new ChecklistTask_adapter(this, id, checknumdb, groupname, name, chbstateinstructed
				,chbstategna,chbstategd,chbstatege,chbstateunna,chbstateund,chbstateune,editableachive,trainee,assessor,trainer,taskguide,role,trainerguide,
				trainerunguide,trainerinstruction,taskgroupvisible,inssig,guidesig,unguidesig);
		
		gridview=(GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(madapter);
	}
	public void paperList()
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		id=new ArrayList<String>();
		checknumdb=new ArrayList<String>();
		groupname=new ArrayList<String>();
		name=new ArrayList<String>();
		taskgroupvisible=new ArrayList<String>();

		Cursor c=mdbHelper.getallchecklisttask(checknum);
		int arraysize=c.getCount();		
		chbstateinstructed=new boolean[arraysize];
		chbstategna=new boolean[arraysize];
		chbstategd=new boolean[arraysize];
		chbstatege=new boolean[arraysize];
		chbstateunna=new boolean[arraysize];
		chbstateund=new boolean[arraysize];
		chbstateune=new boolean[arraysize];
		//we need row number because we need to know which cell of array is true or false and each cell is for one row
		int rownumber=0;
		if(c.getCount()>0)
		{
			String taskgroupuniqe="";
			c.moveToFirst();
			do
			{
				id.add(c.getString(c.getColumnIndex("Id")));
				checknumdb.add(c.getString(c.getColumnIndex("checknum")));
				if(c.getString(c.getColumnIndex("taskgroup")).equalsIgnoreCase(taskgroupuniqe))
				{
					groupname.add("");
					taskgroupvisible.add("GONE");
				}
				else
				{
					groupname.add(c.getString(c.getColumnIndex("taskgroup")));
					taskgroupuniqe=c.getString(c.getColumnIndex("taskgroup"));
					taskgroupvisible.add("VISIBLE");
				}
				
				name.add(c.getString(c.getColumnIndex("name")));
				
				if(c.getString(c.getColumnIndex("instructed"))!=null && c.getString(c.getColumnIndex("instructed")).equalsIgnoreCase("true"))
					{chbstateinstructed[rownumber]=true;}
				if(c.getString(c.getColumnIndex("gna"))!=null && c.getString(c.getColumnIndex("gna")).equalsIgnoreCase("true"))
					{chbstategna[rownumber]=true;}
				if(c.getString(c.getColumnIndex("gd"))!=null && c.getString(c.getColumnIndex("gd")).equalsIgnoreCase("true"))
					{chbstategd[rownumber]=true;}
				if(c.getString(c.getColumnIndex("ge"))!=null && c.getString(c.getColumnIndex("ge")).equalsIgnoreCase("true"))
					{chbstatege[rownumber]=true;}
				if(c.getString(c.getColumnIndex("unna"))!=null && c.getString(c.getColumnIndex("unna")).equalsIgnoreCase("true"))
					{chbstateunna[rownumber]=true;}
				if(c.getString(c.getColumnIndex("und"))!=null && c.getString(c.getColumnIndex("und")).equalsIgnoreCase("true"))
					{chbstateund[rownumber]=true;}
				if(c.getString(c.getColumnIndex("une"))!=null && c.getString(c.getColumnIndex("une")).equalsIgnoreCase("true"))
					{chbstateune[rownumber]=true;}
				
				rownumber=rownumber+1;
			}
			while(c.moveToNext());
		}
	}

}
