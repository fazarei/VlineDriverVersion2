package com.gvm.vlinedriver;

import java.io.IOException;
import java.util.ArrayList;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.gvm.vlinedriver.R;
import com.netdimen.core.Security;

public class Loginact extends Activity {

	//private Menu mymenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_loginact);
		//final TestAdapter MDBHelper=new TestAdapter(this);
		final EditText etuserid=(EditText) findViewById(R.id.editText1);
		final EditText etpassword=(EditText) findViewById(R.id.editText2);
		
		etuserid.setMaxLines(1);
		etpassword.setMaxLines(1);
		
		Button blogin=(Button) findViewById(R.id.button1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		blogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				TestAdapter MDBHelper = new TestAdapter(Loginact.this);	
				final String myuserid = etuserid.getText().toString().replace("'", "''"); 
				String mypassword=etpassword.getText().toString().replace("'", "''");
				if(mypassword.equalsIgnoreCase("") || myuserid.equalsIgnoreCase("") || mypassword.length()<2)
				{
					Toast.makeText(Loginact.this, "Please insert your username and password.", Toast.LENGTH_SHORT).show();
				}
				else
				{
				//Check for Temporary Password
				String checkfortemp=mypassword.substring(mypassword.length()-2,mypassword.length());
				if(checkfortemp.equalsIgnoreCase("04") || checkfortemp.equalsIgnoreCase("70") || checkfortemp.equalsIgnoreCase("74"))
				{
					mypassword=mypassword.substring(0, mypassword.length()-2);
				}
				String ekppass=Security.encrypt(mypassword);
				//First of all here we check do we have database or no

				MDBHelper.createDatabase();       
				MDBHelper.open();
				//if there wasn't trainee role we will create it
				Cursor ctraineexist = MDBHelper.traineexist();
				int rowcountraineexist=ctraineexist.getCount();
				//Trainee existed in system so do login

				//finish
				if(rowcountraineexist>0)
				{
					login(myuserid,ekppass);
				}
				//Create trainee then login
				else
				{
					String serverurl="";
					Cursor cconfig = MDBHelper.getconfig();
					if(cconfig.getCount()>0)
					{
						cconfig.moveToFirst();
						serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
					}
					final HttpClient httpclient = new DefaultHttpClient();
					final HttpPost httppost = new HttpPost(serverurl+"/vlineappadmin/getinfo.php");

					//This is the data to send					
					try {
						// Adding the trainee userId to local database
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						nameValuePairs.add(new BasicNameValuePair("username", myuserid));
						nameValuePairs.add(new BasicNameValuePair("pass", mypassword));
						nameValuePairs.add(new BasicNameValuePair("mode", "trainee"));
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						// Execute HTTP Post Request
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String response = httpclient.execute(httppost, responseHandler);
						//This is the response from a API
						String dataString = response;
						if(dataString.equalsIgnoreCase("No Record Found"))
						{
							Toast.makeText(Loginact.this, "Username and/or password incorrect.", Toast.LENGTH_LONG).show();
						}
						else
						{      
							String[] userinfo = dataString.split(",");
							MDBHelper.insertuser(userinfo[0], userinfo[2], "trainee", userinfo[3], userinfo[1]);
							login(myuserid,ekppass);
						}

						} catch (ClientProtocolException e) {
						Toast.makeText(Loginact.this, R.string.cantconnect, Toast.LENGTH_LONG).show();
						// TODO Auto-generated catch block
						} catch (IOException e) {
							Log.d("LEE",e.toString());
						Toast.makeText(Loginact.this, R.string.cantconnect, Toast.LENGTH_LONG).show();
						// TODO Auto-generated catch block
						}
				}
			}
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_loginact, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		new MyMenuevent(item.toString(),Loginact.this);
		return false;
	}
	
	//Menu mymenu=new onOptionsItemSelected(MenuItem item,Lo);
	/* public boolean onOptionsItemSelected(MenuItem item)
	 {
	        switch (item.getItemId())
	        {
	        case R.id.item1:
	            // Single menu item is selected do something
	            // Ex: launching new activity/screen or show alert message
	            Toast.makeText(Loginact.this, "Back to main", Toast.LENGTH_SHORT).show();
	            return true;
	 
	        case R.id.item2:
	            Toast.makeText(Loginact.this, "Sync", Toast.LENGTH_SHORT).show();
	            return true;
	 
	        case R.id.item3:
	            Toast.makeText(Loginact.this, "Log out", Toast.LENGTH_SHORT).show();
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	 } */  
	public void login(String username,String password)
	{
		TestAdapter MDBHelper = new TestAdapter(Loginact.this);	
		MDBHelper.open();
		
		Cursor ctraineexist = MDBHelper.traineexist();
		int rowcountraineexist=ctraineexist.getCount();

		//Trainee existed in system so do login
		if(rowcountraineexist>0)
		{
				Cursor c = MDBHelper.getuser(username, password);
				int rowcount=c.getCount();
				if(rowcount>0)
				{	c.moveToFirst();
					//Update login field to true for this user and false for another rows because in same time one person can login
					MDBHelper.loginfalse();
					MDBHelper.logintrue(username, password);
					//Invisible fullname and role rows
					TableRow tblerow1=(TableRow) findViewById(R.id.tableRow3);
			    	TableRow tblerow2=(TableRow) findViewById(R.id.tableRow4);
			    	tblerow1.setVisibility(View.GONE);
			    	tblerow2.setVisibility(View.GONE);
			    	//Start next activity
					Intent intent = new Intent (Loginact.this,Stagelist.class);
					startActivity(intent);
				}
				else
				{
					EditText etfullname=(EditText) findViewById(R.id.editText3);
					String fullname=etfullname.getText().toString().replace("'", "''");
					//if fullname is empty show message
					if(fullname.equalsIgnoreCase(""))
					{
						createDialogtemppass();
					}
					else
					{
						//Here we encrypt password (username+"sh@nf@rz115ht1") after encrypt we should get the password that user inserted
						String passbeforencrypt=username+"shanfarz115ht1";
						String passafterencrypt=Security.encrypt(passbeforencrypt);
						//Temporary password
						EditText etpasstmp=(EditText) findViewById(R.id.editText2);
						//last 2 string are fore role of user
						String passtmp=etpasstmp.getText().toString().substring(0, etpasstmp.getText().toString().length()- 2);
						String userrole= etpasstmp.getText().toString().substring(etpasstmp.getText().toString().length()- 2 , etpasstmp.getText().toString().length());
						
						
						//String passtmp = etpasstmp.getText().toString();
						//If encrypt password is equal to temporary password then do login
						if(passafterencrypt.equalsIgnoreCase(passtmp))
						{
							//assessor
							if(userrole.equalsIgnoreCase("04"))
							{
								//We will save password after encrypt because next time user login system will encript it again
								MDBHelper.insertuser(username, password, "assessor", "", fullname);
								login(username,password);
							}
							//trainer
							if(userrole.equalsIgnoreCase("70"))
							{
								MDBHelper.insertuser(username, password, "trainer", "", fullname);
								login(username,password);
							}
							//trainer and assessor
							if(userrole.equalsIgnoreCase("74"))
							{
								MDBHelper.insertuser(username, password, "assessor", "", fullname);
								MDBHelper.insertuser(username, password, "trainer", "", fullname);
								login(username,password);
							}
						}
						else
						{
							Toast.makeText(Loginact.this, "Username and/or password incorrect.", Toast.LENGTH_SHORT).show();
							TableRow tblerow1=(TableRow) findViewById(R.id.tableRow3);
					    	TableRow tblerow2=(TableRow) findViewById(R.id.tableRow4);
					    	tblerow1.setVisibility(View.GONE);
					    	tblerow2.setVisibility(View.GONE);
						}
						etfullname.setText("");
					}
					//Toast.makeText(Loginact.this, "Username and Password is not correct.", Toast.LENGTH_SHORT).show();
				}
		}
		else
		{
			Toast.makeText(Loginact.this, "Trainee should login first time after reset system.", Toast.LENGTH_SHORT).show();
		}
	}
	private void createDialogtemppass() {

	    AlertDialog.Builder alertDlg = new AlertDialog.Builder(Loginact.this);

	    alertDlg.setMessage("Your username and password are not in the system. If you would like to create a temporary password please insert your full name and role.");

	    alertDlg.setCancelable(false); 

	    alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {    

	      public void onClick(DialogInterface dialog, int id) 
	      {
	    	  TableRow tblerow1=(TableRow) findViewById(R.id.tableRow3);
	    	  TableRow tblerow2=(TableRow) findViewById(R.id.tableRow4);
	    	  tblerow1.setVisibility(View.VISIBLE);
	    	  tblerow2.setVisibility(View.VISIBLE);
	    	  //Write code here
	      }     
	    });
	     alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
	      // We do nothing
	    }

	    });
	     alertDlg.create().show();
	}

	public void login_backup(String username,String password)
	{
		TestAdapter MDBHelper=new TestAdapter(this);
		SQLiteDatabase db=openOrCreateDatabase("vlinedatabase", MODE_PRIVATE, null);
		String ekppass=Security.encrypt(password);
		Cursor c=db.rawQuery("SELECT * FROM user where username='"+username+"' and password='"+ekppass+"'",null);
		int rowcount=c.getCount();
		//If we have username ans password in our database
		if(rowcount>0)
		{	c.moveToFirst();
			//Update login field to true for this user and false another rows because in same time one person can login
			MDBHelper.loginfalse();
			MDBHelper.logintrue(username, ekppass);
			Intent intent = new Intent (Loginact.this,Stagelist.class);
			startActivity(intent);
		}
		//If we don't have username and password in our database, that means he is trainer or assessor. so we delete all trainer and assessor and get them again from php URL
		else
		{
			String serverurl="";
			Cursor cconfig = MDBHelper.getconfig();
			if(cconfig.getCount()>0)
			{
				cconfig.moveToFirst();
				serverurl=cconfig.getString(cconfig.getColumnIndex("serverurl"));
			}
			//If username and password doesn't exist we will call php url to update our user table
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpPost httppost = new HttpPost(serverurl + "/vlineappadmin/getinfo.php");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("mode", "trainerassessor"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String response = httpclient.execute(httppost, responseHandler);
				
				String dataString = response;
				if(dataString.equalsIgnoreCase(""))
				{
					Toast.makeText(Loginact.this, "There is no trainer and assessor.", Toast.LENGTH_LONG).show();
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
					 //after insert do login 
					 String ekppassnewtrainer=Security.encrypt(password);
						Cursor cnewtrainer=db.rawQuery("SELECT * FROM user where username='"+username+"' and password='"+ekppassnewtrainer+"'",null);
						int rowcountnewtrainer=cnewtrainer.getCount();
						//If we have username and password in our database
						if(rowcountnewtrainer>0)
						{	cnewtrainer.moveToFirst();
							Log.d("LEE","do update");
							MDBHelper.loginfalse();
							MDBHelper.logintrue(username, ekppass);
							Intent intent = new Intent (Loginact.this,Stagelist.class);
							startActivity(intent);
						}
						else
						{
							Toast.makeText(Loginact.this, "Username and/or password incorrect.", Toast.LENGTH_LONG).show();
						}
				}

				} catch (ClientProtocolException e) {
				Toast.makeText(Loginact.this, R.string.cantconnect, Toast.LENGTH_LONG).show();
				// TODO Auto-generated catch block
				} catch (IOException e) {
				Toast.makeText(Loginact.this, R.string.cantconnect, Toast.LENGTH_LONG).show();
				// TODO Auto-generated catch block
				}
		}
		db.close();
	}
	
	public void onBackPressed() {
		
	}
}
