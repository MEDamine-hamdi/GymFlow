package com.gymflow.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {

    public enum Sender { USER, BOT }

    private final String content;
    private final Sender sender;
    private final String timestamp;

    public ChatMessage(String content, Sender sender) {
        this.content   = content;
        this.sender    = sender;
        this.timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getContent()   { return content; }
    public Sender getSender()    { return sender; }
    public String getTimestamp() { return timestamp; }
}
