package com.rserenity.atmproject.Exception.ProcessInfo;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class Message {

    private String message;
    private MessageType messageType;

    public Message(){

    }

    public MessageWithDto<Object> getMessageWithDto(MessageType messageType, String message, Object object){
        return new MessageWithDto(messageType, message, object);
    }

    public MessageTypeAndMessage getMessageTypeAndMessage(MessageType messageType, String message){
        return new MessageTypeAndMessage(messageType, message);
    }
}

