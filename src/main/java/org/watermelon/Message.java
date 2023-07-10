package org.watermelon;

import java.sql.Timestamp;
import java.util.Objects;

public class Message {

    private final Integer sourceID;
    private final Integer destID;
    private String message;
    private Timestamp timestamp;
    private Integer id;


    public Message(Integer sourceID, Integer destID, String message) {
        this.sourceID = sourceID;
        this.destID = destID;
        this.message = message;
    }

    public Message(Integer sourceID, Integer destID, String message, Integer id, Timestamp timestamp) {
        this(sourceID, destID, message);
        this.id = id;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public Integer getDestID() {
        return destID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sourceID=" + sourceID +
                ", destID=" + destID +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message1)) return false;
        return sourceID.equals(message1.sourceID) && destID.equals(message1.destID) && message.equals(message1.message) && Objects.equals(timestamp, message1.timestamp) && Objects.equals(id, message1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceID, destID, message, timestamp, id);
    }
}
