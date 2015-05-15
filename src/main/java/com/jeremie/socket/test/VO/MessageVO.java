package com.jeremie.socket.test.VO;

import java.io.Serializable;

/**
 * Created by Jeremie on 2015/5/14.
 */
public class MessageVO implements Serializable {
    private String sender;
    private String receiver;
    private String message;
    private Long createTime;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
