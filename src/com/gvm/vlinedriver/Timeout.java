package com.gvm.vlinedriver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;


public class Timeout{
	
	 private Activity activity;
	 
	 public void timeoutactivity(Activity activity)
		{
			this.activity=activity;
		}
	private final int delayTime = 60000*1*45;
	
	private Handler myHandler = new Handler();
	
	public Timeout()
	{
		myHandler.postDelayed(closeControls, delayTime);
	
	}
	
	private Runnable closeControls = new Runnable() {
		
	    public void run() {	 
	    	
	    	Intent intent = new Intent(activity,Loginact.class);
	    	activity.startActivity(intent);
	    }
	};
}
