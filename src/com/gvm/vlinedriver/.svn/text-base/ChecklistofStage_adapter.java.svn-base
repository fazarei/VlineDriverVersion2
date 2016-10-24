package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ChecklistofStage_adapter extends BaseAdapter{

	private ArrayList<String> id;
	private ArrayList<String> checknum;
	private ArrayList<String> checklist;
	private Activity activity;
	public boolean[] chbinprodess;
	public boolean[] chbcomplete;
	public boolean[] chbrpl;
	
	public ChecklistofStage_adapter(Activity activit,ArrayList<String> id,ArrayList<String> checknum,ArrayList<String> checklist
			,boolean[] chbinprodess,boolean[] chbcomplete,boolean[] chbrpl)
	{
		super();
		this.id=id;
		this.checknum=checknum;
		this.checklist=checklist;
		this.activity=activit;
		this.chbinprodess=chbinprodess;
		this.chbcomplete=chbcomplete;
		this.chbrpl=chbrpl;
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
		public TextView txtchecknum;
		public TextView txtchecklist;
		public CheckBox chbprocess;
		public CheckBox chbcomplete;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder view;
		LayoutInflater inflator=activity.getLayoutInflater();
		if(convertView==null)
		{
			view=new ViewHolder();
			convertView=inflator.inflate(R.layout.checklistofstage_row, null);
			
			view.txtId=(TextView) convertView.findViewById(R.id.textView1);
			view.txtchecknum=(TextView) convertView.findViewById(R.id.textView2);
			view.txtchecklist=(TextView) convertView.findViewById(R.id.textView3);
			view.chbprocess=(CheckBox) convertView.findViewById(R.id.checkBox1);
			view.chbcomplete=(CheckBox) convertView.findViewById(R.id.checkBox2);
			view.txtchecknum.setPaintFlags(view.txtchecknum.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
			convertView.setTag(view);
		}
		else
		{
			view=(ViewHolder) convertView.getTag();
		}
		
		view.txtId.setText(id.get(position));
		view.txtchecknum.setText(checknum.get(position));
		view.txtchecklist.setText(checklist.get(position));
		
		view.chbcomplete.setOnCheckedChangeListener(null);
        if(chbcomplete[position])
        	view.chbcomplete.setChecked(true);
        else  
        	view.chbcomplete.setChecked(false);
        
        view.chbprocess.setOnCheckedChangeListener(null);
        if(chbinprodess[position])
        	view.chbprocess.setChecked(true);
        else  
        	view.chbprocess.setChecked(false);
        //If RPL is true or is check user don't have access to checklist
       view.txtchecknum.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Context context=v.getContext();
				if(chbrpl[position])
				{
					Toast.makeText(context, R.string.checklistlockmessage, Toast.LENGTH_LONG).show();
					
				}
				else
				{
					String checknum=view.txtchecknum.getText().toString();
					Intent intent=new Intent(context,ChecklistTaskTab.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("checknum", checknum);
					
					context.startActivity(intent);
				}
				
			}
		});
		return convertView;
	}
	

}
