package com.jackmanwu.smsreceiver.sms;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class SmsDao {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public List<Sms> selectSms(Context context) {
        Uri uri = Telephony.Sms.Inbox.CONTENT_URI;
        String[] projections = new String[]{"_id", "address", "date", "date_sent", "body"};
        long now = DateTime.now().withTimeAtStartOfDay().getMillis();
        @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(uri, projections, "date_sent>?", new String[]{String.valueOf(now)}, "_id desc");

        List<Sms> list = new ArrayList<>();
        try {
            int count = 0;
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                if (count > 1) {
                    break;
                }
                list.add(convert(cursor));
                count++;
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    private Sms convert(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        int address = cursor.getInt(cursor.getColumnIndex("address"));
        long date = cursor.getLong(cursor.getColumnIndex("date"));
        long dateSent = cursor.getLong(cursor.getColumnIndex("date_sent"));
        String body = cursor.getString(cursor.getColumnIndex("body"));
        return new Sms(id, address, date, dateSent, body);
    }
}
