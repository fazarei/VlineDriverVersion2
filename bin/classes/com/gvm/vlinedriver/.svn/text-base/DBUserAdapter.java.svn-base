package com.gvm.vlinedriver;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUserAdapter {
	
	public static final String KEY_ROWID = "_id";  
    public static final String KEY_USERNAME= "username";  
    public static final String KEY_PASSWORD = "password";  
    private static final String TAG = "DBAdapter";  
  
    private static final String DATABASE_NAME = "usersdb";  
    private static final String DATABASE_TABLE = "users";  
    private static final int DATABASE_VERSION = 1;  
  
    private static final String DATABASE_CREATE =  
        "create table users (_id integer primary key autoincrement, "  
        + "username text not null, "  
        + "password text not null);";  
  
    private Context context = null;  
    private DatabaseHelper DBHelper;  
    private static SQLiteDatabase db;  
  
    
    public DBUserAdapter(Context ctx)  
    {  
        this.context = ctx;  
        DBHelper = new DatabaseHelper(context);  
    }  
  
    private static class DatabaseHelper extends SQLiteOpenHelper  
    {  
        DatabaseHelper(Context context)  
        {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }

		@Override
		public void onCreate(SQLiteDatabase sqlitedatabase) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int i, int j) {
			// TODO Auto-generated method stub
			
		}
		
		
    }
    
    public void open() throws SQLException  
    {  
        db = DBHelper.getWritableDatabase();  
    }  
  
    public void close()  
    {  
        DBHelper.close();  
    } 
    
    public boolean Login(String username, String password) throws SQLException  
    {  
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE username=? AND password=?", new String[]{username,password});  
        if (mCursor != null) {  
            if(mCursor.getCount() > 0)  
            {  
                return true;  
            }  
        }  
     return false;  
    }  

}
