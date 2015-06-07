package com.github.bingoohuang.springrestclient.exception;

public class RestException extends RuntimeException {
    private int status = -1;

    public RestException() {
        super();
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(int status, String msg) {
        super(msg);

        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
