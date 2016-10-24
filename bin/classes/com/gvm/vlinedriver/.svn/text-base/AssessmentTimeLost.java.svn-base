package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentTimeLost extends Activity {
	
	private String assessmentid;
	private String trainee="";
	private String assessor="";

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmenttimelost);
		
		assessmentid=getIntent().getStringExtra("assessmentid");
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		final TextView tvtripno1=(TextView) findViewById(R.id.textView7);
		final EditText etminlost1=(EditText) findViewById(R.id.editText1);
		final EditText etsignal1=(EditText) findViewById(R.id.editText2);
		final EditText etpassenger1=(EditText) findViewById(R.id.editText3);
		final EditText etpermanent1=(EditText) findViewById(R.id.editText4);
		final EditText ettrackwork1=(EditText) findViewById(R.id.editText5);
		final EditText ettrainfault1=(EditText) findViewById(R.id.editText6);
		final EditText etother1=(EditText) findViewById(R.id.editText7);
		final EditText etexplain1=(EditText) findViewById(R.id.editText45);
		
		final TextView tvtripno2=(TextView) findViewById(R.id.textView8);
		final EditText etminlost2=(EditText) findViewById(R.id.editText8);
		final EditText etsignal2=(EditText) findViewById(R.id.editText9);
		final EditText etpassenger2=(EditText) findViewById(R.id.editText10);
		final EditText etpermanent2=(EditText) findViewById(R.id.editText11);
		final EditText ettrackwork2=(EditText) findViewById(R.id.editText12);
		final EditText ettrainfault2=(EditText) findViewById(R.id.editText13);
		final EditText etother2=(EditText) findViewById(R.id.editText14);
		final EditText etexplain2=(EditText) findViewById(R.id.editText46);
		
		final TextView tvtripno3=(TextView) findViewById(R.id.textView9);
		final EditText etminlost3=(EditText) findViewById(R.id.editText15);
		final EditText etsignal3=(EditText) findViewById(R.id.editText16);
		final EditText etpassenger3=(EditText) findViewById(R.id.editText17);
		final EditText etpermanent3=(EditText) findViewById(R.id.editText18);
		final EditText ettrackwork3=(EditText) findViewById(R.id.editText19);
		final EditText ettrainfault3=(EditText) findViewById(R.id.editText20);
		final EditText etother3=(EditText) findViewById(R.id.editText21);
		final EditText etexplain3=(EditText) findViewById(R.id.editText47);
		
		final TextView tvtripno4=(TextView) findViewById(R.id.textView10);
		final EditText etminlost4=(EditText) findViewById(R.id.editText22);
		final EditText etsignal4=(EditText) findViewById(R.id.editText23);
		final EditText etpassenger4=(EditText) findViewById(R.id.editText24);
		final EditText etpermanent4=(EditText) findViewById(R.id.editText25);
		final EditText ettrackwork4=(EditText) findViewById(R.id.editText26);
		final EditText ettrainfault4=(EditText) findViewById(R.id.editText27);
		final EditText etother4=(EditText) findViewById(R.id.editText28);
		final EditText etexplain4=(EditText) findViewById(R.id.editText48);
		
		final TextView tvtripno5=(TextView) findViewById(R.id.textView11);
		final EditText etminlost5=(EditText) findViewById(R.id.editText29);
		final EditText etsignal5=(EditText) findViewById(R.id.editText30);
		final EditText etpassenger5=(EditText) findViewById(R.id.editText31);
		final EditText etpermanent5=(EditText) findViewById(R.id.editText32);
		final EditText ettrackwork5=(EditText) findViewById(R.id.editText33);
		final EditText ettrainfault5=(EditText) findViewById(R.id.editText34);
		final EditText etother5=(EditText) findViewById(R.id.editText35);
		final EditText etexplain5=(EditText) findViewById(R.id.editText49);
		
		final TextView tvtripno6=(TextView) findViewById(R.id.textView13);
		final EditText etminlost6=(EditText) findViewById(R.id.editText36);
		final EditText etsignal6=(EditText) findViewById(R.id.editText37);
		final EditText etpassenger6=(EditText) findViewById(R.id.editText38);
		final EditText etpermanent6=(EditText) findViewById(R.id.editText39);
		final EditText ettrackwork6=(EditText) findViewById(R.id.editText40);
		final EditText ettrainfault6=(EditText) findViewById(R.id.editText41);
		final EditText etother6=(EditText) findViewById(R.id.editText42);
		final EditText etexplain6=(EditText) findViewById(R.id.editText50);
		
		final EditText ettimeover=(EditText) findViewById(R.id.editText43);
		final EditText ettimelost=(EditText) findViewById(R.id.editText44);
		final TextView tvtotaltime=(TextView) findViewById(R.id.textView21);
		
		final Button bsave=(Button) findViewById(R.id.button1);
		//Load data
		Cursor c=mdbHelper.getassessmenttimelost(assessmentid);
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("1"))
				{	
					tvtripno1.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost1.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal1.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger1.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent1.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork1.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault1.setText(c.getString(c.getColumnIndex("trainfault")));
					etother1.setText(c.getString(c.getColumnIndex("other")));
					etexplain1.setText(c.getString(c.getColumnIndex("explanation")));
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("2"))
				{						
					tvtripno2.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost2.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal2.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger2.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent2.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork2.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault2.setText(c.getString(c.getColumnIndex("trainfault")));
					etother2.setText(c.getString(c.getColumnIndex("other")));
					etexplain2.setText(c.getString(c.getColumnIndex("explanation")));
					
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("3"))
				{						
					tvtripno3.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost3.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal3.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger3.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent3.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork3.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault3.setText(c.getString(c.getColumnIndex("trainfault")));
					etother3.setText(c.getString(c.getColumnIndex("other")));
					etexplain3.setText(c.getString(c.getColumnIndex("explanation")));
				}
				if(c.getString(c.getColumnIndex("tripno")).equals("4"))
				{						
					tvtripno4.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost4.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal4.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger4.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent4.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork4.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault4.setText(c.getString(c.getColumnIndex("trainfault")));
					etother4.setText(c.getString(c.getColumnIndex("other")));
					etexplain4.setText(c.getString(c.getColumnIndex("explanation")));
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("5"))
				{						
					tvtripno5.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost5.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal5.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger5.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent5.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork5.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault5.setText(c.getString(c.getColumnIndex("trainfault")));
					etother5.setText(c.getString(c.getColumnIndex("other")));
					etexplain5.setText(c.getString(c.getColumnIndex("explanation")));
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("6"))
				{						
					tvtripno6.setText(c.getString(c.getColumnIndex("tripno")));
					etminlost6.setText(c.getString(c.getColumnIndex("minutelost")));
					etsignal6.setText(c.getString(c.getColumnIndex("signals")));
					etpassenger6.setText(c.getString(c.getColumnIndex("passengerdelay")));
					etpermanent6.setText(c.getString(c.getColumnIndex("permanentway")));
					ettrackwork6.setText(c.getString(c.getColumnIndex("trackwork")));
					ettrainfault6.setText(c.getString(c.getColumnIndex("trainfault")));
					etother6.setText(c.getString(c.getColumnIndex("other")));
					etexplain6.setText(c.getString(c.getColumnIndex("explanation")));
				}
			}
			while(c.moveToNext());
		}
		else
		{
			mdbHelper.assessmenttimelostinsert(assessmentid, "1");
			mdbHelper.assessmenttimelostinsert(assessmentid, "2");
			mdbHelper.assessmenttimelostinsert(assessmentid, "3");
			mdbHelper.assessmenttimelostinsert(assessmentid, "4");
			mdbHelper.assessmenttimelostinsert(assessmentid, "5");
			mdbHelper.assessmenttimelostinsert(assessmentid, "6");
			
			tvtripno1.setText("1");
			tvtripno2.setText("2");
			tvtripno3.setText("3");
			tvtripno4.setText("4");
			tvtripno5.setText("5");
			tvtripno6.setText("6");
		}
		timecalculate(ettimeover, ettimelost, tvtotaltime, assessmentid);

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
			if(!crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				bsave.setVisibility(View.INVISIBLE);
				ettimelost.setFocusable(false);
				ettimeover.setFocusable(false);
				
				etminlost1.setFocusable(false);
				etminlost2.setFocusable(false);
				etminlost3.setFocusable(false);
				etminlost4.setFocusable(false);
				etminlost5.setFocusable(false);
				etminlost6.setFocusable(false);
				
				etsignal1.setFocusable(false);
				etsignal2.setFocusable(false);
				etsignal3.setFocusable(false);
				etsignal4.setFocusable(false);
				etsignal5.setFocusable(false);
				etsignal6.setFocusable(false);
				
				etpassenger1.setFocusable(false);
				etpassenger2.setFocusable(false);
				etpassenger3.setFocusable(false);
				etpassenger4.setFocusable(false);
				etpassenger5.setFocusable(false);
				etpassenger6.setFocusable(false);
				
				etpermanent1.setFocusable(false);
				etpermanent2.setFocusable(false);
				etpermanent3.setFocusable(false);
				etpermanent4.setFocusable(false);
				etpermanent5.setFocusable(false);
				etpermanent6.setFocusable(false);
				
				ettrackwork1.setFocusable(false);
				ettrackwork2.setFocusable(false);
				ettrackwork3.setFocusable(false);
				ettrackwork4.setFocusable(false);
				ettrackwork5.setFocusable(false);
				ettrackwork6.setFocusable(false);
				
				ettrainfault1.setFocusable(false);
				ettrainfault2.setFocusable(false);
				ettrainfault3.setFocusable(false);
				ettrainfault4.setFocusable(false);
				ettrainfault5.setFocusable(false);
				ettrainfault6.setFocusable(false);
				
				ettrainfault1.setFocusable(false);
				ettrainfault2.setFocusable(false);
				ettrainfault3.setFocusable(false);
				ettrainfault4.setFocusable(false);
				ettrainfault5.setFocusable(false);
				ettrainfault6.setFocusable(false);
				
				etother1.setFocusable(false);
				etother2.setFocusable(false);
				etother3.setFocusable(false);
				etother4.setFocusable(false);
				etother5.setFocusable(false);
				etother6.setFocusable(false);
				
				etexplain1.setFocusable(false);
				etexplain2.setFocusable(false);
				etexplain3.setFocusable(false);
				etexplain4.setFocusable(false);
				etexplain5.setFocusable(false);
				etexplain6.setFocusable(false);
			}
		}
		//Save data
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//First row
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno1.getText().toString(), etminlost1.getText().toString(),
						etsignal1.getText().toString(), etpassenger1.getText().toString(), etpermanent1.getText().toString()
						, ettrackwork1.getText().toString(), ettrainfault1.getText().toString(),
						etother1.getText().toString(),etexplain1.getText().toString(),assessor,trainee);
				//row 2
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno2.getText().toString(), etminlost2.getText().toString(),
						etsignal2.getText().toString(), etpassenger2.getText().toString(), etpermanent2.getText().toString()
						, ettrackwork2.getText().toString(), ettrainfault2.getText().toString(),
						etother2.getText().toString(),etexplain2.getText().toString(),assessor,trainee);
				//row 3
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno3.getText().toString(), etminlost3.getText().toString(),
						etsignal3.getText().toString(), etpassenger3.getText().toString(), etpermanent3.getText().toString()
						, ettrackwork3.getText().toString(), ettrainfault3.getText().toString(),
						etother3.getText().toString(),etexplain3.getText().toString(),assessor,trainee);
				//row 4
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno4.getText().toString(), etminlost4.getText().toString(),
						etsignal4.getText().toString(), etpassenger4.getText().toString(), etpermanent4.getText().toString()
						, ettrackwork4.getText().toString(), ettrainfault4.getText().toString(),
						etother4.getText().toString(),etexplain4.getText().toString(),assessor,trainee);
				//row 5
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno5.getText().toString(), etminlost5.getText().toString(),
						etsignal5.getText().toString(), etpassenger5.getText().toString(), etpermanent5.getText().toString()
						, ettrackwork5.getText().toString(), ettrainfault5.getText().toString(),
						etother5.getText().toString(),etexplain5.getText().toString(),assessor,trainee);
				//row 6
				mdbHelper.timelost(AssessmentTimeLost.this,assessmentid, tvtripno6.getText().toString(), etminlost6.getText().toString(),
						etsignal6.getText().toString(), etpassenger6.getText().toString(), etpermanent6.getText().toString()
						, ettrackwork6.getText().toString(), ettrainfault6.getText().toString(),
						etother6.getText().toString(),etexplain6.getText().toString(),assessor,trainee);
				
				timecalculate(ettimeover, ettimelost, tvtotaltime, assessmentid);
				mdbHelper.assessmenttime(AssessmentTimeLost.this,assessmentid, ettimeover.getText().toString(), ettimelost.getText().toString());
				//Show message
				Toast toast=Toast.makeText(getApplicationContext(), "Information saved successfully.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_assessmenttimelost, menu);
		return true;
	}
	public void timecalculate(EditText ettimeover,EditText ettotaltimelost,TextView tvtotaltime,String assessmentid)
	{
		TestAdapter mydbHelper=new TestAdapter(AssessmentTimeLost.this);
		mydbHelper.open();
		Cursor ctime=mydbHelper.countassessmenttimelost(assessmentid);
		if(ctime.getCount()>0)
		{
			ctime.moveToFirst();
			Integer timeover=ctime.getInt(ctime.getColumnIndex("minlost"));
			Integer timetotallost=ctime.getInt(ctime.getColumnIndex("signal"))+ctime.getInt(ctime.getColumnIndex("passengerdelay"))
					+ctime.getInt(ctime.getColumnIndex("permanent"))+ctime.getInt(ctime.getColumnIndex("trackwork"))
					+ctime.getInt(ctime.getColumnIndex("trainfault"))+ctime.getInt(ctime.getColumnIndex("other"));
			Integer totaltime=timeover-timetotallost;
			
			ettimeover.setText(timeover.toString());
			ettotaltimelost.setText(timetotallost.toString());
			tvtotaltime.setText(totaltime.toString());
		}
	}

}
