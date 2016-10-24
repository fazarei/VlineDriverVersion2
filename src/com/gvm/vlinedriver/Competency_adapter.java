package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Competency_adapter extends BaseAdapter {
	
	private TestAdapter mDbHelper;
	private ArrayList<String> id;
	private ArrayList<String> competency;	
	public boolean[] chbc;
	public boolean[] chbnyc;
	
	public String editable;
	private String trainee="";
	private String assessor="";
	private String trainer="";
	final public Activity activity;
	
	public Competency_adapter(Activity activity,ArrayList<String> id,ArrayList<String> competency,
			boolean[] chbc,boolean[] chbnyc,String editable,String trainee,String assessor,String trainer)
	{
		super();
		mDbHelper=new TestAdapter(activity);
		
		this.id=id;
		this.competency=competency;
		this.chbc=chbc;
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
		public TextView tvcompetency;
		public CheckBox chbc;
		public CheckBox chbnyc;
	}
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder view;
		LayoutInflater inflator=activity.getLayoutInflater();
		if(convertView==null)
		{
			view=new ViewHolder();
			convertView=inflator.inflate(R.layout.competency_row, null);
			
			view.tvId=(TextView) convertView.findViewById(R.id.textView1);
			view.tvcompetency=(TextView) convertView.findViewById(R.id.textView2);
			view.chbc=(CheckBox) convertView.findViewById(R.id.checkBox1);
			view.chbnyc=(CheckBox) convertView.findViewById(R.id.checkBox2);
			
			convertView.setTag(view);
		}
		else
		{
			view=(ViewHolder) convertView.getTag();
		}
		
		view.tvId.setText(id.get(position));
		view.tvcompetency.setText(competency.get(position));
		view.tvcompetency.setPaintFlags(view.tvcompetency.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
		
		//Competent
		view.chbc.setOnCheckedChangeListener(null);
		if(chbc[position])
			view.chbc.setChecked(true);
		else
			view.chbc.setChecked(false);
		
		view.chbc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
					if(isChecked)
					{
						chbc[position]=true;
						
						chbnyc[position]=false;
						view.chbnyc.setChecked(false);
						mDbHelper.checkchange(activity,"competency", "c", "true", "Id", id.get(position), trainee, assessor, trainer);	
					}
					else
					{
						chbc[position]=false;
						mDbHelper.checkchange(activity,"competency", "c", "false", "Id", id.get(position), trainee, assessor, trainer);
					}			
			}
		});

		view.tvcompetency.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Context context=v.getContext();
				
					String competencyid=view.tvId.getText().toString();
					Intent intent=new Intent(context,Competencytask_tab.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("competencyid", competencyid);
					
					context.startActivity(intent);
				
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
					
					chbc[position]=false;
					view.chbc.setChecked(false);
					mDbHelper.checkchange(activity,"competency", "nyc", "true", "Id", id.get(position), trainee, assessor, trainer);
					
				}
				else
				{
					chbnyc[position]=false;
					mDbHelper.checkchange(activity,"competency", "nyc", "false", "Id", id.get(position), trainee, assessor, trainer);					
				}
			}
		});
		if(editable.equalsIgnoreCase("false"))
		{
			view.chbc.setEnabled(false);
			view.chbnyc.setEnabled(false);
		}
		else
		{
			view.chbc.setEnabled(true);
			view.chbnyc.setEnabled(true);
		}
		
		return convertView;
	}

}
