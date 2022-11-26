package com.pinyougou.mq;

import java.io.Serializable;

public class MessageInfo implements Serializable {
    private static final long serialVersionUID = -4206122703453814687L;

    public static final int METHOD_ADD = 1;
    public static final int METHOD_UPDATE = 2;
    public static final int METHOD_DELETE = 3;

    //执行的动作 增、删、改 1、2、3
    private int method;

    //发送的数据
    private Object context;

    public MessageInfo() {
    }

    public MessageInfo(int method, Object context) {
        this.method = method;
        this.context = context;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }
}
