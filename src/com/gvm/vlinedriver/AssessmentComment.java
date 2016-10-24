package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class AssessmentComment extends Activity {
	
	private String assessmentid;
	private String trainee="";
	private String assessor="";
	final Calendar myCalendar = Calendar.getInstance();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmentcomment);
		
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		final EditText etassessordate=(EditText) findViewById(R.id.editText2);
		final EditText ettraineedate=(EditText) findViewById(R.id.editText4);
		final EditText etcomment=(EditText) findViewById(R.id.editText5);
		final CheckBox chbassessorsig=(CheckBox) findViewById(R.id.checkBox3);
		final CheckBox chbtraineesig=(CheckBox) findViewById(R.id.checkBox4);
		final RadioButton rbcomplete=(RadioButton) findViewById(R.id.rbcmp);
		final RadioButton rbnotcomplete=(RadioButton) findViewById(R.id.rbnotcmp);
		final Button bsave=(Button) findViewById(R.id.button1);
		final ImageView ivassessordate=(ImageView) findViewById(R.id.imageView1);
		final ImageView ivtraineedate=(ImageView) findViewById(R.id.imageView2);
		
		assessmentid=getIntent().getStringExtra("assessmentid");
		
		//Load data
		Cursor c=mdbHelper.getassessmentcomment(assessmentid);
		if(c.moveToFirst())
		{
			do
			{
				etassessordate.setText(c.getString(c.getColumnIndex("assessordate")));
				ettraineedate.setText(c.getString(c.getColumnIndex("traineedate")));
				etcomment.setText(c.getString(c.getColumnIndex("comment")));
				
				if(c.getString(c.getColumnIndex("result"))!=null && c.getString(c.getColumnIndex("result")).equalsIgnoreCase("true"))
				{
					rbcomplete.setChecked(true);
				}
				if(c.getString(c.getColumnIndex("result"))!=null && c.getString(c.getColumnIndex("result")).equalsIgnoreCase("false"))
				{
					rbnotcomplete.setChecked(true);
				}
				if(c.getString(c.getColumnIndex("assessorsig"))!=null && c.getString(c.getColumnIndex("assessorsig")).equalsIgnoreCase("true"))
				{
					chbassessorsig.setChecked(true);
				}
				if(c.getString(c.getColumnIndex("traineesig"))!=null && c.getString(c.getColumnIndex("traineesig")).equalsIgnoreCase("true"))
				{
					chbtraineesig.setChecked(true);
				}
			}
			while(c.moveToNext());
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
		//Get role of user
		Cursor crole=mdbHelper.userexist();
		if(crole.getCount()>0)
		{
			crole.moveToFirst();
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainer"))
			{
				bsave.setVisibility(View.INVISIBLE);
				rbcomplete.setEnabled(false);
				rbnotcomplete.setEnabled(false);
				chbassessorsig.setEnabled(false);
				chbtraineesig.setEnabled(false);
				etassessordate.setEnabled(false);
				etassessordate.setFocusable(false);
				ettraineedate.setEnabled(false);
				ettraineedate.setFocusable(false);
				etcomment.setEnabled(false);
				etcomment.setFocusable(false);
				ivassessordate.setVisibility(View.GONE);
				ivtraineedate.setVisibility(View.GONE);
			}
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				chbtraineesig.setEnabled(false);
				ettraineedate.setEnabled(false);
				ivtraineedate.setVisibility(View.GONE);
				// Need to link 'competent/non-competent boxes to critical tasks.Trainee  cannot be marked competent while a critical task is unmarked or failed.
				Cursor ccriticalcount=mdbHelper.criticalcount(assessmentid);
				Cursor ccompletecriticalcount=mdbHelper.completecriticalcount(assessmentid);
				Integer numbercritical=ccriticalcount.getCount();
				Integer numbercompcritical=ccompletecriticalcount.getCount();

				if(!numbercritical.equals(numbercompcritical))
				//if(numbercritical<numbercompcritical)
				{
					rbcomplete.setEnabled(false);
					//chbassessorsig.setEnabled(false);
				}
				
				if(!chbassessorsig.isChecked())
				{
					//rbnotcomplete.setEnabled(false);
				}
			}
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
			{
				bsave.setVisibility(View.INVISIBLE);
				rbcomplete.setEnabled(false);
				rbnotcomplete.setEnabled(false);
				chbassessorsig.setEnabled(false);
				chbtraineesig.setEnabled(false);
				etassessordate.setEnabled(false);
				etassessordate.setFocusable(false);
				ettraineedate.setEnabled(false);
				ettraineedate.setFocusable(false);
				etcomment.setEnabled(false);
				etcomment.setFocusable(false);
				ivassessordate.setVisibility(View.GONE);
				ivtraineedate.setVisibility(View.GONE);
				//Lock out trainee signature until assessor has signed.
				if(chbassessorsig.isChecked())
				{
					bsave.setVisibility(View.VISIBLE);
					chbtraineesig.setEnabled(true);
					ettraineedate.setEnabled(true);
					ivtraineedate.setVisibility(View.VISIBLE);
				}
			}
		}
		
		
		//Save data
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				String result="false";
				String assessorsig="false";
				String traineesig="false";
				if(rbcomplete.isChecked())
					{result="true";}
				
				if(chbassessorsig.isChecked())
				{	assessorsig="true";	}
				
				if(chbtraineesig.isChecked())
				{	traineesig="true";	}
				mdbHelper.assessmentcomment(AssessmentComment.this,etcomment.getText().toString().replace("'", "''"),result, assessorsig, traineesig, etassessordate.getText().toString(), 
						ettraineedate.getText().toString(),assessor,trainee, assessmentid);				
			}
		});
		
		final DatePickerDialog.OnDateSetListener assessordate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText2);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivassessordate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(AssessmentComment.this, assessordate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		final DatePickerDialog.OnDateSetListener traineedate=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate=(EditText) findViewById(R.id.editText4);
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
				new DatePickerDialog(AssessmentComment.this, traineedate, myCalendar
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
		getMenuInflater().inflate(R.menu.act_assessmentcomment, menu);
		return true;
	}
}
