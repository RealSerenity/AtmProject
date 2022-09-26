package com.rserenity.atmproject.Exception.ProcessInfo;

public class MessageWithDto<T> {

    public final MessageType messageType;
    public final String message;
    public final T object;

    public MessageWithDto(MessageType messageType, String message, T object) {
        this.messageType = messageType;
        this.message = message;
        this.object = object;
    }

}
