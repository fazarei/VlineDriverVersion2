package com.gvm.vlinedriver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DynamicChecklistPractice extends Activity {
	private String checknum;
	private String checklisttmplist="";
	private String trainee="";
	private String assessor="";	
	private String trainer="";
	final Calendar myCalendar = Calendar.getInstance();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_checklistpractice);
		final TestAdapter mdbHelper=new TestAdapter(this);
		mdbHelper.open();
		
		final TextView tvchecknum=(TextView) findViewById(R.id.textView1);
		final TextView tvgconduct=(TextView) findViewById(R.id.textView16);
		final TextView tvungconduct=(TextView) findViewById(R.id.textView27);
		
		final CheckBox chbgsig=(CheckBox) findViewById(R.id.checkBox1);
		final CheckBox chbungsig=(CheckBox) findViewById(R.id.checkBox2);
		
		final EditText etgdate=(EditText) findViewById(R.id.editText3);
		final EditText etgday=(EditText) findViewById(R.id.editText4);
		final EditText etgweather=(EditText) findViewById(R.id.editText5);
		final EditText etglocotype=(EditText) findViewById(R.id.editText6);
		final EditText etgfrom=(EditText) findViewById(R.id.editText7);
		final EditText etgto=(EditText) findViewById(R.id.editText8);
		final EditText etgtainlength=(EditText) findViewById(R.id.editText9);
		final EditText etgtonnage=(EditText) findViewById(R.id.editText10);
		final EditText etgvehicle=(EditText) findViewById(R.id.editText11);
		
		final EditText etungdate=(EditText) findViewById(R.id.editText12);
		final EditText etungday=(EditText) findViewById(R.id.editText20);
		final EditText etungweather=(EditText) findViewById(R.id.editText13);
		final EditText etunglocotype=(EditText) findViewById(R.id.editText14);
		final EditText etungfrom=(EditText) findViewById(R.id.editText15);
		final EditText etungto=(EditText) findViewById(R.id.editText16);
		final EditText etungtainlength=(EditText) findViewById(R.id.editText17);
		final EditText etungtonnage=(EditText) findViewById(R.id.editText18);
		final EditText etungvehicle=(EditText) findViewById(R.id.editText19);
		
		//Guide
		final TableLayout tblguide=(TableLayout) findViewById(R.id.tblguide);
		final TableRow trguide=(TableRow) findViewById(R.id.tableRow5);
		final TextView tvguide=(TextView) findViewById(R.id.textView14);
		final TextView tvgconductbytrainer=(TextView) findViewById(R.id.textView15);
		final TextView tvgtrainersig=(TextView) findViewById(R.id.textView5);
		final TextView tvgdate=(TextView) findViewById(R.id.textView17);
		final TextView tvgday=(TextView) findViewById(R.id.textView18);
		final TextView tvgweather=(TextView) findViewById(R.id.textView19);
		final TextView tvglocotype=(TextView) findViewById(R.id.textView20);
		final TextView tvgfrom=(TextView) findViewById(R.id.textView21);
		final TextView tvgto=(TextView) findViewById(R.id.textView22);
		final TextView tvgtrain=(TextView) findViewById(R.id.textView23);
		final TextView tvgtonnage=(TextView) findViewById(R.id.textView24);
		final TextView tvgnovehicle=(TextView) findViewById(R.id.textView25);
		 ImageView ivgdate=(ImageView) findViewById(R.id.imageView1);
		
		//UnGuide
		final TableLayout tblunguide=(TableLayout) findViewById(R.id.tbleunguide);
		final TableRow trunguide=(TableRow) findViewById(R.id.tableRow2);
		final TextView tvunguide=(TextView) findViewById(R.id.textView3);
		final TextView tvungconductbytrainer=(TextView) findViewById(R.id.textView26);
		final TextView tvungtrainersig=(TextView) findViewById(R.id.textView4);
		final TextView tvungdate=(TextView) findViewById(R.id.textView28);
		final TextView tvungday=(TextView) findViewById(R.id.textView29);
		final TextView tvungweather=(TextView) findViewById(R.id.textView30);
		final TextView tvunglocotype=(TextView) findViewById(R.id.textView36);
		final TextView tvungfrom=(TextView) findViewById(R.id.textView31);
		final TextView tvungto=(TextView) findViewById(R.id.textView32);
		final TextView tvungtrainlength=(TextView) findViewById(R.id.textView33);
		final TextView tvungtonnage=(TextView) findViewById(R.id.textView34);
		final TextView tvungnovehicle=(TextView) findViewById(R.id.textView35);
		final ImageView ivungdate=(ImageView) findViewById(R.id.imageView2);
		
		Button bsave=(Button) findViewById(R.id.button1);

		checknum=getIntent().getStringExtra("checknum");
		
		//Load data
				Cursor c=mdbHelper.getchecklisttrainer(checknum);
				if(c.moveToFirst())
				{
					tvchecknum.setText(c.getString(c.getColumnIndex("checknum"))+"   "+c.getString(c.getColumnIndex("name")));
					//tvcheckname.setText(c.getString(c.getColumnIndex("name")));
					etgdate.setText(c.getString(c.getColumnIndex("gdate")));
					etgday.setText(c.getString(c.getColumnIndex("gdayornight")));
					etgweather.setText(c.getString(c.getColumnIndex("gweather")));
					etglocotype.setText(c.getString(c.getColumnIndex("glocotype")));
					etgfrom.setText(c.getString(c.getColumnIndex("gfrom")));
					etgto.setText(c.getString(c.getColumnIndex("gto")));
					etgtainlength.setText(c.getString(c.getColumnIndex("gtrainlingth")));
					etgtonnage.setText(c.getString(c.getColumnIndex("gtonnage")));
					etgvehicle.setText(c.getString(c.getColumnIndex("gnovehicle")));
					
					etungdate.setText(c.getString(c.getColumnIndex("ungdate")));
					etungday.setText(c.getString(c.getColumnIndex("ungdayornight")));
					etungweather.setText(c.getString(c.getColumnIndex("ungweather")));
					etunglocotype.setText(c.getString(c.getColumnIndex("unglocotype")));
					etungfrom.setText(c.getString(c.getColumnIndex("ungfrom")));
					etungto.setText(c.getString(c.getColumnIndex("ungto")));
					etungtainlength.setText(c.getString(c.getColumnIndex("ungtrainlength")));
					etungtonnage.setText(c.getString(c.getColumnIndex("ungtonnage")));
					etungvehicle.setText(c.getString(c.getColumnIndex("ungnovehicle")));
					tvgconduct.setText(c.getString(c.getColumnIndex("fullname")));	
					tvungconduct.setText(c.getString(c.getColumnIndex("fullname")));	
					
					String gsigtrue=c.getString(c.getColumnIndex("gtrainersig"));
		 			if(gsigtrue !=null && gsigtrue.equalsIgnoreCase("true"))
		 			{
		 				chbgsig.setChecked(true);
		 			}
					
		 			String ungsigtrue=c.getString(c.getColumnIndex("ungtrainersig"));
					if(ungsigtrue !=null && ungsigtrue.equalsIgnoreCase("true"))
					{
						chbungsig.setChecked(true);
					}
				}
		//Visible and invisible Items in template		
				Cursor curCSV = mdbHelper.checklisttemplate(checknum);
				int colmnnum=curCSV.getCount();
				
				checklisttmplist="";
			   if(colmnnum>0)
			   {
				   curCSV.moveToFirst();
				   for(int i=0; i<curCSV.getCount(); i++)
				   {
					   checklisttmplist	+=	curCSV.getString(curCSV.getColumnIndex("heading"))+"|"+curCSV.getString(curCSV.getColumnIndex("subheading"));
					   curCSV.moveToNext();
				   }
				  
			   }
			 //Guide Practice
			   tmptablerow("GUIDED PRACTICE|GUIDED PRACTICE", trguide);
			   if (checklisttmplist.indexOf("GUIDED PRACTICE|GUIDED PRACTICE") != -1) 
			   {
				   tblguide.setVisibility(View.VISIBLE);	
				   tmptextview("GUIDED PRACTICE|Conducted by Trainer",tvgconductbytrainer);
				   tmptextview("GUIDED PRACTICE|Conducted by Trainer",tvgconduct);
				   tmptextview("GUIDED PRACTICE|trainer signature",tvgtrainersig);
				   tmpcheckbox("GUIDED PRACTICE|trainer signature", chbgsig);
				   tmptextviewedittext("GUIDED PRACTICE|Date",etgdate, tvgdate);
				   tmpimage("GUIDED PRACTICE|Date",ivgdate);
				   tmptextviewedittext("GUIDED PRACTICE|Day or Night",etgday, tvgday);
				   tmptextviewedittext("GUIDED PRACTICE|Weather Conditions (As appropriate)",etgweather, tvgweather);
				   tmptextviewedittext("GUIDED PRACTICE|Loco type/Location (As appropriate)",etglocotype, tvglocotype);
				   tmptextviewedittext("GUIDED PRACTICE|From (As appropriate)",etgfrom, tvgfrom);
				   tmptextviewedittext("GUIDED PRACTICE|To (As appropriate)",etgto, tvgto);
				   tmptextviewedittext("GUIDED PRACTICE|Train length (As appropriate)",etgtainlength, tvgtrain);
				   tmptextviewedittext("GUIDED PRACTICE|Tonnage (As appropriate)",etgtonnage, tvgtonnage);
				   tmptextviewedittext("GUIDED PRACTICE|No. of vehicles (As appropriate)",etgvehicle, tvgnovehicle);
			   } 
			   else 
			   {tblguide.setVisibility(View.GONE);}
			
			   //UNGuide Practice
			   tmptablerow("UNGUIDED PRACTICE|UNGUIDED PRACTICE", trunguide);
			   if (checklisttmplist.indexOf("UNGUIDED PRACTICE|UNGUIDED PRACTICE") != -1) 
			   {
				   tblunguide.setVisibility(View.VISIBLE);	
				   tmptextview("UNGUIDED PRACTICE|Conducted by Trainer",tvungconduct);
				   tmptextview("UNGUIDED PRACTICE|Conducted by Trainer",tvungconductbytrainer);
				   tmptextview("UNGUIDED PRACTICE|Trainer Signature",tvungtrainersig);
				   tmpcheckbox("UNGUIDED PRACTICE|Trainer Signature", chbungsig);
				   tmptextviewedittext("UNGUIDED PRACTICE|Date",etungdate, tvungdate);
				   tmpimage("UNGUIDED PRACTICE|Date",ivungdate);
				   tmptextviewedittext("UNGUIDED PRACTICE|Day or Night",etungday, tvungday);
				   tmptextviewedittext("UNGUIDED PRACTICE|Weather Conditions (As appropriate)",etungweather, tvungweather);
				   tmptextviewedittext("UNGUIDED PRACTICE|Loco type/Location (As appropriate)",etunglocotype, tvunglocotype);
				   tmptextviewedittext("UNGUIDED PRACTICE|From (As appropriate)",etungfrom, tvungfrom);
				   tmptextviewedittext("UNGUIDED PRACTICE|To (As appropriate)",etungto, tvungto);
				   tmptextviewedittext("UNGUIDED PRACTICE|Train length (As appropriate)",etungtainlength, tvungtrainlength);
				   tmptextviewedittext("UNGUIDED PRACTICE|Tonnage (As appropriate)",etungtonnage, tvungtonnage);
				   tmptextviewedittext("UNGUIDED PRACTICE|No. of vehicles (As appropriate)",etungvehicle, tvungnovehicle);
			   } 
			   else 
			   {tblunguide.setVisibility(View.GONE);}
			 //Trip 2
			   if (checklisttmplist.indexOf("TRIP 2 (IF REQUIRED)|TRIP 2 (IF REQUIRED)") != -1) 
			   {
				   tblguide.setVisibility(View.VISIBLE);
				   tmptablerow("TRIP 2 (IF REQUIRED)|TRIP 2 (IF REQUIRED)", trguide);
				   tvguide.setText("TRIP 2 (IF REQUIRED)");
				   tmptextview("TRIP 2 (IF REQUIRED)|Conducted by Trainer",tvgconduct);
				   tmptextview("TRIP 2 (IF REQUIRED)|Conducted by Trainer",tvgconductbytrainer);
				   tmptextview("TRIP 2 (IF REQUIRED)|Trainer Signature",tvgtrainersig);
				   tmpcheckbox("TRIP 2 (IF REQUIRED)|Trainer Signature", chbgsig);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Date",etgdate, tvgdate);
				   tmpimage("TRIP 2 (IF REQUIRED)|Date",ivgdate);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Day or Night",etgday, tvgday);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Weather Conditions",etgweather, tvgweather);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Loco type/Location",etunglocotype, tvunglocotype);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|From",etgfrom, tvgfrom);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|To",etgto, tvgto);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Train length (As appropriate)",etgtainlength, tvgtrain);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|Tonnage (As appropriate)",etgtonnage, tvgtonnage);
				   tmptextviewedittext("TRIP 2 (IF REQUIRED)|No. of vehicles (As appropriate)",etgvehicle, tvgnovehicle);
			   } 
			   
			 //Trip 3
			   if (checklisttmplist.indexOf("TRIP 3 (IF REQUIRED)|TRIP 3 (IF REQUIRED)") != -1) 
			   {	
				   tblunguide.setVisibility(View.VISIBLE);	
				   tmptablerow("TRIP 3 (IF REQUIRED)|TRIP 3 (IF REQUIRED)", trunguide);
				   tvunguide.setText("TRIP 3 (IF REQUIRED)");
				   tmptextview("TRIP 3 (IF REQUIRED)|Conducted by Trainer",tvungconduct);
				   tmptextview("TRIP 3 (IF REQUIRED)|Conducted by Trainer",tvungconductbytrainer);
				   tmptextview("TRIP 3 (IF REQUIRED)|Trainer Signature",tvungtrainersig);
				   tmpcheckbox("TRIP 3 (IF REQUIRED)|Trainer Signature", chbungsig);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Date",etungdate, tvungdate);
				   tmpimage("TRIP 3 (IF REQUIRED)|Date",ivungdate);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Day or Night",etungday, tvungday);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Weather Conditions",etungweather, tvungweather);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Loco type/Location",etunglocotype, tvunglocotype);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|From",etungfrom, tvungfrom);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|To",etungto, tvungto);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Train length (As appropriate)",etungtainlength, tvungtrainlength);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|Tonnage (As appropriate)",etungtonnage, tvungtonnage);
				   tmptextviewedittext("TRIP 3 (IF REQUIRED)|No. of vehicles (As appropriate)",etungvehicle, tvungnovehicle);
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
				//Get trainer username
				Cursor ctrainerusername=mdbHelper.traineruserid();
				if(ctrainerusername.getCount()>0)
				{
					ctrainerusername.moveToFirst();
					trainer=ctrainerusername.getString(ctrainerusername.getColumnIndex("username"));
				}	   
		//Check permission
				Cursor crole=mdbHelper.userexist();
				if(crole.getCount()>0)
				{
					crole.moveToFirst();
					if(crole.getString(crole.getColumnIndex("role")).equalsIgnoreCase("trainee"))
					{
						bsave.setVisibility(View.GONE);
					}
				}
		
		//Save data
		bsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String sqlstring="update checklist set trainee='"+trainee+"'";
				String gtrainersig="false";
				String ungtrainersig="false";
				if(!trainer.equalsIgnoreCase(""))
				{
					sqlstring +=",trainer='"+trainer+"'";
				}
				if(!assessor.equalsIgnoreCase(""))
				{
					sqlstring +=",assessor='"+assessor+"'";
				}
				if(chbgsig.getVisibility()==View.VISIBLE)
				{
					if(chbgsig.isChecked())
					{	gtrainersig="true";	}	
					sqlstring +=",gtrainersig='"+gtrainersig+"'";
				}
				if(etgdate.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gdate='"+etgdate.getText().toString()+"'";
				}
				if(etgday.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gdayornight='"+etgday.getText().toString()+"'";
				}
				if(etgweather.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gweather='"+etgweather.getText().toString()+"'";
				}
				if(etglocotype.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",glocotype='"+etglocotype.getText().toString()+"'";
				}
				if(etgfrom.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gfrom='"+etgfrom.getText().toString()+"'";
				}
				if(etgto.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gto='"+etgto.getText().toString()+"'";
				}
				if(etgtainlength.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gtrainlingth='"+etgtainlength.getText().toString()+"'";
				}
				if(etgtonnage.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gtonnage='"+etgtonnage.getText().toString()+"'";
				}
				if(etgvehicle.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",gnovehicle='"+etgvehicle.getText().toString()+"'";
				}
				if(chbungsig.getVisibility()==View.VISIBLE)
				{
					if(chbungsig.isChecked())
					{	ungtrainersig="true";	}	
					sqlstring +=",ungtrainersig='"+ungtrainersig+"'";
				}
				if(etungdate.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungdate='"+etungdate.getText().toString()+"'";
				}
				if(etungday.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungdayornight='"+etungday.getText().toString()+"'";
				}
				if(etungweather.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungweather='"+etungweather.getText().toString()+"'";
				}	
				if(etunglocotype.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",unglocotype='"+etunglocotype.getText().toString()+"'";
				}	
				if(etungfrom.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungfrom='"+etungfrom.getText().toString()+"'";
				}
				if(etungto.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungto='"+etungto.getText().toString()+"'";
				}
				if(etungtainlength.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungtrainlength='"+etungtainlength.getText().toString()+"'";
				}		 
				if(etungtonnage.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungtonnage='"+etungtonnage.getText().toString()+"'";
				}
				if(etungvehicle.getVisibility()==View.VISIBLE)
				{
					sqlstring +=",ungnovehicle='"+etungvehicle.getText().toString()+"'";
				}
				/*mdbHelper.checklistpractice(gtrainersig, etgdate.getText().toString(), etgday.getText().toString(), etgweather.getText().toString(), etglocotype.getText().toString(),
						etgfrom.getText().toString(), etgto.getText().toString(), etgtainlength.getText().toString(),
						etgtonnage.getText().toString(), etgvehicle.getText().toString(), ungtrainersig, etungdate.getText().toString(), 
						etungday.getText().toString(), etungweather.getText().toString(), etunglocotype.getText().toString()
						, etungfrom.getText().toString(), etungto.getText().toString(), etungtainlength.getText().toString()
						, etungtonnage.getText().toString(), etungvehicle.getText().toString(), checknum);*/
				
				sqlstring +="where checknum='"+checknum+"'";
				//mdbHelper.getquery(DynamicChecklistPractice.this,sqlstring);
				
			}
		});
		
		final DatePickerDialog.OnDateSetListener gdate=new DatePickerDialog.OnDateSetListener() {
			
			Date dt = new Date();
			EditText etdate=(EditText) findViewById(R.id.editText3);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivgdate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(DynamicChecklistPractice.this, gdate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		final DatePickerDialog.OnDateSetListener instructiondate=new DatePickerDialog.OnDateSetListener() {
			
			Date dt = new Date();
			EditText etdate=(EditText) findViewById(R.id.editText12);
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel(etdate);
			}
		};	
		
		ivungdate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(DynamicChecklistPractice.this, instructiondate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		
	}
	
	private void updateLabel(EditText et) {

	    String myFormat = "dd/MM/yyyy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	    
	    et.setText(sdf.format(myCalendar.getTime()));
	    }
	public void tmptablerow(String value,TableRow tr)
	{
		if (checklisttmplist.indexOf(value) != -1) 
		   {	tr.setVisibility(View.VISIBLE);} 
		   else 
		   {tr.setVisibility(View.GONE);}
	}
	public void tmptextviewedittext(String value,EditText et, TextView tv)
	{
		if (checklisttmplist.indexOf(value) != -1) 
		   {	
			et.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
		   } 
		   else 
		   {
			   et.setVisibility(View.GONE);
			   tv.setVisibility(View.GONE);
		   }
	}
	public void tmptextview(String value, TextView tv)
	{
		if (checklisttmplist.indexOf(value) != -1) 
		   {	
			tv.setVisibility(View.VISIBLE);
		   } 
		   else 
		   {
			   tv.setVisibility(View.GONE);
		   }
	}
	public void tmpcheckbox(String value, CheckBox chb)
	{
		if (checklisttmplist.indexOf(value) != -1) 
		   {	
			chb.setVisibility(View.VISIBLE);
		   } 
		   else 
		   {
			   chb.setVisibility(View.GONE);
		   }
	}
	public void tmpimage(String value,ImageView img)
	{
		if (checklisttmplist.indexOf(value) != -1) 
		   {	
			img.setVisibility(View.VISIBLE);
		   } 
		   else 
		   {
			   img.setVisibility(View.GONE);
		   }
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_checklistpractice, menu);
		return true;
	}

}
