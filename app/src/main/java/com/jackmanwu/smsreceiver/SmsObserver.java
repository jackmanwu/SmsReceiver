package com.jackmanwu.smsreceiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class SmsObserver extends ContentObserver {
    private Context context;
    private String[] projection = new String[]{"_id", "body"};
    private Cursor cursor;
    private SmsObservedListener listener;

    public SmsObserver(Context context, Handler handler, SmsObservedListener listener) {
        super(handler);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri uri = Uri.parse("content://sms/inbox");
        cursor = context.getContentResolver().query(uri, projection, null, null, "_id desc");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            int bodyColumn = cursor.getColumnIndex("body");
            String body = cursor.getString(bodyColumn);
            listener.onObservedMessage(body);
        }
    }

    public interface SmsObservedListener {
        void onObservedMessage(String message);
    }
}
