package com.gymflow.util;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioService {

    public static final String ACCOUNT_SID = "AC7b7faff1b930986993b25f0319894e1b";
    public static final String AUTH_TOKEN = "c4ea6f45463fb5463848a9bb8b7da47f";
    public static final String FROM_NUMBER = "+12182929617";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSMS(String to, String text) {

        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(FROM_NUMBER),
                text
        ).create();
    }
}