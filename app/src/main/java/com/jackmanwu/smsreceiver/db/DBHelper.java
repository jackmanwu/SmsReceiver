package com.jackmanwu.smsreceiver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.jackmanwu.smsreceiver.db.SmsDao.TABLE_NAME;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_sms.db";
    private static final int VERSION = 1;
    public static final String SMS_CREATE_TABLE = "create table " + TABLE_NAME + "(id integer primary key autoincrement,sms_id integer not null default 0,state integer not null default 0,create_time integer not null default 0)";
    public static final String UNIQUE_INDEX = "create unique index idx_sms_id on " + TABLE_NAME + "(sms_id)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SMS_CREATE_TABLE);
        db.execSQL(UNIQUE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
