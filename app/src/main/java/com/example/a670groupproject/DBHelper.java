package com.example.a670groupproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users(userName TEXT primary key, password TEXT)");
        MyDB.execSQL("create table bloodSugarTable(entryID integer primary key autoincrement, entryValue TEXT, dayEntry integer, monthEntry integer, yearEntry integer, hourEntry integer, minuteEntry integer, twentyFourHour integer, visibleEntry integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop table if exists users");
        MyDB.execSQL("drop table if exists bloodSugarTable");
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("userName", username);
        contentValues.put("password", password);

        long result = MyDB.insert("users", null, contentValues);

        return result != -1;
    }

    public Boolean insertEntry( String tableName,String entryValue, String dayEntry, String monthEntry, String yearEntry, String hourEntry, String minuteEntry, String amPM){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("entryValue", entryValue);
        contentValues.put("dayEntry", Integer.parseInt(dayEntry));
        contentValues.put("monthEntry", Integer.parseInt(monthEntry));
        contentValues.put("yearEntry", Integer.parseInt(yearEntry));
        contentValues.put("hourEntry", Integer.parseInt(hourEntry));
        contentValues.put("minuteEntry", Integer.parseInt(minuteEntry));
        Log.d("DB Helper", "amPM Value: "+amPM);
        if (amPM.equals("AM"))
        {
            Log.d("DB Helper", "Inserting AM Value");
            if (Integer.parseInt(hourEntry)==12)
            {
                contentValues.put("twentyFourHour", 0);
            }
            else
            {
                contentValues.put("twentyFourHour",  Integer.parseInt(hourEntry));
            }
        }
        if (amPM.equals("PM"))
        {
            Log.d("DB Helper", "Inserting PM Value");
            if (Integer.parseInt(hourEntry)==12)
            {
                contentValues.put("twentyFourHour", Integer.parseInt(hourEntry));
            }
            else
            {
                contentValues.put("twentyFourHour",  Integer.parseInt(hourEntry)+12);
            }
        }
        contentValues.put("visibleEntry", 1);

        long result = MyDB.insert(tableName, null, contentValues);

        return result != -1;
    }

    public ArrayList<String[]> getEntries(String tableName, String dayEntry, String monthEntry, String yearEntry)
    {
        Log.d("DB Helper", "Getting records from table "+tableName+" for date "+dayEntry+"-"+monthEntry+"-"+yearEntry);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        String query = "select entryID, entryValue, hourEntry, minuteEntry, twentyFourHour from "+tableName+" where visibleEntry=1 and dayEntry ='"+dayEntry+"' and monthEntry = '"+monthEntry+"' and yearEntry = '"+yearEntry+"' order by twentyFourHour asc, minuteEntry asc";
        Log.d("DB Helper", "Running Query: "+query);
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        Log.d("DB Helper", "Records acquired, parsing now");
        if(c!=null && c.getCount() > 0)
        {
            if (c.moveToFirst())
            {
                c.moveToFirst();
                int idIndex = c.getColumnIndex("entryID");
                int valueIndex = c.getColumnIndex("entryValue");
                int hourIndex = c.getColumnIndex("hourEntry");
                int minuteIndex = c.getColumnIndex("minuteEntry");
                int twentyFourHourIndex = c.getColumnIndex("twentyFourHour");
                Log.d("DB Helper", "Indexes are here: ID="+idIndex+" Value="+valueIndex+" Hour="+hourIndex+" Minute="+minuteIndex+" twentyFourHour="+twentyFourHourIndex);
                for(int i = 0; i < c.getCount(); i++)
                {
                    String amPM = "AM";
                    String id = c.getString(idIndex);
                    String value = c.getString(valueIndex);
                    String hour = c.getString(hourIndex);
                    String minute = c.getString(minuteIndex);
                    if (c.getString(twentyFourHourIndex)!=null)
                    {
                        if (Integer.parseInt(c.getString(twentyFourHourIndex))>11)
                        {
                            amPM = "PM";
                        }
                    }
                    String messageWithID[] = {id, value, hour, minute, amPM};
                    resultList.add(messageWithID);
                    c.moveToNext();
                }
            }

        }
        Log.d("DB Helper", "Returning results");
        return resultList;
    }

    public Boolean deleteEntry(String tableName, String entryID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "Update "+tableName+" set visibleEntry = 0 where entryID ='"+entryID+"'";
        Cursor cursor = MyDB.rawQuery(query, null );
        Log.i("results", String.valueOf(cursor.getCount()));
        return cursor.getCount() > 0;
    }

    public Boolean updateEntry(String tableName, String entryID, String value, String day, String month, String year, String hour, String minute, String amPM) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String twentyFourHour = "00";

        if (amPM.equals("AM"))
        {
            Log.d("DB Helper", "Inserting AM Value");
            if (Integer.parseInt(hour)==12)
            {
                twentyFourHour = "00";
            }
            else
            {
                twentyFourHour = hour;
            }
        }
        if (amPM.equals("PM"))
        {
            Log.d("DB Helper", "Inserting PM Value");
            if (Integer.parseInt(hour)==12)
            {
                twentyFourHour = hour;
            }
            else
            {
                twentyFourHour = Integer.toString(Integer.parseInt(hour)+12);
            }
        }

        String query = "Update "+tableName+" set entryValue='"+value+"', dayEntry='"+day+"', monthEntry='"+month+"', yearEntry='"+year+"', hourEntry='"+hour+"', minuteEntry='"+minute+"', twentyFourHour='"+twentyFourHour+"' where entryID ='"+entryID+"'";
        Log.d("DB Helper", "Running Query: "+query);
        Cursor cursor = MyDB.rawQuery(query, null );
        Log.i("results", String.valueOf(cursor.getCount()));
        return cursor.getCount() > 0;
    }


    public Boolean checkIfUserExists(String userName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where userName = ?", new String[]{userName});
        Log.i("results", String.valueOf(cursor.getCount()));
        return cursor.getCount() > 0;
    }

    public Boolean checkUsernameAndPassword(String userName, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where userName = ? and password = ?", new String[] {userName,password});

        return cursor.getCount() > 0;
    }


}