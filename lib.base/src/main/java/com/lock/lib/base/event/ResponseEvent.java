package com.lock.lib.base.event;


/**
 * Created by hubing on 16/3/22.
 */
public class ResponseEvent extends ServerEvent {
    public Object resultObj;
    public int errorCode;
    public String errorMsg;
}
