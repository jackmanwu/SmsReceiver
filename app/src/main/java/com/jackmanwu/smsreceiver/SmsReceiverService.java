package com.jackmanwu.smsreceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import static com.jackmanwu.smsreceiver.SmsReceiver.SMS_RECEIVER_ACTION;

public class SmsReceiverService extends Service {
    private SmsReceiver smsReceiver;
    private SmsObserver smsObserver;
    private SmsHandler smsHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 25) {
            smsReceiver = new SmsReceiver();
            IntentFilter filter = new IntentFilter(SMS_RECEIVER_ACTION);
            registerReceiver(smsReceiver, filter);
        }else {
            smsHandler = new SmsHandler();
            smsObserver = new SmsObserver(this, smsHandler, new SmsObserver.SmsObservedListener() {
                @Override
                public void onObservedMessage(String message) {
                    new MailTask().execute(message);
                }
            });
            getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true,smsObserver);
        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, SmsReceiverService.class);
        this.startService(intent);
        super.onDestroy();
    }

    private static class SmsHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.v("receive-msg", "收到短信了");
            }
        }
    }
}
