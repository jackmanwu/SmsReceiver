package com.jackmanwu.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVER_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!SMS_RECEIVER_ACTION.equals(intent.getAction())) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus == null) {
            return;
        }
        for (Object pdu : pdus) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
            String msg = message.getMessageBody();
            System.out.println(msg);
            if (msg.contains("xxxxx") && msg.contains("验证码")){
                new MailTask(null).execute(msg);
            }
        }
    }
}
