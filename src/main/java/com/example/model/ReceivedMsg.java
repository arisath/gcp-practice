package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.pubsub.v1.ReceivedMessage;

public class ReceivedMsg
{

    @JsonProperty("ackId")
    private String ackId;

    @JsonProperty("message")
    String message;

    public ReceivedMsg(ReceivedMessage receivedMsg)
    {
        this.message = receivedMsg.getMessage().getData().toStringUtf8();
    }

    public String getAckId()
    {
        return ackId;
    }

    public void setAckId(String ackId)
    {
        this.ackId = ackId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
