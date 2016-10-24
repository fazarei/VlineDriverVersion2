package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class Competencycomment extends Activity {
	
	private String competencyid;
	private String trainee="";
	private String assessor="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_competencycomment);
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		competencyid=getIntent().getStringExtra("competencyid");
		
		final CheckBox chbassessorsig=(CheckBox) findViewById(R.id.checkBox3);
		final CheckBox chbtraineesig=(CheckBox) findViewById(R.id.checkBox4);
		final RadioButton rbcomplete=(RadioButton) findViewById(R.id.rbcmp);
		final RadioButton rbnotcomplete=(RadioButton) findViewById(R.id.rbnotcmp);
		final EditText etcomment=(EditText) findViewById(R.id.editText5);
		final Button bsave=(Button) findViewById(R.id.button1);
		
		//Load data
				Cursor c=mdbHelper.getcompetencyid(competencyid);
				if(c.getCount()>0)
				{
					c.moveToFirst();
					do
					{
						etcomment.setText(c.getString(c.getColumnIndex("comment")));
					
						if(c.getString(c.getColumnIndex("result"))!=null)
						{
							if(c.getString(c.getColumnIndex("result")).equalsIgnoreCase("true"))
							{
								rbcomplete.setChecked(true);
							}
							else
							{
								rbnotcomplete.setChecked(true);
							}
						}
						else
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
						etcomment.setEnabled(false);
						etcomment.setFocusable(false);
					}
					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
					{
						chbtraineesig.setEnabled(false);
						//If all competency task is set to demonstrate or explained, assessor is able to check competent
						Cursor ccompetencytask=mdbHelper.competencytaskcompetent(competencyid);
						if(ccompetencytask.getCount()>0)
						{
							ccompetencytask.moveToFirst();
							Integer allcompetency=ccompetencytask.getInt(ccompetencytask.getColumnIndex("competencycount"));
							Integer allcompcompetency=ccompetencytask.getInt(ccompetencytask.getColumnIndex("compcompetency"));

							if(!allcompetency.equals(allcompcompetency))
							{
								rbcomplete.setEnabled(false);
								chbassessorsig.setEnabled(false);
							}
						}
					}
					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
					{
						rbcomplete.setEnabled(false);
						rbnotcomplete.setEnabled(false);
						chbassessorsig.setEnabled(false);
						etcomment.setEnabled(false);
						etcomment.setFocusable(false);
						if(!chbassessorsig.isChecked())
						{
							chbtraineesig.setEnabled(false);
							bsave.setVisibility(View.INVISIBLE);
						}
					}
				}
				
				
				//Save data
				bsave.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						
						String result="false";
						String c="false";
						String nyc="false";
						String assessorsig="false";
						String traineesig="false";
						if(rbcomplete.isChecked())
						{
							result="true";
							c="true";
						}
						else
						{
							nyc="true";
						}
						
						if(chbassessorsig.isChecked())
						{	assessorsig="true";	}
						
						if(chbtraineesig.isChecked())
						{	traineesig="true";	}
						mdbHelper.updatecompetencycomment(Competencycomment.this,competencyid, result,etcomment.getText().toString(), assessorsig, traineesig
								, trainee, assessor,nyc,c);
					}
				});
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_competencycomment, menu);
		return true;
	}

}
