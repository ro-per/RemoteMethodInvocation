package messages;

import user.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class Message implements Serializable {

    private User sender;
    private final MessageType messageType;
    private final String content;
    private String receiver;
    private final Timestamp timestamp;
    private Set<String> activeUsers;

    /* ----------------------------- CONSTRUCTOR ----------------------------- */
    public Message(User sender, MessageType messageType, String text) {
        this.sender = sender;
        this.messageType = messageType;
        this.content = text;
        this.timestamp = new Timestamp(new Date().getTime());
    }

    /* CONNECT / DISCONNECT MESSAGE */
    public Message(MessageType messageType) {
        this.messageType = messageType;
        this.content = "I want to disconnect !!!";
        this.timestamp = new Timestamp(new Date().getTime());
    }

    public Message(User sender, MessageType messageType, String text, String receiver) {
        this.sender = sender;
        this.messageType = messageType;
        this.content = text;
        this.receiver = receiver;
        this.timestamp = new Timestamp(new Date().getTime());
    }

    public Message(MessageType messageType, String text) {
        this.messageType = messageType;
        this.content = text;
        this.timestamp = new Timestamp(new Date().getTime());
    }

    /* ----------------------------- GETTERS ----------------------------- */
    public User getSender() {
        return sender;
    }

    public MessageType getType() {
        return messageType;
    }

    public String getReceiverString() {
        return receiver;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        switch (messageType) {
            case BROADCAST:
                return "[BROADCAST:" + sender.getName() + "]: " + content;
            case PRIVATE:
                return "[PRIVATE:" + sender.getName() + "]: " + content;
            default:
                return content;
        }
    }

    public Set<String> getActiveUsers() {
        return activeUsers;
    }

    /* ----------------------------- SETTERS ----------------------------- */
    public void setSender(User user) {
        this.sender = user;
    }

    public void setActiveUsers(Set<String> activeUsers) {
        this.activeUsers = activeUsers;
    }

    /* ----------------------------- OVERRIDE ----------------------------- */
    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", messageType=" + messageType +
                ", group=" + receiver +
                ", timestamp=" + timestamp +
                '}';
    }

    /* ----------------------------- EQUALS/HASH ----------------------------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return sender.equals(message.sender) &&
                messageType == message.messageType &&
                timestamp.equals(message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, messageType, timestamp);
    }

}
