package com.jackmanwu.smsreceiver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jackmanwu.smsreceiver.db.SmsDao;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTask extends AsyncTask<String, Void, Void> {
    private static final String FROM = "xxx@qq.com";
    private static final String TO = "xxx@qq.com";
    private static final String SECRET = "gcjubzvlljeebbeb";

    private final SmsDao smsDao;

    public MailTask(Context context) {
        super();
        smsDao = new SmsDao(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        int smsId = Integer.parseInt(strings[0]);
        int oldId = strings.length == 3 ? Integer.parseInt(strings[2]) : -1;
        String body = strings[1];
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            try {
                sendMail(body);
                flag = true;
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (flag) {
            for (int i = 0; i < 3; i++) {
                try {
                    long id;
                    if (oldId == -1) {
                        id = smsDao.save(smsId);
                    } else {
                        id = smsDao.updateSuccessState(oldId);
                    }
                    if (id > 0) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void sendMail(String body) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.qq.com");

        Session session = Session.getInstance(properties);
//        session.setDebug(true);

        Transport transport = null;
        try {
            Message message = new MimeMessage(session);
            message.setSubject("验证码");
            message.setText(body);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
            message.saveChanges();

            transport = session.getTransport();
            transport.connect(FROM, SECRET);
            transport.sendMessage(message, message.getAllRecipients());
            Log.v("send-mail", "send mail success");
        } catch (Exception e) {
            Log.e("send-mail", "send mail err", e);
            throw e;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
