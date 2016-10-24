package com.gvm.vlinedriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gvm.vlinedriver.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CreateandBackupDB extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_createandbackupdb);
		
		//To create database just remember remove statement exist database from DatabaseHelper class
				TestAdapter mDbHelper = new TestAdapter(this);         
		    	mDbHelper.createDatabase();       
		    	mDbHelper.open();
		 
		//To get Backup from Database
		 Button btnbackup=(Button) findViewById(R.id.button1);
		 btnbackup.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				try {
		            File f1 = new File("/data/data/com.gvm.vlinedriver/databases/vlinedatabase");
		            if (f1.exists()) {
		            	Log.d("LEE", "Found database");
		                File f2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/vlinedatabase");
		                f2.createNewFile();
		                InputStream in = new FileInputStream(f1);
		                OutputStream out = new FileOutputStream(f2);
		                byte[] buf = new byte[1024];
		                int len;
		                while ((len = in.read(buf)) > 0) {
		                	
		                    out.write(buf, 0, len);
		                }
		                in.close();
		                out.close();
		            }
		        } catch (FileNotFoundException ex) {
		            System.out.println(ex.getMessage() + " in the specified directory.");
		            System.exit(0);
		        } catch (IOException e) {
		            e.printStackTrace();
		            System.out.println(e.getMessage());
		        }
				
			}
		});
	}

}
