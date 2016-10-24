package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class AssessmentSubject_adapter extends BaseAdapter{
	
	private TestAdapter mDbHelper;
	private ArrayList<String> id;
	private ArrayList<String> subject;
	private ArrayList<String> desc;
	private ArrayList<String> total;
	private ArrayList<String> required;
	public Activity activity;
	public String[] keepetachived;
	private String editableachive;
	private String assessor;
	private String trainee;
	// private String[] guess;


	public AssessmentSubject_adapter(Activity activity,ArrayList<String> id,ArrayList<String> subject,ArrayList<String> desc
			,ArrayList<String> total,ArrayList<String> required,String[] etachived,String editableachive,String assessor,String trainee)
	{
		super();
		this.id=id;
		this.subject=subject;
		this.desc=desc;
		this.total=total;
		this.required=required;
		this.activity=activity;
		mDbHelper = new TestAdapter(activity);
		mDbHelper.open();
		this.keepetachived=etachived;
		this.editableachive=editableachive;
		this.assessor=assessor;
		this.trainee=trainee;
	}
	
	public int getCount() {
		return id.size();
	}

	public Object getItem(int position) {
		return id.get(position);
		//this.position=position;
		//return position;
	}
	
	public String getItem2(int position) {        return keepetachived[position];       }

	public long getItemId(int position) {
		return 0;
		//return position;
	}
	
	public static class ViewHolder
	{
		public TextView txtId;
		public TextView txtsubject;
		public TextView txtdesc;
		public TextView txttotal;
		public TextView txtrequire;
		public EditText etachived;
		public TextView txtachived;
	}

	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		//Log.d("LEE","a"+position);
		final ViewHolder view;
		LayoutInflater inflator=activity.getLayoutInflater();
		if(convertView==null)
		{
			view=new ViewHolder();
			convertView=inflator.inflate(R.layout.assessmentsubject_row, null);
			
			view.txtId=(TextView) convertView.findViewById(R.id.textView1);
			view.txtsubject=(TextView) convertView.findViewById(R.id.textView2);
			view.txttotal=(TextView) convertView.findViewById(R.id.textView3);
			view.txtrequire=(TextView) convertView.findViewById(R.id.textView4);
			view.txtdesc=(TextView) convertView.findViewById(R.id.textView5);
			view.etachived=(EditText) convertView.findViewById(R.id.editText1);
			view.txtachived=(TextView) convertView.findViewById(R.id.textView6);
			convertView.setTag(view);
		}
		else
		{
			view=(ViewHolder) convertView.getTag();
		}
		
		view.txtId.setText(id.get(position));
		view.txtsubject.setText(subject.get(position));
		view.txttotal.setText(total.get(position));
		view.txtrequire.setText(required.get(position));
		view.txtdesc.setText(desc.get(position));
		
		view.txtsubject.setPaintFlags(view.txtsubject.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
		view.txtachived.setPaintFlags(view.txtachived.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

		if(keepetachived[position]==null || keepetachived[position].isEmpty())
		{
			view.txtachived.setText("0");
		}
		else
		{
			view.txtachived.setText(keepetachived[position]);
		}
		
		view.txtsubject.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String subjectid=view.txtId.getText().toString();
				Context context=v.getContext();
				
				Intent intent=new Intent(context,ChecklistTab.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("subjectid", subjectid);
				
				context.startActivity(intent);
			}
		});
		
		view.txtachived.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(editableachive.equalsIgnoreCase("true"))
				{
					view.txtachived.setVisibility(View.GONE);
					view.etachived.setVisibility(View.VISIBLE);
				}
			}
		});
		(view.etachived).setOnEditorActionListener(
			    new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	//Log.d("LEE",view.etachived.getText().toString());
			        	keepetachived[position]=view.etachived.getText().toString();
			        	mDbHelper.subjectachive(view.etachived.getText().toString().replace("'", "''"), id.get(position),assessor,trainee);
			        	view.txtachived.setVisibility(View.VISIBLE);
						view.etachived.setVisibility(View.GONE);
			        	return true; // consume.
			        } 
			        
			    }
			    return false; // pass on to other listeners. 
			}
			});

		if(!editableachive.equalsIgnoreCase("true"))
		{
			view.etachived.setEnabled(false);
			view.etachived.setFocusable(false);
		}
		else
		{
			view.etachived.setEnabled(true);
			view.etachived.setFocusable(true);
		}
		
		return convertView;
	};
	
	public static class DataBucket
    {
        private String someData;

        public String getSomeData()
        {
            return someData;
        }

        public void setSomeData(String someData)
        {
            this.someData = someData;
        }
    }
	
}
