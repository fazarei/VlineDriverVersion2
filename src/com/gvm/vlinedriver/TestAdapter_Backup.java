package com.gvm.vlinedriver;

import java.io.IOException; 
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvm.vlinedriver.R;

import android.content.Context; 
import android.database.Cursor;
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
import android.util.Log; 
import android.view.Gravity;
import android.widget.Toast;

public class TestAdapter_Backup {
	
	protected static final String TAG = "DataAdapter"; 
	 
    private final Context mContext; 
    private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
 
    public TestAdapter_Backup(Context context)  
    { 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext); 
    } 
 
    public TestAdapter_Backup createDatabase() throws SQLException  
    { 
        try  
        { 
            mDbHelper.createDataBase(); 
        }  
        catch (IOException mIOException)  
        { 
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase"); 
            throw new Error("UnableToCreateDatabase"); 
        } 
        return this; 
    } 
//Here we check role of user again 
    public String checkrole() 
    { 
    	try
        {
            String sql ="select * from user where login='true'";
            
            Cursor mCur = mDb.rawQuery(sql, null);
            Log.d("LEE","hi2");
            if(mCur.getCount()>0)
            {
            	mCur.moveToFirst();
            	return mCur.getString(mCur.getColumnIndex("role"));
            }           
            else
            {
            	return "logout";
            }
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    } 
    public TestAdapter_Backup resetdatabase() throws SQLException  
    { 
        try  
        { 
        	insertauditlog("Reser database", "", "");
            mDbHelper.Resetdatabase(); 
        }  
        catch (IOException mIOException)  
        { 
            Log.e(TAG, mIOException.toString() + "  UnableToResetDatabase"); 
            throw new Error("UnableToReaetDatabase"); 
        } 
        return this; 
    } 
 
    public TestAdapter_Backup open() throws SQLException  
    { 
        try  
        { 
            mDbHelper.openDataBase(); 
            mDbHelper.close(); 
            mDb = mDbHelper.getReadableDatabase(); 
        }  
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "open >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
        return this; 
    } 
    
    public TestAdapter_Backup createDatabase2(String a,String id) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update subject set name='"+a+"' where Id='"+id+"';");
        return this; 
    } 
    
    public TestAdapter_Backup subjectachive(String achive,String id,String assessor,String trainee) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update subject set  achieved='"+achive+"',assessor='"+assessor+"',trainee='"+trainee+"' where Id='"+id+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "subject", sql);
    	}
    	
    	return this; 
    } 
    
    public TestAdapter_Backup checklistexplained(String explained,String checknum,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	
    	mDb.execSQL("update checklist set  explained='"+explained+"',trainee='"+trainee+"',assessor='"+assessor+"'" +
    			"where checknum='"+checknum+"';");
    	}
        
    	return this; 
    } 
    
    public TestAdapter_Backup checkchange(String tbl,String setfeild,String checkvalue,String wherefeild,String id
    			,String trainee,String assessor,String trainer) throws SQLException  
    {
    	
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
    			",trainer='"+trainer+"' where "+wherefeild+"='"+id+"';");
    	
    	return this; 
    }
    public TestAdapter_Backup checkchangeassessor(String tbl,String setfeild,String checkvalue,String wherefeild,String id
			,String trainee,String assessor) throws SQLException  
		{
    	
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
					" where "+wherefeild+"='"+id+"';");
    		
			return this; 
		}
    public TestAdapter_Backup checklisttask(String checknum,String setfeild,String checkvalue,String id
			,String trainee,String assessor,String trainer,String trainerfeild) throws SQLException  
	{
		mDb = mDbHelper.getWritableDatabase();
		mDb.execSQL("update checklist set "+trainerfeild+"='"+trainer+"' where checknum='"+checknum+"'");
		mDb.execSQL("update checklisttask set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
				" where Id='"+id+"';");

		return this; 
	}
    
    public TestAdapter_Backup checkchangetest(String tbl,String setfeild,String checkvalue,String wherefeild,String id
			,String trainee,String assessor,String trainer) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor"))
			{
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainer='"+trainer+"' where "+wherefeild+"='"+id+"';");		
			}
			return this; 
		} 
    
    public TestAdapter_Backup checkchangecompetency(Context context,String tbl,String setfeild,String checkvalue,String wherefeild,String id
			,String trainee,String assessor,String trainer) throws SQLException  
	{
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
		mDb = mDbHelper.getWritableDatabase();
		mDb.execSQL("update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
				",trainer='"+trainer+"' where "+wherefeild+"='"+id+"';");
    	}
		
		return this; 
	} 
    
    public TestAdapter_Backup checklistapprove(Context context,String traineesig,String traineedate,String trainersig,String trainerdate,String checknum,String trainee,String trainer,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
    	{
	    	mDb = mDbHelper.getWritableDatabase();
	    	if(assessor.equalsIgnoreCase(""))
	    	{
	    		mDb.execSQL("update checklist set trainee='"+trainee+"',trainer='"+trainer+"',tasktraineesig='"+traineesig+"',tasktraineedate='"+traineedate+"',tasktrainersig='"+trainersig+"',tasktrainerdate='"+trainerdate+"' where checknum='"+checknum+"';");
	    	}
	    	else
	    	{
	    		mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"',tasktraineesig='"+traineesig+"',tasktraineedate='"+traineedate+"',tasktrainersig='"+trainersig+"',tasktrainerdate='"+trainerdate+"' where checknum='"+checknum+"';");
	    	}
	    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}
		else
		{
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}  
    	return this; 
    } 
    
    public TestAdapter_Backup checklistpractice(Context context,String gtrainersig,String gdate,String gday,String gweather,String gloco,String gfrom,
    		String gto,String glength,String gtonnage,String gnovehicle,String ungtrainersig,String ungdate,String ungday,String ungweather,
    		String ungloco,String ungfrom,String ungto,String unglength,String ungtonnage,String ungnovehicle,String checknum) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update checklist set gtrainersig='"+gtrainersig+"',gdate='"+gdate+"',gdayornight='"+gday+"'" +
    			", gweather='"+gweather+"',glocotype='"+gloco+"',gfrom='"+gfrom+"',gto='"+gto+"', gtrainlingth='"+glength+"'" +
    					", gtonnage='"+gtonnage+"',gnovehicle='"+gnovehicle+"',ungtrainersig='"+ungtrainersig+"',ungdate='"+ungdate+"',ungdayornight='"+ungday+"'" +
    							",ungweather='"+ungweather+"',unglocotype='"+ungloco+"',ungfrom='"+ungfrom+"'" +
    									",ungto='"+ungto+"',ungtrainlength='"+unglength+"', ungtonnage='"+ungtonnage+"'" +
    											",ungnovehicle='"+ungnovehicle+"'where checknum='"+checknum+"';");
        return this; 
    } 
    
    public TestAdapter_Backup checklistinstruction(Context context,String trainersig,String date,String day,String weather,String loco,String from,
    		String to,String length,String tonnage,String novehicle,String checknum,
    		String trainee,String trainer,String assessor,String nameofdepot,String corridorfrom,
    		String corridorto,String up,String down,String locomotiveclass,String shutdownless,String shutdownmore,
    		String instdno,String inscarriageno,String siding) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update checklist set trainee='"+trainee+"',trainer='"+trainer+"',assessor='"+assessor+"', instrainersig='"+trainersig+"',insdate='"+date+"',insdayornight='"+day+"'" +
    			", insweather='"+weather+"',inslocotype='"+loco+"',insfrom='"+from+"', insto='"+to+"', instrainlength='"+length+"'" +
    					", instonnage='"+tonnage+"',insnovehicle='"+novehicle+"',nameofdepot='"+nameofdepot+"'," +
    							"corridorfrom='"+corridorfrom+"',corridorto='"+corridorto+"',up='"+up+"',down='"+down+"'," +
    									"locomotiveclass='"+locomotiveclass+"',shutdownless='"+shutdownless+"',shutdownmore='"+shutdownmore+"',instdno='"+instdno+"'," +
    											"inscarriageno='"+inscarriageno+"',siding='"+siding+"' where checknum='"+checknum+"';");
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
		}
		else
		{
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}  
    	return this; 
    }
    public TestAdapter_Backup getquery(Context context,String sqlstr)
    {
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL(sqlstr);
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
		}
		else
		{
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}  
    	return this;
    }
    public TestAdapter_Backup subjectcomment(Context context,String comment,String id,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update subject set comment='"+comment+"',trainee='"+trainee+"',assessor='"+assessor+"' where Id='"+id+"';");
	    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}  
    	return this; 
    } 
    public TestAdapter_Backup assessmentcomment(Context context,String comment,String result,String assessorsig,String traineesig,String assessordate,String traineedate,String assessorname,String trainee,String id) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update  assessment set comment='"+comment+"',result='"+result+"',assessorsig='"+assessorsig+"',traineesig='"+traineesig+"'" +
    			",assessordate='"+assessordate+"',traineedate='"+traineedate+"',assessor='"+assessorname+"',trainee='"+trainee+"' where Id='"+id+"';");
    	
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}  
    	return this; 
    } 
    public TestAdapter_Backup timelost(Context context,String assessmentid,String tripno,String minutelost,String signals
    		,String passengerdelay,String permanentway,String trackwork,String trainfault
    		,String other,String explanation,String assessor,String trainee) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update timelost set minutelost='"+minutelost+"',signals='"+signals+"',passengerdelay='"+passengerdelay+"'," +
    			"permanentway='"+permanentway+"',trackwork='"+trackwork+"',trainfault='"+trainfault+"',other='"+other+"'," +
    					"explanation='"+explanation+"',assessor='"+assessor+"',trainee='"+trainee+"' where tripno='"+tripno+"' and  assessmentId='"+assessmentid+"';");
    	
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}  
    	return this; 
    } 
    
    public TestAdapter_Backup assessmenttime(Context context,String id,String timeoverschedule,String timelost) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update assessment set timeoverschedule='"+timeoverschedule+"',timelost='"+timelost+"' where Id='"+id+"';");
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}    	
    	return this; 
    } 
    
    public TestAdapter_Backup assessmentdetail(Context context,String assessmentId,String tripno,String time,String location,String destination
    		,String tdNo,String mputype,String mpuno,String weather,String assessor,String trainee) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update assessmentdetail set time='"+time+"',location='"+location+"',destination='"+destination+"'," +
    			"tdNo='"+tdNo+"',mputype='"+mputype+"',mpuno='"+mpuno+"',weather='"+weather+"',assessor='"+assessor+"',trainee='"+trainee+"'" +
    					" where assessmentId='"+assessmentId+"' and tripno='"+tripno+"';");
	    	
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}    	
        return this; 
    } 
    
    public TestAdapter_Backup insertuser(String username,String password,String role,String employeeno,String fullname) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("insert into user (username,password,role,employeeno,fullname) values ('"+username+"','"+password+"','"+role+"','"+employeeno+"'" +
    			",'"+fullname+"');");
        return this; 
    }
    public TestAdapter_Backup loginfalse() throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update user set login='false';");
        return this; 
    }
    public TestAdapter_Backup logintrue(String username,String password) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update user set login='true' where username='"+username+"' and password='"+password+"';";
    	mDb.execSQL(sql);
    	insertauditlog("login", "user", sql);
        return this; 
    }
    public TestAdapter_Backup deletetrainerassessor() throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("delete from user where role='trainer' or role='assessor';");
        return this; 
    }
    public Cursor getTestData(String tablename)
    {
        try
        {
            String sql ="select * from "+ tablename +" where trainee<>''";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor userexist()
    {
        try
        {
            String sql ="select * from user where login='true'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public Cursor getuser(String username, String password)
    {
        try
        {
            String sql ="select * from user where username='"+username+"' and password='"+password+"'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
   
    public Cursor traineexist()
    {
        try
        {
            String sql ="select * from user where role='trainee'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor assessoruserid()
    {
        try
        {
            String sql ="select * from user where role='assessor' and login='true'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public Cursor traineruserid()
    {
        try
        {
            String sql ="select * from user where role='trainer' and login='true'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getalltrainer()
    {
        try
        {
            String sql ="select * from user where role='trainer'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getassessment(String Id)
    {
        try
        {
            String sql ="select assessment.*,stage.name as stagename,user.fullname,user.employeeno as assessoremployeeno from assessment join stage on assessment.stageId=stage.Id left join user on assessment.assessor=user.username where assessment.Id='"+Id+"'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getchecklisttrainer(String checknum)
    {
        try
        {
            String sql ="SELECT user.fullname as trainerfullname,userg.fullname as guidetrainerfullname," +
            		"userung.fullname as unguidetrainerfullname,userins.fullname as trainerinsfullname," +
            		"checklist.* FROM  checklist " +
            		"left join user on checklist.trainer=user.username " +
            		"left join user as userg on checklist.trainerguide=userg.username " +
            		"left join user as userung on checklist.trainerunguide=userung.username " +
            		"left join user as userins on checklist.trainerinstruction=userins.username  " +
            		"where checknum ='"+checknum+"'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
               mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor checklisttemplate(String checknum)
    {
    	try
    	{
    		String sql="select * from checklisttemplate where Checknum='"+checknum+"'";
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor depotvalue(String checknum,String heading,String subheading)
    {
    	try
    	{
    		String sql="select checklisttemplate.value,checklist.nameofdepot  from checklisttemplate left join checklist on checklisttemplate.Checknum=checklist.checknum where checklisttemplate.Checknum='"+checknum+"' and heading='"+heading+"' and subheading='"+subheading+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor stagelist()
    {
    	try
    	{
    		String sql="SELECT * FROM stage";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //To get all assessment completed for each stage
    public Cursor getassessmentcmp(String stageid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessment where stageId='"+stageid+"' and result='true'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //To get all assessment for each stage
    public Cursor getallsessment(String stageid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessment where stageId='"+stageid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //To get all checklist for each stage from checklistindex
    public Cursor getallchecklistindex(String stageid)
    {
    	try
    	{
    		String sql="select checklist.Id,checklist.checknum,checklist.name,checklist.rpl from checklistindex inner join checklist on checklistindex.checknum=checklist.checknum where checklistindex.stageid='"+stageid+"' GROUP BY checklistindex.checknum";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
  //To get all checklist task for each checklist 
    public Cursor getallchecklisttask(String checknum)
    {
    	try
    	{
    		String sql="SELECT * FROM checklisttask where checknum='"+checknum+"'";
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
  //To get all checklist task completed for each checklist 
    public Cursor getchecklisttaskcmp(String checknum)
    {
    	try
    	{
    		String sql="SELECT * FROM checklisttask where checknum='"+checknum+"' and (unna='true' or und='true' or une='true')";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
  //To get all subject for each assessment 
    public Cursor getallsubject(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT * FROM subject where assessmentId='"+assessmentid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
  //To get all checklist for each subject 
    public Cursor getallsubjectchecklist(String subjectid)
    {
    	try
    	{
    		String sql="SELECT checklist.*,subjectchecklist.subjectId FROM subjectchecklist join checklist on checklist.checknum=subjectchecklist.checknum where subjectId='"+subjectid+"'group by subjectchecklist.checknum";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //To get all checklist required for each subject 
    public Cursor getallchecklistrequired(String subjectid)
    {
    	try
    	{
    		String sql="SELECT subjectchecklist.id FROM subjectchecklist join  checklist on  subjectchecklist.checknum=checklist.checknum where subjectId='"+subjectid+"' and critical='C' GROUP BY subjectchecklist.id";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor getassessmentdetail(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessmentdetail where assessmentId='"+assessmentid+"' order by tripno";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor getassessmenttimelost(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT * FROM timelost where assessmentId='"+assessmentid+"' order by tripno";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor countassessmenttimelost(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT sum(minutelost) as minlost,sum(signals) as signal,sum(passengerdelay) as passengerdelay,sum(permanentway) as permanent,sum(trackwork) as trackwork,sum(trainfault) as trainfault,sum(other) as other FROM timelost where assessmentId='"+assessmentid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor getassessmentcomment(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessment where Id='"+assessmentid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor getsubject(String subjectid)
    {
    	try
    	{
    		String sql="SELECT * FROM subject where Id='"+subjectid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public TestAdapter_Backup assessment(Context context,String id,String location,String date,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
	    	mDb = mDbHelper.getWritableDatabase();
	    	mDb.execSQL("update  assessment set location='"+location+"',date='"+date+"',trainee='"+trainee+"',assessor='"+assessor+"' where Id='"+id+"';");
	    	//Show message
	    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	    	toast.show();
    	}
    	else
    	{
	    	Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	    	toast.show();
    	}
    	return this; 
    } 
    
    public TestAdapter_Backup assessmenttimelostinsert(String assessmentid,String tripno) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("insert into timelost (assessmentId,tripno) values ('"+assessmentid+"','"+tripno+"');");
        return this; 
    } 
    
    public TestAdapter_Backup assessmentdetailinsert(String assessmentid,String tripno) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("insert into assessmentdetail (assessmentId,tripno) values ('"+assessmentid+"','"+tripno+"');");
        return this; 
    } 
    
    public Cursor getconfig()
    {
    	try
    	{
    		String sql="SELECT * FROM config";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    
    public TestAdapter_Backup updateconfig(Context context,String feildname,String feildvalue) throws SQLException  
    { 
    	
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update config set "+feildname+" ='"+feildvalue+"' ");
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
    	
        return this; 
    } 
    
    public Cursor getcompetencytask(String competencyid)
    {
    	try
    	{
    		String sql="SELECT * FROM competencytask where competencyId='"+competencyid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
   
    public Cursor getcompetencyid(String competencyid)
    {
    	try
    	{
    		String sql="SELECT * FROM competency where Id='"+competencyid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //Get all competency
    public Cursor getcompetency()
    {
    	try
    	{
    		String sql="SELECT * FROM competency";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public Cursor getuserrole()
    {
    	try
    	{
    		String sql="select * from user where login='true'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //Get competency task desc
    public Cursor getcompetencytaskdesk(String competencyid)
    {
    	try
    	{
    		String sql="SELECT * FROM competencytaskdesc where competencyId='"+competencyid+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public TestAdapter_Backup insertcompetencytaskdesc(String competencyid,String tablenumber) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	
    	mDb.execSQL("Insert into competencytaskdesc (competencyId,tablenumber) Values('"+competencyid+"','"+tablenumber+"');");
        return this; 
    }
    
    public TestAdapter_Backup updatecompetencytaskdesc(Context context,String motivepower,String task,String date,String mpu,
    		String dayornight,String tdno,String origin,String destination
    		,String assessor,String trainee,String competencyid,String tablenumber) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("Update competencytaskdesc set motivepower='"+motivepower+"',task='"+task+"'," +
    			"date='"+date+"',mpu='"+mpu+"',dayornight='"+dayornight+"'," +
    					"tdno='"+tdno+"',origin='"+origin+"',destination='"+destination+"',assessor='"+assessor+"'" +
    							",trainee='"+trainee+"' " +
    			"where competencyid='"+competencyid+"' and tablenumber='"+tablenumber+"'");
    	//Show message
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    	toast.show();
    	}
    	else
    	 {
    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	 }
        return this; 
    }
    
    public TestAdapter_Backup assessment10date(Context context,String assessmentId,String date) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update assessment set date='"+date+"' where Id='"+assessmentId+"';");
    	//Show message
		Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
    	}
    	else
    	{
    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
    	}
        return this; 
    }
    
    public TestAdapter_Backup updatecompetencycomment(Context context,String competencyid,String result,String comment,String assessorsig,
    		String traineesig,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
	    	mDb.execSQL("update competency set result='"+result+"',comment='"+comment+"'" +
	    			",assessorsig='"+assessorsig+"',traineesig='"+traineesig+"',trainee='"+trainee+"'," +
	    					"assessor='"+assessor+"' where Id='"+competencyid+"';");
	    	//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
    	}
    	else
    	{
    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
    	}
	        return this; 
    }
    
    
    public Cursor getspinnervalue(String Id)
    {
    	try
    	{
    		String sql="SELECT * FROM dropdownvalue where Id='"+Id+"'";
    		
    		Cursor mCur=mDb.rawQuery(sql, null);
    		if(mCur!=null)
    		{
    			mCur.moveToNext();
    		}
    		return mCur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    		throw mSQLException;
    	}
    }
    public TestAdapter_Backup updatechecklistguide(Context context,String trainee,String assessor,String trainerguide,String gtrainersig,String gdate,
    		String gdayornight,String gweather,String glocotype,String gfrom,String gto,String gtrainlingth,
    		String gtonnage,String gnovehicle,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    	
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerguide='"+trainerguide+"',gtrainersig='"+gtrainersig+"',gdate='"+gdate+"'" +
							",gdayornight='"+gdayornight+"',gweather='"+gweather+"',glocotype='"+glocotype+"'" +
									",gfrom='"+gfrom+"',gto='"+gto+"',gtrainlingth='"+gtrainlingth+"'" +
											",gtonnage='"+gtonnage+"',gnovehicle='"+gnovehicle+"' where checknum='"+checknum+"';");		
			//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}
		    else
		    {
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		    }
			return this; 
		} 
    public TestAdapter_Backup updatechecklistunguide(Context context,String trainee,String assessor,String trainerunguide,String ungtrainersig,String ungdate,
    		String ungdayornight,String ungweather,String unglocotype,String ungfrom,String ungto,String ungtrainlength,
    		String ungtonnage,String ungnovehicle,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerunguide='"+trainerunguide+"',ungtrainersig='"+ungtrainersig+"',ungdate='"+ungdate+"'" +
							",ungdayornight='"+ungdayornight+"',ungweather='"+ungweather+"',unglocotype='"+unglocotype+"'" +
									",ungfrom='"+ungfrom+"',ungto='"+ungto+"',ungtrainlength='"+ungtrainlength+"'" +
											",ungtonnage='"+ungtonnage+"',ungnovehicle='"+ungnovehicle+"' where checknum='"+checknum+"';");		
			//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}
		    else
		    {
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		    }
			return this; 
		} 
    public TestAdapter_Backup updatechecklistinstruction(Context context,String trainee,String assessor,String trainerinstruction,String instrainersig,String insdate,
    		String insdayornight,String insweather,String inslocotype,String insfrom,String insto,String instrainlength,
    		String instonnage,String insnovehicle,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerinstruction='"+trainerinstruction+"',instrainersig='"+instrainersig+"',insdate='"+insdate+"'" +
							",insdayornight='"+insdayornight+"',insweather='"+insweather+"',inslocotype='"+inslocotype+"'" +
									",insfrom='"+insfrom+"',insto='"+insto+"',instrainlength='"+instrainlength+"'" +
											",instonnage='"+instonnage+"',insnovehicle='"+insnovehicle+"' where checknum='"+checknum+"';");		
			//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}
		    else
		    {
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		    }
			return this; 
		} 
    public TestAdapter_Backup updatechecklistheaderassessor(Context context,String trainee,String assessor,
    		String locomotiveclass,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") )
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",locomotiveclass='"+locomotiveclass+"'" +
							" where checknum='"+checknum+"';");		
			//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}
		    else
		    {
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		    }
			return this; 
		} 
    public TestAdapter_Backup updatechecklistheadertrainer(Context context,String trainee,String assessor,String trainer,
    		String locomotiveclass,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL("update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainer='"+trainer+"',locomotiveclass='"+locomotiveclass+"'" +
							" where checknum='"+checknum+"';");		
			//Show message
			Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			}
		    else
		    {
		    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		    }
			return this; 
		} 
 
    public void insertauditlog(String action,String tablename,String query)   
		{	
    	
	    	 String sql ="select * from user where login='true'";
	         Cursor mCur = mDb.rawQuery(sql, null);
	         if(mCur.getCount()>0)
	         {
	        	 mCur.moveToFirst();
	        	 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        	 String querymodify= query.replace("\'", "\"");
	        	 mDb = mDbHelper.getWritableDatabase();
	        	 String username=mCur.getString(mCur.getColumnIndex("username"));
	        	 mDb.execSQL("insert into auditlog (userid,datetime,action,tablename,query) VALUES ('"+username+"','"+sdf.format(new Date())+"','"+action+"','"+tablename+"','"+querymodify+"');");
	        	 
	         }
		} 
    public void close()  
    { 
        mDbHelper.close(); 
    } 
     
}
