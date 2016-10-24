package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChecklistComment extends Activity {
	
	private String subjectid;
	private String trainee="";
	private String assessor="";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklistcomment);
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		subjectid=getIntent().getStringExtra("subjectid");
		final EditText etcomment =(EditText) findViewById(R.id.editText1);
		final Button bsave=(Button) findViewById(R.id.button1);
		//Get assessor username
				Cursor cassessorusername=mdbHelper.assessoruserid();
				if(cassessorusername.getCount()>0)
				{
					cassessorusername.moveToFirst();
					assessor=cassessorusername.getString(cassessorusername.getColumnIndex("username"));
				}		
				//Get role o
		//get trainee username
				Cursor ctraineeusername=mdbHelper.traineexist();
				
				if(ctraineeusername.getCount()>0)
				{
					ctraineeusername.moveToFirst();
					trainee=ctraineeusername.getString(ctraineeusername.getColumnIndex("username"));
				}		

		//Get role of user
		Cursor crole=mdbHelper.userexist();
		if(crole.getCount()>0)
		{
			crole.moveToFirst();
			if(!crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				bsave.setVisibility(View.GONE);
				etcomment.setFocusable(false);
			}
		}
		
		//load data
		Cursor c=mdbHelper.getsubject(subjectid);
		if(c.getCount()>0)
		{
			c.moveToFirst();
			etcomment.setText(c.getString(c.getColumnIndex("comment")));
		}
		
		//Save data
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mdbHelper.subjectcomment(ChecklistComment.this,etcomment.getText().toString().replace("'", "''"), subjectid,trainee,assessor);			
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_checklistcomment, menu);
		
		return true;
	}

}
