package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectofAssessment_adapter extends BaseAdapter {
	private ArrayList<String> id;
	private Activity activity;
	
	public SubjectofAssessment_adapter(Activity activity,ArrayList<String> id)
	{
		super();
		this.id=id;
		this.activity=activity;
	}

	public int getCount() {
		return id.size();
	}

	public Object getItem(int position) {
		return id.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
	
	public static class ViewHolder
	{
		public TextView txtId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();
		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.subjectofassessment_row, null);
			
			view.txtId=(TextView) convertView.findViewById(R.id.textView1);
			
			convertView.setTag(view);
		}
		else
		{
			view=(ViewHolder) convertView.getTag();
		}
		
		view.txtId.setText(id.get(position));
		
		return convertView;
	}
	
	

}
