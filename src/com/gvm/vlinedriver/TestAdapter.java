package com.gvm.vlinedriver;

import java.io.IOException; 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.gvm.vlinedriver.R;

import android.R.string;
import android.content.Context; 
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
import android.util.Log; 
import android.view.Gravity;
import android.widget.Toast;

public class TestAdapter {
	
	protected static final String TAG = "DataAdapter"; 
	 
    private final Context mContext; 
    private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
 
    public TestAdapter(Context context)  
    { 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext); 
    } 
 
    public TestAdapter createDatabase() throws SQLException  
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
            mDb = mDbHelper.getWritableDatabase();
            Cursor mCur = mDb.rawQuery(sql, null);
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
    public TestAdapter resetdatabase() throws SQLException  
    { 
        try  
        { 
            mDbHelper.Resetdatabase(); 
        }  
        catch (IOException mIOException)  
        { 
            Log.e(TAG, mIOException.toString() + "  UnableToResetDatabase"); 
            throw new Error("UnableToReaetDatabase"); 
        } 
        return this; 
    } 
 
    public TestAdapter open() throws SQLException  
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
    
    public TestAdapter createDatabase2(String a,String id) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	mDb.execSQL("update subject set name='"+a+"' where Id='"+id+"';");
        return this; 
    } 
    
    public TestAdapter subjectachive(String achive,String id,String assessor,String trainee) throws SQLException  
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

    public TestAdapter checkchange(Context context,String tbl,String setfeild,String checkvalue,String wherefeild,String id
    			,String trainee,String assessor,String trainer) throws SQLException  
    {
    	
    	mDb = mDbHelper.getWritableDatabase();
    	if(checkrole().equalsIgnoreCase("assessor"))
		{
    	String sql="update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
    			",trainer='"+trainer+"' where "+wherefeild+"='"+id+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update",tbl, sql);
		}
    	else
    	{
    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}
    	return this; 
    }

    public TestAdapter checkchangeassessor(Context context,String tbl,String setfeild,String checkvalue,String wherefeild,String id
			,String trainee,String assessor) throws SQLException  
		{
    	
			mDb = mDbHelper.getWritableDatabase();
			if(checkrole().equalsIgnoreCase("assessor"))
			{
				String sql="update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
						" where "+wherefeild+"='"+id+"';";
				mDb.execSQL(sql);
	    		insertauditlog("Update", tbl, sql);
			}
			else
	    	{
	    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
	    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	    		toast.show();
	    	}
			return this; 
		}

    public TestAdapter checklisttask(Context context,String checknum,String setfeild,String checkvalue,String id
			,String trainee,String assessor,String trainer,String trainerfeild) throws SQLException  
	{
		mDb = mDbHelper.getWritableDatabase();
		if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
		{
			if(trainer!="")
			{
			String sql1="update checklist set "+trainerfeild+"='"+trainer+"' where checknum='"+checknum+"'";
			mDb.execSQL(sql1);
			insertauditlog("Update", "checklist", sql1);
			}
			
			if(assessor!="")
			{
			String sql1="update checklist set assessor='"+assessor+"' where checknum='"+checknum+"'";
			mDb.execSQL(sql1);
			insertauditlog("Update", "checklist", sql1);
			}
			
			String sql2="update checklisttask set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
					" where Id='"+id+"';";
			mDb.execSQL(sql2);		
			insertauditlog("Update", "checklisttask", sql2);
		}
		else
    	{
    		Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}
		return this; 
	}
    
    public TestAdapter checkchangetest(String tbl,String setfeild,String checkvalue,String wherefeild,String id
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
    
    public TestAdapter checkchangecompetency(Context context,String tbl,String setfeild,String checkvalue,String wherefeild,String id
			,String trainee,String assessor,String trainer) throws SQLException  
	{
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
		mDb = mDbHelper.getWritableDatabase();
		String sql="update "+tbl+" set  "+setfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
				",trainer='"+trainer+"' where "+wherefeild+"='"+id+"';";
		mDb.execSQL(sql);
		insertauditlog("Update", tbl, sql);
    	}
		
		return this; 
	} 
    
    public TestAdapter checklistapprove(Context context,String traineesig,String traineedate,String trainersig,String trainerdate,String checknum,String trainee,String trainerunguide,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer") || checkrole().equalsIgnoreCase("trainee"))
    	{
	    	mDb = mDbHelper.getWritableDatabase();
	    	String sql="";
	    	if(assessor.equalsIgnoreCase(""))
	    	{
	    		sql="update checklist set trainee='"+trainee+"',tasktraineesig='"+traineesig+"',tasktraineedate='"+traineedate+"',tasktrainersig='"+trainersig+"',tasktrainerdate='"+trainerdate+"',trainerunguide='"+trainerunguide+"' where checknum='"+checknum+"';";
	    		mDb.execSQL(sql);	    		
	    	}
	    	else
	    	{
	    		sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"',tasktraineesig='"+traineesig+"',tasktraineedate='"+traineedate+"',tasktrainersig='"+trainersig+"',tasktrainerdate='"+trainerdate+"' where checknum='"+checknum+"';";
	    		mDb.execSQL(sql);
	    	}
	    	insertauditlog("Update", "checklist", sql);
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

    public TestAdapter subjectcomment(Context context,String comment,String id,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update subject set comment='"+comment+"',trainee='"+trainee+"',assessor='"+assessor+"' where Id='"+id+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "subject", sql);
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
    public TestAdapter assessmentcomment(Context context,String comment,String result,String assessorsig,String traineesig,String assessordate,String traineedate,String assessorname,String trainee,String id) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainee"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update  assessment set comment='"+comment.replace("'", "''")+"',result='"+result+"',assessorsig='"+assessorsig+"',traineesig='"+traineesig+"'" +
    			",assessordate='"+assessordate+"',traineedate='"+traineedate+"',assessor='"+assessorname+"',trainee='"+trainee+"' where Id='"+id+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "assessment", sql);
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
    public TestAdapter timelost(Context context,String assessmentid,String tripno,String minutelost,String signals
    		,String passengerdelay,String permanentway,String trackwork,String trainfault
    		,String other,String explanation,String assessor,String trainee) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update timelost set minutelost='"+minutelost.replace("'", "''")+"',signals='"+signals.replace("'", "''")+"',passengerdelay='"+passengerdelay.replace("'", "''")+"'," +
    			"permanentway='"+permanentway.replace("'", "''")+"',trackwork='"+trackwork.replace("'", "''")+"',trainfault='"+trainfault.replace("'", "''")+"',other='"+other.replace("'", "''")+"'," +
				"explanation='"+explanation.replace("'", "''")+"',assessor='"+assessor+"',trainee='"+trainee+"' where tripno='"+tripno+"' and  assessmentId='"+assessmentid+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "timelost", sql);
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}  
    	return this; 
    } 
    
    public TestAdapter assessmenttime(Context context,String id,String timeoverschedule,String timelost) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update assessment set timeoverschedule='"+timeoverschedule+"',timelost='"+timelost+"' where Id='"+id+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "assessment", sql);
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}    	
    	return this; 
    } 
    
    public TestAdapter assessmentdetail(Context context,String assessmentId,String tripno,String time,String location,String destination
    		,String tdNo,String mputype,String mpuno,String weather,String assessor,String trainee) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update assessmentdetail set time='"+time.replace("'", "''")+"',location='"+location.replace("'", "''")+"',destination='"+destination.replace("'", "''")+"'," +
    			"tdNo='"+tdNo.replace("'", "''")+"',mputype='"+mputype.replace("'", "''")+"',mpuno='"+mpuno.replace("'", "''")+"',weather='"+weather.replace("'", "''")+"',assessor='"+assessor+"',trainee='"+trainee+"'" +
				" where assessmentId='"+assessmentId+"' and tripno='"+tripno+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "assessmentdetail", sql);
    	}
    	else
    	{
    	    Toast toast=Toast.makeText(context, R.string.dologin, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    		toast.show();
    	}    	
        return this; 
    } 
    
    public TestAdapter insertuser(String username,String password,String role,String employeeno,String fullname) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="insert into user (username,password,role,employeeno,fullname) values ('"+username+"','"+password.replace("'", "''")+"','"+role.replace("'", "''")+"','"+employeeno.replace("'", "''")+"'" +
    			",'"+fullname.replace("'", "''")+"');";
    	mDb.execSQL(sql);
    	insertauditlog2("","Insert", "user", sql);
        return this; 
    }
    public TestAdapter updateuser(String username,String password,String role,String employeeno,String fullname) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update user set password='"+password.replace("'", "''")+"',employeeno='"+employeeno.replace("'", "''")+"',fullname='"+fullname.replace("'", "''")+"' where username='"+username+"' and role='"+role.replace("'", "''")+"'";
    	mDb.execSQL(sql);
    	insertauditlog2("","Update trainee Password", "user", sql);
        return this; 
    }
    public TestAdapter loginfalse() throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update user set login='false';";    
    	insertauditlog("Logout", "user", sql);
    	mDb.execSQL(sql); 
    	updateCheckedVersionAlready("false");
        return this; 
    }
    public TestAdapter logintrue(String username,String password) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update user set login='true' where username='"+username+"' and password='"+password+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Login", "user", sql);
        return this; 
    }
    public TestAdapter deletetrainerassessor() throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="delete from user where role='trainer' or role='assessor';";
    	mDb.execSQL(sql);
    	insertauditlog("Delete", "user", sql);
        return this; 
    }
    public Cursor getTestData(String tablename)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		if(tablename.equalsIgnoreCase("auditlog"))
    		{
    			sql ="select * from "+ tablename +"";	
    		}
    		else
    		{
    			sql ="select * from "+ tablename +" where trainee<>''";	
    		} 
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
    public Cursor criticalcount(String assessmentid)
    {
    	Log.d("LEE",assessmentid);
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		sql ="select * from assessment join subject on assessment.Id=subject.assessmentId " +
    				"join subjectchecklist on subject.Id=subjectchecklist.subjectId " +
    				" where assessment.Id="+assessmentid+" and subjectchecklist.critical='C' ";	
    		
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
    public Cursor completecriticalcount(String assessmentid)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		/*sql ="select * from assessment join subject on assessment.Id=subject.assessmentId " +
    				"join subjectchecklist on subject.Id=subjectchecklist.subjectId " +
    				"join checklist on subjectchecklist.checknum=checklist.checknum " +
    				" where assessment.Id="+assessmentid+" and subjectchecklist.critical='C' and (subjectchecklist.explained='true' OR subjectchecklist.demonstration='true')";	 */
    		sql ="select * from assessment join subject on assessment.Id=subject.assessmentId " +
    				"join subjectchecklist on subject.Id=subjectchecklist.subjectId " +
    				" where assessment.Id="+assessmentid+" and subjectchecklist.critical='C' and (subjectchecklist.explained='true' OR subjectchecklist.demonstration='true')";

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

    public Cursor assessment10competent(String assessmentid)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		sql="select count(ID)competencycount,(select count(ID) from competency " +
        			"where assessmentId="+assessmentid+" and result='true' and c='true' and assessorsig='true' and traineesig='true') as compcompetency  " +
        					"from competency where assessmentId="+assessmentid+""; 
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
    public Cursor competencytaskcompetent(String competencyid)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		sql="select count(ID)competencycount,(select count(ID) from competencytask " +
        			"where competencyId="+competencyid+" and (demonstrated='true' OR explained='true')) as compcompetency  " +
        					"from competencytask where competencyId="+competencyid+""; 
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
    public Cursor checklistinfo(String checknum)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="";
    	try
        {
    		sql ="select * from checklist where checknum='"+checknum+"'";	
    		
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
    public Cursor sendtabletoekp(String tablename)
    {
    	mDb = mDbHelper.getWritableDatabase();
    	try
        {
            String sql ="select * from "+ tablename +"";

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
            mDb = mDbHelper.getWritableDatabase();
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
    public Cursor traineechangepassword()
    {
        try
        {
            String sql ="select * from user where role='trainee'";
            mDb = mDbHelper.getWritableDatabase();
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
        	mDb = mDbHelper.getWritableDatabase();
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
        	mDb = mDbHelper.getWritableDatabase();
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
        	 mDb = mDbHelper.getWritableDatabase();
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
        	 mDb = mDbHelper.getWritableDatabase();
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
            mDb = mDbHelper.getWritableDatabase();
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
            mDb = mDbHelper.getWritableDatabase();
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
            mDb = mDbHelper.getWritableDatabase();
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
    		mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		String sql="SELECT * FROM assessment where stageId='"+stageid+"' and result='true' and assessorsig='true' and traineesig='true'";
    		mDb = mDbHelper.getWritableDatabase();  		
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
    //To get assessment timelost tab
    public Cursor assessmenttimelostvisible(String assessmentid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessment where Id='"+assessmentid+"'";
    		mDb=mDbHelper.getWritableDatabase();
    		Cursor mcur=mDb.rawQuery(sql, null);
    		if(mcur!=null)
    		{
    			mcur.moveToNext();
    		}
    		return mcur;
    	}
    	catch(SQLException mSQLException)
    	{
    		Log.e(TAG,"GetTestdata >>"+mSQLException.toString());
    		throw mSQLException;
    	}
    }
    //To get all assessment for each stage
    public Cursor getallsessment(String stageid)
    {
    	try
    	{
    		String sql="SELECT * FROM assessment where stageId='"+stageid+"'";
    		mDb = mDbHelper.getWritableDatabase();    		
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
    		String sql="select checklist.Id,checklist.checknum,checklist.name,checklist.rpl,checklist.tasktrainersig,checklist.tasktraineesig from checklistindex inner join checklist on checklistindex.checknum=checklist.checknum where checklistindex.stageid='"+stageid+"' and real='TRUE' GROUP BY checklistindex.checknum";
    		mDb = mDbHelper.getWritableDatabase();    		
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
    		String sql="SELECT * FROM checklisttask where checknum='"+checknum+"' order by CAST (Id AS INT)";
    		mDb = mDbHelper.getWritableDatabase();
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
    		mDb = mDbHelper.getWritableDatabase();    		
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
    
    //To get all checklist task completed in instructed 
    public Cursor getchecklisttaskinstcmp(String checknum)
    {
    	try
    	{
    		String sql="SELECT * FROM checklisttask where checknum='"+checknum+"' and instructed='true'";

    		mDb = mDbHelper.getWritableDatabase();    		
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
  //To get all checklist task completed in guide part
    public Cursor getchecklisttaskcmpguide(String checknum)
    {
    	try
    	{
    		String sql="SELECT * FROM checklisttask where checknum='"+checknum+"' and (gna='true' or gd='true' or ge='true')";
    		mDb = mDbHelper.getWritableDatabase();    		
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
    		 mDb = mDbHelper.getWritableDatabase();   		
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
    		String sql="SELECT checklist.*,subjectchecklist.Id as subjectchecklistid,subjectchecklist.subjectId, subjectchecklist.nyc as subjectnyc,subjectchecklist.explained as subjectexplained,subjectchecklist.demonstration as subjectdemonstration,subjectchecklist.critical as sbjectcritical" +
    				" FROM subjectchecklist join checklist on checklist.checknum=subjectchecklist.checknum where subjectId='"+subjectid+"'group by subjectchecklist.checknum";
    		mDb = mDbHelper.getWritableDatabase();
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
    		String sql="SELECT id FROM subjectchecklist where subjectId='"+subjectid+"' and critical='C' GROUP BY id";
    		mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    public TestAdapter assessment(Context context,String id,String location,String date,String trainee,String assessor) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
	    	mDb = mDbHelper.getWritableDatabase();
	    	String sql="update  assessment set location='"+location+"',date='"+date+"',trainee='"+trainee+"',assessor='"+assessor+"' where Id='"+id+"';";
	    	mDb.execSQL(sql);
	    	insertauditlog("Update", "assessment", sql);
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
    
    public TestAdapter assessmenttimelostinsert(String assessmentid,String tripno) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="insert into timelost (assessmentId,tripno) values ('"+assessmentid+"','"+tripno+"');";
    	mDb.execSQL(sql);
    	insertauditlog("Insert", "timelost", sql);
    	return this; 
    } 
    
    public TestAdapter assessmentdetailinsert(String assessmentid,String tripno) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="insert into assessmentdetail (assessmentId,tripno) values ('"+assessmentid+"','"+tripno+"');";
    	mDb.execSQL(sql);
    	insertauditlog("Insert", "assessmentdetail", sql);
        return this; 
    } 
    
    public Cursor getconfig()
    {
    	try
    	{
    		String sql="SELECT * FROM config";
    		 mDb = mDbHelper.getWritableDatabase();
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
    //Do we have any checklist in process
    public Cursor getchecklistinprocess(String checknum)
    {
    	try
    	{
    		String sql="SELECT * FROM checklistindex join checklist on checklistindex.checknum=checklist.checknum where checklistindex.checknum='"+checknum+"' and (trainee<>'' OR checklist.assessor<>'')";
    		 mDb = mDbHelper.getWritableDatabase();
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
    
    public TestAdapter updateconfig(Context context,String feildname,String feildvalue) throws SQLException  
    { 
    	
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update config set "+feildname+" ='"+feildvalue+"'";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "config", sql);
    	Toast toast=Toast.makeText(context, R.string.savemessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
    	
        return this; 
    } 
    
    public Cursor getcompetencytask(String competencyid)
    {
    	try
    	{
    		String sql="SELECT * FROM competencytask where competencyId='"+competencyid+"'  order by CAST (Id AS INT)";
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    		 mDb = mDbHelper.getWritableDatabase();   		
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
    		 mDb = mDbHelper.getWritableDatabase();   		
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
    		 mDb = mDbHelper.getWritableDatabase();    		
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
    public TestAdapter insertcompetencytaskdesc(String competencyid,String tablenumber) throws SQLException  
    { 
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="Insert into competencytaskdesc (competencyId,tablenumber) Values('"+competencyid+"','"+tablenumber+"');";
    	mDb.execSQL(sql);
    	insertauditlog("Insert", "competencytaskdesc", sql);
        return this; 
    }
    
    public TestAdapter updatecompetencytaskdesc(Context context,String motivepower,String task,String date,String mpu,
    		String dayornight,String tdno,String origin,String destination
    		,String assessor,String trainee,String competencyid,String tablenumber) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="Update competencytaskdesc set motivepower='"+motivepower.replace("'", "''")+"',task='"+task.replace("'", "''")+"'," +
    			"date='"+date+"',mpu='"+mpu.replace("'", "''")+"',dayornight='"+dayornight.replace("'", "''")+"'," +
				"tdno='"+tdno.replace("'", "''")+"',origin='"+origin.replace("'", "''")+"',destination='"+destination.replace("'", "''")+"',assessor='"+assessor+"'" +
						",trainee='"+trainee+"' " +
						"where competencyid='"+competencyid+"' and tablenumber='"+tablenumber+"'";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "competencytaskdesc", sql);
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
    
    public TestAdapter assessment10date(Context context,String assessmentId,String date) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor"))
    	{
    	mDb = mDbHelper.getWritableDatabase();
    	String sql="update assessment set date='"+date+"' where Id='"+assessmentId+"';";
    	mDb.execSQL(sql);
    	insertauditlog("Update", "assessment", sql);
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
    
    public TestAdapter updatecompetencycomment(Context context,String competencyid,String result,String comment,String assessorsig,
    		String traineesig,String trainee,String assessor,String nyc,String c) throws SQLException  
    { 
    	if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainee"))
    	{
    		mDb = mDbHelper.getWritableDatabase();
    		String sql="update competency set result='"+result+"',comment='"+comment.replace("'", "''")+"'" +
	    			",assessorsig='"+assessorsig+"',traineesig='"+traineesig+"',trainee='"+trainee+"'," +
					"assessor='"+assessor+"',c='"+c+"',nyc='"+nyc+"' where Id='"+competencyid+"';";
    		mDb.execSQL(sql);
    		insertauditlog("Update", "competency", sql);
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
    		 mDb = mDbHelper.getWritableDatabase();
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
    public Cursor getspinnervaluefilter(String Id,String descitem)
    {
    	try
    	{
    		String sql="SELECT * FROM dropdownvalue where Id='"+Id+"' and itemdesc='"+descitem+"'";
    		 mDb = mDbHelper.getWritableDatabase();
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
    public TestAdapter updatechecklistguide(Context context,String trainee,String assessor,String trainerguide,String gtrainersig,String gdate,
    		String gdayornight,String gweather,String gfrom,String gto,
    		String gmpu,String gnovehicle,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    	
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerguide='"+trainerguide+"',gtrainersig='"+gtrainersig+"',gdate='"+gdate+"'" +
					",gdayornight='"+gdayornight.replace("'", "''")+"',gweather='"+gweather.replace("'", "''")+"'" +
							",gfrom='"+gfrom.replace("'", "''")+"',gto='"+gto.replace("'", "''")+"'" +
									",gmpu='"+gmpu.replace("'", "''")+"',gnovehicle='"+gnovehicle.replace("'", "''")+"' where checknum='"+checknum+"';";
			mDb.execSQL(sql);
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatechecklistunguide(Context context,String trainee,String assessor,String trainerunguide,String ungtrainersig,String ungdate,
    		String ungdayornight,String ungweather,String ungfrom,String ungto,
    		String ungmpu,String ungnovehicle,String ungperform,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerunguide='"+trainerunguide+"',ungtrainersig='"+ungtrainersig+"',ungdate='"+ungdate+"'" +
					",ungdayornight='"+ungdayornight+"',ungweather='"+ungweather+"'" +
							",ungfrom='"+ungfrom+"',ungto='"+ungto+"'" +
									",ungmpu='"+ungmpu+"',ungnovehicle='"+ungnovehicle+"',ungperform='"+ungperform+"' where checknum='"+checknum+"';";
			mDb.execSQL(sql);	
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatechecklistinstruction(Context context,String trainee,String assessor,String trainerinstruction,String instrainersig,String insdate,
    		String insdayornight,String insweather,String insfrom,String insto,
    		String insmpu,String insnovehicle,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainerinstruction='"+trainerinstruction+"',instrainersig='"+instrainersig+"',insdate='"+insdate+"'" +
					",insdayornight='"+insdayornight.replace("'","''")+"',insweather='"+insweather.replace("'", "''")+"'," +
							"insfrom='"+insfrom.replace("'", "''")+"',insto='"+insto.replace("'", "''")+"'," +
									"insnovehicle='"+insnovehicle.replace("'", "''")+"',insmpu='"+insmpu.replace("'", "''")+"' where checknum='"+checknum+"';";

			mDb.execSQL(sql);	
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatechecklistheaderassessor(Context context,String trainee,String assessor,
    		String locomotiveclass,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") )
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",locomotiveclass='"+locomotiveclass+"'" +
					" where checknum='"+checknum+"';";
			mDb.execSQL(sql);	
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatechecklistheadertrainer(Context context,String trainee,String assessor,String trainer,
    		String locomotivetype,String corridordown,String corridorup,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",trainer='"+trainer+"',locomotivetype='"+locomotivetype+"',corridordown='"+corridordown+"',corridorup='"+corridorup+"'" +
					" where checknum='"+checknum+"';";
			mDb.execSQL(sql);
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatechecklistheaderassessor(Context context,String trainee,String assessor,
    		String locomotivetype,String corridordown,String corridorup,String checknum) throws SQLException  
		{
			
		    if(checkrole().equalsIgnoreCase("assessor") || checkrole().equalsIgnoreCase("trainer"))
			{
		    
			mDb = mDbHelper.getWritableDatabase();
			String sql="update checklist set trainee='"+trainee+"',assessor='"+assessor+"' " +
					",locomotivetype='"+locomotivetype+"',corridordown='"+corridordown+"',corridorup='"+corridorup+"'" +
					" where checknum='"+checknum+"';";
			mDb.execSQL(sql);
			insertauditlog("Update", "checklist", sql);
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
    public TestAdapter updatestage(String id,String name,String desc) throws SQLException  
		{		    
			mDb = mDbHelper.getWritableDatabase();
			String sql="update stage set name='"+name.replace("'", "''")+"',desc='"+desc.replace("'", "''")+"' where Id='"+id+"';";
			mDb.execSQL(sql);	
			insertauditlog("Update", "stage", sql);

			return this; 
		}
    public TestAdapter updateassessmenttitle(String id,String stageid,String name) throws SQLException  
	{		    
		mDb = mDbHelper.getWritableDatabase();
		String sql="update assessment set name='"+name.replace("'", "''")+"' where Id='"+id+"' and stageId='"+stageid+"';";

		//mDb.execSQL(sql);	
		//insertauditlog("Update", "assessment", sql);

		return this; 
	}
    public void insertauditlog(String action,String tablename,String query)   
		{	
    	     mDb = mDbHelper.getWritableDatabase();
	    	 String sql ="select * from user where login='true'";
	         Cursor mCur = mDb.rawQuery(sql, null);
	         if(mCur.getCount()>0)
	         {
	        	 mCur.moveToFirst();
	        	 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        	 String querymodify= query.replace("\'", "\"");
	        	 mDb = mDbHelper.getWritableDatabase();
	        	 String username=mCur.getString(mCur.getColumnIndex("username"));
	        	 mDb.execSQL("insert into auditlog (userid,datetime,action,tablename,query) VALUES ('"+username+"','"+sdf.format(new Date())+"','"+action+"','"+tablename+"','"+querymodify.replace("'", "''")+"');");	        	 
	         }
	         
		} 
    //Login is not import
    public void insertauditlog2(String username,String action,String tablename,String query)   
	{	
	     mDb = mDbHelper.getWritableDatabase();
    	
        	 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        	 String querymodify= query.replace("\'", "\"");
        	 mDb = mDbHelper.getWritableDatabase();
        	 mDb.execSQL("insert into auditlog (userid,datetime,action,tablename,query) VALUES ('"+username+"','"+sdf.format(new Date())+"','"+action+"','"+tablename+"','"+querymodify+"');");	        	     
	} 
    public void deletetauditlog()   
	{	
	     mDb = mDbHelper.getWritableDatabase();
    	 String sql ="select * from user where login='true'";
         Cursor mCur = mDb.rawQuery(sql, null);
         if(mCur.getCount()>0)
         {
        	 mCur.moveToFirst();
        	 mDb.execSQL("delete from auditlog");	        	 
         }        
	} 
 
    public void checklisttaskallselect(String checkfeild,String checkvalue,String checknum,String trainee,String assessor)
    {
    	mDb=mDbHelper.getWritableDatabase();
    	String sql="update checklisttask set "+checkfeild+"='"+checkvalue+"',trainee='"+trainee+"',assessor='"+assessor+"' " +
					" where checknum='"+checknum+"';";
    	Cursor mcur=mDb.rawQuery(sql, null);
    	if(mcur.getCount()>0)
    	{
    		mcur.moveToFirst();
    		mDb.execSQL("Check all instructed");
    	}
    }
    
    public void checklisttaskallunselect(String checkfeild1,String checkfeild2,String checknum,String trainee,String assessor)
    {
    	mDb=mDbHelper.getWritableDatabase();
    	String sql="update checklisttask set "+checkfeild1+"='false',"+checkfeild2+"='false',trainee='"+trainee+"',assessor='"+assessor+"' " +
					" where checknum='"+checknum+"';";
    	
    	Cursor mcur=mDb.rawQuery(sql, null);
    	if(mcur.getCount()>0)
    	{
    		mcur.moveToFirst();
    		mDb.execSQL("Check all instructed");
    	}
    }
    public void close()  
    { 
        mDbHelper.close(); 
    } 

    public void chcklistnotstart()
    {
    	String sql="SELECT * FROM user where role='trainee'";
		 mDb = mDbHelper.getWritableDatabase();
		Cursor mCur=mDb.rawQuery(sql, null);
		mCur.moveToFirst();
		String trainee=mCur.getString(mCur.getColumnIndex("username"));
		
		String sqlupdate="update checklist set trainee='"+trainee+"' where checklist.trainee='';";
		mDb.execSQL(sqlupdate);	
		
    }
    
    public void addtraineetosubject()
    {
    	String sql="SELECT * FROM user where role='trainee'";
		 mDb = mDbHelper.getWritableDatabase();
		Cursor mCur=mDb.rawQuery(sql, null);
		mCur.moveToFirst();
		String trainee=mCur.getString(mCur.getColumnIndex("username"));
		
		String sqlupdate="update subject set trainee='"+trainee+"' where trainee='';";
		mDb.execSQL(sqlupdate);	
		
    }
    
    public void addtraineetoassessment()
    {
    	String sql="SELECT * FROM user where role='trainee'";
		 mDb = mDbHelper.getWritableDatabase();
		Cursor mCur=mDb.rawQuery(sql, null);
		mCur.moveToFirst();
		String trainee=mCur.getString(mCur.getColumnIndex("username"));
		
		String sqlupdate="update assessment set trainee='"+trainee+"' where trainee='';";
		mDb.execSQL(sqlupdate);	
		
    }
    
    public void addtraineetocompetency()
    {
    	String sql="SELECT * FROM user where role='trainee'";
		 mDb = mDbHelper.getWritableDatabase();
		Cursor mCur=mDb.rawQuery(sql, null);
		mCur.moveToFirst();
		String trainee=mCur.getString(mCur.getColumnIndex("username"));
		
		String sqlupdate="update competency set trainee='"+trainee+"' where trainee='';";
		mDb.execSQL(sqlupdate);	
		
    }
    
    public Cursor getTrainee()
    {
        try
        {
            String sql ="select * from user where role='trainee'";
            mDb = mDbHelper.getWritableDatabase();
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
    public Cursor selectDataLoad()
    {
    	try
    	{
    		String sql="SELECT * FROM dataLoad order by Id DESC LIMIT 1";
    		 mDb = mDbHelper.getWritableDatabase();
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
    
    public TestAdapter loadCoretStage(String dataString) throws SQLException  
    { 
    	String stageValue="";
    	String[] stageDataRow = dataString.split("\\!");
		 for (int i = 0, n = stageDataRow.length; i < n; i++)
		 {
			 String[] stageDataFeild=stageDataRow[i].split("\\^",-1);
			 stageValue = stageValue + "('"+stageDataFeild[0]+"','"+stageDataFeild[1]+"','"+stageDataFeild[2]+"'),";
		 }
    	mDb = mDbHelper.getWritableDatabase();
    	String sqlStageRowNumber="SELECT * FROM stage";
    	Cursor mCur=mDb.rawQuery(sqlStageRowNumber, null);
    	if(mCur.getCount()==0)
    	{
    		String stageValueQuery=stageValue.substring(0, stageValue.length() - 1) + ';';
    		String sql="insert into stage (Id,name,desc) values" + stageValueQuery ;
    		mDb.execSQL(sql);
    	}
        return this; 
    }
    
    public TestAdapter loadCoreAssessment(String dataString) throws SQLException  
    { 
    	String assessmentValue="";
    	String[] dataRow = dataString.split("\\!");
		 for (int i = 0, n = dataRow.length; i < n; i++)
		 {
			 String[] dataFeild=dataRow[i].split("\\^",-1);
			 assessmentValue = assessmentValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
		 }
    	mDb = mDbHelper.getWritableDatabase();
    	String sqlRowNumber="SELECT * FROM assessment";
    	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
    	if(mCur.getCount()==0)
    	{
    		String valueQuery=assessmentValue.substring(0, assessmentValue.length() - 1) + ';';
    		String sql="insert into assessment (Id,stageId,name,timevisible) values" + valueQuery ;
    		mDb.execSQL(sql);
    	}
        return this; 
    }
  
    public TestAdapter loadCoreSubject(String dataString) throws SQLException  
    { 
    	String subjectValue="";
    	String[] dataRow = dataString.split("\\!");
		 for (int i = 0, n = dataRow.length; i < n; i++)
		 {
			 String[] dataFeild=dataRow[i].split("\\^",-1);
			 subjectValue = subjectValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'),";
		 }
    	mDb = mDbHelper.getWritableDatabase();
    	String sqlRowNumber="SELECT * FROM subject";
    	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
    	if(mCur.getCount()==0)
    	{
    		String valueQuery=subjectValue.substring(0, subjectValue.length() - 1) + ';';
    		String sql="insert into subject (Id,assessmentId,name,objective,requirednum) values" + valueQuery ;
    		mDb.execSQL(sql);
    	}
        return this; 
    }
    
    public TestAdapter loadCoreSubjectChecklist(String dataString) throws SQLException  
    { 
    	String[] dataRow = dataString.split("\\!");

    	int n=dataRow.length;
    	int a ;
    	if(n/100==0)	{a=n/10;}
    	else	{a=(n/100)+1;}
    	
    	mDb = mDbHelper.getWritableDatabase();
    	String sqlRowNumber="SELECT * FROM subjectchecklist";
    	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
    	if(mCur.getCount()==0)
    	{
	    	for(int i=0; i<a; i++)
	    	{
	    		String subjectChecklistValue="";
	    	    if((i+1)*100>n)
	    	    {
	    	        for (int j = i * 100; j < n; j++) {
	    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
	    				subjectChecklistValue = subjectChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
	    	        }
	    	    }
	    	    else
	    	    {
	    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
	    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
	    				subjectChecklistValue = subjectChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
	    	        }
	    	    }
	    	    if(subjectChecklistValue!="")
	    	    {
	    	    	String valueQuery=subjectChecklistValue.substring(0, subjectChecklistValue.length() - 1) + ';';
	        		String sql="insert into subjectchecklist (Id,subjectId,checknum,critical) values" + valueQuery ;
	        		mDb.execSQL(sql);
	    	    }
	    	}
    	}
        return this; 
    }
public TestAdapter loadCoreChecklist(String dataString) throws SQLException  
    { 
    	String[] dataRow = dataString.split("\\!");

    	int n=dataRow.length;
    	int a ;
    	if(n/100==0)	{a=n/10;}
    	else	{a=(n/100)+1;}
    	
    	mDb = mDbHelper.getWritableDatabase();
    	String sqlRowNumber="SELECT * FROM checklist";
    	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
    	if(mCur.getCount()==0)
    	{
	    	for(int i=0; i<a; i++)
	    	{
	    		String ChecklistValue="";
	    	    if((i+1)*100>n)
	    	    {
	    	        for (int j = i * 100; j < n; j++) {
	    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
	    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
	    	        }
	    	    }
	    	    else
	    	    {
	    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
	    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
	    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
	    	        }
	    	    }
	    	    if(ChecklistValue!="")
	    	    {
	    	    	String valueQuery=ChecklistValue.substring(0, ChecklistValue.length() - 1) + ';';
	        		String sql="insert into checklist (Id,checknum,name,objective) values" + valueQuery ;
	        		mDb.execSQL(sql);
	    	    }
	    	}
    	}
        return this; 
    }

public TestAdapter loadCoreChecklistTask(String dataString) throws SQLException  
{ 
	String[] dataRow = dataString.split("\\!");

	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/10;}
	else	{a=(n/100)+1;}
	
	mDb = mDbHelper.getWritableDatabase();
    	for(int i=0; i<a; i++)
    	{
    		String ChecklistValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    if(ChecklistValue!="")
    	    {
    	    	String valueQuery=ChecklistValue.substring(0, ChecklistValue.length() - 1) + ';';
        		String sql="insert into checklisttask (Id,checknum,name,taskgroup) values" + valueQuery ;
        		mDb.execSQL(sql);
    	    }
    	}
    return this; 
}

public TestAdapter loadCoreChecklistIndex(String dataString) throws SQLException  
{ 
	String[] dataRow = dataString.split("\\!");

	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/10;}
	else	{a=(n/100)+1;}
	
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM checklistindex";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
    	for(int i=0; i<a; i++)
    	{
    		String ChecklistValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    if(ChecklistValue!="")
    	    {
    	    	String valueQuery=ChecklistValue.substring(0, ChecklistValue.length() - 1) + ';';
        		String sql="insert into checklistindex (Id,stageid,checknum,real) values" + valueQuery ;
        		mDb.execSQL(sql);
    	    }
    	}
	}
    return this; 
}
public TestAdapter loadCoreCompetency(String dataString) throws SQLException  
{ 
	String competencyValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 competencyValue = competencyValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'),";
	 }
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM competency";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		String valueQuery=competencyValue.substring(0, competencyValue.length() - 1) + ';';
		String sql="insert into competency (Id,assessmentId,taskactivity,corridor,objective) values" + valueQuery ;
		mDb.execSQL(sql);
	}
    return this; 
}
public TestAdapter loadCoreCompetencyTask(String dataString) throws SQLException  
{ 
	String[] dataRow = dataString.split("\\!");

	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/10;}
	else	{a=(n/100)+1;}
	
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM competencytask";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
    	for(int i=0; i<a; i++)
    	{
    		String competencyTaskValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	competencyTaskValue = competencyTaskValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	competencyTaskValue = competencyTaskValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"'),";
    	        }
    	    }
    	    if(competencyTaskValue!="")
    	    {
    	    	String valueQuery=competencyTaskValue.substring(0, competencyTaskValue.length() - 1) + ';';
        		String sql="insert into competencytask (Id,competencyId,no,name) values" + valueQuery ;
        		mDb.execSQL(sql);
    	    }
    	}
	}
    return this; 
}
public Cursor getVersionNumber()   
{ 
	try
	{
		mDb = mDbHelper.getWritableDatabase();
		String sql="SELECT * FROM updateversion";
		Cursor mCur=mDb.rawQuery(sql, null);
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


public TestAdapter insertVersionNumber(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM updateversion";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		String sql="insert into updateversion (LoadDate,versionNumber) values (DATETIME('now'),'"+dataString+"')";
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter updateVersionNumber(String versionNumber) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM updateversion";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		insertVersionNumber(versionNumber);
	}
	else
	{
		String sql="update updateversion set versionNumber='"+versionNumber+"', LoadDate=DATETIME('now')";
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter upgradeStage(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] stageDataRow = dataString.split("\\!");
	 for (int i = 0, n = stageDataRow.length; i < n; i++)
	 {
		 String[] stageDataFeild=stageDataRow[i].split("\\^",-1);
		 if(stageDataFeild[3].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update stage set name='"+stageDataFeild[1]+"',desc='"+stageDataFeild[2]+"' where Id='"+stageDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(stageDataFeild[3].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM stage where Id='"+stageDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into stage (Id,name,desc) values ('"+stageDataFeild[0]+"','"+stageDataFeild[1]+"','"+stageDataFeild[2]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(stageDataFeild[3].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from stage where Id='"+stageDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}

public TestAdapter upgradeAssessment(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] assessmentDataRow = dataString.split("\\!");
	 for (int i = 0, n = assessmentDataRow.length; i < n; i++)
	 {
		 String[] AssessmentDataFeild=assessmentDataRow[i].split("\\^",-1);
		 if(AssessmentDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update assessment set name='"+AssessmentDataFeild[2]+"',timevisible='"+AssessmentDataFeild[3]+"' where Id='"+AssessmentDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(AssessmentDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM assessment where Id='"+AssessmentDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into assessment (Id,stageId,name,timevisible) values ('"+AssessmentDataFeild[0]+"','"+AssessmentDataFeild[1]+"','"+AssessmentDataFeild[2]+"','"+AssessmentDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(AssessmentDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from assessment where Id='"+AssessmentDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}

public TestAdapter upgradeSubject(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] subjectDataRow = dataString.split("\\!");
	 for (int i = 0, n = subjectDataRow.length; i < n; i++)
	 {
		 String[] subjectDataFeild=subjectDataRow[i].split("\\^",-1);
		 if(subjectDataFeild[5].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update subject set name='"+subjectDataFeild[2]+"',requirednum='"+subjectDataFeild[4]+"' where Id='"+subjectDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(subjectDataFeild[5].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM subject where Id='"+subjectDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into subject (Id,assessmentId,name,objective,requirednum) values ('"+subjectDataFeild[0]+"','"+subjectDataFeild[1]+"','"+subjectDataFeild[2]+"','"+subjectDataFeild[3]+"','"+subjectDataFeild[4]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(subjectDataFeild[5].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from subject where Id='"+subjectDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}

public TestAdapter upgradeSubjectChecklist(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] subjectChecklistDataRow = dataString.split("\\!");
	 for (int i = 0, n = subjectChecklistDataRow.length; i < n; i++)
	 {
		 String[] subjectChecklistDataFeild=subjectChecklistDataRow[i].split("\\^",-1);
		 Log.d("LEE","subjectChecklistDataFeild:"+subjectChecklistDataFeild[4]);
		 if(subjectChecklistDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update subjectchecklist set subjectId='"+subjectChecklistDataFeild[1]+"',checknum='"+subjectChecklistDataFeild[2]+"',critical='"+subjectChecklistDataFeild[3]+"' where Id='"+subjectChecklistDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(subjectChecklistDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM subjectchecklist where Id='"+subjectChecklistDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into subjectchecklist (Id,subjectId,checknum,critical) values ('"+subjectChecklistDataFeild[0]+"','"+subjectChecklistDataFeild[1]+"','"+subjectChecklistDataFeild[2]+"','"+subjectChecklistDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(subjectChecklistDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from subjectchecklist where Id='"+subjectChecklistDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}
public TestAdapter upgradeChecklist(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] checklistDataRow = dataString.split("\\!");
	 for (int i = 0, n = checklistDataRow.length; i < n; i++)
	 {
		 String[] checklistDataFeild=checklistDataRow[i].split("\\^",-1);
		 Log.d("LEE","checklistDataFeild:"+checklistDataFeild[4]);
		 if(checklistDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update checklist set checknum='"+checklistDataFeild[1]+"',name='"+checklistDataFeild[2]+"',objective='"+checklistDataFeild[3]+"' where Id='"+checklistDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(checklistDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM checklist where Id='"+checklistDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into checklist (Id,checknum,name,objective) values ('"+checklistDataFeild[0]+"','"+checklistDataFeild[1]+"','"+checklistDataFeild[2]+"','"+checklistDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(checklistDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from checklist where Id='"+checklistDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}
public TestAdapter upgradeChecklistIndex(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] checklistIndexDataRow = dataString.split("\\!");
	 for (int i = 0, n = checklistIndexDataRow.length; i < n; i++)
	 {
		 String[] checklistIndexDataFeild=checklistIndexDataRow[i].split("\\^",-1);
		 if(checklistIndexDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update checklistindex set stageid='"+checklistIndexDataFeild[1]+"',checknum='"+checklistIndexDataFeild[2]+"',real='"+checklistIndexDataFeild[3]+"' where Id='"+checklistIndexDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(checklistIndexDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM checklistindex where Id='"+checklistIndexDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into checklistindex (Id,stageid,checknum,real) values ('"+checklistIndexDataFeild[0]+"','"+checklistIndexDataFeild[1]+"','"+checklistIndexDataFeild[2]+"','"+checklistIndexDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(checklistIndexDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from checklistindex where Id='"+checklistIndexDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}
public TestAdapter upgradeCompetency(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] competencyDataRow = dataString.split("\\!");
	 for (int i = 0, n = competencyDataRow.length; i < n; i++)
	 {
		 String[] competencyDataFeild=competencyDataRow[i].split("\\^",-1);
		 if(competencyDataFeild[5].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update competency set assessmentId='"+competencyDataFeild[1]+"',taskactivity='"+competencyDataFeild[2]+"',corridor='"+competencyDataFeild[3]+"',objective='"+competencyDataFeild[4]+"' where Id='"+competencyDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(competencyDataFeild[5].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM competency where Id='"+competencyDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into competency (Id,assessmentId,taskactivity,corridor,objective) values ('"+competencyDataFeild[0]+"','"+competencyDataFeild[1]+"','"+competencyDataFeild[2]+"','"+competencyDataFeild[3]+"','"+competencyDataFeild[4]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(competencyDataFeild[5].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from competency where Id='"+competencyDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}
public TestAdapter upgradeCompetencyTask(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] competencyTaskDataRow = dataString.split("\\!");
	 for (int i = 0, n = competencyTaskDataRow.length; i < n; i++)
	 {
		 String[] competencyTaskDataFeild=competencyTaskDataRow[i].split("\\^",-1);
		 if(competencyTaskDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update competencytask set competencyId='"+competencyTaskDataFeild[1]+"',no='"+competencyTaskDataFeild[2]+"',name='"+competencyTaskDataFeild[3]+"' where Id='"+competencyTaskDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(competencyTaskDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM competencytask where Id='"+competencyTaskDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into competencytask (Id,competencyId,no,name) values ('"+competencyTaskDataFeild[0]+"','"+competencyTaskDataFeild[1]+"','"+competencyTaskDataFeild[2]+"','"+competencyTaskDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(competencyTaskDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from competencytask where Id='"+competencyTaskDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}

public TestAdapter upgradeChecklistTask(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String[] checklistTaskDataRow = dataString.split("\\!");
	 for (int i = 0, n = checklistTaskDataRow.length; i < n; i++)
	 {
		 String[] checklistTaskDataFeild=checklistTaskDataRow[i].split("\\^",-1);
		 if(checklistTaskDataFeild[4].equalsIgnoreCase("update"))
		 {
			 String sqlupdate="update checklisttask set checknum='"+checklistTaskDataFeild[1]+"',name='"+checklistTaskDataFeild[2]+"' where Id='"+checklistTaskDataFeild[0]+"'";
			 mDb.execSQL(sqlupdate);
		 }
		 if(checklistTaskDataFeild[4].equalsIgnoreCase("insert"))
		 {
			 String sqlRowNumber="SELECT * FROM checklisttask where Id='"+checklistTaskDataFeild[0]+"'";
				Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
				if(mCur.getCount()==0)
				{
					String sqlinsert="insert into checklisttask (Id,checknum,name,taskgroup) values ('"+checklistTaskDataFeild[0]+"','"+checklistTaskDataFeild[1]+"','"+checklistTaskDataFeild[2]+"','"+checklistTaskDataFeild[3]+"')";
					mDb.execSQL(sqlinsert);
				}
		 }
		 if(checklistTaskDataFeild[4].equalsIgnoreCase("delete"))
		 {
			 String sqldelete="delete from checklisttask where Id='"+checklistTaskDataFeild[0]+"'";
			 mDb.execSQL(sqldelete);
		 }
	 }
    return this; 
}
public TestAdapter updateCheckedVersionAlready(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String sqlupdate="update updateversion set checkVersion='"+dataString+"'";
	mDb.execSQL(sqlupdate);
    return this; 
}

public TestAdapter loadStageSync(String dataString) throws SQLException  
{ 
	String stageValue="";
	String[] stageDataRow = dataString.split("\\!");
	 for (int i = 0, n = stageDataRow.length; i < n; i++)
	 {
		 String[] stageDataFeild=stageDataRow[i].split("\\^",-1);
		 stageValue = stageValue + "('"+stageDataFeild[0]+"','"+stageDataFeild[1]+"','"+stageDataFeild[2]+"'),";
	 }
	mDb = mDbHelper.getWritableDatabase();
	String sqlStageRowNumber="SELECT * FROM stage";
	Cursor mCur=mDb.rawQuery(sqlStageRowNumber, null);
	if(mCur.getCount()==0)
	{
		String stageValueQuery=stageValue.substring(0, stageValue.length() - 1) + ';';
		String sql="insert into stage (Id,name,desc) values" + stageValueQuery ;
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter loadAssessmentSync(String dataString) throws SQLException  
{ 
	String assessmentValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 assessmentValue = assessmentValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"','"+dataFeild[14]+"','"+dataFeild[15]+"','"+dataFeild[16]+"','"+dataFeild[17]+"'),";
	 }
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM assessment";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		String valueQuery=assessmentValue.substring(0, assessmentValue.length() - 1) + ';';
		String sql="insert into assessment (timevisible,trainee,assessor,trainer,Id,stageId,name,location," +
				"date,result,assessorsig,assessordate,traineesig,traineedate,comment,timeoverschedule,timelost,objective) values" + valueQuery ;
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter loadSubjectSync(String dataString) throws SQLException  
{ 
	String subjectValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 subjectValue = subjectValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'),";
	 }
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM subject";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{	
		String valueQuery=subjectValue.substring(0, subjectValue.length() - 1) + ';';
		String sql="insert into subject (assessor,trainer,trainee,Id,assessmentId,name,objective,achieved,comment,requirednum) values" + valueQuery ;
		mDb.execSQL(sql);
	}
    return this; 
}


public TestAdapter loadChecklistSync(String dataString) throws SQLException  
{ 
	String[] dataRow = dataString.split("\\!");

	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/10;}
	else	{a=(n/100)+1;}
	
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM checklist";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
    	for(int i=0; i<a; i++)
    	{
    		String ChecklistValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"','"+dataFeild[14]+"','"+dataFeild[15]+"','"+dataFeild[16]+"','"+dataFeild[17]+"','"+dataFeild[18]+"','"+dataFeild[19]+"','"+dataFeild[20]+"','"+dataFeild[21]+"','"+dataFeild[22]+"','"+dataFeild[23]+"','"+dataFeild[24]+"','"+dataFeild[25]+"','"+dataFeild[26]+"','"+dataFeild[27]+"','"+dataFeild[28]+"','"+dataFeild[29]+"','"+dataFeild[30]+"','"+dataFeild[31]+"','"+dataFeild[32]+"','"+dataFeild[33]+"','"+dataFeild[34]+"','"+dataFeild[35]+"','"+dataFeild[36]+"','"+dataFeild[37]+"','"+dataFeild[38]+"','"+dataFeild[39]+"','"+dataFeild[40]+"','"+dataFeild[41]+"','"+dataFeild[42]+"','"+dataFeild[43]+"','"+dataFeild[44]+"','"+dataFeild[45]+"','"+dataFeild[46]+"','"+dataFeild[47]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	ChecklistValue = ChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"','"+dataFeild[14]+"','"+dataFeild[15]+"','"+dataFeild[16]+"','"+dataFeild[17]+"','"+dataFeild[18]+"','"+dataFeild[19]+"','"+dataFeild[20]+"','"+dataFeild[21]+"','"+dataFeild[22]+"','"+dataFeild[23]+"','"+dataFeild[24]+"','"+dataFeild[25]+"','"+dataFeild[26]+"','"+dataFeild[27]+"','"+dataFeild[28]+"','"+dataFeild[29]+"','"+dataFeild[30]+"','"+dataFeild[31]+"','"+dataFeild[32]+"','"+dataFeild[33]+"','"+dataFeild[34]+"','"+dataFeild[35]+"','"+dataFeild[36]+"','"+dataFeild[37]+"','"+dataFeild[38]+"','"+dataFeild[39]+"','"+dataFeild[40]+"','"+dataFeild[41]+"','"+dataFeild[42]+"','"+dataFeild[43]+"','"+dataFeild[44]+"','"+dataFeild[45]+"','"+dataFeild[46]+"','"+dataFeild[47]+"'),";
    	        }
    	    }
    	    if(ChecklistValue!="")
    	    {
    	    	String valueQuery=ChecklistValue.substring(0, ChecklistValue.length() - 1) + ';';
        		String sql="insert into checklist (ungperform,ungmpu,gmpu,insmpu,corridorup,corridordown,trainerinstruction," +
        				"trainerguide,trainerunguide,rpl,locomotivetype,desc,trainee,assessor,trainer,Id,checknum,name,objective," +
        				"critical,explained,demonstrated,nyc,tasktraineesig,tasktraineedate,tasktrainersig,tasktrainerdate," +
        				"instrainersig,insdate,insdayornight,insweather,insfrom,insto,insnovehicle,gtrainersig,gdate,gdayornight," +
        				"gweather,gfrom,gto,gnovehicle,ungtrainersig,ungdate,ungdayornight,ungweather,ungfrom,ungto,ungnovehicle) values" + valueQuery ;
        		mDb.execSQL(sql);
    	    }
    	}
	}
    return this; 
}

public TestAdapter loadCompetencySync(String dataString) throws SQLException  
{ 
	String competencyValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 competencyValue = competencyValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'," +
		 		"'"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'," +
		 				"'"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"'),";
	 }
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM competency";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		String valueQuery=competencyValue.substring(0, competencyValue.length() - 1) + ';';
		String sql="insert into competency (result,trainee,assessor,trainer,Id,assessmentId,taskactivity,corridor," +
				"objective,c,nyc,comment,assessorsig,traineesig) values" + valueQuery ;
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter insertVersionNumberSyncBefore(String dataString) throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	String sqlRowNumber="SELECT * FROM updateversion";
	Cursor mCur=mDb.rawQuery(sqlRowNumber, null);
	if(mCur.getCount()==0)
	{
		String sql="insert into updateversion (LoadDate,versionNumber) values (DATETIME('now'),'"+dataString+"')";
		mDb.execSQL(sql);
	}
    return this; 
}

public TestAdapter assessmentdetailAndTimeDefault() throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	
	String getAllassessment="SELECT * FROM assessment";
	Cursor cursorGetAllAssessment=mDb.rawQuery(getAllassessment, null);
	cursorGetAllAssessment.moveToFirst();
	do
	{
		if(cursorGetAllAssessment.getString(cursorGetAllAssessment.getColumnIndex("Id"))!=null )
		{
			String Id = cursorGetAllAssessment.getString(cursorGetAllAssessment.getColumnIndex("Id"));
			assessmentdetailinsert(Id, "1");
			assessmentdetailinsert(Id, "2");
			assessmentdetailinsert(Id, "3");
			assessmentdetailinsert(Id, "4");
			assessmentdetailinsert(Id, "5");
			assessmentdetailinsert(Id, "6");
			
			assessmenttimelostinsert(Id, "1");			
			assessmenttimelostinsert(Id, "2");
			assessmenttimelostinsert(Id, "3");
			assessmenttimelostinsert(Id, "4");
			assessmenttimelostinsert(Id, "5");
			assessmenttimelostinsert(Id, "6");
		}
	}while(cursorGetAllAssessment.moveToNext());	
	
	 return this; 
} 

public TestAdapter loadAssessmentDetailSync(String dataString) throws SQLException  
{ 
	String insertAssessmentDetaiValue="";
	String deleteAssessmentDetaiValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 insertAssessmentDetaiValue = insertAssessmentDetaiValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'," +
		 		"'"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'," +
		 				"'"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"'),";
		 
		 deleteAssessmentDetaiValue = deleteAssessmentDetaiValue + " (assessmentId='" + dataFeild[4] + "' and tripno='"+ dataFeild[5] +"') OR";
	 }

	//Delete
	 if(deleteAssessmentDetaiValue!="") {
			String deleteValueQuery=deleteAssessmentDetaiValue.substring(0, deleteAssessmentDetaiValue.length() - 2) + ';';
			String sqlDelete="delete from assessmentdetail where" + deleteValueQuery;
			mDb.execSQL(sqlDelete);
	 }
			
	 //Insert
	 if(insertAssessmentDetaiValue!="") {
		String insertValueQuery=insertAssessmentDetaiValue.substring(0, insertAssessmentDetaiValue.length() - 1) + ';';
		String sqlInsert="insert into assessmentdetail values" + insertValueQuery;
		mDb.execSQL(sqlInsert);
	 }
    return this; 
}

public TestAdapter loadTimeLostSync(String dataString) throws SQLException  
{ 
	String insertTimeLostValue="";
	String deleteTimeLostValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 insertTimeLostValue = insertTimeLostValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'," +
		 		"'"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'," +
		 				"'"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"'),";
		 
		 deleteTimeLostValue = deleteTimeLostValue + " (assessmentId='" + dataFeild[4] + "' and tripno='"+ dataFeild[5] +"') OR";
	 }

	//Delete
	 if(deleteTimeLostValue!="")
	 {
			String deleteValueQuery=deleteTimeLostValue.substring(0, deleteTimeLostValue.length() - 2) + ';';
			String sqlDelete="delete from timelost where" + deleteValueQuery;
			mDb.execSQL(sqlDelete);
	 }
			
	 //Insert
	 if(insertTimeLostValue !="") {
		String insertValueQuery=insertTimeLostValue.substring(0, insertTimeLostValue.length() - 1) + ';';
		String sqlInsert="insert into timelost values" + insertValueQuery;
		mDb.execSQL(sqlInsert);
	 }
		
    return this; 
}

public TestAdapter loadSubjectChecklistSync(String dataString) throws SQLException  
{ 
	String insertSubjectChecklistValue="";
	String deleteSubjectChecklistValue="";
	
	String[] dataRow = dataString.split("\\!");
	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/100;}
	else	{a=(n/100)+1;}
	mDb = mDbHelper.getWritableDatabase();
	
	if(n>100)
	{
    	for(int i=0; i<a; i++)
    	{
    		insertSubjectChecklistValue="";
    		deleteSubjectChecklistValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteSubjectChecklistValue = deleteSubjectChecklistValue + " (Id='" + dataFeild[8] + "' and subjectId='"+ dataFeild[9] +"') OR";
    	        	insertSubjectChecklistValue = insertSubjectChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteSubjectChecklistValue = deleteSubjectChecklistValue + " (Id='" + dataFeild[8] + "' and subjectId='"+ dataFeild[9] +"') OR";
    	        	insertSubjectChecklistValue = insertSubjectChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"'),";
    	        }
    	    }
    	    
    	    if(deleteSubjectChecklistValue!="")
    	    {
    	    	String deleteValueQuery=deleteSubjectChecklistValue.substring(0, deleteSubjectChecklistValue.length() - 2) + ';';
    			String sqlDelete="delete from subjectchecklist where" + deleteValueQuery;
    			mDb.execSQL(sqlDelete);
    	    }
    	    
    	    if(insertSubjectChecklistValue!="")
    	    {
    	    	String insertValueQuery=insertSubjectChecklistValue.substring(0, insertSubjectChecklistValue.length() - 1) + ';';
        		String sqlInser="insert into subjectchecklist values" + insertValueQuery ;
        		mDb.execSQL(sqlInser);
    	    }
    	}
	}
	else
	{
		insertSubjectChecklistValue="";
		deleteSubjectChecklistValue="";
		 for (int i = 0, j = dataRow.length; i < j; i++)
		 {
			 String[] dataFeild=dataRow[i].split("\\^",-1);

			deleteSubjectChecklistValue = deleteSubjectChecklistValue + " (Id='" + dataFeild[8] + "' and subjectId='"+ dataFeild[9] +"') OR";
	        insertSubjectChecklistValue = insertSubjectChecklistValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"'),";
		 }
		 
		 if(deleteSubjectChecklistValue!="")
 	    {
 	    	String deleteValueQuery=deleteSubjectChecklistValue.substring(0, deleteSubjectChecklistValue.length() - 2) + ';';
 			String sqlDelete="delete from subjectchecklist where" + deleteValueQuery;
 			mDb.execSQL(sqlDelete);
 	    }
 	    
 	    if(insertSubjectChecklistValue!="")
 	    {
 	    	String insertValueQuery=insertSubjectChecklistValue.substring(0, insertSubjectChecklistValue.length() - 1) + ';';
     		String sqlInser="insert into subjectchecklist values" + insertValueQuery ;
     		mDb.execSQL(sqlInser);
 	    }
	}
		
    return this; 
}

public TestAdapter loadChecklistTaskSync(String dataString) throws SQLException  
{ 
	String insertChecklistTaskValue="";
	String deleteChecklistTaskValue="";
	
	String[] dataRow = dataString.split("\\!");
	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/100;}
	else	{a=(n/100)+1;}
	mDb = mDbHelper.getWritableDatabase();
	
	if(n>100)
	{
    	for(int i=0; i<a; i++)
    	{
    		insertChecklistTaskValue="";
    		deleteChecklistTaskValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteChecklistTaskValue = deleteChecklistTaskValue + " (Id='" + dataFeild[2] + "' and checknum='"+ dataFeild[3] +"') OR";
    	        	insertChecklistTaskValue = insertChecklistTaskValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteChecklistTaskValue = deleteChecklistTaskValue + " (Id='" + dataFeild[2] + "' and checknum='"+ dataFeild[3] +"') OR";
    	        	insertChecklistTaskValue = insertChecklistTaskValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"'),";
    	        }
    	    }
    	    
    	    if(deleteChecklistTaskValue!="")
    	    {
    	    	String deleteValueQuery=deleteChecklistTaskValue.substring(0, deleteChecklistTaskValue.length() - 2) + ';';
    			String sqlDelete="delete from checklisttask where" + deleteValueQuery;
    			mDb.execSQL(sqlDelete);
    	    }
    	    
    	    if(insertChecklistTaskValue!="")
    	    {
    	    	String insertValueQuery=insertChecklistTaskValue.substring(0, insertChecklistTaskValue.length() - 1) + ';';
        		String sqlInser="insert into checklisttask values" + insertValueQuery ;
        		mDb.execSQL(sqlInser);
    	    }
    	}
	}
	else
	{
		insertChecklistTaskValue="";
		deleteChecklistTaskValue="";
		 for (int i = 0, j = dataRow.length; i < j; i++)
		 {
			 String[] dataFeild=dataRow[i].split("\\^",-1);

	        	deleteChecklistTaskValue = deleteChecklistTaskValue + " (Id='" + dataFeild[2] + "' and checknum='"+ dataFeild[3] +"') OR";
	        	insertChecklistTaskValue = insertChecklistTaskValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"'),";
		 }
		 
		 if(deleteChecklistTaskValue!="")
 	    {
 	    	String deleteValueQuery=deleteChecklistTaskValue.substring(0, deleteChecklistTaskValue.length() - 2) + ';';
 			String sqlDelete="delete from checklisttask where" + deleteValueQuery;
 			mDb.execSQL(sqlDelete);
 	    }
 	    
 	    if(insertChecklistTaskValue!="")
 	    {
 	    	String insertValueQuery=insertChecklistTaskValue.substring(0, insertChecklistTaskValue.length() - 1) + ';';
     		String sqlInser="insert into checklisttask values" + insertValueQuery ;
     		mDb.execSQL(sqlInser);
 	    }
	}
		
    return this;
}

public TestAdapter loadCompetencyTaskSync(String dataString) throws SQLException  
{ 
	String insertCompetencyTaskkValue="";
	String deleteCompetencyTaskValue="";
	
	String[] dataRow = dataString.split("\\!");
	int n=dataRow.length;
	int a ;
	if(n/100==0)	{a=n/100;}
	else	{a=(n/100)+1;}
	mDb = mDbHelper.getWritableDatabase();
	
	if(n>100)
	{
    	for(int i=0; i<a; i++)
    	{
    		insertCompetencyTaskkValue="";
    		deleteCompetencyTaskValue="";
    	    if((i+1)*100>n)
    	    {
    	        for (int j = i * 100; j < n; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteCompetencyTaskValue = deleteCompetencyTaskValue + " (Id='" + dataFeild[3] + "' and competencyId='"+ dataFeild[4] +"') OR";
    	        	insertCompetencyTaskkValue = insertCompetencyTaskkValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'),";
    	        }
    	    }
    	    else
    	    {
    	        for (int j = i * 100; j < (i + 1) * 100; j++) {
    	        	String[] dataFeild=dataRow[j].split("\\^",-1);
    	        	deleteCompetencyTaskValue = deleteCompetencyTaskValue + " (Id='" + dataFeild[3] + "' and competencyId='"+ dataFeild[4] +"') OR";
    	        	insertCompetencyTaskkValue = insertCompetencyTaskkValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'),";
    	        }
    	    }
    	    
    	    if(deleteCompetencyTaskValue!="")
    	    {
    	    	String deleteValueQuery=deleteCompetencyTaskValue.substring(0, deleteCompetencyTaskValue.length() - 2) + ';';
    			String sqlDelete="delete from competencytask where" + deleteValueQuery;
    			mDb.execSQL(sqlDelete);
    	    }
    	    
    	    if(insertCompetencyTaskkValue!="")
    	    {
    	    	String insertValueQuery=insertCompetencyTaskkValue.substring(0, insertCompetencyTaskkValue.length() - 1) + ';';
        		String sqlInser="insert into competencytask values" + insertValueQuery ;
        		mDb.execSQL(sqlInser);
    	    }
    	}
	}
	else
	{
		insertCompetencyTaskkValue="";
		deleteCompetencyTaskValue="";
		 for (int i = 0, j = dataRow.length; i < j; i++)
		 {
			 String[] dataFeild=dataRow[i].split("\\^",-1);

	        	deleteCompetencyTaskValue = deleteCompetencyTaskValue + " (Id='" + dataFeild[3] + "' and competencyId='"+ dataFeild[4] +"') OR";
	        	insertCompetencyTaskkValue = insertCompetencyTaskkValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"','"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"'),";
		 }
		 
		 if(deleteCompetencyTaskValue!="")
 	    {
 	    	String deleteValueQuery=deleteCompetencyTaskValue.substring(0, deleteCompetencyTaskValue.length() - 2) + ';';
 			String sqlDelete="delete from competencytask where" + deleteValueQuery;
 			mDb.execSQL(sqlDelete);
 	    }
 	    
 	    if(insertCompetencyTaskkValue!="")
 	    {
 	    	String insertValueQuery=insertCompetencyTaskkValue.substring(0, insertCompetencyTaskkValue.length() - 1) + ';';
     		String sqlInser="insert into competencytask values" + insertValueQuery ;
     		mDb.execSQL(sqlInser);
 	    }
	}
		
    return this;
}


public TestAdapter loadCompetencyTaskDescSync(String dataString) throws SQLException  
{ 
	String insertCompetencyTaskDescValue="";
	String deleteCompetencyTaskDescValue="";
	String[] dataRow = dataString.split("\\!");
	 for (int i = 0, n = dataRow.length; i < n; i++)
	 {
		 String[] dataFeild=dataRow[i].split("\\^",-1);
		 insertCompetencyTaskDescValue = insertCompetencyTaskDescValue + "('"+dataFeild[0]+"','"+dataFeild[1]+"','"+dataFeild[2]+"','"+dataFeild[3]+"','"+dataFeild[4]+"'," +
		 		"'"+dataFeild[5]+"','"+dataFeild[6]+"','"+dataFeild[7]+"','"+dataFeild[8]+"','"+dataFeild[9]+"','"+dataFeild[10]+"','"+dataFeild[11]+"','"+dataFeild[12]+"','"+dataFeild[13]+"'),";
		 
		 deleteCompetencyTaskDescValue = deleteCompetencyTaskDescValue + " (tablenumber='" + dataFeild[0] + "' and competencyId='"+ dataFeild[7] +"') OR";
	 }

	//Delete
	 if(deleteCompetencyTaskDescValue!="")
	 {
			String deleteValueQuery=deleteCompetencyTaskDescValue.substring(0, deleteCompetencyTaskDescValue.length() - 2) + ';';
			String sqlDelete="delete from competencytaskdesc where" + deleteValueQuery;
			mDb.execSQL(sqlDelete);
	 }
			
	 //Insert
	 if(insertCompetencyTaskDescValue !="") {
		String insertValueQuery=insertCompetencyTaskDescValue.substring(0, insertCompetencyTaskDescValue.length() - 1) + ';';
		String sqlInsert="insert into competencytaskdesc values" + insertValueQuery;
		mDb.execSQL(sqlInsert);
	 }
		
    return this; 
}
public TestAdapter competencyTaskDescDefault() throws SQLException  
{ 
	mDb = mDbHelper.getWritableDatabase();
	
	String getAllCompetency="SELECT * FROM competency";
	Cursor cursorGetAllCompetency=mDb.rawQuery(getAllCompetency, null);
	cursorGetAllCompetency.moveToFirst();
	do
	{
		if(cursorGetAllCompetency.getString(cursorGetAllCompetency.getColumnIndex("Id"))!=null )
		{
			String Id = cursorGetAllCompetency.getString(cursorGetAllCompetency.getColumnIndex("Id"));
			String corridor=cursorGetAllCompetency.getString(cursorGetAllCompetency.getColumnIndex("corridor"));			
			//Doesn't Include Corridor
			if(corridor.isEmpty())
			{
				insertcompetencytaskdesc(Id, "1");
				insertcompetencytaskdesc(Id, "2");
				insertcompetencytaskdesc(Id, "3");
				insertcompetencytaskdesc(Id, "4");
				insertcompetencytaskdesc(Id, "5");
			}
			else
			{
				insertcompetencytaskdesc(Id, "1");
				insertcompetencytaskdesc(Id, "2");
			}
		}
	} while(cursorGetAllCompetency.moveToNext());	
	
	 return this; 
} 
}
