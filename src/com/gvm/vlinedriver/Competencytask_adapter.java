package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Competencytask_adapter extends BaseAdapter {
	
	private TestAdapter mDbHelper;
	private ArrayList<String> id;
	private ArrayList<String> number;
	private ArrayList<String> task;	
	public boolean[] chbd;
	public boolean[] chbe;
	public boolean[] chbnyc;
	
	public String editable;
	private String trainee="";
	private String assessor="";
	private String trainer="";
	public Activity activity;
	
	public Competencytask_adapter(Activity activity,ArrayList<String> id,ArrayList<String> number,ArrayList<String> task,
			boolean[] chbd,boolean[] chbe,boolean[] chbnyc,String editable,String trainee,String assessor,String trainer)
	{
		super();
		mDbHelper=new TestAdapter(activity);
		
		this.id=id;
		this.number=number;
		this.task=task;
		this.chbd=chbd;
		this.chbe=chbe;
		this.chbnyc=chbnyc;
		this.editable=editable;
		this.trainee=trainee;
		this.assessor=assessor;
		this.trainer=trainer;
		
		this.activity=activity;
	}

	public int getCount() {
		return id.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder
	{
		public TextView tvId;
		public TextView tvnumber;
		public TextView tvtask;
		public CheckBox chbd;
		public CheckBox chbe;
		public CheckBox chbnyc;
	}
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		
		final ViewHolder view;
		LayoutInflater inflator=activity.getLayoutInflater();
		if(convertView==null)
		{
			view=new ViewHolder();
			convertView=inflator.inflate(R.layout.competencytask_row, null);
			
			view.tvId=(TextView) convertView.findViewById(R.id.textView1);
			view.tvnumber=(TextView) convertView.findViewById(R.id.textView2);
			view.tvtask=(TextView) convertView.findViewById(R.id.textView3);
			view.chbd=(CheckBox) convertView.findViewById(R.id.checkBox1);
			view.chbe=(CheckBox) convertView.findViewById(R.id.checkBox2);
			view.chbnyc=(CheckBox) convertView.findViewById(R.id.checkBox3);
			
			convertView.setTag(view);
		}
		else
		{
			view=(ViewHolder) convertView.getTag();
		}
		
		view.tvId.setText(id.get(position));
		view.tvnumber.setText(number.get(position));
		view.tvtask.setText(task.get(position));
		
		//Demonstrated
		view.chbd.setOnCheckedChangeListener(null);
		if(chbd[position])
			view.chbd.setChecked(true);
		else
			view.chbd.setChecked(false);
		
		view.chbd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					chbd[position]=true;
					
					chbe[position]=false;
					view.chbe.setChecked(false);
					chbnyc[position]=false;
					view.chbnyc.setChecked(false);
					
					mDbHelper.checkchange(activity,"competencytask", "demonstrated", "true", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
					mDbHelper.checkchange(activity,"competencytask", "explained", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
					mDbHelper.checkchange(activity,"competencytask", "nyc", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
				}
				else
				{
					chbd[position]=false;
					mDbHelper.checkchange(activity,"competencytask", "demonstrated", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
				}
			}
		});
		
		//Explained
		view.chbe.setOnCheckedChangeListener(null);
		if(chbe[position])
			view.chbe.setChecked(true);
		else
			view.chbe.setChecked(false);
		
		view.chbe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					chbe[position]=true;
					
					chbd[position]=false;
					view.chbd.setChecked(false);
					chbnyc[position]=false;
					view.chbnyc.setChecked(false);
					
					mDbHelper.checkchange(activity,"competencytask", "explained", "true", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
					mDbHelper.checkchange(activity,"competencytask", "demonstrated", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);				
					mDbHelper.checkchange(activity,"competencytask", "nyc", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
				}
				else
				{
					chbe[position]=false;
					mDbHelper.checkchange(activity,"competencytask", "explained", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
				}
			}
		});
		
		//Not Yet Competent
		view.chbnyc.setOnCheckedChangeListener(null);
		if(chbnyc[position])
			view.chbnyc.setChecked(true);
		else
			view.chbnyc.setChecked(false);
		
		view.chbnyc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					chbnyc[position]=true;
					
					chbd[position]=false;
					view.chbd.setChecked(false);
					chbe[position]=false;
					view.chbe.setChecked(false);
					
					mDbHelper.checkchange(activity,"competencytask", "nyc", "true", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
					mDbHelper.checkchange(activity,"competencytask", "explained", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
					mDbHelper.checkchange(activity,"competencytask", "demonstrated", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);									
				}
				else
				{
					chbnyc[position]=false;
					mDbHelper.checkchange(activity,"competencytask", "nyc", "false", "Id", view.tvId.getText().toString(), trainee, assessor, trainer);
				}
			}
		});
		if(!editable.equalsIgnoreCase("true"))
		{
			view.chbd.setEnabled(false);
			view.chbd.setFocusable(false);
			view.chbe.setEnabled(false);
			view.chbe.setFocusable(false);
			view.chbnyc.setEnabled(false);
			view.chbnyc.setFocusable(false);
		}
		return convertView;
	}

}
