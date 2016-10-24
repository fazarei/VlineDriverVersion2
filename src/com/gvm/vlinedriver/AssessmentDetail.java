package com.gvm.vlinedriver;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AssessmentDetail extends Activity {
	private String assessmentid;
	private String trainee="";
	private String assessor="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmentdetail);
		
		assessmentid=getIntent().getStringExtra("assessmentid");
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		final TextView tvtripno1=(TextView) findViewById(R.id.textView7);
		final EditText ettime1=(EditText) findViewById(R.id.editText1);
		final Spinner splocation1=(Spinner) findViewById(R.id.splocation1);
		final Spinner spdestination1=(Spinner) findViewById(R.id.spdestination1);
		final EditText ettdno1=(EditText) findViewById(R.id.editText4);
		final EditText etmpu1=(EditText) findViewById(R.id.editText5);
		final Spinner spmpuno1=(Spinner) findViewById(R.id.spmpuno1);
		final Spinner spweather1=(Spinner) findViewById(R.id.spweather1);
		
		final TextView tvtripno2=(TextView) findViewById(R.id.textView8);
		final EditText ettime2=(EditText) findViewById(R.id.editText8);
		final Spinner splocation2=(Spinner) findViewById(R.id.splocation2);
		final Spinner spdestination2=(Spinner) findViewById(R.id.spdestination2);
		final EditText ettdno2=(EditText) findViewById(R.id.editText11);
		final EditText etmpu2=(EditText) findViewById(R.id.editText12);
		final Spinner spmpuno2=(Spinner) findViewById(R.id.spmpuno2);
		final Spinner spweather2=(Spinner) findViewById(R.id.spweather2);
		
		final TextView tvtripno3=(TextView) findViewById(R.id.textView9);
		final EditText ettime3=(EditText) findViewById(R.id.editText15);
		final Spinner splocation3=(Spinner) findViewById(R.id.splocation3);
		final Spinner spdestination3=(Spinner) findViewById(R.id.spdestination3);
		final EditText ettdno3=(EditText) findViewById(R.id.editText18);
		final EditText etmpu3=(EditText) findViewById(R.id.editText19);
		final Spinner spmpuno3=(Spinner) findViewById(R.id.spmpuno3);
		final Spinner spweather3=(Spinner) findViewById(R.id.spweather3);
		
		final TextView tvtripno4=(TextView) findViewById(R.id.textView10);
		final EditText ettime4=(EditText) findViewById(R.id.editText22);
		final Spinner splocation4=(Spinner) findViewById(R.id.splocation4);
		final Spinner spdestination4=(Spinner) findViewById(R.id.spdestination4);
		final EditText ettdno4=(EditText) findViewById(R.id.editText25);
		final EditText etmpu4=(EditText) findViewById(R.id.editText26);
		final Spinner spmpuno4=(Spinner) findViewById(R.id.spmpuno4);
		final Spinner spweather4=(Spinner) findViewById(R.id.spweather4);
		
		final TextView tvtripno5=(TextView) findViewById(R.id.textView11);
		final EditText ettime5=(EditText) findViewById(R.id.editText29);
		final Spinner splocation5=(Spinner) findViewById(R.id.splocation5);
		final Spinner spdestination5=(Spinner) findViewById(R.id.spdestination5);
		final EditText ettdno5=(EditText) findViewById(R.id.editText32);
		final EditText etmpu5=(EditText) findViewById(R.id.editText33);
		final Spinner spmpuno5=(Spinner) findViewById(R.id.spmpuno5);
		final Spinner spweather5=(Spinner) findViewById(R.id.spweather5);
		
		final TextView tvtripno6=(TextView) findViewById(R.id.textView13);
		final EditText ettime6=(EditText) findViewById(R.id.editText36);
		final Spinner splocation6=(Spinner) findViewById(R.id.splocation6);
		final Spinner spdestination6=(Spinner) findViewById(R.id.spdestination6);
		final EditText ettdno6=(EditText) findViewById(R.id.editText39);
		final EditText etmpu6=(EditText) findViewById(R.id.editText40);
		final Spinner spmpuno6=(Spinner) findViewById(R.id.spmpuno6);
		final Spinner spweather6=(Spinner) findViewById(R.id.spweather6);
		
		final Button bsave=(Button) findViewById(R.id.button1);
		
		//Load data
		Cursor c=mdbHelper.getassessmentdetail(assessmentid);
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("1"))
				{						
					tvtripno1.setText(c.getString(c.getColumnIndex("tripno")));
					ettime1.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation1);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination1,splocation1.getSelectedItem().toString());
					ettdno1.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu1.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno1);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather1);					
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("2"))
				{	
					tvtripno2.setText(c.getString(c.getColumnIndex("tripno")));
					ettime2.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation2);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination2,splocation2.getSelectedItem().toString());
					ettdno2.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu2.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno2);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather2);
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("3"))
				{	
					tvtripno3.setText(c.getString(c.getColumnIndex("tripno")));
					ettime3.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation3);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination3,splocation3.getSelectedItem().toString());
					ettdno3.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu3.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno3);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather3);
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("4"))
				{
					tvtripno4.setText(c.getString(c.getColumnIndex("tripno")));
					ettime4.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation4);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination4,splocation4.getSelectedItem().toString());
					ettdno4.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu4.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno4);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather4);
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("5"))
				{
					tvtripno5.setText(c.getString(c.getColumnIndex("tripno")));
					ettime5.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation5);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination5,splocation5.getSelectedItem().toString());
					ettdno5.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu5.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno5);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather5);
				}
				if(c.getString(c.getColumnIndex("tripno")).equalsIgnoreCase("6"))
				{
					tvtripno6.setText(c.getString(c.getColumnIndex("tripno")));
					ettime6.setText(c.getString(c.getColumnIndex("time")));
					loadspiner("1",c.getString(c.getColumnIndex("location")),splocation6);
					loadspinerfilter("1",c.getString(c.getColumnIndex("destination")),spdestination6,splocation6.getSelectedItem().toString());
					ettdno6.setText(c.getString(c.getColumnIndex("tdNo")));
					etmpu6.setText(c.getString(c.getColumnIndex("mputype")));
					loadspiner("5",c.getString(c.getColumnIndex("mpuno")),spmpuno6);
					loadspiner("3",c.getString(c.getColumnIndex("weather")),spweather6);
				}
			}
			while(c.moveToNext());
		}
		else
		{
			mdbHelper.assessmentdetailinsert(assessmentid, "1");
			mdbHelper.assessmentdetailinsert(assessmentid, "2");
			mdbHelper.assessmentdetailinsert(assessmentid, "3");
			mdbHelper.assessmentdetailinsert(assessmentid, "4");
			mdbHelper.assessmentdetailinsert(assessmentid, "5");
			mdbHelper.assessmentdetailinsert(assessmentid, "6");
			
			tvtripno1.setText("1");
			tvtripno2.setText("2");
			tvtripno3.setText("3");
			tvtripno4.setText("4");
			tvtripno5.setText("5");
			tvtripno6.setText("6");
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
					if(!crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
					{
						bsave.setVisibility(View.INVISIBLE);
						ettime1.setFocusable(false);
						ettime2.setFocusable(false);
						ettime3.setFocusable(false);
						ettime4.setFocusable(false);
						ettime5.setFocusable(false);
						ettime6.setFocusable(false);
						
						ettdno1.setFocusable(false);
						ettdno2.setFocusable(false);
						ettdno3.setFocusable(false);
						ettdno4.setFocusable(false);
						ettdno5.setFocusable(false);
						ettdno6.setFocusable(false);
						
						etmpu1.setFocusable(false);
						etmpu2.setFocusable(false);
						etmpu3.setFocusable(false);
						etmpu4.setFocusable(false);
						etmpu5.setFocusable(false);
						etmpu6.setFocusable(false);
						
						spmpuno1.setClickable(false);
						spmpuno2.setClickable(false);
						spmpuno3.setClickable(false);
						spmpuno4.setClickable(false);
						spmpuno5.setClickable(false);
						spmpuno6.setClickable(false);
						
						splocation1.setClickable(false);
						splocation2.setClickable(false);
						splocation3.setClickable(false);
						splocation4.setClickable(false);
						splocation5.setClickable(false);
						splocation6.setClickable(false);
						
						spdestination1.setClickable(false);
						spdestination2.setClickable(false);
						spdestination3.setClickable(false);
						spdestination4.setClickable(false);
						spdestination5.setClickable(false);
						spdestination6.setClickable(false);
						
						spweather1.setClickable(false);
						spweather2.setClickable(false);
						spweather3.setClickable(false);
						spweather4.setClickable(false);
						spweather5.setClickable(false);
						spweather6.setClickable(false);
					}
					
				}
		//Save data
		bsave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//row 1
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno1.getText().toString(), ettime1.getText().toString(),
						splocation1.getSelectedItem().toString(), spdestination1.getSelectedItem().toString(), ettdno1.getText().toString(),
						 etmpu1.getText().toString(),spmpuno1.getSelectedItem().toString(), spweather1.getSelectedItem().toString(),assessor,trainee);
				//row 2
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno2.getText().toString(), ettime2.getText().toString(),
						splocation2.getSelectedItem().toString(), spdestination2.getSelectedItem().toString(), ettdno2.getText().toString(),
						 etmpu2.getText().toString(),spmpuno2.getSelectedItem().toString(), spweather2.getSelectedItem().toString(),assessor,trainee);
				//row 3
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno3.getText().toString(), ettime3.getText().toString(),
						splocation3.getSelectedItem().toString(), spdestination3.getSelectedItem().toString(), ettdno3.getText().toString(),
						etmpu3.getText().toString(),spmpuno3.getSelectedItem().toString(), spweather3.getSelectedItem().toString(),assessor,trainee);
				//row 4
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno4.getText().toString(), ettime4.getText().toString(),
						splocation4.getSelectedItem().toString(), spdestination4.getSelectedItem().toString(), ettdno4.getText().toString(),
						etmpu4.getText().toString(),spmpuno4.getSelectedItem().toString(), spweather4.getSelectedItem().toString(),assessor,trainee);
				//row 5
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno5.getText().toString(), ettime5.getText().toString(),
						splocation5.getSelectedItem().toString(), spdestination5.getSelectedItem().toString(), ettdno5.getText().toString(),
						etmpu5.getText().toString(),spmpuno5.getSelectedItem().toString(), spweather5.getSelectedItem().toString(),assessor,trainee);
				//row 6
				mdbHelper.assessmentdetail(AssessmentDetail.this,assessmentid, tvtripno6.getText().toString(), ettime6.getText().toString(),
						splocation6.getSelectedItem().toString(), spdestination6.getSelectedItem().toString(), ettdno6.getText().toString(),
						etmpu6.getText().toString(),spmpuno6.getSelectedItem().toString(), spweather6.getSelectedItem().toString(),assessor,trainee);
				
				//Show message
				Toast toast=Toast.makeText(getApplicationContext(), R.string.savemessage, Toast.LENGTH_LONG);
				toast.show();
			}
		});
		
		//When we change corridor up, we need to upload corridor down, from and to
				splocation1.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination1.getSelectedItem().toString(),spdestination1,splocation1.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				
				splocation2.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination2.getSelectedItem().toString(),spdestination2,splocation2.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				
				splocation3.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination3.getSelectedItem().toString(),spdestination3,splocation3.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				
				splocation4.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination4.getSelectedItem().toString(),spdestination4,splocation4.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				
				splocation5.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination5.getSelectedItem().toString(),spdestination5,splocation5.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
				
				splocation6.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loadspinerfilter("1",spdestination6.getSelectedItem().toString(),spdestination6,splocation6.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}
				});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_assessmentdetail, menu);
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
