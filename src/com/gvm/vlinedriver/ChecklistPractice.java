package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChecklistPractice extends Activity {
	private String checknum;
	private String trainee="";
	private String assessor="";	
	private String trainer="";
	private String trainerguide="";
	private String trainerunguide="";
	final Calendar myCalendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklistpractice);
		
		
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		final TextView tvchecknum=(TextView) findViewById(R.id.textView1);
		final TextView tvgconduct=(TextView) findViewById(R.id.textView16);
		final TextView tvungconduct=(TextView) findViewById(R.id.textView27);
		
		final CheckBox chbgsig=(CheckBox) findViewById(R.id.checkBox1);
		final CheckBox chbungsig=(CheckBox) findViewById(R.id.checkBox2);
		
		final EditText etgdate=(EditText) findViewById(R.id.editText3);
		
		final EditText etungdate=(EditText) findViewById(R.id.editText12);
		
		final Spinner gspday=(Spinner) findViewById(R.id.gspday);
		final Spinner ungspday=(Spinner) findViewById(R.id.ungspday);
		final Spinner gspweather=(Spinner) findViewById(R.id.gspweather);
		final Spinner ungspweather=(Spinner) findViewById(R.id.ungspweather);
		final Spinner gspfrom=(Spinner) findViewById(R.id.gspfrom);
		final Spinner ungspfrom=(Spinner) findViewById(R.id.ungspfrom);
		final Spinner gspto=(Spinner) findViewById(R.id.gspto);
		final Spinner ungspto=(Spinner) findViewById(R.id.ungspto);
		final Spinner gspnovehicle=(Spinner) findViewById(R.id.gspnovehicle);
		final Spinner ungspnovehicle=(Spinner) findViewById(R.id.ungspnovehicle);
		final Spinner gsptrainer=(Spinner) findViewById(R.id.gsptrainer);
		final Spinner ungsptrainer=(Spinner) findViewById(R.id.ungsptrainer);
		final Spinner gspmpu=(Spinner) findViewById(R.id.gspmpu);
		final Spinner ungspmpu=(Spinner) findViewById(R.id.ungspmpu);
		final Spinner ungspperform=(Spinner) findViewById(R.id.ungspperform);
		
		//Guide
		 ImageView ivgdate=(ImageView) findViewById(R.id.imageView1);
		
		//UnGuide
		final ImageView ivungdate=(ImageView) findViewById(R.id.imageView2);
		
		Button bsave=(Button) findViewById(R.id.button1);

		checknum=getIntent().getStringExtra("checknum");
		
		//Load data
				Cursor c=mdbHelper.getchecklisttrainer(checknum);
				if(c.moveToFirst())
				{
					trainerguide=c.getString(c.getColumnIndex("trainerguide"));
					trainerunguide=c.getString(c.getColumnIndex("trainerunguide"));
					//All From and To depend on the corridor up
					//Fill Spinner
					loadspiner("2",c.getString(c.getColumnIndex("gdayornight")),gspday);
					loadspiner("2",c.getString(c.getColumnIndex("ungdayornight")),ungspday);
					loadspiner("3",c.getString(c.getColumnIndex("gweather")),gspweather);
					loadspiner("3",c.getString(c.getColumnIndex("ungweather")),ungspweather);
					loadspiner("4",c.getString(c.getColumnIndex("gnovehicle")),gspnovehicle);
					loadspiner("4",c.getString(c.getColumnIndex("ungnovehicle")),ungspnovehicle);
					loadspiner("5",c.getString(c.getColumnIndex("gmpu")),gspmpu);
					loadspiner("5",c.getString(c.getColumnIndex("ungmpu")),ungspmpu);
					loadspiner("7",c.getString(c.getColumnIndex("ungperform")),ungspperform);
					
					loadspinertrainer(trainerguide,gsptrainer);
					loadspinertrainer(trainerunguide,ungsptrainer);

					loadspiner("1",c.getString(c.getColumnIndex("gfrom")),gspfrom);
					loadspinerfilter("1",c.getString(c.getColumnIndex("gto")),gspto,gspfrom.getSelectedItem().toString());
					
					loadspiner("1",c.getString(c.getColumnIndex("ungfrom")),ungspfrom);
					loadspinerfilter("1",c.getString(c.getColumnIndex("ungto")),ungspto,ungspfrom.getSelectedItem().toString());
					
					tvchecknum.setText(c.getString(c.getColumnIndex("checknum"))+"   "+c.getString(c.getColumnIndex("name")));
					etgdate.setText(c.getString(c.getColumnIndex("gdate")));
					
					etungdate.setText(c.getString(c.getColumnIndex("ungdate")));
					
					//Name of trainer
					tvgconduct.setText(c.getString(c.getColumnIndex("guidetrainerfullname")));	
					tvungconduct.setText(c.getString(c.getColumnIndex("unguidetrainerfullname")));	
					
					String gsigtrue=c.getString(c.getColumnIndex("gtrainersig"));
		 			if(gsigtrue !=null && gsigtrue.equalsIgnoreCase("true"))
		 			{
		 				chbgsig.setChecked(true);
		 			}
					
		 			String ungsigtrue=c.getString(c.getColumnIndex("ungtrainersig"));
					if(ungsigtrue !=null && ungsigtrue.equalsIgnoreCase("true"))
					{
						chbungsig.setChecked(true);
					}
				}

			 //get trainee username
				Cursor ctraineeusername=mdbHelper.traineexist();
				
				if(ctraineeusername.getCount()>0)
				{
					ctraineeusername.moveToFirst();
					trainee=ctraineeusername.getString(ctraineeusername.getColumnIndex("username"));
				}
				//Get assessor username
				Cursor cassessorusername=mdbHelper.assessoruserid();
				if(cassessorusername.getCount()>0)
				{
					cassessorusername.moveToFirst();
					assessor=cassessorusername.getString(cassessorusername.getColumnIndex("username"));
				}
				//Get trainer username who is login
				Cursor ctrainerusername=mdbHelper.traineruserid();
				if(ctrainerusername.getCount()>0)
				{
					ctrainerusername.moveToFirst();
					trainer=ctrainerusername.getString(ctrainerusername.getColumnIndex("username"));
				}	
		//Guided practice box needs to be locked out until instruction complete and unguided box locked out until guided box complete.
		Cursor cchecklistinfo=mdbHelper.checklistinfo(checknum);
		cchecklistinfo.moveToFirst();
		//if(cchecklistinfo.getString(cchecklistinfo.getColumnIndex("instrainersig")).equalsIgnoreCase("true"))
		//{
				//Check permission
				Cursor crole=mdbHelper.userexist();
				if(crole.getCount()>0)
				{
					crole.moveToFirst();
					//Assessor Have full access, We just invisible name of conduct because we visible spinner for them 
					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
					{
						//Guided practice box needs to be locked out until Instruction complete.
						if(cchecklistinfo.getString(cchecklistinfo.getColumnIndex("instrainersig"))!=null && cchecklistinfo.getString(cchecklistinfo.getColumnIndex("instrainersig")).equalsIgnoreCase("true"))
						{
							//If (date/trainer/from/to) are filled, we will enable signature 
							if(!(gsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etgdate.getText().toString().equalsIgnoreCase("") || gspfrom.getSelectedItem().toString().equalsIgnoreCase("") || gspto.getSelectedItem().toString().equalsIgnoreCase("")))
							{
								chbgsig.setEnabled(true);
							}
							
							tvgconduct.setVisibility(View.GONE);
							tvungconduct.setVisibility(View.GONE);
							bsave.setVisibility(View.VISIBLE);
							
							ivgdate.setVisibility(View.VISIBLE);
							gspday.setClickable(true);
							gspweather.setClickable(true);
							gspfrom.setClickable(true);
							gspto.setClickable(true);
							gspnovehicle.setClickable(true);
							gspmpu.setClickable(true);
						}
						//UNGuided practice box needs to be locked out until Guided complete.
						if(chbgsig.isChecked())
						{
							if(!(ungsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etungdate.getText().toString().equalsIgnoreCase("") || ungspfrom.getSelectedItem().toString().equalsIgnoreCase("") || ungspto.getSelectedItem().toString().equalsIgnoreCase("")))
							{
								chbungsig.setEnabled(true);
							}
							ivungdate.setVisibility(View.VISIBLE);
							ungspday.setClickable(true);
							ungspweather.setClickable(true);
							ungspfrom.setClickable(true);
							ungspto.setClickable(true);
							ungspnovehicle.setClickable(true);
							ungspmpu.setClickable(true);
							ungspperform.setClickable(true);
							ungsptrainer.setClickable(true);
						}
						
					}
					else
					{
						gsptrainer.setVisibility(View.GONE);
						ungsptrainer.setVisibility(View.GONE);
					}

					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainer") )
					{
						//Guided practice box needs to be locked out until Instruction complete.
						if(cchecklistinfo.getString(cchecklistinfo.getColumnIndex("instrainersig"))!=null && cchecklistinfo.getString(cchecklistinfo.getColumnIndex("instrainersig")).equalsIgnoreCase("true"))
						{
						//If nobody worked on the guide part before, trainer has access
						if((trainerguide==null) ||(trainerguide.isEmpty()) || (crole.getString(crole.getColumnIndex("username")).equalsIgnoreCase(trainerguide)))
						{
							if(!(gsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etgdate.getText().toString().equalsIgnoreCase("") || gspfrom.getSelectedItem().toString().equalsIgnoreCase("") || gspto.getSelectedItem().toString().equalsIgnoreCase("")))
							{
								chbgsig.setEnabled(true);
							}
							
							ivgdate.setVisibility(View.VISIBLE);
							gspday.setClickable(true);
							gspweather.setClickable(true);
							gspfrom.setClickable(true);
							gspto.setClickable(true);
							gspnovehicle.setClickable(true);
							gspmpu.setClickable(true);
						}
						}
						
						//If everybody worked on the unguide part before, just this person has access
						
						if((trainerunguide==null) || (trainerunguide.isEmpty()) || (crole.getString(crole.getColumnIndex("username")).equalsIgnoreCase(trainerunguide)))
						{
							//UNGuided practice box needs to be locked out until Guided complete.
							if(chbgsig.isChecked())
							{
							//Lock out signatures until required information is required e.g.Instructionpractice screen info such as name, date, corridor
							//&& (etdate.getText().toString().isEmpty()) && spcorridordown.getSelectedItem().toString().isEmpty() && spcorridorup.getSelectedItem().toString().isEmpty()
							if(!(ungsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etungdate.getText().toString().equalsIgnoreCase("") || ungspfrom.getSelectedItem().toString().equalsIgnoreCase("") || ungspto.getSelectedItem().toString().equalsIgnoreCase("")))
							{
								chbungsig.setEnabled(true);
							}
							ivungdate.setVisibility(View.VISIBLE);
							ungspday.setClickable(true);
							ungspweather.setClickable(true);
							ungspfrom.setClickable(true);
							ungspto.setClickable(true);
							ungspnovehicle.setClickable(true);
							ungspmpu.setClickable(true);
							ungspperform.setClickable(true);
							}
							
						}
						
											
						bsave.setVisibility(View.VISIBLE);
					}
					//Trainee doesn't have access to anything
					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
					{
						bsave.setVisibility(View.INVISIBLE);
					}
				}
		
				//When we change guidefrom, we need to upload guideTo
				gspfrom.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						loadspinerfilter("1",gspto.getSelectedItem().toString(),gspto,gspfrom.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				//When we change unguidefrom, we need to upload unguideTo
				ungspfrom.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						loadspinerfilter("1",ungspto.getSelectedItem().toString(),ungspto,ungspfrom.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
		//Save data
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String gtrainersig="false";
				String ungtrainersig="false";
				if(chbgsig.isChecked())
				{
					gtrainersig="true";
				}
				if(chbungsig.isChecked())
				{
					ungtrainersig="true";
				}
				//Check role of user
				Cursor cuserrole=mdbHelper.userexist();
				if(cuserrole.getCount()>0)
				{
					cuserrole.moveToFirst();
					//Assessor Have full access, We just invisible name of conduct because we visible spinner for them 
					if(cuserrole.getString(cuserrole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
					{
						//Update guide part
						mdbHelper.updatechecklistguide(ChecklistPractice.this,trainee, assessor, gsptrainer.getSelectedItem().toString(), gtrainersig, 
						etgdate.getText().toString(), gspday.getSelectedItem().toString(), gspweather.getSelectedItem().toString(), 
						gspfrom.getSelectedItem().toString(), gspto.getSelectedItem().toString(),
						gspmpu.getSelectedItem().toString(), 
						gspnovehicle.getSelectedItem().toString(), checknum);
						//Update unguide part
						mdbHelper.updatechecklistunguide(ChecklistPractice.this,trainee, assessor, ungsptrainer.getSelectedItem().toString(),
						ungtrainersig,etungdate.getText().toString(), ungspday.getSelectedItem().toString(),
						ungspweather.getSelectedItem().toString(), ungspfrom.getSelectedItem().toString(),
						ungspto.getSelectedItem().toString(), 
						ungspmpu.getSelectedItem().toString(), ungspnovehicle.getSelectedItem().toString(),ungspperform.getSelectedItem().toString(), checknum);
						
						//Active guide Signiture
						if(!(gsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etgdate.getText().toString().equalsIgnoreCase("") || gspfrom.getSelectedItem().toString().equalsIgnoreCase("") || gspto.getSelectedItem().toString().equalsIgnoreCase("")))
						{
							chbgsig.setEnabled(true);
						}
						//Active and disactive unguide items
						if(chbgsig.isChecked())
						{
							ungsptrainer.setClickable(true);
							ungspday.setClickable(true);
							ungspweather.setClickable(true);
							ungspfrom.setClickable(true);
							ungspto.setClickable(true);
							ungspmpu.setClickable(true);
							ungspnovehicle.setClickable(true);
							ungspperform.setClickable(true);
							ivungdate.setVisibility(View.VISIBLE);
						}
						else
						{
							chbungsig.setEnabled(false);
							ungsptrainer.setClickable(false);
							ungspday.setClickable(false);
							ungspweather.setClickable(false);
							ungspfrom.setClickable(false);
							ungspto.setClickable(false);
							ungspmpu.setClickable(false);
							ungspnovehicle.setClickable(false);
							ungspperform.setClickable(false);
							ivungdate.setVisibility(View.INVISIBLE);
						}
						//Active unguide Signiture
						
						if(!(ungsptrainer.getSelectedItem().toString().equalsIgnoreCase("") || etungdate.getText().toString().equalsIgnoreCase("") || ungspfrom.getSelectedItem().toString().equalsIgnoreCase("") || ungspto.getSelectedItem().toString().equalsIgnoreCase("")))
						{
							if(chbgsig.isChecked())	
								chbungsig.setEnabled(true);
						}
						else
						{	chbungsig.setEnabled(false);	}
					}
					//If user is trainer and have access to guide part
					if((cuserrole.getString(cuserrole.getColumnIndex("role")).equalsIgnoreCase("trainer")) && ((trainerguide==null) || (trainerguide.isEmpty()) || (cuserrole.getString(cuserrole.getColumnIndex("username")).equalsIgnoreCase(trainerguide))))
					{
						//At least one of the items from guide part should be filled to save data
						if(!(gtrainersig.equalsIgnoreCase("false") && etgdate.getText().toString().isEmpty() && 
								gspday.getSelectedItem().toString().isEmpty() && gspweather.getSelectedItem().toString().isEmpty() && 
								gspfrom.getSelectedItem().toString().isEmpty() && gspto.getSelectedItem().toString().isEmpty() 
								&& gspmpu.getSelectedItem().toString().isEmpty() && gspnovehicle.getSelectedItem().toString().isEmpty()))
							{
							mdbHelper.updatechecklistguide(ChecklistPractice.this,trainee, assessor, trainer, gtrainersig, 
									etgdate.getText().toString(), gspday.getSelectedItem().toString(), gspweather.getSelectedItem().toString(), 
									gspfrom.getSelectedItem().toString(), gspto.getSelectedItem().toString(),
									gspmpu.getSelectedItem().toString(), 
									gspnovehicle.getSelectedItem().toString(), checknum);
							tvgconduct.setText(trainer);
							}
						//Active sig in guide part
						if(!((gsptrainer.getSelectedItem().toString().equalsIgnoreCase("") && tvgconduct.getText().toString().equalsIgnoreCase("")) || etgdate.getText().toString().equalsIgnoreCase("") || gspfrom.getSelectedItem().toString().equalsIgnoreCase("") || gspto.getSelectedItem().toString().equalsIgnoreCase("")))
						{
							chbgsig.setEnabled(true);
						}
						
						if(chbgsig.isChecked())
						{
							ungsptrainer.setClickable(true);
							ungspday.setClickable(true);
							ungspweather.setClickable(true);
							ungspfrom.setClickable(true);
							ungspto.setClickable(true);
							ungspmpu.setClickable(true);
							ungspnovehicle.setClickable(true);
							ungspperform.setClickable(true);
							ivungdate.setVisibility(View.VISIBLE);
						}	
					}
					//If user is trainer and have access to unguide part
					if((cuserrole.getString(cuserrole.getColumnIndex("role")).equalsIgnoreCase("trainer")) && ((trainerunguide==null) || (trainerunguide.isEmpty()) || (cuserrole.getString(cuserrole.getColumnIndex("username")).equalsIgnoreCase(trainerunguide))))
					{
						//At least one of the items from unguide part should be filled to save data
						if(!(ungtrainersig.equalsIgnoreCase("false") && etungdate.getText().toString().isEmpty() && 
								ungspday.getSelectedItem().toString().isEmpty() && ungspweather.getSelectedItem().toString().isEmpty() && 
								ungspfrom.getSelectedItem().toString().isEmpty() && ungspto.getSelectedItem().toString().isEmpty()
								&& ungspmpu.getSelectedItem().toString().isEmpty() && ungspnovehicle.getSelectedItem().toString().isEmpty() && ungspperform.getSelectedItem().toString().isEmpty()))
							{
								//Update unguide part
								mdbHelper.updatechecklistunguide(ChecklistPractice.this,trainee, assessor, trainer,
								ungtrainersig,etungdate.getText().toString(), ungspday.getSelectedItem().toString(),
								ungspweather.getSelectedItem().toString(), ungspfrom.getSelectedItem().toString(),
								ungspto.getSelectedItem().toString(),
								ungspmpu.getSelectedItem().toString(), ungspnovehicle.getSelectedItem().toString(),ungspperform.getSelectedItem().toString(),checknum);
							
								tvungconduct.setText(trainer);
							}
						//Active Sig in unguide part
						if(!((ungsptrainer.getSelectedItem().toString().equalsIgnoreCase("") && tvungconduct.getText().toString().equalsIgnoreCase("")) || etungdate.getText().toString().equalsIgnoreCase("") || ungspfrom.getSelectedItem().toString().equalsIgnoreCase("") || ungspto.getSelectedItem().toString().equalsIgnoreCase("")))
						{
							chbungsig.setEnabled(true);
						}
					}
					
					}
			

			}
		});
		
		final DatePickerDialog.OnDateSetListener gdate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText3);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivgdate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(ChecklistPractice.this, gdate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		final DatePickerDialog.OnDateSetListener instructiondate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText12);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivungdate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(ChecklistPractice.this, instructiondate, myCalendar
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
		getMenuInflater().inflate(R.menu.act_checklistpractice, menu);
		return true;
	}
	public void loadspiner(String Id,String currentvalue,Spinner spid)
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		List<String> listdesc=new ArrayList<String>();
		int position=0;
		int countspinnerselection=0;
		
		Cursor curspinner=mdbHelper.getspinnervalue(Id);
		listdesc.add("");
		if(curspinner.getCount()>0)
		{
			
			curspinner.moveToFirst();
			do
			{
				countspinnerselection++;
				listdesc.add(curspinner.getString(curspinner.getColumnIndex("itemdesc")));
				if((currentvalue!=null) && (curspinner.getString(curspinner.getColumnIndex("itemdesc")).equalsIgnoreCase(currentvalue)))
				{
					position=countspinnerselection;
				}
				
			}
			while(curspinner.moveToNext());	
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listdesc);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spid.setAdapter(dataAdapter);
		spid.setSelection(position);
	}
	
	public void loadspinertrainer(String currentvalue,Spinner spid)
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		List<String> listdesc=new ArrayList<String>();
		int position=0;
		int countspinnerselection=0;
		
		Cursor curspinner=mdbHelper.getalltrainer();
		listdesc.add("");
		if(curspinner.getCount()>0)
		{
			curspinner.moveToFirst();
			do
			{
				countspinnerselection++;
				listdesc.add(curspinner.getString(curspinner.getColumnIndex("username")));
				if((currentvalue!=null) && (curspinner.getString(curspinner.getColumnIndex("username")).equalsIgnoreCase(currentvalue)))
				{
					position=countspinnerselection;
				}
				
			}
			while(curspinner.moveToNext());	
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listdesc);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spid.setAdapter(dataAdapter);
		spid.setSelection(position);
	}
	public void loadspinerfilter(String Id,String currentvalue,Spinner spid,String itemdesc)
	{
		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		List<String> listdesc=new ArrayList<String>();
		int position=0;
		int countspinnerselection=0;
		
		Cursor curspinner=mdbHelper.getspinnervaluefilter(Id,itemdesc);
		listdesc.add("");
		if(curspinner.getCount()>0)
		{
			String[] filteritem = curspinner.getString(curspinner.getColumnIndex("filter")).split(",");
			curspinner.moveToFirst();
			for (int i = 0; i < filteritem.length; i ++)
			{
				countspinnerselection++;
				listdesc.add(filteritem[i]);
				if((currentvalue!=null) && (filteritem[i].equalsIgnoreCase(currentvalue)))
				{
					position=countspinnerselection;
				}
			}
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listdesc);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spid.setAdapter(dataAdapter);
		spid.setSelection(position);
	}

	
}
