package com.infoempire.wavetoget;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import android.os.AsyncTask;
import android.util.Log;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message;

public class GMail {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    String ToEmail;
    String emailSubject;
    String emailBody;

    String taskCompleteMessage;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public GMail() {

    }

    public GMail(String fromEmail, String fromPassword,
                 String ToEmail, String emailSubject, String emailBody, String taskCompleteMessage) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.ToEmail = ToEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.taskCompleteMessage = taskCompleteMessage;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        emailMessage.addRecipient(Message.RecipientType.TO,
                new InternetAddress(ToEmail));//

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Transport transport = mailSession.getTransport("smtp");
                    transport.connect(emailHost, fromEmail, fromPassword);
                    Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
                    transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
                    transport.close();
                    Log.i("GMail", "Email sent successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

//        Transport.send(emailMessage);
    }

}