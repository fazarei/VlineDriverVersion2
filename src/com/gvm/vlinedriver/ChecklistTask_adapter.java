package com.gvm.vlinedriver;

import java.util.ArrayList;

import com.gvm.vlinedriver.R;

import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ChecklistTask_adapter extends BaseAdapter{
	
	private TestAdapter mDbHelper;
	private ArrayList<String> id;
	private ArrayList<String> checknum;
	private ArrayList<String> groupname;
	private ArrayList<String> name;
	private ArrayList<String> taskgroupvisible;
	public Activity activity;
	public boolean[] chbstateinstructed;
	public boolean[] chbstategna;
	public boolean[] chbstategd;
	public boolean[] chbstatege;
	public boolean[] chbstateunna;
	public boolean[] chbstateund;
	public boolean[] chbstateune;
	public String editableachive;
	private String trainee="";
	private String assessor="";
	private String trainer="";
	private String trainerguide="";
	private String trainerunguide="";
	private String trainerinstruction="";
	private String role="";
	
	private String inssig="";
	private String guidesig="";
	private String unguidesig="";
	
	public ChecklistTask_adapter(Activity activity,ArrayList<String> id,ArrayList<String> checknum
			,ArrayList<String> groupname,ArrayList<String> name,
			 boolean[] chbstateinstructed,boolean[] chbstategna,boolean[] chbstategd,boolean[] chbstatege
			,boolean[] chbstateunna,boolean[] chbstateund,boolean[] chbstateune,String editableachive,String trainee,String assessor
			,String trainer,String taskguide,String role,String trainerguide,String trainerunguide,String trainerinstruction,
			ArrayList<String> taskgroupvisible,String inssig,String guidesig,String unguidesig)
	{
		super();
		mDbHelper = new TestAdapter(activity);
		this.id=id;
		this.checknum=checknum;
		this.groupname=groupname;
		this.name=name;
		this.activity=activity;
		this.chbstateinstructed=chbstateinstructed;
		this.chbstategna=chbstategna;
		this.chbstategd=chbstategd;
		this.chbstatege=chbstatege;
		this.chbstateunna=chbstateunna;
		this.chbstateund=chbstateund;
		this.chbstateune=chbstateune;
		this.editableachive=editableachive;
		this.trainee=trainee;
		this.assessor=assessor;
		this.trainer=trainer;
		this.role=role;
		this.trainerguide=trainerguide;
		this.trainerunguide=trainerunguide;
		this.trainerinstruction=trainerinstruction;
		this.taskgroupvisible=taskgroupvisible;
		
		this.inssig=inssig;
		this.guidesig=guidesig;
		this.unguidesig=unguidesig;
	}

	public int getCount() {
		return id.size();
	}

	public Object getItem(int position) {
		return id.get(position);
		//return position;
	}

	public long getItemId(int position) {
		return 0;
	}
	
	public static class ViewHolder
	{
		public TextView txtId;
		public TextView txtchecknum;
		public TextView txtgroup;
		public TextView txtname;
		public TextView tvtaskgrop;
		public CheckBox chbinstructed;
		public CheckBox chbgna;
		public CheckBox chbgd;
		public CheckBox chbge;
		public CheckBox chbungna;
		public CheckBox chbungd;
		public CheckBox chbunge;
	}

	public View getView(final int position, View converView, ViewGroup parent) 
	{
		final ViewHolder view;
		LayoutInflater inflator=activity.getLayoutInflater();
		if(converView==null)
		{
			view=new ViewHolder();
			converView=inflator.inflate(R.layout.checklisttask_row, null);
			
			view.txtId=(TextView) converView.findViewById(R.id.textView3);
			view.txtchecknum=(TextView) converView.findViewById(R.id.textView4);
			view.txtgroup=(TextView) converView.findViewById(R.id.textView1);
			view.txtname=(TextView) converView.findViewById(R.id.textView2);
			view.chbinstructed=(CheckBox) converView.findViewById(R.id.checkBox1);
			view.chbgna=(CheckBox) converView.findViewById(R.id.checkBox2);
			view.chbgd=(CheckBox) converView.findViewById(R.id.checkBox3);
			view.chbge=(CheckBox) converView.findViewById(R.id.checkBox4);
			view.chbungna=(CheckBox) converView.findViewById(R.id.checkBox5);
			view.chbungd=(CheckBox) converView.findViewById(R.id.checkBox6);
			view.chbunge=(CheckBox) converView.findViewById(R.id.checkBox7);
			view.tvtaskgrop=(TextView) converView.findViewById(R.id.tvtaskgroup);
			
			converView.setTag(view);
		}
		else
		{
			view=(ViewHolder) converView.getTag();
		}
		
		view.txtId.setText(id.get(position));
		view.txtchecknum.setText(checknum.get(position));
		//view.txtgroup.setText(groupname.get(position));
		view.txtname.setText(name.get(position));
		view.tvtaskgrop.setText(groupname.get(position));
		if(taskgroupvisible.get(position).equalsIgnoreCase("GONE"))
		{
			view.tvtaskgrop.setVisibility(View.GONE);
		}
		else
		{
			view.tvtaskgrop.setVisibility(View.VISIBLE);
		}
		//view.tvtaskgrop.setVisibility(View.(taskgroupvisible.get(position)));
		//Instructed
		view.chbinstructed.setOnCheckedChangeListener(null);
        if(chbstateinstructed[position])
        {
        	view.chbinstructed.setChecked(true);
        	//Make active guide part	
        	instructionactive(view,position);
        }
        else  
        {
        	view.chbinstructed.setChecked(false);
     
        	//Make disactive guide part	
        	instructiondisactive(view);
        }
        
        view.chbinstructed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if(isChecked){
            	 chbstateinstructed[position]=true; 
            	 
            	 //Make active guide part if trainer signed guide part
            	 instructionactive(view,position);
            	 
            	 mDbHelper.checklisttask(activity,checknum.get(position),"instructed", "true",id.get(position),trainee,assessor,
            			 trainer,"trainerinstruction");
             }
             else{
            	 chbstateinstructed[position]=false;
            	
            	 //Make disactive guide part and unguide part
            	 view.chbgna.setEnabled(false);
         		 view.chbgd.setEnabled(false);
         		 view.chbge.setEnabled(false);
         		 
         		view.chbungna.setEnabled(false);
        		view.chbungd.setEnabled(false);
        		view.chbunge.setEnabled(false);
            	 
            	 mDbHelper.checklisttask(activity,checknum.get(position),"instructed", "false",id.get(position),trainee,
            			 assessor,trainer,"trainerinstruction");
             }
            }
            });
        //g N/A

        view.chbgna.setOnCheckedChangeListener(null);
              if(chbstategna[position])
              {
              	view.chbgna.setChecked(true);
              	
              	//Make active unguide part	
            	guideactive(view,position);
              }
              else  
              {
              	view.chbgna.setChecked(false);
              	
            	//Make disactive unguide part	
            	guidedisactive(view,position);
              }
              
              view.chbgna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {     
                   if(isChecked){
                  	 chbstategna[position]=true;
                  	 
                  	//Make active guide part	
                  	 guideactive(view,position);
                 	
	                  	//When we checked Guide N/A we should uncheck GD and GE
	                  	 chbstategd[position]=false;
	                  	 view.chbgd.setChecked(false);
	                  	 chbstatege[position]=false;
	                  	 view.chbge.setChecked(false);
	                  	mDbHelper.checklisttask(activity,checknum.get(position),"gna", "true",id.get(position),trainee,assessor,
	               			 trainer,"trainerguide");
                   }
                   else{
                	 chbstategna[position]=false;
                	 
                	//Make disactive unguide part
                	 if(!(chbstategna[position] || chbstategd[position] || chbstatege[position]))
                	 {
	                	view.chbungna.setEnabled(false);
	             		view.chbungd.setEnabled(false);
	             		view.chbunge.setEnabled(false);
                	 }
                 	
                	 mDbHelper.checklisttask(activity,checknum.get(position),"gna", "false",id.get(position),trainee,assessor,
	               			 trainer,"trainerguide");
                   }
                  }
                  });
        //gd
      		view.chbgd.setOnCheckedChangeListener(null);
              if(chbstategd[position])
              {
              	view.chbgd.setChecked(true);
	             //Make active unguide part	
              		guideactive(view,position);
              }
              else 
              {
              	view.chbgd.setChecked(false);
              
              	//Make unactive unguide part	
          		guidedisactive(view,position);
              }
              
              view.chbgd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {   
	                   if(isChecked){
	                	 chbstategd[position]=true;
	                	 
	                	//Make active unguide part	
	                 	guideactive(view,position);
	                 	
	                	//When we checked Guide D we should uncheck G N/A and GE
	                  	 chbstategna[position]=false;
	                  	 view.chbgna.setChecked(false);
	                  	 chbstatege[position]=false;
	                  	 view.chbge.setChecked(false);
	                  	mDbHelper.checklisttask(activity,checknum.get(position),"gd", "true",id.get(position),trainee,assessor,
		               			 trainer,"trainerguide");                	
	                   }
	                   else{
	                	 chbstategd[position]=false;
	                	 
	                	//Make disactive unguide part	
	                	 if(!(chbstategna[position] || chbstategd[position] || chbstatege[position]))
	                	 {
		                	view.chbungna.setEnabled(false);
		             		view.chbungd.setEnabled(false);
		             		view.chbunge.setEnabled(false);
	                	 }
	              		
	                  	 mDbHelper.checklisttask(activity,checknum.get(position),"gd", "false",id.get(position),trainee,assessor,
		               			 trainer,"trainerguide");
	                   }
                  }
                  }); 
        //ge
      		view.chbge.setOnCheckedChangeListener(null);
              if(chbstatege[position])
              {
              	view.chbge.setChecked(true);
              	
              	//Make active unguide part	
          		guideactive(view,position);
              }
              else 
              {
              	view.chbge.setChecked(false);
              	
              	//Make disactive unguide part	
          		guidedisactive(view,position);
              }
              
              view.chbge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {   
                   if(isChecked){
                	   chbstatege[position]=true; 
                	   
                	 //Make active unguide part	
                 		guideactive(view,position);
                	   
                	 //When we checked Guide E we should uncheck N/A and GD
	                  	 chbstategd[position]=false;
	                  	 view.chbgd.setChecked(false);
	                  	 chbstategna[position]=false;
	                  	 view.chbgna.setChecked(false);                 	 
                  	 mDbHelper.checklisttask(activity,checknum.get(position),"ge", "true",id.get(position),trainee,assessor,
	               			 trainer,"trainerguide");
                   }
                   else{
                	   chbstatege[position]=false;
                	   
                	 //Make disactive unguide part	
                	   if(!(chbstategna[position] || chbstategd[position] || chbstatege[position]))
                  	 {
  	                	view.chbungna.setEnabled(false);
  	             		view.chbungd.setEnabled(false);
  	             		view.chbunge.setEnabled(false);
                  	 }
	              		
                	   mDbHelper.checklisttask(activity,checknum.get(position),"ge", "false",id.get(position),trainee,assessor,
  	               			 trainer,"trainerguide");
                   }
                  }
                  });
        //unna
      		view.chbungna.setOnCheckedChangeListener(null);
              if(chbstateunna[position])
              	view.chbungna.setChecked(true);
              else  
              	view.chbungna.setChecked(false);
              
              view.chbungna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {   
                   if(isChecked){
                	 chbstateunna[position]=true; 
                	//When we checked UnGuide N/A we should uncheck GD and GE
                  	 chbstateund[position]=false;
                  	 view.chbungd.setChecked(false);
                  	 chbstateune[position]=false;
                  	 view.chbunge.setChecked(false);
                  	mDbHelper.checklisttask(activity,checknum.get(position),"unna", "true",id.get(position),trainee,assessor,
	               			 trainer,"trainerunguide");
                   }
                   else{
                	   chbstateunna[position]=false;
                	   mDbHelper.checklisttask(activity,checknum.get(position),"unna", "false",id.get(position),trainee,assessor,
  	               			 trainer,"trainerunguide");
                   }
                  }
                  });
        //und
      		view.chbungd.setOnCheckedChangeListener(null);
              if(chbstateund[position])
              	view.chbungd.setChecked(true);
              else  
              	view.chbungd.setChecked(false);
              
              view.chbungd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {     
                   if(isChecked){
                	   chbstateund[position]=true; 
                	 //When we checked UnGuide D we should uncheck GN/A and GE
                    	 chbstateunna[position]=false;
                    	 view.chbungna.setChecked(false);
                    	 chbstateune[position]=false;
                    	 view.chbunge.setChecked(false);
                    	 mDbHelper.checklisttask(activity,checknum.get(position),"und", "true",id.get(position),trainee,assessor,
    	               			 trainer,"trainerunguide");
                   }
                   else{
                	   chbstateund[position]=false;
                	   mDbHelper.checklisttask(activity,checknum.get(position),"und", "false",id.get(position),trainee,assessor,
  	               			 trainer,"trainerunguide");
                   }
                  }
                  });
        //une
      		view.chbunge.setOnCheckedChangeListener(null);
              if(chbstateune[position])
              	view.chbunge.setChecked(true);
              else  
              	view.chbunge.setChecked(false);
              
              view.chbunge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {   
                   if(isChecked){
                	   chbstateune[position]=true; 
                	 //When we checked UnGuide E we should uncheck GN/A and GD
                    	 chbstateund[position]=false;
                    	 view.chbungd.setChecked(false);
                    	 chbstateunna[position]=false;
                    	 view.chbungna.setChecked(false);
                    	 mDbHelper.checklisttask(activity,checknum.get(position),"une", "true",id.get(position),trainee,assessor,
    	               			 trainer,"trainerunguide");
                   }
                   else{
                	   chbstateune[position]=false;
                	   mDbHelper.checklisttask(activity,checknum.get(position),"une", "false",id.get(position),trainee,assessor,
  	               			 trainer,"trainerunguide");
                   }
                  }
                  });
		return converView;
	}
	//Guided practice box needs to be locked out until instruction complete and unguided box locked out until guided box complete.
    
	public void instructionactive(ViewHolder view,int position)
	{
		if(role.equalsIgnoreCase("assessor"))
    	{
        	if(inssig.equalsIgnoreCase("true"))
        		view.chbinstructed.setEnabled(true);
        	
        	//Make active guide part if trainer signed guide part
        	if(guidesig.equalsIgnoreCase("true"))
        	{
        		view.chbgna.setEnabled(true);
    			view.chbgd.setEnabled(true);
    			view.chbge.setEnabled(true);
        	}
        	
        	if(guidesig.equalsIgnoreCase("true") && unguidesig.equalsIgnoreCase("true") && (chbstategna[position] || chbstategd[position] || chbstatege[position]))
        	{
        		view.chbungna.setEnabled(true);
    			view.chbungd.setEnabled(true);
    			view.chbunge.setEnabled(true);
        	}
    	}
    	if(role.equalsIgnoreCase("trainer"))
        {
        	  if((trainerinstruction==null) ||(trainerinstruction.isEmpty()) || (trainer.equalsIgnoreCase(trainerinstruction)))
				{	            	  
	          	  view.chbinstructed.setEnabled(true);
			    }
              
        	  if((trainerguide==null) ||(trainerguide.isEmpty()) || (trainer.equalsIgnoreCase(trainerguide)))
				{
        		  if(guidesig.equalsIgnoreCase("true"))
  	        		{
	        		  	view.chbgna.setEnabled(true);
		    			view.chbgd.setEnabled(true);
		    			view.chbge.setEnabled(true);
  	        		}
				}
        	  
        	  if((trainerunguide==null) ||(trainerunguide.isEmpty()) || (trainer.equalsIgnoreCase(trainerunguide)))
				{
	      		  if(unguidesig.equalsIgnoreCase("true")  && (chbstategna[position] || chbstategd[position] || chbstatege[position]))
		        		{
		        		  	view.chbungna.setEnabled(true);
			    			view.chbungd.setEnabled(true);
			    			view.chbunge.setEnabled(true);
		        		}
				}
		}
	}
	public void instructiondisactive(ViewHolder view)
	{
		if(role.equalsIgnoreCase("assessor"))
    	{
        	if(inssig.equalsIgnoreCase("true"))
        		view.chbinstructed.setEnabled(true);
    	}
    	if(role.equalsIgnoreCase("trainer"))
        {
        	  if((trainerinstruction==null) ||(trainerinstruction.isEmpty()) || (trainer.equalsIgnoreCase(trainerinstruction)))
				{	            	  
	          	  view.chbinstructed.setEnabled(true);
			    }
        }
		view.chbgna.setEnabled(false);
		view.chbgd.setEnabled(false);
		view.chbge.setEnabled(false);
		
		view.chbungna.setEnabled(false);
		view.chbungd.setEnabled(false);
		view.chbunge.setEnabled(false);
	}
	
	public void guideactive(ViewHolder view,int position)
	{
		if(role.equalsIgnoreCase("assessor"))
    	{
        	if(guidesig.equalsIgnoreCase("true") && chbstateinstructed[position])
        	{
        		view.chbgna.setEnabled(true);
    			view.chbgd.setEnabled(true);
    			view.chbge.setEnabled(true);
        	}
        	
        	//Make active unguide part if trainer signed guide part
        	if(unguidesig.equalsIgnoreCase("true") && chbstateinstructed[position])
        	{
        		view.chbungna.setEnabled(true);
    			view.chbungd.setEnabled(true);
    			view.chbunge.setEnabled(true);
        	}
    	}
    	if(role.equalsIgnoreCase("trainer") && chbstateinstructed[position])
        {
        	  if((trainerguide==null) ||(trainerguide.isEmpty()) || (trainer.equalsIgnoreCase(trainerguide)))
				{	            	  
        		  	view.chbgna.setEnabled(true);
      				view.chbgd.setEnabled(true);
      				view.chbge.setEnabled(true);
			    }
              
        	  if((trainerunguide==null) ||(trainerunguide.isEmpty()) || (trainer.equalsIgnoreCase(trainerunguide)))
				{
        		  if(guidesig.equalsIgnoreCase("true"))
  	        		{
	        		  	view.chbungna.setEnabled(true);
		    			view.chbungd.setEnabled(true);
		    			view.chbunge.setEnabled(true);
  	        		}
				}
		}
	}
	public void guidedisactive(ViewHolder view,int position)
	{
		if(role.equalsIgnoreCase("assessor") && chbstateinstructed[position])
    	{
        	if(guidesig.equalsIgnoreCase("true"))
        	{
        		view.chbgna.setEnabled(true);
        		view.chbgd.setEnabled(true);
        		view.chbge.setEnabled(true);
        	}
    	}
    	if(role.equalsIgnoreCase("trainer") && chbstateinstructed[position])
        {
        	  if((trainerguide==null) ||(trainerguide.isEmpty()) || (trainer.equalsIgnoreCase(trainerguide)))
				{	            	  
        		  	view.chbgna.setEnabled(true);
	        			view.chbgd.setEnabled(true);
	        			view.chbge.setEnabled(true);
			    }
        }
    	
    	if(!(chbstategna[position] || chbstategd[position] || chbstatege[position]))
		   	 {
		       		view.chbungna.setEnabled(false);
		    		view.chbungd.setEnabled(false);
		    		view.chbunge.setEnabled(false);
		   	 }
	}

}
