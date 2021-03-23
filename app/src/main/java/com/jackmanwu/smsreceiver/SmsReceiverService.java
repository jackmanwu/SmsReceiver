package com.jackmanwu.smsreceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.jackmanwu.smsreceiver.sms.Sms;
import com.jackmanwu.smsreceiver.sms.SmsDao;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.jackmanwu.smsreceiver.SmsReceiver.SMS_RECEIVER_ACTION;

public class SmsReceiverService extends Service {
    private SmsDao smsDao;
    private com.jackmanwu.smsreceiver.db.SmsDao smsDbDao;

//    private SmsReceiver smsReceiver;
//    private SmsObserver smsObserver;
//    private SmsHandler smsHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        smsDao = new SmsDao();
        smsDbDao = new com.jackmanwu.smsreceiver.db.SmsDao(this);
        Timer timer = new Timer();
        timer.schedule(new SmsTimerTask(this), 0, 10000);

//        if (Build.VERSION.SDK_INT >= 25) {
//            smsReceiver = new SmsReceiver();
//            IntentFilter filter = new IntentFilter(SMS_RECEIVER_ACTION);
//            registerReceiver(smsReceiver, filter);
//        }else {
//            smsHandler = new SmsHandler();
//            smsObserver = new SmsObserver(this, smsHandler, message -> new MailTask(null).execute(message));
//            getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true,smsObserver);
//        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, SmsReceiverService.class);
        this.startService(intent);
        super.onDestroy();
    }

    class SmsTimerTask extends TimerTask {
        private final Context context;

        public SmsTimerTask(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            List<Sms> smsList = smsDao.selectSms(context);
            for (Sms sms : smsList) {
                if (!sms.getBody().contains("验证码")) {
                    continue;
                }
                try {
                    List<com.jackmanwu.smsreceiver.db.Sms> list = smsDbDao.find(sms.getId());
                    if (list.size() == 0) {
                        new MailTask(context).execute(String.valueOf(sms.getId()), sms.getBody());
                    } else if (list.get(0).getState() == 0) {
                        new MailTask(context).execute(String.valueOf(sms.getId()), sms.getBody(), String.valueOf(list.get(0).getId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private static class SmsHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                Log.v("receive-msg", "收到短信了");
//            }
//        }
//    }
}
