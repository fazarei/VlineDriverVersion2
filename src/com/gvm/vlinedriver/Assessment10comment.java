package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
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

public class Assessment10comment extends Activity {
	private String assessmentid;
	private String trainee="";
	private String assessor="";
	final Calendar myCalendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessment10comment);
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		final EditText etassessordate=(EditText) findViewById(R.id.editText2);
		final EditText etcomment=(EditText) findViewById(R.id.editText5);
		final CheckBox chbassessorsig=(CheckBox) findViewById(R.id.checkBox3);
		final CheckBox chbtraineesig=(CheckBox) findViewById(R.id.checkBox4);
		final RadioButton rbcomplete=(RadioButton) findViewById(R.id.rbcmp);
		final RadioButton rbnotcomplete=(RadioButton) findViewById(R.id.rbnotcmp);
		final Button bsave=(Button) findViewById(R.id.button1);
		final ImageView ivassessordate=(ImageView) findViewById(R.id.imageView1);
		
		assessmentid=getIntent().getStringExtra("assessmentid");
		
		//Load data
		Cursor c=mdbHelper.getassessmentcomment(assessmentid);
		if(c.moveToFirst())
		{
			do
			{
				etassessordate.setText(c.getString(c.getColumnIndex("assessordate")));
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
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				chbtraineesig.setEnabled(false);
				//If all competency is competent then assessor can signed
				Cursor ccompallcompetency=mdbHelper.assessment10competent(assessmentid);
				if(ccompallcompetency.getCount()>0)
				{
					ccompallcompetency.moveToFirst();
					Integer allcompetency=ccompallcompetency.getInt(ccompallcompetency.getColumnIndex("competencycount"));
					Integer allcompcompetency=ccompallcompetency.getInt(ccompallcompetency.getColumnIndex("compcompetency"));

					//if(!allcompetency.equals(allcompcompetency))
					if(!(allcompcompetency>=1))
					{
						rbcomplete.setEnabled(false);
						//Check box assessor always should be active even when not competent is checked
						//chbassessorsig.setEnabled(false);
					}
				}
			}
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
			{
				bsave.setVisibility(View.INVISIBLE);
				rbcomplete.setEnabled(false);
				rbnotcomplete.setEnabled(false);
				chbassessorsig.setEnabled(false);
				chbtraineesig.setEnabled(false);
				etassessordate.setFocusable(false);
				etcomment.setFocusable(false);
				ivassessordate.setVisibility(View.INVISIBLE);
				if(chbassessorsig.isChecked())
				{
					chbtraineesig.setEnabled(true);
					bsave.setVisibility(View.VISIBLE);
				}
			}
			if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainer"))
			{
				bsave.setVisibility(View.INVISIBLE);
				rbcomplete.setEnabled(false);
				rbnotcomplete.setEnabled(false);
				chbassessorsig.setEnabled(false);
				chbtraineesig.setEnabled(false);
				etassessordate.setFocusable(false);
				etcomment.setFocusable(false);
				ivassessordate.setVisibility(View.INVISIBLE);
				chbtraineesig.setEnabled(false);
				
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
				mdbHelper.assessmentcomment(Assessment10comment.this,etcomment.getText().toString(),result, assessorsig, traineesig, etassessordate.getText().toString(), 
						"",assessor,trainee, assessmentid);				

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
				new DatePickerDialog(Assessment10comment.this, assessordate, myCalendar
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
