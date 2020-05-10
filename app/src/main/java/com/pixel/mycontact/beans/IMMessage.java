package com.pixel.mycontact.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Carl Su
 * @date 2019/11/26
 */
@RealmClass
public class IMMessage implements RealmModel {

    @PrimaryKey
    private int msgID;
    private String msgSource;
    private String msgDestination;

    private Date msgTime;
    private String msgUser;
    private String msgBody;

    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public String getMsgDestination() {
        return msgDestination;
    }

    public void setMsgDestination(String msgDestination) {
        this.msgDestination = msgDestination;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getSimpleTime() {
        return SimpleDateFormat.getTimeInstance().format(msgTime);
    }
}
