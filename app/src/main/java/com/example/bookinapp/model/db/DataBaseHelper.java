package com.example.bookinapp.model.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG= "DataBaseHelper";

    private static final String DB_NAME="booking.db";

    //Remember to increase whenever you call onUpdate
    private static final int DB_VERSION=1;

    private static String CREATE_MEETING_SQL;

    private static String CREATE_USER_SQL;

    private static DataBaseHelper instance;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
        Resources res= context.getResources();
        CREATE_MEETING_SQL=
                "CREATE TABLE meeting(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER," +
                        "title VARCHAR(" + "100" + ")," +
                        "start TIMESTAMP," +
                        "end TIMESTAMP," +
                        "pincode INTEGER default 0000)";
        CREATE_USER_SQL =
               "CREATE TABLE user(" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "name VARCHAR(" + "30" +"), " +
                       "email VARCHAR(" + "30" + "), " +
                       "password VARCHAR(" + "50" + ")," +
                       "salt VARCHAR(50)," +
                       "is_admin BOOLEAN default false";
    }

    public static DataBaseHelper getInstance(){
        if (instance == null)
            throw new IllegalStateException("instance has not been instantiated");
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.d(TAG, "going to create tables");
      db.execSQL(CREATE_MEETING_SQL);
      db.execSQL(CREATE_USER_SQL);

      Log.d("DBHELPER", "tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    /**
     * *@return
     */
    public static void setContext(Context context){
        instance= new DataBaseHelper(context);
    }


}
