package com.ftn.pma.globals;

import android.os.AsyncTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendMail extends AsyncTask<Message,String,String> {
    @Override
    protected String doInBackground(Message... messages) {

        try {
            Transport.send(messages[0]);
            return "Uspesno";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Neuspesno";
        }

    }
}
