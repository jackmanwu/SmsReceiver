package com.jackmanwu.smsreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendMailBtn = (Button) findViewById(R.id.send_mail);
        sendMailBtn.setOnClickListener(view -> new MailTask().execute("xxxxx"));
        Intent intent = new Intent(MainActivity.this,SmsReceiverService.class);
        startService(intent);
    }
}