package com.gvm.vlinedriver;

import com.gvm.vlinedriver.R;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about);
		
		final TextView txtVersion=(TextView) findViewById(R.id.textView6);
		String versionNumber="V/Line Driver Training Application V: ";
		
		TestAdapter MDBHelper = new TestAdapter(About.this);	
		MDBHelper.open();
		Cursor cursorVersion = MDBHelper.getVersionNumber();
		if(cursorVersion.getCount()>0)
		{
			cursorVersion.moveToFirst();
			versionNumber+=cursorVersion.getString(cursorVersion.getColumnIndex("versionNumber"));
		}	
		txtVersion.setText(versionNumber.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		TestAdapter mdbhelper=new TestAdapter(this);
		mdbhelper.open();
		Cursor cassessor=mdbhelper.assessoruserid();
		if(cassessor.getCount()>0)
		{
			menu.getItem(1).getSubMenu().getItem(3).setVisible(true);
		}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		new MyMenuevent(item.toString(),this);
		return false;
	}

}
