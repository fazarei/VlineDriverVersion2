package com.gvm.vlinedriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.gvm.vlinedriver.R;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.Toast;

public class Stagelist extends Activity {
	
	private Stagelist_adapter madapter;
	private ArrayList<String> id;
	private ArrayList<String> name;
	private ArrayList<String> desc;
	private ArrayList<String> checklistproportion;
	private ArrayList<String> assessment;
	private GridView gridview;
	Bitmap myBitmap;
	private String traineeUserId;
	private String newVersion="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_stagelist);
		paperList();

		madapter=new Stagelist_adapter(this, id,name,desc,checklistproportion,assessment);
		gridview=(GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(madapter);
		
		//Here we have to check, Does data load before or no?
		TestAdapter mdbhelper=new TestAdapter(this);
		mdbhelper.open();
		Cursor cVerssionNumber=mdbhelper.getVersionNumber();
		cVerssionNumber.moveToFirst();

		if(cVerssionNumber.getCount()==0)
		{
			Log.d("LEE","Noersion number");
			getTrainee();
			/*There is a posibility that we want to upgarde app and trainee  has to install new app but he already has some data 
			in backend. In this situation we don't get data from core table */
			dialogLoadAllData();
		}
		else
		{
			//We shouldn't check update version every time we come to stage. Just one time
			String checkVersion=cVerssionNumber.getString(cVerssionNumber.getColumnIndex("checkVersion"));
			if(!checkVersion.equalsIgnoreCase("true"))
			{
				//Check connectivity
				if (isNetworkAvailable(Stagelist.this)) 
				{
					getTrainee();
					getNewVersion(Stagelist.this);
					if(!newVersion.equalsIgnoreCase(""))
					{
						dialogUpgrade();
					}
					mdbhelper.updateCheckedVersionAlready("true");
				}
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		//Allow only assessor access to "Reset to Factory" item.
		TestAdapter mdbhelper=new TestAdapter(this);
		mdbhelper.open();
		Cursor cassessor=mdbhelper.assessoruserid();
		if(cassessor.getCount()>0)
		{
			menu.getItem(1).getSubMenu().getItem(3).setVisible(true);
			menu.getItem(1).getSubMenu().getItem(4).setVisible(true);
		}
		if(!newVersion.equalsIgnoreCase(""))
		{	
			menu.getItem(4).setVisible(true);
			menu.getItem(4).setTitle(Html.fromHtml("<font color='#ff3824'>New Version: "+newVersion+"</font>"));
		}
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		new MyMenuevent(item.toString(),this);
		
		switch (item.getItemId()) {
			 case R.id.itemscreenshot:
			 //final GridView iv = (GridView) findViewById(R.id.gridView1);
			 View v1 = getWindow().getDecorView().getRootView();
			 // View v1 = iv.getRootView(); //even this works
			 // View v1 = findViewById(android.R.id.content); //this works too
			 // but gives only content
			 v1.setDrawingCacheEnabled(true);
			 myBitmap = v1.getDrawingCache();
			 saveBitmap(myBitmap);
			 case R.id.item8:
				 dialogUpgrade();
			 //return true;
			 
			 //default:
			 //return super.onOptionsItemSelected(item);
		 }

		return false;
	}
	public void paperList()
	{
		TestAdapter mdbhelper=new TestAdapter(this);
		id=new ArrayList<String>();
		name=new ArrayList<String>();
		desc=new ArrayList<String>();
		checklistproportion=new ArrayList<String>();
		assessment=new ArrayList<String>();
		String proportion="";
		String assessmentproportion="";
		int checklisttaskcomplete=0;
		mdbhelper.open();
		//SQLiteDatabase db=openOrCreateDatabase("vlinedatabase", MODE_PRIVATE, null);
		Cursor c=mdbhelper.stagelist();
		//Cursor c=db.rawQuery("SELECT * FROM stage",null);
		if(c.getCount()>0)
		{
			c.moveToFirst();
			do
			{
				id.add(c.getString(c.getColumnIndex("Id")));
				name.add(c.getString(c.getColumnIndex("name")));
				desc.add(c.getString(c.getColumnIndex("desc")));
				
				//Count all checklist for each stage and count how many of them is completed
				/*For counting checklist is complete or no? we should check all checklist tasks of each checklist if all 
				of them is complete then means checklist is complete but even one of them is not complete then 
				checklist is not complete 
				How we undrestand one task is complete or not? there should be one checked on unguided part*/
				//Cursor callchecklist=db.rawQuery("select checklist.checknum from checklist inner join subjectchecklist on checklist.checknum=subjectchecklist .checknum join subject on subject.Id=subjectchecklist.subjectId join assessment on subject.assessmentId=assessment.Id join stage on assessment.stageId=stage.Id where stage.Id='"+c.getString(c.getColumnIndex("Id"))+"' GROUP BY checklist.checknum", null);
				
				//We should count checklistindex
				Cursor callchecklist=mdbhelper.getallchecklistindex(c.getString(c.getColumnIndex("Id")));
				checklisttaskcomplete=0;
					if(callchecklist.moveToFirst())
					{
						do
						{
							//For each checklist we count all checklisttask and all checklistTaskcompleted So if number of them are same and number of checklist task is not 0 means checklist is completed otherwise is not completed
							/*Cursor calltask=mdbhelper.getallchecklisttask(callchecklist.getString(callchecklist.getColumnIndex("checknum")));
							Cursor ccomptask=mdbhelper.getchecklisttaskcmp(callchecklist.getString(callchecklist.getColumnIndex("checknum")));
							int alltask=calltask.getCount();
							int comptask=ccomptask.getCount();
							
								if((alltask==comptask) && alltask!=0)*/
							//For checking checklist is complete, we just check trainer and trainee signed the checklisttask or not
								if(callchecklist.getString(callchecklist.getColumnIndex("tasktrainersig"))!=null && callchecklist.getString(callchecklist.getColumnIndex("tasktrainersig")).equalsIgnoreCase("true") && callchecklist.getString(callchecklist.getColumnIndex("tasktraineesig"))!=null && callchecklist.getString(callchecklist.getColumnIndex("tasktraineesig")).equalsIgnoreCase("true"))
								{
									checklisttaskcomplete++;
								}
						}
						while(callchecklist.moveToNext());
					}
				
				int allchecklist=callchecklist.getCount();
				if(allchecklist==0)
				{
					proportion ="";
				}
				else
				{
					proportion ="Checklist ("+checklisttaskcomplete+"/"+allchecklist+" )";
				}
				checklistproportion.add(proportion);
				
				//count assessment completed
				Cursor callassessment=mdbhelper.getallsessment(c.getString(c.getColumnIndex("Id")));
				Cursor cassessmentcmp=mdbhelper.getassessmentcmp(c.getString(c.getColumnIndex("Id")));
				if(callassessment.getCount()==0)
				{
					assessmentproportion="";
				}
				else
				{
					assessmentproportion="Assessment ("+cassessmentcmp.getCount() +"/"+callassessment.getCount() +")";
				}
				assessment.add(assessmentproportion.toString());
			}
			while(c.moveToNext());
		}

	}
	
	public static class GenerateCsv {
	    public static FileWriter generateCsvFile(File sFileName,String fileContent) {
	        FileWriter writer = null;

	        try {
	            writer = new FileWriter(sFileName);
	            writer.append(fileContent);
	                         writer.flush();

	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }finally
	        {
	            try {
	                writer.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        return writer;
	    }
	}
	
	public void onBackPressed() {
		
	}
	
	
	
	public void saveBitmap(Bitmap bitmap) {		
		 String filePath = Environment.getExternalStorageDirectory()
		 + File.separator + "Pictures/screenshot.png";
		 File imagePath = new File(filePath);
		 FileOutputStream fos;
		 try {
		 fos = new FileOutputStream(imagePath);
		 bitmap.compress(CompressFormat.PNG, 100, fos);
		 fos.flush();
		 fos.close();
		 sendMail(filePath);
		 } catch (FileNotFoundException e) {
		 Log.e("GREC", e.getMessage(), e);
		 } catch (IOException e) {
		 Log.e("GREC", e.getMessage(), e);
		 }
		 }
		 
		 public void sendMail(String path)
		 {
			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
			String today=sdf.format(cal.getTime());
			
			 Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			 emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "" });
			 emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Vline App-Main Page-Screenshot-"+today);
			 emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"");
			 
			 //emailIntent.setType("image/png");

		 
		 	ArrayList<Uri> items = new ArrayList<Uri>();
	        Uri uri = Uri.fromFile(getDatabasePath("/data/data/com.gvm.vlinedriver/databases/vlinedatabase"));
	        //items.add(uri);
	        
	        Uri myUri = Uri.parse("file://" + path);
	        items.add(myUri);
	        
	        String filePath2 = Environment.getExternalStorageDirectory()
	       		 + File.separator + "vlinedatabase";
	        Uri myUri2 = Uri.parse("file://" + filePath2);
	        items.add(myUri2);
	        
	        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, items);
	        emailIntent.putExtra(Intent.EXTRA_STREAM, items); 
	        emailIntent.setType("application/octet-stream");

	        startActivity(Intent.createChooser(emailIntent,"Send mail..."));
		
		 }

		 public void getTrainee()
		 {
			 TestAdapter MDBHelper = new TestAdapter(Stagelist.this);	
			 	MDBHelper.open();
			     Cursor cursorTrainee = MDBHelper.getTrainee();
			     if(cursorTrainee.getCount()>0)
			     {
			    	 cursorTrainee.moveToFirst();
			    	 traineeUserId=cursorTrainee.getString(cursorTrainee.getColumnIndex("username"));
			     }
		 }
		 private void dialogLoadAllData() {
					//TODO If we already have data for that trainee in server, we get data from main tables not core data
				    AlertDialog.Builder alertDlg = new AlertDialog.Builder(Stagelist.this);
	
				    alertDlg.setMessage("It is the first you login in app. So, you need to load data first. Please press OK and wait untill all data to be loaded.");
	
				    alertDlg.setCancelable(false); 
	
				    alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {    
	
				      public void onClick(DialogInterface dialog, int id) 
				      {
				    	  ProgressDialog progress = new ProgressDialog(Stagelist.this);
				    	  progress.setTitle("Loading");
				    	  progress.setMessage("Wait while loading...");
				    	  progress.show();
				    	  
				    	  //Get all trainer and assessor
				    	  trainerassessor(Stagelist.this);
				    	  //Here to check is there any data before in portal or no
				    	  Log.d("LEE","Check user already sync");
				    	  String userAlreadySynchedData = userAlreadySynchedData(Stagelist.this);
				    	  Log.d("LEE",userAlreadySynchedData);
				    	  if(userAlreadySynchedData.equalsIgnoreCase("True"))
				    	  {
				    		  //Get all data from main table
				    		  Log.d("LEE","Check user already sync return True");
				    		  //These five table already sync and we only insert their data
				    		  loadAssessmentSyncData(Stagelist.this);
				    		  loadChecklistSync(Stagelist.this);
				    		  loadCompetencySync(Stagelist.this);			
				    		  loadSubjectSync(Stagelist.this);
				    		  
				    		  //Always same, so we insert it from core table 
				    		  loadStageData(Stagelist.this);
				    		  loadChecklistIndexData(Stagelist.this);
				    		  
				    		  //Assessment Detail and assessment timelost need to insert 6 row for each assessment in default 
				    		  //then update them from real data in server
				    		  //competency task desk is same
				    		   TestAdapter mdbhelper=new TestAdapter(Stagelist.this);
							   mdbhelper.open();
							   mdbhelper.assessmentdetailAndTimeDefault();
							   mdbhelper.competencyTaskDescDefault();
							   mdbhelper.close();
							   
							   //Delete and Insert
							   loadAssessmentDetailSync(Stagelist.this);
							   loadTimeLostSync(Stagelist.this);
							   loadCompetencyTaskDescSync(Stagelist.this);
							   
							   //For these table we have to insert core data. Then, we deleted the synced data and insert them again
							   //First get the core data
							   loadSubjectChecklistData(Stagelist.this);
							   loadSubjectChecklistSync(Stagelist.this);
						       loadCompetencyTaskData(Stagelist.this);
							   loadCompetencyTaskSync(Stagelist.this);
							   
							   //Checklist task has 6000 record. so, we keep them in database and then we delete and insert the sync data
							   loadChecklistTaskSync(Stagelist.this);
				    		  
							   progress.dismiss();
							   
							   getNewVersion(Stagelist.this);
							   Log.d("LEE","version number:"+newVersion);
							   if(!newVersion.equalsIgnoreCase(""))
								{
									dialogUpgrade();
								}
							   //Insert a record into update version and update app_trainee_track in server
					    	  //insertVersionNumber(Stagelist.this);
				    	  }
				    	  else
				    	  {
					    	  loadStageData(Stagelist.this);
					    	  loadAssessmentData(Stagelist.this);
					    	  loadSubjectData(Stagelist.this);
					    	  loadSubjectChecklistData(Stagelist.this);
					    	  loadChecklistData(Stagelist.this);
					    	  loadChecklistTaskData(Stagelist.this);
					    	  loadChecklistIndexData(Stagelist.this);
					    	  loadCompetencyData(Stagelist.this);
					    	  loadCompetencyTaskData(Stagelist.this);
					    	  //Insert a record into update version and update app_trainee_track in server
					    	  insertVersionNumber(Stagelist.this);
					    	  
					    	//To dismiss the dialog
					    	  progress.dismiss();
					    	  
					    	  //Log out
					    	  logout(Stagelist.this);
				    	  }
				      }     
				    });
				    alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	
				    public void onClick(DialogInterface dialog, int which) {	
				    	//TODO Logout
				    	logout(Stagelist.this);
				    }
	
				    });
				     alertDlg.create().show();
			}

	 private void dialogUpgrade() {
				//TODO If we already have data for that trainee in server, we get data from main tables not core data
			    AlertDialog.Builder alertDlg = new AlertDialog.Builder(Stagelist.this);

			    alertDlg.setMessage("You need to upgrade your App. Please press OK and wait untill upgrade will be completed.");

			    alertDlg.setCancelable(false); 

			    alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {    

			      public void onClick(DialogInterface dialog, int id) 
			      {
			    	  ProgressDialog progress = new ProgressDialog(Stagelist.this);
			    	  progress.setTitle("Loading");
			    	  progress.setMessage("Wait while loading...");
			    	  progress.show();
			    	  
			    	  //Upgrade here
						upgradeStage(Stagelist.this);
						upgradeAssessment(Stagelist.this);
						upgradeSubject(Stagelist.this);
						upgradeSubjectChecklist(Stagelist.this);
						upgradeChecklist(Stagelist.this);
						upgradeChecklistIndex(Stagelist.this);
						upgradeCompetency(Stagelist.this);
						upgradeCompetencyTask(Stagelist.this);
						upgradeChecklistTask(Stagelist.this);
						
						//Change version number in app and portal
						TestAdapter mdbhelper=new TestAdapter(Stagelist.this);
						mdbhelper.open();
						mdbhelper.updateVersionNumber(newVersion);
						updateTraineeVersionAfterUpgrade(Stagelist.this);
			    	  
			    	  //To dismiss the dialog
			    	  progress.dismiss();
			    	  
			    	  //Log out
			    	  logout(Stagelist.this);
			      }     
			    });
			    alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {	
			    	//TODO Logout
			    	//logout(Stagelist.this);
			    }

			    });
			     alertDlg.create().show();
		}
		 
public void trainerassessor(Context context)
		 {
		 	String Serverurl="";
		 	TestAdapter MDBHelper = new TestAdapter(context);	
		 	MDBHelper.open();

		     //Get URL from config table
		     Cursor cconfig = MDBHelper.getconfig();
		     if(cconfig.getCount()>0)
		     {
		     	cconfig.moveToFirst();
		     	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
		     }
		 	final HttpClient httpclient = new DefaultHttpClient();
		 	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
		 	try {
		 		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		 		nameValuePairs.add(new BasicNameValuePair("mode", "trainerassessor"));

		 		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		 		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		 		String response = httpclient.execute(httppost, responseHandler);
		 		
		 		String dataString = response;
		 		if(dataString.equalsIgnoreCase(""))
		 		{
		 			Toast.makeText(context, "Trainer and assessor data not available.", Toast.LENGTH_LONG).show();
		 		}
		 		else
		 		{
		 			//delete all trainer and assessor
		 			MDBHelper.deletetrainerassessor();
		 			
		 			//Insert all trainer and assessor
		 			String[] userinfo = dataString.split("\r\n");
		 			 for (int i = 0, n = userinfo.length; i < n; i++)
		 			 {
		 				 String[] trainerassessorinfo=userinfo[i].split(",");
		 				 MDBHelper.insertuser(trainerassessorinfo[0], trainerassessorinfo[2], trainerassessorinfo[3], "", trainerassessorinfo[1]);	 
		 			 }
		 		}

		 		} catch (ClientProtocolException e) {
		 		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		 		// TODO Auto-generated catch block
		 		} catch (IOException e) {
		 		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		 		// TODO Auto-generated catch block
		 		}
		 }

public void loadStageData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadStage"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoretStage(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}		 

public void loadAssessmentData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadAssessment"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreAssessment(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadSubjectData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadSubject"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreSubject(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadSubjectChecklistData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadSubjectChecklist"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreSubjectChecklist(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}
public void loadChecklistData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadChecklist"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreChecklist(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadChecklistTaskData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadChecklistTask"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreChecklistTask(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadChecklistIndexData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadChecklistIndex"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreChecklistIndex(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadCompetencyData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadCompetency"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreCompetency(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadCompetencyTaskData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadCompetencyTask"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCoreCompetencyTask(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void insertVersionNumber(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateTraineeVersion"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Version Number"))
		{
			Toast.makeText(context, "There is no version number.", Toast.LENGTH_LONG).show();
		}
		else
		{
			MDBHelper.insertVersionNumber(response);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}
public void logout(Context context)
{
	TestAdapter MDBHelper = new TestAdapter(context);
	MDBHelper.loginfalse();
	Intent intent = new Intent(context,Loginact.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
	context.startActivity(intent);
}

public void getNewVersion(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "getNewVersionNumber"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		Log.d("LEE","Version:" + dataString);
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
				{}
		else	{newVersion=response;}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void updateTraineeVersionAfterUpgrade(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateTraineeVersionAfterUpgrade"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("Upgrade Done"))
				{Log.d("LEE","Upgrade"+response);}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void upgradeStage(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeStage"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			Log.d("LEE",replaceDataString);
			MDBHelper.upgradeStage(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeAssessment(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeAssessment"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeAssessment(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeSubject(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeSubject"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeSubject(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeSubjectChecklist(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeSubjectChecklist"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeSubjectChecklist(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	
public void upgradeCompetency(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeCompetency"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeCompetency(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeCompetencyTask(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeCompetencyTask"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeCompetencyTask(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeChecklist(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeChecklist"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeChecklist(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeChecklistIndex(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeChecklistIndex"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeChecklistIndex(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}	

public void upgradeChecklistTask(Context context)
{
	Log.d("LEE","Task:"+newVersion);
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "upgradeChecklistTask"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));
		nameValuePairs.add(new BasicNameValuePair("version", newVersion));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Log.d("LEE","No data to upgrade");
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.upgradeChecklistTask(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public boolean isNetworkAvailable(final Context context) {
    final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
    return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
}

//To understand is there any data already in server
public String userAlreadySynchedData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "userAlreadySynchedData"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		return dataString;	

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
	return "";
}
	
public void loadAssessmentSyncData(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadAssessmentSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			Log.d("LEE",replaceDataString);
			MDBHelper.loadAssessmentSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadSubjectSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadSubjectSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadSubjectSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadChecklistSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadChecklistSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase(""))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadChecklistSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadCompetencySync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "loadCompetencySync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCompetencySync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void insertVersionNumberSyncBefore(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateTraineeVersionSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Version Number"))
		{
			Toast.makeText(context, "There is no version number.", Toast.LENGTH_LONG).show();
		}
		else
		{
			MDBHelper.insertVersionNumberSyncBefore(response);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadAssessmentDetailSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateAssessmentDetailSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no new data for assessment Detai.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadAssessmentDetailSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadTimeLostSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateTimeLostSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load for assessment time lost.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadTimeLostSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadSubjectChecklistSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateSubjectChecklistSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		Log.d("LEE","dataString:" + dataString);
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load for subject checklist.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			Log.d("LEE",replaceDataString);
			MDBHelper.loadSubjectChecklistSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadChecklistTaskSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateChecklistTaskSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load for checklist task.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadChecklistTaskSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadCompetencyTaskSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateCompetencyTaskSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load for competency task.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCompetencyTaskSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}

public void loadCompetencyTaskDescSync(Context context)
{
	String Serverurl="";
	TestAdapter MDBHelper = new TestAdapter(context);	
	MDBHelper.open();

    Cursor cconfig = MDBHelper.getconfig();
    if(cconfig.getCount()>0)
    {
    	cconfig.moveToFirst();
    	Serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
    }
	final HttpClient httpclient = new DefaultHttpClient();
	final HttpPost httppost = new HttpPost(Serverurl+"/vlineappadmin/getinfo.php");
	try {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("mode", "updateCompetencyTaskDescSync"));
		nameValuePairs.add(new BasicNameValuePair("trainee", traineeUserId));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);
		
		String dataString = response;
		if(dataString.equalsIgnoreCase("") || dataString.equalsIgnoreCase("No Record Found"))
		{
			Toast.makeText(context, "There is no data to load.", Toast.LENGTH_LONG).show();
		}
		else
		{
			String replaceDataString= dataString.replace("'", "''");
			MDBHelper.loadCompetencyTaskDescSync(replaceDataString);
		}

		} catch (ClientProtocolException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(context, R.string.cantconnect, Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
}
}


