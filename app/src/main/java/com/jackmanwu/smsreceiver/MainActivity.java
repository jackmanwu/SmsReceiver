package com.jackmanwu.smsreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jackmanwu.smsreceiver.db.Sms;
import com.jackmanwu.smsreceiver.sms.SmsDao;

import java.util.List;

public class MainActivity extends Activity {
    private SmsDao smsDao;
    private com.jackmanwu.smsreceiver.db.SmsDao smsDaoV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsDao = new SmsDao();
        smsDaoV2 = new com.jackmanwu.smsreceiver.db.SmsDao(this);

        Button sendMailBtn = (Button) findViewById(R.id.send_mail);
        sendMailBtn.setOnClickListener(view -> {
//            new MailTask().execute("xxxxx");
//            smsDaoV2.dropTable();
            List<com.jackmanwu.smsreceiver.db.Sms> list = smsDaoV2.find(4071);
            for (Sms sms : list) {
                Log.v("sms", sms.toString());
            }
        });
        Intent intent = new Intent(MainActivity.this, SmsReceiverService.class);
        startService(intent);

    }
}