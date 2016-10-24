package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.gvm.vlinedriver.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AssessmentSubject extends Activity {
	
	private AssessmentSubject_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> subject;
	private ArrayList<String> desc;
	private ArrayList<String> total;
	private ArrayList<String> required;
	private int sumtotal;
	private int sumcomp;
	private String assessmentid;
	public String[] etachived;
	private String editableachive="true"; 
	
	private GridView gridview;
	private String trainee="";
	private String assessor="";
	
	final Calendar myCalendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_assessmentsubject);
		
		final TestAdapter MDBHelper = new TestAdapter(AssessmentSubject.this);
		MDBHelper.open();
		assessmentid=getIntent().getStringExtra("assessmentid");
		
		//Fill data in top of the page
		TextView tvtraineename=(TextView) findViewById(R.id.textView6);
		TextView tvempno=(TextView) findViewById(R.id.textView12);
		TextView tvassessorname=(TextView) findViewById(R.id.textView16);
		TextView tvtitle=(TextView) findViewById(R.id.textView5);
		TextView tvtotal=(TextView) findViewById(R.id.textView15);
		TextView tvtotalcomp=(TextView) findViewById(R.id.textView17);
		ImageView ivcalendar=(ImageView) findViewById(R.id.imageView1);
		
		final EditText etlocation=(EditText) findViewById(R.id.editText1);
		final EditText etdate=(EditText) findViewById(R.id.editText2);
		
		Button bsave=(Button) findViewById(R.id.button1);
		//get trainee username
		Cursor ctraineeusername=MDBHelper.traineexist();
		
		if(ctraineeusername.getCount()>0)
		{
			ctraineeusername.moveToFirst();
			trainee=ctraineeusername.getString(ctraineeusername.getColumnIndex("username"));
			tvtraineename.setText(ctraineeusername.getString(ctraineeusername.getColumnIndex("fullname")));
			tvempno.setText(ctraineeusername.getString(ctraineeusername.getColumnIndex("employeeno")));
		}
		//Get assessor username
		Cursor cassessorusername=MDBHelper.assessoruserid();
		if(cassessorusername.getCount()>0)
		{
			cassessorusername.moveToFirst();
			assessor=cassessorusername.getString(cassessorusername.getColumnIndex("username"));
		}
		//Get role of user
		Cursor crole=MDBHelper.userexist();
		if(crole.getCount()>0)
		{
			crole.moveToFirst();
			if(!crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("assessor"))
			{
				bsave.setVisibility(View.INVISIBLE);
				etlocation.setFocusable(false);
				ivcalendar.setVisibility(View.INVISIBLE);
				editableachive="false";
			}
		}
		//Get assessment Record

		Cursor cassessor=MDBHelper.getassessment(assessmentid);
		if(cassessor.getCount()>0)
		{
			cassessor.moveToFirst();
			tvassessorname.setText(cassessor.getString(cassessor.getColumnIndex("fullname")));
			etlocation.setText(cassessor.getString(cassessor.getColumnIndex("location")));
			etdate.setText(cassessor.getString(cassessor.getColumnIndex("date")));
			tvtitle.setText(cassessor.getString(cassessor.getColumnIndex("stagename"))+"   "+cassessor.getString(cassessor.getColumnIndex("name")));
		}
		
		//Fill gridview
		paperList();
		
		madapter=new AssessmentSubject_adapter(this, id, subject, desc, total, required,etachived,editableachive,assessor,trainee);
		gridview=(GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(madapter);

		tvtotal.setText(Integer.toString(sumtotal));
		tvtotalcomp.setText(Integer.toString(sumcomp));

		//To save data
		bsave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Save data in top of the page
				MDBHelper.assessment(AssessmentSubject.this,assessmentid, etlocation.getText().toString().replace("'", "''"), etdate.getText().toString(), trainee, assessor);
				
				//In this code we can save all rows in gridview together
				/*Save data in gridview
				int countrow=gridview.getChildCount();
				Log.d("LEE",""+madapter.getCount());
				for(int i=0; i<countrow ; i++)
				{
					EditText etachieve=(EditText)gridview.getChildAt(i).findViewById(R.id.editText1);
					String achieve=etachieve.getText().toString();
					
					TextView tvid=(TextView) gridview.getChildAt(i).findViewById(R.id.textView1);
					String id=tvid.getText().toString();
					
					//db.execSQL("update subject set achieved='"+achieve+"' where Id='"+id+"';");
				    Log.d("LEE",id);
				}*/
				
			}
		});	
		
		final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
			
			EditText etdate2=(EditText) findViewById(R.id.editText2);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate2);
			}
		};

		ivcalendar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(AssessmentSubject.this, date, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		//We insert 6 rows for assessment detail here
		Cursor cassessmentdetail=MDBHelper.getassessmentdetail(assessmentid);
		if(cassessmentdetail.getCount()>0)
		{}
		else
		{
			MDBHelper.assessmentdetailinsert(assessmentid, "1");
			MDBHelper.assessmentdetailinsert(assessmentid, "2");
			MDBHelper.assessmentdetailinsert(assessmentid, "3");
			MDBHelper.assessmentdetailinsert(assessmentid, "4");
			MDBHelper.assessmentdetailinsert(assessmentid, "5");
			MDBHelper.assessmentdetailinsert(assessmentid, "6");
		}
	}

	private void updateLabel(EditText et) {

	    String myFormat = "dd/MM/yyyy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	    
	    et.setText(sdf.format(myCalendar.getTime()));
	    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_assessmentsubject, menu);
		return true;
	}
	
	public void paperList()
	{

		TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		id=new ArrayList<String>();
		subject=new ArrayList<String>();
		desc=new ArrayList<String>();
		total=new ArrayList<String>();
		required=new ArrayList<String>();
		
		Cursor c=mdbHelper.getallsubject(assessmentid);
		int arraysize=c.getCount();		
		etachived=new String[arraysize];
		int rownumber=0;
		if(c.moveToFirst())
		{
			do
			{
				id.add(c.getString(c.getColumnIndex("Id")));
				subject.add(c.getString(c.getColumnIndex("name")));
				desc.add(c.getString(c.getColumnIndex("objective")));

				etachived[rownumber]=c.getString(c.getColumnIndex("achieved"));

				Cursor ctotal=mdbHelper.getallsubjectchecklist(c.getString(c.getColumnIndex("Id")));
				
				//Critical=required
				//Cursor ccomp=mdbHelper.getallchecklistrequired(c.getString(c.getColumnIndex("Id")));

				int totalcount=ctotal.getCount();
				//int comp=ccomp.getCount();

				sumtotal=sumtotal + totalcount;
				//sumcomp=sumcomp + comp;

				required.add(c.getString(c.getColumnIndex("requirednum")));
				total.add(Integer.toString(totalcount));
				rownumber=rownumber+1;
			}
			while(c.moveToNext());
		}
	}
	

}
