package com.abcnv.nvone.lib_https.test;

import java.io.Serializable;

/**
 * Created by xmc on 12/4/20
 */
public class ResponseObject<T> implements Serializable {
    private String message;
    private String requestId;
    private int status;
    private int code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return status == 200;
    }

    @Override
    public String toString() {
        String dataStr = "";
        if (data != null)
            dataStr = data.toString();
        return "ResponseObject{" +
                "message='" + message + '\'' +
                ", requestId='" + requestId + '\'' +
                ", status=" + status +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + dataStr +
                '}';
    }
}
