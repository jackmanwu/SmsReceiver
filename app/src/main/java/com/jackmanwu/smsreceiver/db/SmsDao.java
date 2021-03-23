package com.jackmanwu.smsreceiver.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SmsDao {
    public static final String TABLE_NAME = "sms_send_state";

    private static final String[] COLUMNS = new String[]{"id", "sms_id", "state", "create_time"};

    private final DBHelper dbHelper;

    public SmsDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Sms> find(int csmsId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_NAME, COLUMNS, "sms_id=?", new String[]{String.valueOf(csmsId)}, null, null, "id desc");
        List<Sms> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int smsId = cursor.getInt(cursor.getColumnIndex("sms_id"));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                int createTime = cursor.getInt(cursor.getColumnIndex("create_time"));
                list.add(new Sms(id, smsId, state, createTime));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public long save(int smsId) {
        ContentValues values = new ContentValues();
        values.put("sms_id", smsId);
        values.put("state",1);
        values.put("create_time", System.currentTimeMillis() / 1000);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.insert(TABLE_NAME, null, values);
    }

    public long updateSuccessState(int id) {
        ContentValues values = new ContentValues();
        values.put("state", 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
    }

    public void dropTable() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("drop table " + TABLE_NAME);
    }
}
