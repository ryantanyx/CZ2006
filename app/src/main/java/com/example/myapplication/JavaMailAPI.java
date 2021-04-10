package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Represents the JavaMailAPI that model a mail system
 */
public class JavaMailAPI extends AsyncTask<Void, Void, Void> {
    /**
     * The current context of the application
     */
    private Context context;
    /**
     * The mail session
     */
    private Session session;
    /**
     * The email address of the recipient, subject and message of the mail
     */
    private String email, subject, message;

    public JavaMailAPI(Context context, String email, String subject, String message){
       this.context = context;
       this.email = email;
       this.subject = subject;
       this.message = message;
   }

    /**
     * Sends the email to the recipient
     * @param voids
     * @return null
     */
    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(javamailUtils.EMAIL, javamailUtils.PASSWORD);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setFrom(new InternetAddress(javamailUtils.EMAIL));
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
