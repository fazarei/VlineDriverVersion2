package com.gvm.vlinedriver;

  import java.io.File;
  import java.io.FileWriter;
  import java.io.IOException;

import com.gvm.vlinedriver.R;

  import android.app.Activity;
import android.content.Context;
  import android.database.Cursor;
  import android.database.SQLException;
  import android.database.sqlite.SQLiteDatabase;
  import android.os.AsyncTask;
  import android.os.Bundle;
  import android.os.Environment;
  import android.util.Log;
import android.widget.Toast;
  public class Uploadfile extends Activity
  {

SQLiteDatabase myDatabase=null;
Cursor c1,c2;

@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_uploadfile);

    try
    {
        myDatabase=openOrCreateDatabase("vlinedatabase", MODE_PRIVATE, null);
        try
        {
            //new ExportDatabaseCSVTask().execute("");
        	ExportDatabaseCSVTask exportdatabase=new ExportDatabaseCSVTask();
        	exportdatabase.doInBackground("assessmentdetail","assessmentdetail.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("timelost","timelost.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("subject","subject.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("assessment","assessment.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("checklist","checklist.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("task","task.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("taskdesc","taskdesc.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("taskcoridordesc","taskcoridordesc.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("competency","competency.csv","vlinedatabase.db");
        	exportdatabase.doInBackground("checklisttask","checklisttask.csv","vlinedatabase.db");
        }
        catch(Exception ex)
        {
            Log.e("Error in MainActivity",ex.toString());
        }

    }
    catch(SQLException ex)
    {
        ex.printStackTrace();
    }
}
public void csvupload(Context context)
{

	try
    {
        TestAdapter myDatabase = new TestAdapter(context);		
        myDatabase.open();
        try
        {
        	ExportDatabaseCSVTask exportdatabase=new ExportDatabaseCSVTask();
        	exportdatabase.doInBackground("assessmentdetail","assessmentdetail.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("timelost","timelost.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("subject","subject.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("assessment","assessment.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("checklist","checklist.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("task","task.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("taskdesc","taskdesc.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("taskcoridordesc","taskcoridordesc.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("competency","competency.csv","vlinedatabase.db",context);
        	exportdatabase.doInBackground("checklisttask","checklisttask.csv","vlinedatabase.db",context);
        }
        catch(Exception ex)
        {
            Log.e("Error in MainActivity",ex.toString());
        }

    }
    catch(SQLException ex)
    {
        ex.printStackTrace();
    }
}
public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>

{
    //private final ProgressDialog dialog = new ProgressDialog(Uploadfile.this);

    protected void onPreExecute()
    {
        //this.dialog.setMessage("Exporting database...");

        //this.dialog.show();
    }

    protected Boolean doInBackground(String tablename,String csvfilename,String databasename,Context context)
    {
       // File dbFile=getDatabasePath(databasename);
    	
        //System.out.println(dbFile);  // displays the data base path in your logcat 

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, csvfilename);
        try
        {
        	TestAdapter myDatabase = new TestAdapter(context);    		
            myDatabase.open();
            Cursor curCSV = myDatabase.getTestData(tablename);
            
            file.createNewFile();

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            //csvWrite.writeNext(curCSV.getColumnNames());
            if(curCSV.moveToFirst())
            {
            	do
            	{
            		int colmnnum=curCSV.getColumnCount();
                	
                	String[] arrStr=new String[colmnnum];
                	
                	for(int i=0; i<colmnnum; i++)
                	{
                		if(curCSV.getString(i)==null)
                		{
                			arrStr[i]=curCSV.getString(i);
                		}
                		else
                		{
    	            		arrStr[i] =curCSV.getString(i).replace(",", ";");
                		}
                	}
            		csvWrite.writeNext(arrStr);
            	}
            	while(curCSV.moveToNext());
            }

            csvWrite.close();
            curCSV.close();
            return true;
        }

        catch(SQLException sqlEx)

        {
            return false;
        }

        catch (IOException e)
        {
            return false;
        }

    }

    protected void onPostExecute(final Boolean success)
    {
      /* if (this.dialog.isShowing())
        {
            this.dialog.dismiss();
        }*/

        if (success)
        {
            Toast.makeText(Uploadfile.this, "Export successful!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Uploadfile.this, "Export failed", Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
}
