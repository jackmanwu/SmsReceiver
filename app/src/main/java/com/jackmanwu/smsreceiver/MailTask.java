package com.jackmanwu.smsreceiver;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTask extends AsyncTask<String, Void, Void> {
    private static final String FROM = "xxxxx@qq.com";
    private static final String TO = "xxxxxx@qq.com";
    private static final String SECRET = "gcjubzvlljeebbeb";

    @Override
    protected Void doInBackground(String... strings) {
        for (int i = 0; i < 3; i++) {
            try {
                sendMail(strings[0]);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
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
        session.setDebug(true);

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
